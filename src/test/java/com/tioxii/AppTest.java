package com.tioxii;

import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testRandomValues() {
        Random r = new Random();
        double count = 0;

        for (int i = 0; i < 10000000; i++) {
            count += (double) r.nextInt(1001);
        }
        count /= 10000000;

        System.out.println(count);
    }
}
