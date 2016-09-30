package com.circuitwall.ml.platform.flink.evolution.model;

/**
 * Project: evolution
 * Created by andrew on 10/08/16.
 */
public class GeneArray {
    private Comparable[] payload;

    public GeneArray() {
    }

    public GeneArray(Comparable[] payload) {
        this.payload = payload;
    }

    public Comparable[] getPayload() {
        return payload;
    }

    public ComparableArray getCompareablePayload() {
        return new ComparableArray(getPayload());
    }

    public void setPayload(Comparable[] payload) {
        this.payload = payload;
    }

    @Override
    public int hashCode() {
        return getCompareablePayload().hashCode();
    }
}
