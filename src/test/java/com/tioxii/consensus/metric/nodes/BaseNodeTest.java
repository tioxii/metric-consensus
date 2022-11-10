package com.tioxii.consensus.metric.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

public class BaseNodeTest {
    
    @Test
    public void testRange() {
        BaseNode node = new BaseNode(10000);
        double[] opinion = node.getOpinion();

        for (double d : opinion) {
            assertTrue((d >= 0) && (d <= 1));
        }
    }

    @Test
    public void testLength() {
        Random r = new Random();
        int number = r.nextInt(10000);
        BaseNode node = new BaseNode(number);

        assertEquals(node.getOpinion().length, number);
    }
}
