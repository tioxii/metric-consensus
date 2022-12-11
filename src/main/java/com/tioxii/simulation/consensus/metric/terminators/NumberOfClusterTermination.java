package com.tioxii.simulation.consensus.metric.terminators;

import java.util.ArrayList;
import java.util.Arrays;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.ITerminate;

public class NumberOfClusterTermination implements ITerminate {

    public int clusters = 2;

    @Override
    public boolean shouldTerminate(Node[] nodes) {
        ArrayList<double[]> set = new ArrayList<double[]>();
        boolean hasElement = true;

        for(int i = 0; i < nodes.length; i++) {
            for(int j = 0; j < set.size(); j++) {
                if(Arrays.equals(set.get(j), nodes[i].getOpinion())) {
                    hasElement = true;
                    break;
                }
            }
            if(!hasElement) {
                set.add(nodes[i].getOpinion());
            }
            if(set.size() > clusters)
                return false;

            hasElement = false;
        }
        return true;
    }

    @Override
    public void synchronous(Node[] nodes, int index) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void asynchronous(Node[] nodes, int index, Node oldNode) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ITerminate copyThis() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
