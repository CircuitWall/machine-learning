package com.circuitwall.ml.algorithm.evolution;

import com.circuitwall.ml.algorithm.util.RandomUtil;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by andrew on 22/03/16.
 */
public class SmallestSumEA implements EvolutionAlgorithm {

    @Override
    public void processBestChild(Comparable[] bestChild, int round,double score) {
        System.out.println("Best child round:" + round + " score:" + scoreIndividual(bestChild) + " config:" + Arrays.toString(bestChild));
    }

    @Override
    public Comparable[] generateParent() {
        return new Comparable[]{
                Double.valueOf(RandomUtil.getRandom().nextInt(100)),
                Double.valueOf(RandomUtil.getRandom().nextInt(100)),
                Double.valueOf(RandomUtil.getRandom().nextInt(100)),
                Double.valueOf(RandomUtil.getRandom().nextInt(100)),
                Double.valueOf(RandomUtil.getRandom().nextInt(100)),
                Double.valueOf(RandomUtil.getRandom().nextInt(100)),
                Double.valueOf(RandomUtil.getRandom().nextInt(100)),
                Double.valueOf(RandomUtil.getRandom().nextInt(100)),
                Double.valueOf(RandomUtil.getRandom().nextInt(100)),
                Double.valueOf(RandomUtil.getRandom().nextInt(100)),
                Double.valueOf(RandomUtil.getRandom().nextInt(100)),
                Double.valueOf(RandomUtil.getRandom().nextInt(100))
        };
    }

    @Override
    public Comparable[] mutate(Comparable[] orig) {
        orig[RandomUtil.getRandom().nextInt(orig.length)] = (double) RandomUtil.getRandom().nextInt(100);
        return orig;
    }

    @Override
    public Double scoreIndividual(Comparable[] individual) {
        return 0 - Stream.of(individual).map(Double.class::cast).mapToDouble(Double::doubleValue).sum();
    }
}
