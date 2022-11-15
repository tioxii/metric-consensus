package com.tioxii.consensus.metric.dynamics;

import com.tioxii.consensus.metric.api.IDynamic;
import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.util.DynamicUtil;

public class OneMajorityDynamic implements IDynamic {

    /**
     * Just jumps to a random other node;
     */
    @Override
    public INode applyDynamicOn(int index, INode[] nodes) {
        double[] newOpinion = DynamicUtil.selectRandomOpinion(index, 1, nodes)[0];
        
        return DynamicUtil.createNewNode(nodes[index], newOpinion);
    }
    
}
