package com.circuitwall.ml.platform.flink.evolution;

import com.circuitwall.ml.platform.flink.evolution.algorithm.EvolutionAlgorithmFlink;
import com.circuitwall.ml.platform.flink.evolution.model.ScoringResult;
import com.circuitwall.ml.platform.flink.evolution.operator.MutateGenerator;
import com.circuitwall.ml.platform.flink.evolution.operator.ScoreOperator;
import com.circuitwall.ml.algorithm.evolution.EvolutionAlgorithm;
import com.circuitwall.ml.platform.flink.evolution.operator.BestChildOperator;
import com.circuitwall.ml.platform.flink.evolution.operator.OffspringGenerator;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.GroupCombineOperator;
import org.apache.flink.api.java.operators.IterativeDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Project: evolution
 * Created by andrew on 09/08/16.
 */
public class EvolutionFlink implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(EvolutionFlink.class);
    private final EvolutionAlgorithmFlink algorithm;

    public EvolutionFlink(EvolutionAlgorithmFlink algorithm) {
        this.algorithm = algorithm;
    }

    public EvolutionFlink(EvolutionAlgorithm algorithm) {
        LOGGER.warn("NOTE:Wrapping an Evolution Algorithm into a Flink version will ignore reconfiguration.");
        this.algorithm = EvolutionAlgorithmFlink.wrap(algorithm);
    }

    public Comparable[] execute(ExecutionEnvironment env, int nrParents, int nrChildren, int rounds, Double mutationPercentage) throws Exception {
        List<Comparable[]> parents = Stream.generate(algorithm::generateParent).limit(nrParents).collect(Collectors.toList());
        IterativeDataSet<Comparable[]> source = env.fromCollection(parents).iterate(rounds).name("Source");
        GroupCombineOperator<ScoringResult, Comparable[]> generation = source
                //Reproduce
                .combineGroup(new OffspringGenerator(algorithm, nrChildren)).name("OffspringGenerator")
                //Mutate
                .map(new MutateGenerator(algorithm, mutationPercentage, (((nrChildren - nrParents) * 100 / (double) nrChildren) - mutationPercentage) / rounds)).name("MutateGenerator")
                //Score
                .partitionByHash(geneArray -> geneArray.getCompareablePayload()).map(new ScoreOperator(algorithm)).name("ScoreOperator")
                //Sort and remove bad children
                .combineGroup(new BestChildOperator(algorithm, nrParents)).name("BestChildOperator");
        DataSet<Comparable[]> result = source.closeWith(generation);
        List<Comparable[]> toReturn = result.collect();
        return toReturn != null && !toReturn.isEmpty() ? toReturn.get(0) : null;
    }
}

