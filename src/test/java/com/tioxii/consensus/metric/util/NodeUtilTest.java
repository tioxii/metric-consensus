package com.tioxii.consensus.metric.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.tioxii.consensus.metric.nodes.INode;

public class NodeUtilTest {
    
    @Test
    public void testGenerateClusters() {
        double[][] positions = {{0.25, 0.5},{0.75, 0.5}};
        int size = 500;

        INode[] nodes = NodeUtil.generateClusters(positions,size);
        
        int countOne = 0;

        for (int i = 0; i < nodes.length; i++) {
            if(Arrays.equals(nodes[i].getOpinion(), positions[0])) {
                countOne++;
            }
        }

        assertEquals(nodes.length, size * positions.length);
        assertEquals(size, countOne);
    }
}
