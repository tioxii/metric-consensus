package com.tioxii.simulation.consensus.metric.dynamics;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IDynamic;
import com.tioxii.simulation.consensus.metric.util.DynamicUtil;

public class OneMajorityDynamic implements IDynamic {

    /**
     * Just jumps to a random other node;
     */
    @Override
    public Node applyDynamicOn(int index, Node[] nodes) {
        double[] newOpinion = DynamicUtil.selectRandomOpinion(index, 1, nodes)[0];
        
        return DynamicUtil.createNewNode(nodes[index], newOpinion);
    }
    
}
