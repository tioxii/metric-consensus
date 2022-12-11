package com.tioxii;

import java.io.IOException;

import org.junit.Test;

import com.tioxii.simulation.Evaluation;

public class EvaluationTest {
    
    @Test
    public void testPotential() {
        try {
            Evaluation eval = new Evaluation("results/10-12-2022_11-57-47_R-1_SYNC-true_POSITIONS.csv");

            double pot = eval.calculatePotential1(1);
            System.out.println(pot);

            eval.calculatePotential2(8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
