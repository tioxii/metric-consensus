package com.tioxii.simulation.consensus.metric.api;

import com.tioxii.simulation.consensus.metric.Node;

/**
 * This interface is used to define a termination criterion.
 * A termination is a condition that is checked each turn.
 * If the condition is true, the simulation will terminate.
 */
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
     * Sometimes there are local variables, that are changed during the simulation.
     * At the start of the simulation, each simulation gets a copy of the original termination object.
     * This way the termination object can changed its local variables, without affecting other simulations.
     * @return A copy of the termination object.
     */
    ITermination copyThis();
}
