package com.tioxii.simulation.consensus.metric.dynamics;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IDynamics;
import com.tioxii.simulation.consensus.metric.util.DynamicsUtil;

public class VoterDynamics implements IDynamics {

    /**
     * Just jumps to a random other node;
     */
    @Override
    public Node applyDynamicOn(int index, Node[] nodes) {
        double[] newOpinion = DynamicsUtil.selectRandomOpinion(index, 1, nodes)[0];
        
        return DynamicsUtil.createNewNode(nodes[index], newOpinion);
    }
    
}
