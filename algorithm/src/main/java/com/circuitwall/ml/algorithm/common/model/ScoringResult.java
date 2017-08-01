package com.circuitwall.ml.algorithm.common.model;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * Project: evolution
 * Created by andrew on 10/08/16.
 */
public class ScoringResult implements Serializable, Comparable<ScoringResult> {

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
        if (o != null) {
            return getChild().getCompareablePayload().compareTo(o.getChild().getCompareablePayload());
        } else {
            return Integer.MIN_VALUE;
        }
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
