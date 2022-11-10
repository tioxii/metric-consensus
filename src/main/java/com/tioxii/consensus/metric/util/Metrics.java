package com.tioxii.consensus.metric.util;

public class Metrics {
    
    /**
     * Calculate the distance with the euclidean norm
     * @param vectorOne
     * @param vectorTwo
     * @return
     */
    public static double getDistanceEuclidean(double[] vectorOne, double[] vectorTwo) { 
        //TODO throw Exception
        if(!(vectorOne.length == vectorTwo.length)) { return 0.0; }
        
        double distance = 0.0f;
        for (int i = 0; i < vectorOne.length; i++) {
            distance += Math.pow(vectorOne[i] - vectorTwo[i], 2);
        }
        return (double) Math.sqrt(distance);
     }
    
    /**
     * Calculate the distance with the first norm
     * @param vectorOne
     * @param vectorTwo
     * @return
     */
    public static double getDistanceFirstNorm(double[] vectorOne, double[] vectorTwo) { 
        //TODO throw Exception
        if(!(vectorOne.length == vectorTwo.length)) { return 0.0; }

        double distance = 0;
        
        for(int i = 0; i < vectorOne.length; i++) {
            distance += Math.abs(vectorOne[i] - vectorTwo[i]);
        }
        return distance;
    }

}
