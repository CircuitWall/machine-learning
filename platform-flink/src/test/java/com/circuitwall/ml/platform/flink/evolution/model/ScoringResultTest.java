package com.circuitwall.ml.platform.flink.evolution.model;

import com.circuitwall.ml.algorithm.common.model.GeneArray;
import com.circuitwall.ml.algorithm.common.model.ScoringResult;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Project: evolution
 * Created by andrew on 29/08/16.
 */
public class ScoringResultTest {

    @Test
    public void equals() throws Exception {
        ScoringResult scoringResult1 = new ScoringResult(new GeneArray(new Integer[]{0, 1, 1}), 0.3);//1
        ScoringResult scoringResult2 = new ScoringResult(new GeneArray(new Integer[]{0, 1, 1}), 0.4);//2

        ScoringResult scoringResult3 = new ScoringResult(new GeneArray(new Integer[]{0, 1, 2}), 0.4);//3

        ScoringResult scoringResult4 = new ScoringResult(new GeneArray(new Integer[]{1, 0, 1}), 0.4);//4

        ScoringResult scoringResult5 = new ScoringResult(new GeneArray(new Integer[]{0, 1, 1}), 0.4);
        Assert.assertTrue(scoringResult1.equals(scoringResult2));
        Set<ScoringResult> set = new HashSet<>();
        set.add(scoringResult1);
        set.add(scoringResult2);
        set.add(scoringResult3);
        set.add(scoringResult4);
        set.add(scoringResult5);
        Assert.assertEquals(4, set.size());
    }

}