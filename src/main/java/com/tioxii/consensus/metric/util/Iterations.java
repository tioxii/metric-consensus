package com.tioxii.consensus.metric.util;

public class Iterations {
    
    /**
     * Generate an Array that contains the number of participating nodes. Increments are exponential to base 10.
     * @param start inclusive
     * @param end exclusive
     * @param steps
     * @return
     * @throws Exception
     */
    public static int[] iterationsExponential(int start, int end, int steps) {
        if(start > end && end <= 9) {
            return new int[0];
        }
        
        int[] ret = new int[end - start];
        for(int i = 0; i < ret.length; i += steps) {
            ret[i] = (int) Math.pow(10, i + start);
        }

        return ret;
    }

    /**
     * Generate an Array that contains the number of participating nodes. Increments are linear.
     * @param start inclusive
     * @param end exclusive
     * @param steps
     * @return
     */
    public static int[] iterationsLinear(int start, int end, int steps) {
        if(start > end && end < (int) Math.pow(2, 16)) {
            return new int[0];
        }
        
        int[] ret = new int[end - start];
        for(int i = 0; i < end - start; i += steps) {
            ret[i] = start + i;
        }

        return ret;
    }
}
