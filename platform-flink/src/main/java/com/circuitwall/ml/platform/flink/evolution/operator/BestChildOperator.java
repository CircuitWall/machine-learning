package com.circuitwall.ml.platform.flink.evolution.operator;

import com.circuitwall.ml.platform.flink.evolution.algorithm.EvolutionAlgorithmFlink;
import com.circuitwall.ml.platform.flink.evolution.exception.ConfigurationException;
import com.circuitwall.ml.platform.flink.evolution.model.ScoringResult;
import org.apache.commons.lang3.RandomUtils;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.functions.RichGroupCombineFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Project: evolution
 * Created by andrew on 10/08/16.
 */
public class BestChildOperator extends RichGroupCombineFunction<ScoringResult, Comparable[]> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BestChildOperator.class);
    private final EvolutionAlgorithmFlink algorithm;
    private final int limit;
    private final double eliteProportion;

    public BestChildOperator(EvolutionAlgorithmFlink algorithm, int nrParents) {
        this(algorithm, nrParents, 0.1);
    }

    public BestChildOperator(EvolutionAlgorithmFlink algorithm, int nrParents, double eliteProportion) {
        this.algorithm = algorithm;
        this.limit = nrParents;
        if (eliteProportion > 1 || eliteProportion < 0) {
            throw new ConfigurationException("Elite should be a number between 0 to 1");
        }
        this.eliteProportion = eliteProportion;
    }


    @Override
    public void combine(Iterable<ScoringResult> values, Collector<Comparable[]> out) throws Exception {
        List<ScoringResult> bestChildren = StreamSupport.stream(values.spliterator(), false).sorted((o1, o2) -> Double.compare(o2.getScore(), o1.getScore())).limit(limit).distinct().collect(Collectors.toList());
        ScoringResult theBest = bestChildren.get(0);
        LOGGER.info("{} duplicates found, fill with the best child", limit - bestChildren.size());
        algorithm.processBestChild(theBest.getChild().getPayload(), getIterationRuntimeContext().getSuperstepNumber(), theBest.getScore());
        while (bestChildren.size() < limit) {
            int elite = RandomUtils.nextInt(0, RandomUtils.nextInt(0, (int) (bestChildren.size() * eliteProportion)));
            // System.out.println("Backfilling elite:"+elite);
            bestChildren.add(bestChildren.get(elite));
        }
        bestChildren.forEach(current -> out.collect(current.getChild().getPayload()));
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        ExecutionConfig.GlobalJobParameters config = getRuntimeContext().getExecutionConfig().getGlobalJobParameters();
        if (config != null) {
            algorithm.applyConfig(ParameterTool.class.cast(config).getConfiguration());
        }
    }
}
