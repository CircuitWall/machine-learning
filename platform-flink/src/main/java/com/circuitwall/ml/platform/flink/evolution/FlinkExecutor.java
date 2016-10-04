package com.circuitwall.ml.platform.flink.evolution;

import com.circuitwall.ml.algorithm.evolution.Executor;
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
public class FlinkExecutor implements Serializable,Executor {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlinkExecutor.class);
    private final ExecutionEnvironment env;

    public FlinkExecutor(ExecutionEnvironment env) {
        this.env=env;
    }

    @Override
    public Comparable[] execute(EvolutionAlgorithm algorithm, int nrParents, int nrChildren, int rounds, Double mutationPercentage) throws Exception {
        EvolutionAlgorithmFlink flinkAlgorithm = EvolutionAlgorithmFlink.wrap(algorithm);
        List<Comparable[]> parents = Stream.generate(algorithm::generateParent).limit(nrParents).collect(Collectors.toList());
        IterativeDataSet<Comparable[]> source = env.fromCollection(parents).iterate(rounds).name("Source");
        GroupCombineOperator<ScoringResult, Comparable[]> generation = source
                //Reproduce
                .combineGroup(new OffspringGenerator(flinkAlgorithm, nrChildren)).name("OffspringGenerator")
                //Mutate
                .map(new MutateGenerator(flinkAlgorithm, mutationPercentage, (((nrChildren - nrParents) * 100 / (double) nrChildren) - mutationPercentage) / rounds)).name("MutateGenerator")
                //Score
                .partitionByHash(geneArray -> geneArray.getCompareablePayload()).map(new ScoreOperator(flinkAlgorithm)).name("ScoreOperator")
                //Sort and remove bad children
                .combineGroup(new BestChildOperator(flinkAlgorithm, nrParents)).name("BestChildOperator");
        DataSet<Comparable[]> result = source.closeWith(generation);
        List<Comparable[]> toReturn = result.collect();
        return toReturn != null && !toReturn.isEmpty() ? toReturn.get(0) : null;
    }
}

