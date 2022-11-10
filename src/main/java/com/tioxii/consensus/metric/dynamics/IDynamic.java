package com.tioxii.consensus.metric.dynamics;

import com.tioxii.consensus.metric.nodes.INode;

public interface IDynamic {
    
    /**
     * Apply Dynamic
     * @param index
     * @param nodes
     * @return
     */
    INode applyDynamicOn(int index, final INode[] nodes);
}
