package com.circuitwall.ml.algorithm.common.model;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Project: evolution
 * Created by andrew on 19/08/16.
 */
public class ComparableArray implements Comparable<ComparableArray> {
    private final Comparable[] content;

    public ComparableArray(Comparable[] content) {
        this.content = content;
    }

    @Override
    public int compareTo(ComparableArray o) {
        boolean ifEquals = o != null && o.content != null && Arrays.equals(content, o.content);
        return ifEquals ? 0 : -1;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
