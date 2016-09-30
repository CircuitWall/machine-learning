package com.circuitwall.ml.platform.flink.evolution.model;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Project: evolution
 * Created by andrew on 10/08/16.
 */
public class ScoringResult implements Comparable<ScoringResult> {

    private GeneArray child;
    private Double score;

    public ScoringResult() {
    }

    public ScoringResult(GeneArray child, Double score) {
        this.child = child;
        this.score = score;
    }

    public GeneArray getChild() {
        return child;
    }

    public Double getScore() {
        return score;
    }

    public void setChild(GeneArray child) {
        this.child = child;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public int compareTo(ScoringResult o) {
        return getChild().getCompareablePayload().compareTo(o.getChild().getCompareablePayload());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ScoringResult) {
            return compareTo((ScoringResult) obj) == 0;
        } else {
            return super.equals(obj);
        }
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
