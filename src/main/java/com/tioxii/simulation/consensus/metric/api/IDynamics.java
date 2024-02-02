package com.tioxii.simulation.consensus.metric.api;

import com.tioxii.simulation.consensus.metric.Node;

/**
 * This interface is used to define a dynamic.
 * A dynamics is an update rule that is applied to a node, each turn.
 */
public interface IDynamics {
    
    /**
     * Applies the dynamic on the node.
     * @param node The index to the node to apply the dynamic on.
     * @param nodes The other nodes, including the current node.
     * @return The node after the dynamic has been applied.
     */
    Node applyDynamicOn(int index, final Node[] nodes);
}
