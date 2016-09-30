package com.circuitwall.ml.platform.flink.evolution.operator;

import com.circuitwall.ml.platform.flink.evolution.algorithm.EvolutionAlgorithmFlink;
import com.circuitwall.ml.platform.flink.evolution.model.GeneArray;
import com.circuitwall.ml.algorithm.util.RandomUtil;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.hadoop.shaded.org.jboss.netty.util.internal.ConcurrentHashMap;

import java.util.Map;

/**
 * Project: evolution
 * Created by andrew on 10/08/16.
 */
public class MutateGenerator extends RichMapFunction<Comparable[], GeneArray> {
    private final Double mutationPercentage;
    private final Double mutationGrowthRate;
    private final EvolutionAlgorithmFlink algorithm;
    private transient Map<Double, RandomUtil.ProbabilityTester> testers;

    public MutateGenerator(EvolutionAlgorithmFlink algorithm, Double mutationPercentage) {
        this(algorithm, mutationPercentage, 0D);
    }

    public MutateGenerator(EvolutionAlgorithmFlink algorithm, Double mutationPercentage, Double mutationGrowthRate) {
        this.mutationPercentage = mutationPercentage;
        this.mutationGrowthRate = mutationGrowthRate;
        this.algorithm = algorithm;
    }

    @Override
    public GeneArray map(Comparable[] child) throws Exception {
        if (getTester().test()) {
            return new GeneArray(algorithm.mutate(child));
        } else {
            return new GeneArray(child);
        }
    }


    private RandomUtil.ProbabilityTester getTester() {
        double currentRound = getIterationRuntimeContext().getSuperstepNumber();
        if (testers == null) testers = new ConcurrentHashMap<>();
        if (!testers.containsKey(currentRound)) {
            testers.put(currentRound, RandomUtil.byProbability(mutationPercentage + currentRound * mutationGrowthRate));
        }
        return testers.get(currentRound);
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        ExecutionConfig.GlobalJobParameters config = getRuntimeContext().getExecutionConfig().getGlobalJobParameters();
        if (config != null) {
            algorithm.applyConfig(ParameterTool.class.cast(config).getConfiguration());
        }
    }
}
