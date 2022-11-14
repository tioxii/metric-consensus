package com.tioxii.consensus.metric.generation;

import com.tioxii.consensus.metric.nodes.INode;

public interface INodeGenerator {
    INode[] generate();
}
