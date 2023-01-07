package com.tioxii.simulation.consensus.metric.generators;

import java.lang.reflect.InvocationTargetException;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.INodeGenerator;
import com.tioxii.simulation.consensus.metric.exceptions.NodeGenerationException;
import com.tioxii.simulation.consensus.metric.util.DynamicUtil;
import com.tioxii.util.Parameter;

public class RandomNodes implements INodeGenerator {

    @Parameter(isParameter = false, name = "Dimension")
    public int dimension = 2;
    Class<? extends Node> clazz;

    public RandomNodes(int dimension, Class<? extends Node> clazz) {
        this.dimension = dimension;
        this.clazz = clazz;
    }

    @Override
    public Node[] generate(int number) throws NodeGenerationException {
        Node[] nodes = new Node[number];

        for (int i = 0; i < nodes.length; i++) {
            double[] opinion = new double[dimension];
            DynamicUtil.fillArrayWithRandomNumbers(opinion, 0);
            try {
                nodes[i] = clazz.getConstructor(double[].class).newInstance(opinion);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                throw new NodeGenerationException("Failed to create new node: " + e.getMessage());
            }
        }
        return nodes;
    }
    
}
