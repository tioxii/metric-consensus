package com.tioxii.simulation.consensus.metric.api;

import com.tioxii.simulation.consensus.metric.Node;

public interface ITerminate {
    boolean shouldTerminate(Node[] nodes);
    void synchronous(Node[] nodes, int index);
    void asynchronous(Node[] nodes, int index, Node oldNode);
    ITerminate copyThis();
}
