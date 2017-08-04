package com.circuitwall.ml.platform.spark.evolution;

import com.circuitwall.ml.algorithm.test.evolution.ComplexEquationAlgorithm;
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
    public void execute2() throws Exception {

        long start = System.currentTimeMillis();
        ComplexEquationAlgorithm simulator = new ComplexEquationAlgorithm(8848D, "${val0}+${val1}*${val2}-Math.sqrt(${val3}+${val1})", 0, 100);
        SparkConf conf = new SparkConf();
        conf.setMaster("local");
        JavaSparkContext context = new JavaSparkContext("local", "EA");
        SparkExecutor executor = new SparkExecutor(context, 10);
        Comparable[] finalResult = executor.execute(simulator, 100, 5000, 100, 5D);
        System.out.println("Took:" + (System.currentTimeMillis() - start) + " Final result:" + Arrays.toString(finalResult));
    }
}