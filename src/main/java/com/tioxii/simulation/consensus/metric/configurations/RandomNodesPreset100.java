package com.tioxii.simulation.consensus.metric.configurations;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IConfiguration;
import com.tioxii.simulation.consensus.metric.exceptions.ConfigurationInitException;

public class RandomNodesPreset100 implements IConfiguration {
    
    double[][] opinions = new double[100][];

    public RandomNodesPreset100() {}

    @Override
    public Node[] generate(int number) throws ConfigurationInitException {
        Node[] nodes = new Node[100];
        for(int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node(opinions[i]);
        }
        return nodes;
    }
}
