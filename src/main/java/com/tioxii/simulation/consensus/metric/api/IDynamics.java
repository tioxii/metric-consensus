package com.tioxii.simulation.consensus.metric.api;

import com.tioxii.simulation.consensus.metric.Node;

public interface IDynamics {
    
    /**
     * Apply Dynamic
     * @param index
     * @param nodes
     * @return
     */
    Node applyDynamicOn(int index, final Node[] nodes);
}
