package com.tioxii.consensus.metric.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.configurations.OneLargeCluster;

public class OneLargeClusterTest {
    
    @Test
    public void testGenerateOneLargeCluster() {
        OneLargeCluster generator = new OneLargeCluster(0.0);
        Node[] nodes = null;
        
        try {
            nodes = generator.generate(1000);
            
            assertEquals(1000, nodes.length);
        } catch (Exception e) {
            nodes = null;
        }

        assertNotNull(nodes);
    }
}
