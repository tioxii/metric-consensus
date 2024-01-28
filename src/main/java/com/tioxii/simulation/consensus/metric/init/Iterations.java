package com.tioxii.simulation.consensus.metric.init;

public class Iterations {
    
    /**
     * Generate an Array that contains the number of participating nodes. Increments are exponential to base 10.
     * @param start inclusive
     * @param end exclusive
     * @param steps Increment value. Must be greater than 0.
     * @return Array that indicates the number of nodes that should be used for the simulation process.
     */
    public static int[] iterationsExponential(int start, int end, int steps) {
        
        /* Check if the parameters are valid */
        if(start > end && end <= 9) {
            return new int[0];
        }
        
        /* Generate the array */
        int[] numbersOfNodes = new int[end - start];
        for(int i = 0; i < numbersOfNodes.length; i += steps) {
            numbersOfNodes[i] = (int) Math.pow(10, i + start);
        }
        return numbersOfNodes;
    }

    /**
     * Generate an Array that contains the number of participating nodes. Increments are exponential to base 10.
     * @param start Inclusive. Start value must be greater than 0.
     * @param end Exclusive. End value must be greater than 0.
     * @param steps Increment value. Must be greater than 0.
     * @return Array that indicates the number of nodes that should be used for the simulation process.
     */
    public static int[] iterationsExpLin(int start, int end, int steps) {
        
        /* Check if the parameters are valid */
        if(start > end) {
            return new int[0];
        }

        /* Generate the array */
        int[] numbersOfNodes = new int[(end - start) * 9];
        for(int i = 0; i < end - start; i += steps) {
            for(int j = 0; j < 9; j++) {
                numbersOfNodes[i * 9 + j] = (int) Math.pow(10, i + start) * (j + 1);
            }
        }
        return numbersOfNodes;
    }

    /**
     * Generate an Array that contains the number of participating nodes. Increments are linear.
     * @param start Inclusive. Start value must be greater than 0.
     * @param end Exclusive. End value must be greater than 0.
     * @param steps Increment value. Must be greater than 0.
     * @return Array that indicates the number of nodes that should be used for the simulation process.
     */
    public static int[] iterationsLinear(int start, int end, int steps) {
        
        /* Check if the parameters are valid */
        if(start > end && end < (int) Math.pow(2, 16)) {
            return new int[0];
        }
        
        /* Generate the array */
        int[] numbersOfNodes = new int[end - start];
        for(int i = 0; i < end - start; i += steps) {
            numbersOfNodes[i] = start + i;
        }

        return numbersOfNodes;
    }
}
