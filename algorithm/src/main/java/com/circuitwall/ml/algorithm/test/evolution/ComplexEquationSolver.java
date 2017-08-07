package com.circuitwall.ml.algorithm.test.evolution;

import com.circuitwall.ml.algorithm.evolution.EvolutionAlgorithm;
import com.circuitwall.ml.algorithm.util.RandomUtil;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ComplexEquationSolver implements EvolutionAlgorithm {
    private final Double result;
    private final String equation;
    private final AtomicInteger nrParams = new AtomicInteger();
    private final int max;
    private final int min;
    private transient ScriptEngine engine;


    public ComplexEquationSolver(Double result, String equation, int rangMin, int rangMax) {
        this.result = result;
        this.equation = equation;
        if (rangMin > rangMax)
            throw new IllegalArgumentException(String.format("Invalid range %d -> %d", rangMin, rangMax));
        this.min = rangMin;
        this.max = rangMax;
        int nrParamsTmp = StringUtils.countMatches(this.equation, "${val");
        IntStream.range(0, nrParamsTmp).filter(value -> this.equation.contains("${val" + value + "}")).forEach(value -> this.nrParams.incrementAndGet());
        if (IntStream.range(0, this.nrParams.get()).anyMatch(value -> !this.equation.contains("${val" + value + "}"))) {
            throw new IllegalArgumentException("\"val\"s must NOT skip number. e.g ${val0} ${val1} ${val2} ${val3}");
        }

    }

    @Override
    public void processBestChild(Comparable[] bestChild, int round, double score) {
        if (!finished.get()) {
            if (score == 0) {
                System.out.println("Best child found in round:" + round + " config:" + Arrays.toString(bestChild));
                finished.set(true);
            } else {
                System.out.println("Best child round:" + round + " score:" + scoreIndividual(bestChild) + " config:" + Arrays.toString(bestChild));
            }
        }
    }

    private ScriptEngine getEngine() {
        if (this.engine == null) {
            ScriptEngineManager factory = new ScriptEngineManager();
            engine = factory.getEngineByName("JavaScript");
        }
        return engine;
    }

    @Override
    public Comparable[] generateParent() {
        return Stream.generate(() -> RandomUtil.getRandom().nextInt(max + 1 - min) + min)
                .limit(nrParams.get()).collect(Collectors.toList()).toArray(new Comparable[nrParams.get()]);
    }

    @Override
    public Comparable[] mutate(Comparable[] orig) {
        orig[RandomUtil.getRandom().nextInt(orig.length)] = RandomUtil.getRandom().nextInt(max + 1 - min) + min;
        return orig;
    }

    @Override
    public Double scoreIndividual(Comparable[] individual) {
        String filledEquation = equation;
        for (int i = 0; i < individual.length; i++) {
            filledEquation = filledEquation.replace("${val" + i + "}", individual[i].toString());
        }
        try {
            Double currentResult = Double.valueOf(getEngine().eval(filledEquation).toString());
            if (currentResult.isNaN())
                return -Double.MAX_VALUE;
            else
                return -Math.abs(result - currentResult);
        } catch (ScriptException e) {
            e.printStackTrace();
            return -Double.MAX_VALUE;
        }
    }
}
