package com.circuitwall.ml.platform.monolithic.evolution;

import com.circuitwall.ml.algorithm.test.evolution.ComplexEquationSolver;
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
        new LocalExecutor().execute(simulator, 1000, 5000, 100, 5D);
        System.out.println("Took:" + (System.currentTimeMillis() - start));
    }


    @Test
    public void execute2() throws Exception {
        long start = System.currentTimeMillis();
        ComplexEquationSolver simulator =
                new ComplexEquationSolver(8848D, "${val0}+${val1}*${val2}-Math.sqrt(${val3}+${val1})+Math.pow(${val4},${val2})", -1000, 1000);
        new LocalExecutor().execute(simulator, 1000, 5000, 100, 5D);
        System.out.println("Took:" + (System.currentTimeMillis() - start));
    }

    @Test
    public void execute3() throws Exception {
        long start = System.currentTimeMillis();
        ComplexEquationSolver simulator =
                new ComplexEquationSolver(2017D, "${val0}+${val1}+${val2}+${val3}", 0, 505);
        new LocalExecutor().execute(simulator, 100, 500, 100, 50D);
        System.out.println("Took:" + (System.currentTimeMillis() - start));
    }

}