package com.circuitwall.ml.platform.spark.evolution;

import com.circuitwall.ml.algorithm.common.model.GeneArray;
import com.circuitwall.ml.algorithm.common.model.ScoringResult;
import com.circuitwall.ml.algorithm.evolution.EvolutionAlgorithm;
import com.circuitwall.ml.algorithm.evolution.Executor;
import com.circuitwall.ml.algorithm.util.RandomUtil;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.circuitwall.ml.algorithm.util.RandomUtil.ProbabilityTester;

public class SparkExecutor implements Serializable, Executor {

    private final JavaSparkContext sparkContext;

    public SparkExecutor(JavaSparkContext sparkContext) {
        this.sparkContext = sparkContext;
    }

    @Override
    public Comparable[] execute(EvolutionAlgorithm algorithm, int nrParents, int nrChildren, int rounds, Double mutationPercentage) throws Exception {
        List<Comparable[]> parents = Stream.generate(algorithm::generateParent).limit(nrParents).collect(Collectors.toList());
        Comparable[] bestChild = new Comparable[0];
        for (int r = 0; r < rounds && parents.size() > 0; r++) {
            final List<Comparable[]> finalParents = parents;
            ProbabilityTester tester = RandomUtil.byProbability(mutationPercentage);
            List<Comparable[]> children = Stream.generate(() -> algorithm.procreate(algorithm.anyItem(finalParents), algorithm.anyItem(finalParents))).limit(nrChildren).collect(Collectors.toList());
            List<ScoringResult> scoredChildren = sparkContext.parallelize(children)//Mutate
                    .map(aChild -> {
                        if (tester.test()) {
                            return new GeneArray(algorithm.mutate(aChild));
                        } else {
                            return new GeneArray(aChild);
                        }
                    })
                    .map(v1 -> new ScoringResult(v1, algorithm.scoreIndividual(v1.getPayload())))
                    .collect();
            List<ScoringResult> sortedChildren = new ArrayList<>(scoredChildren);
            sortedChildren.sort(Comparator.comparing(ScoringResult::getScore));
            Collections.reverse(sortedChildren);
            List<ScoringResult> nextGen = sortedChildren.stream().limit(nrParents).collect(Collectors.toList());
            if (nextGen != null && nextGen.size() > 0) {
                //Best child
                bestChild = nextGen.get(0).getChild().getPayload();
                parents = nextGen.stream().map(ScoringResult::getChild).map(GeneArray::getPayload).collect(Collectors.toList());
            }
        }
        return bestChild;
    }
}
