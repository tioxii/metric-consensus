package com.tioxii.consensus.metric.util;

import org.junit.Test;

import com.tioxii.simulation.consensus.metric.util.Iterations;

public class IterationTest {
    
    @Test
    public void testExpLin() {
        int start = 2;
        int end = 3;
        int step = 1;
        int[] reference = {100, 200, 300, 400, 500, 600, 700, 800, 900};
        
        int[] result = Iterations.iterationsExpLin(start, end, step);

        for(int i = 0; i < result.length; i++) {
            System.out.print(result[i]);
            System.out.println();
        }
    }
}
