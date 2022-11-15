package com.tioxii.consensus.metric.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.nodes.BaseNode;

public class NodeUtilTest {
    
    @Test
    public void testGenerateClusters() {
        double[][] positions = {{0.25, 0.5},{0.75, 0.5}};
        int size = 500;

        INode[] nodes = NodeUtil.generateClusters(positions, BaseNode.class, size);
        
        int countOne = 0;

        for (int i = 0; i < nodes.length; i++) {
            if(Arrays.equals(nodes[i].getOpinion(), positions[0])) {
                countOne++;
            }
        }

        assertEquals(nodes.length, size * positions.length);
        assertEquals(size, countOne);
    }

    @Test
    public void lengthOfGeneratedNodes() {
        Random r = new Random();
        int number = r.nextInt(10000);
        
        INode[] nodes = NodeUtil.generateRandom(number, BaseNode.class, 2);

        assertEquals(nodes.length, number);
    }
}
