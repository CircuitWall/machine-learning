package com.circuitwall.ml.algorithm.accelerator;

import com.circuitwall.ml.algorithm.common.model.ComparableArray;
import com.circuitwall.ml.algorithm.util.RandomUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class EliteBackFillProcessor {
    public static List<ComparableArray> backFill(List<ComparableArray> input) {
        ArrayList<ComparableArray> deduplicated = new ArrayList<>();
        for (ComparableArray comparableArray : input) {
            if (!deduplicated.contains(comparableArray)) {
                deduplicated.add(comparableArray);
            }
        }
        List<ComparableArray> newItems = Stream.generate(() -> deduplicated.get(RandomUtil.BiasedIntGenerator(2, deduplicated.size())))
                .limit(input.size() - deduplicated.size())
                .collect(Collectors.toList());
        deduplicated.addAll(newItems);
        return deduplicated;
    }

}
