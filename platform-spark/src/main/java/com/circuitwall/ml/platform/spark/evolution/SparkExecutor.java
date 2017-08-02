package com.circuitwall.ml.platform.spark.evolution;

import com.circuitwall.ml.algorithm.accelerator.EliteOffspringGenerator;
import com.circuitwall.ml.algorithm.common.model.GeneArray;
import com.circuitwall.ml.algorithm.common.model.ScoringResult;
import com.circuitwall.ml.algorithm.evolution.EvolutionAlgorithm;
import com.circuitwall.ml.algorithm.evolution.Executor;
import com.circuitwall.ml.algorithm.util.RandomUtil;
import com.circuitwall.ml.platform.spark.evolution.function.MutateFunction;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.circuitwall.ml.algorithm.util.RandomUtil.ProbabilityTester;

public class SparkExecutor implements Serializable, Executor {

    private final JavaSparkContext sparkContext;
    private final int parallelization;

    public SparkExecutor(JavaSparkContext sparkContext, int parallelization) {
        this.sparkContext = sparkContext;
        this.parallelization = parallelization > 0 ? parallelization : 1;
    }

    @Override
    public Comparable[] execute(EvolutionAlgorithm algorithm, int nrParents, int nrChildren, int rounds, Double mutationPercentage) throws Exception {
        List<Comparable[]> parents = Stream.generate(algorithm::generateParent).limit(nrParents).collect(Collectors.toList());
        ScoringResult bestChild = new ScoringResult();
        MutateFunction mutationFunction = new MutateFunction(algorithm, mutationPercentage);
        for (int r = 0; r < rounds && parents.size() > 0 && !algorithm.isFinished(); r++) {
            final List<Comparable[]> finalParents = parents;
            List<Comparable[]> children = Stream.generate(() -> EliteOffspringGenerator.generate(algorithm, finalParents)).limit(nrChildren).collect(Collectors.toList());
            List<ScoringResult> scoredChildren = sparkContext.parallelize(children, parallelization)
                    .map(mutationFunction)//Mutate
                    .map(v1 -> new ScoringResult(v1, algorithm.scoreIndividual(v1.getPayload())))//Scoring
                    .collect();
            //Prepare next gen
            List<ScoringResult> sortedChildren = new ArrayList<>(scoredChildren);
            sortedChildren.sort(Comparator.comparing(ScoringResult::getScore));
            Collections.reverse(sortedChildren);
            List<ScoringResult> nextGen = sortedChildren.stream().limit(nrParents).collect(Collectors.toList());
            if (nextGen != null && nextGen.size() > 0) {
                //Best child
                bestChild = nextGen.get(0);
                algorithm.processBestChild(bestChild.getChild().getPayload(), r, bestChild.getScore());
                parents = nextGen.stream().map(ScoringResult::getChild).map(GeneArray::getPayload).collect(Collectors.toList());
            }
        }
        return bestChild.getChild().getPayload();
    }

    public void close() {
        this.sparkContext.close();
    }
}
