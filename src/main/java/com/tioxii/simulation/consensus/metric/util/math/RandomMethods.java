package com.tioxii.simulation.consensus.metric.util.math;

import java.util.List;
import java.util.Random;

/**
 * This class contains collections functions that are used for random operations in the simulation.
 */
public class RandomMethods {
    
    /**
     * Selects a random subset of the given object expect the excluded objects.
     * @param objects to be selected from.
     * @param size of the subset.
     * @param excludedObjects object excluded from the draw.
     * @return A subset of the given size without the excluded objects.
     */
    public static Object[] subsetOf(Object[] objects, int size, Object[] excludedObjects) {
        
        /* If the size of the subset is larger than the objects array, return null. */
        if(objects.length - excludedObjects.length < size) { return null; }
        
        /* Initializes the return subset, a list of excluded objects and then randomizer. */
        Object[] subset = new Object[size];
        List<Object> list = List.of(excludedObjects);
        Random r = new Random();
        
        /* While the subset is not full, add a random object from the objects array. */
        while(size != 0) {

            /* Get a random index from the objects array. */
            int i = r.nextInt(objects.length);

            /* Check if the object at the random index is not in the excluded list. */
            /* If it is not, add it to the subset and also add it to the excluded list. */
            /* This is to ensure that the same object is not selected twice. */
            if(!list.contains(objects[i])) {
                list.add(objects[i]);
                subset[size - 1] = objects[i];
                size--;
            }
        }

        return subset;
    }

    /**
     * Flip a coin. Uses pseudorandom numbers.
     * @param beta is the chance of getting head.
     * @return True if head. False if Tail.
     */
    public static boolean flipCoin(double beta) {
        if(Math.random() < beta) {
            return true;
        }
        return false;
    }
}
