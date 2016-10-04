package com.circuitwall.ml.platform.monolithic.evolution;

import com.circuitwall.ml.algorithm.test.evolution.TestAlgorithm;
import org.junit.Test;

/**
 * Project: machine-learning
 * Created by andrew on 2016-10-04.
 */
public class LocalExecutorTest {
    @Test
    public void execute() throws Exception {
        long start = System.currentTimeMillis();
        TestAlgorithm simulator = new TestAlgorithm();
        new LocalExecutor().execute(simulator,1000, 5000, 100, 5D);
        System.out.println("Took:" + (System.currentTimeMillis() - start));
    }

}