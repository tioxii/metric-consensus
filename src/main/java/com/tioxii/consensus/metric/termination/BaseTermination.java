package com.tioxii.consensus.metric.termination;

import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.api.ITerminate;

public class BaseTermination implements ITerminate {

    @Override
    public boolean shouldTerminate(INode[] nodes) {
        for (int i = 1; i < nodes.length; i++) {
            if(!nodes[0].equals(nodes[i]))
                return false;    
        }
        return true;
    }
    
}
