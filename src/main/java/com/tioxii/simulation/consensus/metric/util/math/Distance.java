package com.tioxii.simulation.consensus.metric.util.math;

import com.tioxii.simulation.consensus.metric.util.math.exceptions.DifferentDimensionsException;

/**
 * This class contains methods to calculate distances between points. 
 */
public class Distance {

    //double

    /**
     * Calculate the euclidean distance of two points, represented as double arrays.
     * @param pointOne is the first point.
     * @param pointTwo is the second point.
     * @return the euclidean distance
     * @throws DifferentDimensionsException when the two arrays have not the same length.
     */
    public static double getDistanceEuclidean(double[] pointOne, double[] pointTwo) throws DifferentDimensionsException {
        if(!(pointOne.length == pointTwo.length)) { throw new DifferentDimensionsException("The points have different dimensions"); }
        
        double distance = 0;
        for (int i = 0; i < pointOne.length; i++) {
            distance += Math.pow(pointOne[i] - pointTwo[i], 2);
        }
        return Math.sqrt(distance);
    }

    /**
     * Calculate the euclidean distance of two points, represented as double arrays.
     * This method does not check if the two arrays have the same length.
     * @param pointOne is the first point.
     * @param pointTwo is the second point.
     * @return the euclidean distance
     */
    public static double getDistanceEuclideanWithOutCheck(double[] pointOne, double[] pointTwo) {
        double distance = 0;
        for (int i = 0; i < pointOne.length; i++) {
            distance += Math.pow(pointOne[i] - pointTwo[i], 2);
        }
        return (double) Math.sqrt(distance);
    }

    /**
     * Calculate the first-norm distance of two points, represented as double arrays.
     * @param pointOne is the first point.
     * @param pointTwo is the second point.
     * @return the first-norm distance
     * @throws DifferentDimensionsException when the two arrays have not the same length.
     */
    public static double getDistanceFirstNorm(double[] pointOne, double[] pointTwo) throws DifferentDimensionsException {
        if(!(pointOne.length == pointTwo.length)) { throw new DifferentDimensionsException("The points have different dimensions"); }

        double distance = 0;
        for(int i = 0; i < pointOne.length; i++) {
            distance += Math.abs(pointOne[i] - pointTwo[i]);
        }
        return distance;
    }

    //int

    /**
     * Calculate the first-norm distance of two points, represented as integer arrays.
     * @param pointOne is the first point.
     * @param pointTwo is the second point.
     * @return the first-norm distance.
     * @throws DifferentDimensionsException when the two arrays have not the same length.
     */
    public static int getDistanceFirstNorm(int[] pointOne, int[] pointTwo) throws DifferentDimensionsException {
        if(!(pointOne.length == pointTwo.length)) { throw new DifferentDimensionsException("The points have different dimensions"); }

        int distance = 0;
        for(int i = 0; i < pointOne.length; i++) {
            distance += Math.abs(pointOne[i] - pointTwo[i]);
        }
        return distance;
    }

    //float

    /**
     * Calculate the euclidean distance of two points, represented as float arrays.
     * @param pointOne is the first point.
     * @param pointTwo is the second point.
     * @return the euclidean distance
     * @throws DifferentDimensionsException when the two arrays have not the same length.
     */
    public static float getDistanceEuclidean(float[] pointOne, float[] pointTwo) throws DifferentDimensionsException {
        if(!(pointOne.length == pointTwo.length)) { throw new DifferentDimensionsException("The points have different dimensions"); }
        
        float distance = 0;
        for (int i = 0; i < pointOne.length; i++) {
            distance += Math.pow(pointOne[i] - pointTwo[i], 2);
        }
        return (float) Math.sqrt(distance);
    }

    /**
     * Calculate the first-norm distance of two points, represented as double arrays.
     * @param pointOne is the first point.
     * @param pointTwo is the second point.
     * @return the first-norm distance
     * @throws DifferentDimensionsException when the two arrays have not the same length.
     */
    public static float getDistanceFirstNorm(float[] pointOne, float[] pointTwo) throws DifferentDimensionsException {
        if(!(pointOne.length == pointTwo.length)) { throw new DifferentDimensionsException("The points have different dimensions"); }

        float distance = 0;
        for(int i = 0; i < pointOne.length; i++) {
            distance += Math.abs(pointOne[i] - pointTwo[i]);
        }
        return distance;
    }

    /**
     * Calculate the mean point of all the points in double.
     * @param points
     * @return Mean of the points in the array.
     */
    public static double[] calculateMean(double[][] points) {
        double[] meanPoint = new double[points[0].length];

        for(int i = 0; i < points.length; i++) {
            for(int j = 0; j < meanPoint.length; j++) {
                meanPoint[j] += points[i][j] / (double) points.length;
            }
        }
        
        return meanPoint;
    }

    /**
     * Calcualte the mean point of all the points in float
     * @param points
     * @return
     */
    public static float[] calculateMean(float[][] points) {
        float[] meanPoint = new float[points[0].length];

        for(int i = 0; i < points.length; i++) {
            for(int j = 0; j < meanPoint.length; j++) {
                meanPoint[j] += points[i][j] / (float) points.length;
            }
        }
        
        return meanPoint;
    }
}
