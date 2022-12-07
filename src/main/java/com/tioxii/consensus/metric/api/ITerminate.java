package com.tioxii.consensus.metric.api;

public interface ITerminate {
    boolean shouldTerminate(INode[] nodes);
    void synchronous(INode[] nodes, int index);
    void asynchronous(INode[] nodes, int index, INode oldNode);
    ITerminate copyThis();
}
