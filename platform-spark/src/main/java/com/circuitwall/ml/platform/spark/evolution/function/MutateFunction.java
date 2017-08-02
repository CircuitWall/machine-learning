package com.circuitwall.ml.platform.spark.evolution.function;

import com.circuitwall.ml.algorithm.common.model.GeneArray;
import com.circuitwall.ml.algorithm.evolution.EvolutionAlgorithm;
import com.circuitwall.ml.algorithm.util.RandomUtil;
import org.apache.spark.api.java.function.Function;

public class MutateFunction implements Function<Comparable[], GeneArray> {
    private final RandomUtil.ProbabilityTester tester;
    private final EvolutionAlgorithm algorithm;

    public MutateFunction(EvolutionAlgorithm algorithm,Double mutationPercentage) {
        this.tester = RandomUtil.byProbability(mutationPercentage);
        this.algorithm=algorithm;
    }

    @Override
    public GeneArray call(Comparable[] input) throws Exception {

        if (tester.test()) {
            return new GeneArray(algorithm.mutate(input));
        } else {
            return new GeneArray(input);
        }
    }
}
