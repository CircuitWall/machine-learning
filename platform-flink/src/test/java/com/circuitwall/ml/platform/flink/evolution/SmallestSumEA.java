package com.circuitwall.ml.platform.flink.evolution;

import com.circuitwall.ml.algorithm.evolution.EvolutionAlgorithm;
import com.circuitwall.ml.algorithm.util.RandomUtil;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by andrew on 22/03/16.
 */
public class SmallestSumEA implements EvolutionAlgorithm {
    private boolean bestChildFound = false;

    @Override
    public void processBestChild(Comparable[] bestChild, int round, double score) {
        if(!bestChildFound){
            if (score == 0) {
                System.out.println("Best child found in round:" + round);
                bestChildFound = true;
            } else {
                System.out.println("Best child round:" + round + " score:" + scoreIndividual(bestChild) + " config:" + Arrays.toString(bestChild));
            }
        }
    }

    @Override
    public Comparable[] generateParent() {
        return new Comparable[]{
                100,
                100,
                100,
                100,
                100,
                100,
                100,
                100,
                100,
                100,
                100,
                100,
                100,
                100,
        };
    }

    @Override
    public Comparable[] mutate(Comparable[] orig) {
        orig[RandomUtil.getRandom().nextInt(orig.length)] = RandomUtil.getRandom().nextInt(100);
        return orig;
    }

    @Override
    public Double scoreIndividual(Comparable[] individual) {
        return 0 - Stream.of(individual).map(Integer.class::cast).mapToDouble(Integer::doubleValue).sum();
    }
}
