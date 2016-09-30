package com.circuitwall.ml.platform.flink.evolution;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.junit.Test;

import java.util.Arrays;

/**
 * Project: evolution
 * Created by andrew on 11/08/16.
 */
public class EvolutionFlinkTest {
    @Test
    public void testRun() throws Exception {
        long start = System.currentTimeMillis();
        SmallestSumEA simulator = new SmallestSumEA();
        EvolutionFlink evolutionFlink = new EvolutionFlink(simulator);
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(10);
        Comparable[] finalResult = evolutionFlink.execute(env, 100, 5000, 100, 5D);
        System.out.println("Took:" + (System.currentTimeMillis() - start) + " Final result:" + Arrays.toString(finalResult));
    }

}