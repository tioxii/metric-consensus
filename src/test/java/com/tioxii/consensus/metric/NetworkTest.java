package com.tioxii.consensus.metric;
import java.util.Arrays;

import org.junit.Test;

import com.tioxii.simulation.consensus.metric.Node;

public class NetworkTest {
    
    @Test
    public void testConsensusReached() {
        Node[] nodesEqual = new Node[1000];
        double[] opinionEqual = {0.5, 0.5, 0.5, 0.5};

        for(int i = 0; i < nodesEqual.length; i++) {
            nodesEqual[i] = new Node(opinionEqual);
        }

        
    }

    @Test
    public void testConsensusNotReached() {
        Node[] nodesNotEqual = new Node[1000];
        double[] opinionNotEqual = {0.5, 0.5, 0.5, 0.5};

        for(int i = 0; i < nodesNotEqual.length; i++) {
            opinionNotEqual[3] += (double) i;
            nodesNotEqual[i] = new Node(Arrays.copyOf(opinionNotEqual, 1000));
        }
    }
    
    @Test
    public void testNetworkNodes() {
        Node[] nodesNotEqual = new Node[1000];
        double[] opinionNotEqual = {0.5, 0.5, 0.5, 0.5};

        for(int i = 0; i < nodesNotEqual.length; i++) {
            opinionNotEqual[3] += (double) i;
            nodesNotEqual[i] = new Node(opinionNotEqual);
        }
    }
}