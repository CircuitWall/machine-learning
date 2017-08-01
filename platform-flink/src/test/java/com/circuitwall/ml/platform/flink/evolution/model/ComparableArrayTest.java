package com.circuitwall.ml.platform.flink.evolution.model;

import com.circuitwall.ml.algorithm.common.model.ComparableArray;
import org.junit.Assert;
import org.junit.Test;

/**
 * Project: evolution
 * Created by andrew on 19/08/16.
 */
public class ComparableArrayTest {
    @Test
    public void compareTo() throws Exception {
        ComparableArray array1 = new ComparableArray(new Comparable[]{0.5, 1, 2, 3});
        ComparableArray array2 = new ComparableArray(new Comparable[]{0.5, 1, 2, 3});
        ComparableArray array3 = new ComparableArray(new Comparable[]{0.5, 1, 2, 4});
        ComparableArray array4 = new ComparableArray(new Comparable[]{"Something", 1, 2, 4});
        Assert.assertEquals(0,array1.compareTo(array2));
        Assert.assertNotEquals(0,array1.compareTo(array3));
        Assert.assertNotEquals(0,array1.compareTo(array4));
    }

}