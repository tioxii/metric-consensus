package com.tioxii.consensus.metric.termination;

import java.util.ArrayList;
import java.util.Arrays;

import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.api.ITerminate;

public class NumberOfClusterTermination implements ITerminate {

    public int clusters = 2;

    @Override
    public boolean shouldTerminate(INode[] nodes) {
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
    public void synchronous(INode[] nodes, int index) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void asynchronous(INode[] nodes, int index) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ITerminate copyThis() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
