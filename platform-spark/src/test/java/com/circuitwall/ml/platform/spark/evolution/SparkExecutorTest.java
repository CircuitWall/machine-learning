package com.circuitwall.ml.platform.spark.evolution;

import com.circuitwall.ml.algorithm.test.evolution.TestAlgorithm;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

public class SparkExecutorTest {
    @Test
    public void execute() throws Exception {
        long start = System.currentTimeMillis();
        TestAlgorithm simulator = new TestAlgorithm();
        SparkConf conf = new SparkConf();
        conf.setMaster("local");
        JavaSparkContext context = new JavaSparkContext("local", "EA");
        SparkExecutor executor = new SparkExecutor(context, 10);
        Comparable[] finalResult = executor.execute(simulator, 100, 5000, 100, 5D);
        System.out.println("Took:" + (System.currentTimeMillis() - start) + " Final result:" + Arrays.toString(finalResult));
    }

    @Test
    @Ignore
    public void testCluster() throws Exception {

        long start = System.currentTimeMillis();
        TestAlgorithm simulator = new TestAlgorithm();
        JavaSparkContext context = new JavaSparkContext("spark://127.0.0.1:7077", "EA");
        SparkExecutor executor = new SparkExecutor(context, 10);
        Comparable[] finalResult = executor.execute(simulator, 100, 5000, 100, 5D);
        System.out.println("Took:" + (System.currentTimeMillis() - start) + " Final result:" + Arrays.toString(finalResult));
    }
}