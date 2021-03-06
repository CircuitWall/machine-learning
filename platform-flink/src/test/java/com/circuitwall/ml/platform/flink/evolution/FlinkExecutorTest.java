package com.circuitwall.ml.platform.flink.evolution;

import com.circuitwall.ml.algorithm.test.evolution.TestAlgorithm;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.junit.Test;

import java.util.Arrays;

/**
 * Project: evolution
 * Created by andrew on 11/08/16.
 */
public class FlinkExecutorTest {
    @Test
    public void testRun() throws Exception {
        long start = System.currentTimeMillis();
        TestAlgorithm simulator = new TestAlgorithm();
        FlinkExecutor flinkExecutor = new FlinkExecutor(ExecutionEnvironment.getExecutionEnvironment());
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(10);
        Comparable[] finalResult = flinkExecutor.execute(simulator, 100, 5000, 100, 5D);
        System.out.println("Took:" + (System.currentTimeMillis() - start) + " Final result:" + Arrays.toString(finalResult));
    }
}