package com.circuitwall.ml.algorithm.evolution;

import com.circuitwall.ml.algorithm.util.RandomUtil;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Project: evolution-com.circuitwall.ml.algorithm
 * Created by andrew on 14/02/16.
 */
public interface EvolutionAlgorithm extends Serializable {
    AtomicBoolean finished = new AtomicBoolean(false);

    /**
     * Process best child hook
     *
     * @param bestChild The best child configuration.
     * @param round     Which round this child is from.
     * @param score     the score made this child best child.
     */
    void processBestChild(Comparable[] bestChild, int round, double score);

    /**
     * Get initial group of seed as parents
     *
     * @return one parent configuration
     */
    Comparable[] generateParent();

    /**
     * Default implementation will keep sequence of parents
     *
     * @param parent1 One of the parents
     * @param parent2 One of the parents
     * @return Child from parent1 and 2
     */
    default Comparable[] procreate(Comparable[] parent1, Comparable[] parent2) {
        if (parent1 != null && parent1.length > 0 && parent2 != null && parent2.length > 0) {
            Comparable[] toReturn = parent1.clone();
            int fromParent1 = RandomUtil.getRandom().nextInt(parent1.length);
            System.arraycopy(parent2, fromParent1, toReturn, fromParent1, parent2.length - fromParent1);
            return toReturn;
        } else {
            throw new IllegalArgumentException("Invalid parents. " + Arrays.toString(parent1) + " " + Arrays.toString(parent2));
        }
    }

    /**
     * By a certain chance, a child can mutate
     *
     * @param orig Original configuration.
     * @return New configuration after mutation.
     */
    Comparable[] mutate(Comparable[] orig);

    /**
     * Put a score on a child for offspring selection
     *
     * @param individual An individual to be scored
     * @return score of that individual, higher score means better child
     */
    Double scoreIndividual(Comparable[] individual);


    /**
     * Optional check if the program can exit earlier
     *
     * @return if more rounds be needed
     */
    default boolean isFinished() {
        return finished.get();
    }
}

