package com.tioxii.consensus.metric.api;

public interface INode {
    double[] getOpinion();
    double[] askOpinion();
    boolean equals(INode node);
    boolean ishonest();
}
