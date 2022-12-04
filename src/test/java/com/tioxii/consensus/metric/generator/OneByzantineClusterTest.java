package com.tioxii.consensus.metric.generator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.exceptions.NodeGenerationException;
import com.tioxii.consensus.metric.generation.OneByzantineCluster;
import com.tioxii.consensus.metric.nodes.BaseNode;

public class OneByzantineClusterTest {
    
    @Test
    public void testgenerateNodes() {
        int number = 1000;
        double[] honestClusterPosition = {0.25,0.5};
        double[] byzantineClusterPosition = {0.75, 0.5};
        Class<? extends INode> type = BaseNode.class;
        INode[] nodes = null;

        OneByzantineCluster generator = new OneByzantineCluster(0.5, honestClusterPosition, byzantineClusterPosition, type);

        try {
            nodes = generator.generate(number);
            
        } catch (NodeGenerationException e) {
            e.printStackTrace();
        }   
        int l = -1;
        if(nodes != null)
            l = nodes.length;

        assertEquals(number, l);
    }
}
