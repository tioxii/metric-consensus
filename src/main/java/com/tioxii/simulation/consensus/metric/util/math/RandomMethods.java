package com.tioxii.simulation.consensus.metric.util.math;

import java.util.List;
import java.util.Random;

public class RandomMethods {
    
    /**
     * Selects a random subset of the given object expect the excluded objects.
     * @param objects to be selected from.
     * @param size of the subset.
     * @param excludedObjects object excluded from the draw.
     * @return a subset of the given size without the excluded objects.
     */
    public static Object[] subsetOf(Object[] objects, int size, Object[] excludedObjects) {
        if(objects.length - excludedObjects.length < size) { return null; }
        
        Object[] subset = new Object[size];
        List<Object> list = List.of(excludedObjects);
        Random r = new Random();
        
        while(size != 0) {
            int i = r.nextInt(objects.length);
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
