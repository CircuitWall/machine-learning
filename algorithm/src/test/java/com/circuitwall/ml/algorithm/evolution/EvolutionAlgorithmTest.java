package com.circuitwall.ml.algorithm.evolution;

import org.junit.Test;

/**
 * Created by andrew on 22/03/16.
 */
public class EvolutionAlgorithmTest {
    @Test
    public void testRun() throws Exception {
        long start = System.currentTimeMillis();
        SmallestSumEA simulator = new SmallestSumEA();
        simulator.execute(1000, 5000, 100, 5D);
        System.out.println("Took:" + (System.currentTimeMillis() - start));
    }
}