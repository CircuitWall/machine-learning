package com.circuitwall.ml.algorithm.evolution;

/**
 * Project: machine-learning
 * Created by andrew on 2016-10-04.
 */
public interface Executor {
    Comparable[] execute(EvolutionAlgorithm algorithm, int nrParents, int nrChildren, int rounds, Double mutationPercentage) throws Exception;
}
