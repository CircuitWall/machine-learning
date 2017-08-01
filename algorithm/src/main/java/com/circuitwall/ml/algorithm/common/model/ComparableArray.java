package com.circuitwall.ml.algorithm.common.model;

import org.apache.commons.lang3.builder.HashCodeBuilder;

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
        if (content.length == o.content.length) {
            final AtomicInteger toReturn = new AtomicInteger();
            IntStream.range(0, content.length).forEach(
                    value -> {
                        Comparable mine = this.content[value];
                        Comparable other = o.content[value];
                        if (mine.getClass() == other.getClass()) {
                            toReturn.addAndGet(mine.compareTo(other));
                        } else {
                            toReturn.addAndGet(-1);
                        }
                    }
            );
            return toReturn.get();
        } else {
            return -1;
        }
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
