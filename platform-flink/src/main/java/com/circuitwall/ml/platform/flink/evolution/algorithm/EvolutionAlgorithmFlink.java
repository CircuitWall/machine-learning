package com.circuitwall.ml.platform.flink.evolution.algorithm;

import com.circuitwall.ml.algorithm.evolution.EvolutionAlgorithm;
import org.apache.flink.configuration.Configuration;

/**
 * Project: evolution
 * Created by andrew on 16/08/16.
 */
public interface EvolutionAlgorithmFlink extends EvolutionAlgorithm {
    void applyConfig(Configuration parameters);

    static EvolutionAlgorithmFlink wrap(EvolutionAlgorithm evolutionAlgorithm) {
        return new DelegateEvolutionAlgorithmFlink(evolutionAlgorithm);
    }
}
