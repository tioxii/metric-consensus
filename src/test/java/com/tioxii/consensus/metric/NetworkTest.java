package com.tioxii.consensus.metric;
import java.util.Arrays;

import org.junit.Test;

import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.nodes.BaseNode;

public class NetworkTest {
    
    @Test
    public void testConsensusReached() {
        INode[] nodesEqual = new INode[1000];
        double[] opinionEqual = {0.5, 0.5, 0.5, 0.5};

        for(int i = 0; i < nodesEqual.length; i++) {
            nodesEqual[i] = new BaseNode(opinionEqual);
        }

        
    }

    @Test
    public void testConsensusNotReached() {
        INode[] nodesNotEqual = new INode[1000];
        double[] opinionNotEqual = {0.5, 0.5, 0.5, 0.5};

        for(int i = 0; i < nodesNotEqual.length; i++) {
            opinionNotEqual[3] += (double) i;
            nodesNotEqual[i] = new BaseNode(Arrays.copyOf(opinionNotEqual, 1000));
        }

        
    }
    
    @Test
    public void testNetworkNodes() {
        INode[] nodesNotEqual = new INode[1000];
        double[] opinionNotEqual = {0.5, 0.5, 0.5, 0.5};

        for(int i = 0; i < nodesNotEqual.length; i++) {
            opinionNotEqual[3] += (double) i;
            nodesNotEqual[i] = new BaseNode(opinionNotEqual);
        }

        for(int i = 0; i < 1000; i ++) {
        
        }
    }
}
