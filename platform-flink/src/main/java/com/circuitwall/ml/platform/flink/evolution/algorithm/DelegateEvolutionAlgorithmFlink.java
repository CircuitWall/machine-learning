package com.circuitwall.ml.platform.flink.evolution.algorithm;

import com.circuitwall.ml.algorithm.evolution.EvolutionAlgorithm;
import org.apache.flink.configuration.Configuration;

/**
 * Project: evolution
 * Created by andrew on 16/08/16.
 */
public class DelegateEvolutionAlgorithmFlink implements EvolutionAlgorithmFlink {
    private final EvolutionAlgorithm delegate;

    public DelegateEvolutionAlgorithmFlink(EvolutionAlgorithm delegate) {
        this.delegate = delegate;
    }

    @Override
    public void applyConfig(Configuration parameters) {

    }

    @Override
    public void processBestChild(Comparable[] bestChild, int round, double score) {
        delegate.processBestChild(bestChild, round, score);
    }

    @Override
    public Comparable[] generateParent() {
        return delegate.generateParent();
    }

    @Override
    public Comparable[] mutate(Comparable[] orig) {
        return delegate.mutate(orig);
    }

    @Override
    public Double scoreIndividual(Comparable[] individual) {
        return delegate.scoreIndividual(individual);
    }

    @Override
    public Comparable[] procreate(Comparable[] parent1, Comparable[] parent2) {
        return delegate.procreate(parent1, parent2);
    }
}
