package com.tioxii.consensus.metric;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

import com.tioxii.consensus.metric.nodes.BaseNode;
import com.tioxii.consensus.metric.nodes.INode;

public class SimulationTest {
    
    @Test
    public void lengthOfGeneratedNodes() {
        Random r = new Random();
        int number = r.nextInt(10000);
        
        INode[] nodes = Simulation.generateRandomNodes(number, BaseNode.class);

        assertEquals(nodes.length, number);
    }
}
