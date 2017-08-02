package com.circuitwall.ml.algorithm.accelerator;

import com.circuitwall.ml.algorithm.evolution.EvolutionAlgorithm;
import com.circuitwall.ml.algorithm.util.RandomUtil;

import java.util.List;

public class EliteOffspringGenerator {
    /**
     * @param algorithm
     * @param parents
     * @return
     */
    public static Comparable[] generate(EvolutionAlgorithm algorithm, List<Comparable[]> parents) {
        int p1Index = RandomUtil.BiasedIntGenerator(2, parents.size());
        Comparable[] parent1 = parents.get(p1Index);
        int p2Index = p1Index == 0 ? 1 : RandomUtil.getRandom().nextInt(p1Index);
        Comparable[] parent2 = parents.get(p2Index);
        return algorithm.procreate(parent1, parent2);
    }
}
