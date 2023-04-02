package com.tioxii.consensus.metric.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.tioxii.simulation.consensus.metric.util.DynamicsUtil;

public class DynamicUtilTest {
    
    private boolean checkIsInRange(double value, double lowerBound, double upperBound) {
        if(value < upperBound && value >= lowerBound) {
            return true;
        }
        return false;
    }

    @Test
    public void testfillArrayWithRandomNumbers() {
        double[] doubleArray = new double[100];
        DynamicsUtil.fillArrayWithRandomNumbers(doubleArray, 0);
        boolean isInRange = true;
        for(int i = 0; i < doubleArray.length; i++) {
            isInRange &= checkIsInRange(doubleArray[i], 0, 1);
        }

        assertTrue(isInRange);
    }
}
