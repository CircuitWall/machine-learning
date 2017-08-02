package com.circuitwall.ml.algorithm.util;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    @Test
    public void testBiasedRandomGenerator() throws Exception {

        List<Integer> result = Stream.generate(() -> RandomUtil.BiasedIntGenerator(1, 20)).limit(500).collect(Collectors.toList());
        System.out.println("Unbiased result");
        printDistribution(result);
        result = Stream.generate(() -> RandomUtil.BiasedIntGenerator(2, 20)).limit(500).collect(Collectors.toList());
        System.out.println("2 rounds result");
        printDistribution(result);
        result = Stream.generate(() -> RandomUtil.BiasedIntGenerator(3, 20)).limit(500).collect(Collectors.toList());
        System.out.println("3 rounds result");
        printDistribution(result);
    }

    private void printDistribution(List<Integer> input) {
        if (input != null && input.size() > 0) {
            Collections.sort(input);
            int min = input.get(0);
            int max = input.get(input.size() - 1);
            Map<Integer, AtomicInteger> distribution = new HashMap<>();
            input.forEach(integer -> {
                if (!distribution.containsKey(integer)) {
                    distribution.put(integer, new AtomicInteger());
                }
                distribution.get(integer).incrementAndGet();
            });
            IntStream.range(min, max + 1).forEach(value -> {
                AtomicInteger atomicCount = distribution.get(value);
                int count = atomicCount == null ? 0 : atomicCount.get();
                StringBuilder sizeString = new StringBuilder();
                IntStream.range(0, count).forEach(value1 -> sizeString.append("#"));
                System.out.println(value + ":\t" + sizeString.toString());
            });
        }
    }
}