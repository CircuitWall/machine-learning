package com.circuitwall.ml.platform.flink.evolution.operator;

import com.circuitwall.ml.platform.flink.evolution.algorithm.EvolutionAlgorithmFlink;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.functions.RichGroupCombineFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Project: evolution
 * Created by andrew on 10/08/16.
 */
public class OffspringGenerator extends RichGroupCombineFunction<Comparable[], Comparable[]> {
    private static final Logger LOGGER = LoggerFactory.getLogger(OffspringGenerator.class);
    private final EvolutionAlgorithmFlink algorithm;
    private final int nrChildren;

    public OffspringGenerator(EvolutionAlgorithmFlink algorithm, int nrChildren) {
        this.algorithm = algorithm;
        this.nrChildren = nrChildren;
    }

    private Comparable[] generate(List<Comparable[]> parents) {
        return algorithm.procreate(algorithm.anyItem(parents), algorithm.anyItem(parents));
    }

    @Override
    public void combine(Iterable<Comparable[]> iterable, Collector<Comparable[]> collector) throws Exception {
        List<Comparable[]> parents = new ArrayList<>();
        iterable.forEach(parents::add);
        LOGGER.info("{} parents received and");
        Stream.generate(() -> generate(parents)).limit(nrChildren).forEach(collector::collect);
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        ExecutionConfig.GlobalJobParameters config = getRuntimeContext().getExecutionConfig().getGlobalJobParameters();
        if(config!=null){
            algorithm.applyConfig(ParameterTool.class.cast(config).getConfiguration());
        }
    }
}
