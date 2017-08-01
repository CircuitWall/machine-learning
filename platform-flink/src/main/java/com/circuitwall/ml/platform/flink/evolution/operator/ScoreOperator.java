package com.circuitwall.ml.platform.flink.evolution.operator;

import com.circuitwall.ml.algorithm.common.model.GeneArray;
import com.circuitwall.ml.algorithm.common.model.ScoringResult;
import com.circuitwall.ml.platform.flink.evolution.algorithm.EvolutionAlgorithmFlink;import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.com.google.common.cache.Cache;
import org.apache.flink.shaded.com.google.common.cache.CacheBuilder;

/**
 * Project: evolution
 * Created by andrew on 10/08/16.
 */
public class ScoreOperator extends RichMapFunction<GeneArray, ScoringResult> {

    private final Cache<Integer, Double> scoreCache;
    private final EvolutionAlgorithmFlink algorithm;

    public ScoreOperator(EvolutionAlgorithmFlink algorithm) {
        this.algorithm = algorithm;
        scoreCache = CacheBuilder.newBuilder().maximumSize(1000000).build();
    }

    @Override
    public ScoringResult map(GeneArray geneArray) throws Exception {
        Double score = scoreCache.get(geneArray.getCompareablePayload().hashCode(), () -> algorithm.scoreIndividual(geneArray.getPayload()));
        return new ScoringResult(geneArray, score);
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        ExecutionConfig.GlobalJobParameters config = getRuntimeContext().getExecutionConfig().getGlobalJobParameters();
        if (config != null) {
            algorithm.applyConfig(ParameterTool.class.cast(config).getConfiguration());
        }
    }
}
