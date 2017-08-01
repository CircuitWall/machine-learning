package com.circuitwall.ml.platform.spark.evolution;

import com.circuitwall.ml.algorithm.test.evolution.TestAlgorithm;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;

public class SparkExecutorTest {
    @org.junit.Test
    public void execute() throws Exception {
        long start = System.currentTimeMillis();
        TestAlgorithm simulator = new TestAlgorithm();
        SparkConf conf=new SparkConf();
        conf.setMaster("local");
        JavaSparkContext context = new JavaSparkContext("local","EA");
        SparkExecutor executor = new SparkExecutor(context);
        Comparable[] finalResult = executor.execute(simulator, 100, 5000, 100, 5D);
        System.out.println("Took:" + (System.currentTimeMillis() - start) + " Final result:" + Arrays.toString(finalResult));

    }

}