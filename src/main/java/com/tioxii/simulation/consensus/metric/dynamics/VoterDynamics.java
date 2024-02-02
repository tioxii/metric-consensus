package com.tioxii.simulation.consensus.metric.dynamics;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IDynamics;
import com.tioxii.simulation.consensus.metric.util.DynamicsUtil;

/**
 * Voter Dynamics.
 * A node will ask one random node for its opinion and adapt the opinion/position of the node.
 */
public class VoterDynamics implements IDynamics {

    /**
     * Selects a random node and adapts its opinion/position.
     * @param index of the node the dynamics is applied on.
     * @param nodes that are present in the simulation.
     * @return A new node with the new opinion.
     */
    @Override
    public Node applyDynamicOn(int index, Node[] nodes) {
        double[] newOpinion = DynamicsUtil.selectRandomOpinion(index, 1, nodes)[0];
        return DynamicsUtil.createNewNode(nodes[index], newOpinion);
    }
    
}
