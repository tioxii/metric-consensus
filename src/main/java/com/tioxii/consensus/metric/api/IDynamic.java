package com.tioxii.consensus.metric.api;

public interface IDynamic {
    
    /**
     * Apply Dynamic
     * @param index
     * @param nodes
     * @return
     */
    INode applyDynamicOn(int index, final INode[] nodes);
}
