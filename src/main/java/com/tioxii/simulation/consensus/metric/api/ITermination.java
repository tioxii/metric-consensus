package com.tioxii.simulation.consensus.metric.api;

import com.tioxii.simulation.consensus.metric.Node;

public interface ITermination {

    /**
     * Checks if the simulation should terminate.
     * @param nodes The nodes to check.
     * @return True if the simulation should terminate, false otherwise.
     */
    boolean shouldTerminate(Node[] nodes);
    
    /**
     * Called every time a nodes is updated.
     * This method should help to reduce the complexity of the shouldTerminate method.
     * @param nodes The nodes to check.
     * @param index The index of the node that has been updated.
     */
    void synchronous(Node[] nodes, int index);
    
    /**
     * Called every time a nodes is updated.
     * This method should help to reduce the complexity of the shouldTerminate method.
     * @param nodes The nodes to check.
     * @param index The index of the node that has been updated.
     * @param oldNode The node before the update.
     */
    void asynchronous(Node[] nodes, int index, Node oldNode);
    
    /**
     * Creates a copy of the termination object.
     * tbh I don't know why this is needed. But I remember I had a good reason.
     * @return A copy of the termination object.
     */
    ITermination copyThis();
}
