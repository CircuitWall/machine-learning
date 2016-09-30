package com.circuitwall.ml.algorithm.util;

import org.junit.Test;

import java.util.stream.IntStream;

/**
 * Created by andrew on 22/03/16.
 */
public class RandomUtilTest {
    @Test
    public void testProbabilityTester() throws Exception {
        RandomUtil.ProbabilityTester tester = RandomUtil.byProbability(0.35);
        final int[] trueCases = {0};
        int total = 10000000;
        IntStream.range(0, total).forEach(value -> {
            if (tester.test()) trueCases[0]++;
        });
        double result = ((double) trueCases[0] / total) * 100;
        System.out.println("Actual percentage:" + result + "%");
        //  Assert.assertEquals(3.5, trueCases[0] / total);
    }
}