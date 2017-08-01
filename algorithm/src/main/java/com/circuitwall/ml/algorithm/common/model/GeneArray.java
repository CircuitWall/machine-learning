package com.circuitwall.ml.algorithm.common.model;

import java.io.Serializable;

/**
 * Project: evolution
 * Created by andrew on 10/08/16.
 */
public class GeneArray implements Serializable{
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
