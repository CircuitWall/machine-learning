package com.circuitwall.ml.platform.monolithic.evolution;

import com.circuitwall.ml.algorithm.accelerator.EliteOffspringGenerator;
import com.circuitwall.ml.algorithm.evolution.EvolutionAlgorithm;
import com.circuitwall.ml.algorithm.evolution.Executor;
import com.circuitwall.ml.algorithm.util.RandomUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Project: machine-learning
 * Created by andrew on 2016-10-04.
 */
public class LocalExecutor implements Executor {
    @Override
    public Comparable[] execute(EvolutionAlgorithm algorithm, int nrParents, int nrChildren, int rounds, Double mutationPercentage) {
        List<Comparable[]> parents = Stream.generate(algorithm::generateParent).limit(nrParents).collect(Collectors.toList());
        RandomUtil.ProbabilityTester tester = RandomUtil.byProbability(mutationPercentage);
        for (int i = 0; i < rounds; i++) {
            final List<Comparable[]> finalParents = parents;
            Set<Comparable[]> children = Stream.generate(() -> EliteOffspringGenerator.generate(algorithm, finalParents)).limit(nrChildren).map(
                    child -> {
                        if (tester.test()) {
                            return algorithm.mutate(child);
                        } else {
                            return child;
                        }
                    }
            ).collect(Collectors.toSet());
            Map<Comparable[], Double> result = children.parallelStream().collect(Collectors.toConcurrentMap(Function.identity(), algorithm::scoreIndividual));
            List<Comparable[]> sortedChildren = result.entrySet().stream().sorted((o1, o2) -> Double.compare(o2.getValue(), o1.getValue())).map(Map.Entry::getKey).collect(Collectors.toList());
            algorithm.processBestChild(sortedChildren.get(0), i, result.get(sortedChildren.get(0)));
            parents = sortedChildren.subList(0, parents.size());
        }
        return parents != null && !parents.isEmpty() ? parents.get(0) : null;
    }
}
