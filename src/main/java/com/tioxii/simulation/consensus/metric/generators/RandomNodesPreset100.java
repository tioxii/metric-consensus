package com.tioxii.simulation.consensus.metric.generators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.INodeGenerator;
import com.tioxii.simulation.consensus.metric.exceptions.NodeGenerationException;

public class RandomNodesPreset100 implements INodeGenerator {
    
    double[][] opinions = new double[100][];

    public RandomNodesPreset100() {
        //TODO
    }

    @Override
    public Node[] generate(int number) throws NodeGenerationException {
        Node[] nodes = new Node[100];
        for(int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node(opinions[i]);
        }
        return nodes;
    }
}
