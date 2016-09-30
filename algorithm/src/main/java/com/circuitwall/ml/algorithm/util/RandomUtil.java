package com.circuitwall.ml.algorithm.util;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by andrew on 22/03/16.
 */
public class RandomUtil {
    private static Random randomGenerator = new Random();

    public static Random getRandom() {
        return randomGenerator;
    }

    public static ProbabilityTester byProbability(double percentage) {
        int idx = Double.toString(percentage).indexOf(".");
        int magnify = 0;
        if (idx != -1) {
            magnify = Double.toString(percentage).substring(idx + 1).length();
        }
        Double base = 100D;
        Double range = percentage;
        if (magnify > 0) {
            base = base * Math.pow(10, magnify);
            range = percentage * Math.pow(10, magnify);
        }
        return new ProbabilityTester(base.intValue(), range.intValue());
    }

    public static class ProbabilityTester implements Serializable {
        private final int base;
        private final int trueRange;

        private ProbabilityTester(int base, int trueRange) {
            this.base = base;
            this.trueRange = trueRange;
        }

        public boolean test() {
            return getRandom().nextInt(base) < trueRange;
        }
    }
}
