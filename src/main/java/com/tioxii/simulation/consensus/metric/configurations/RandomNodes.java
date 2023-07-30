package com.tioxii.simulation.consensus.metric.configurations;

import java.lang.reflect.InvocationTargetException;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IConfiguration;
import com.tioxii.simulation.consensus.metric.exceptions.ConfigurationInitException;
import com.tioxii.simulation.consensus.metric.util.DynamicsUtil;
import com.tioxii.simulation.consensus.metric.util.Parameter;

public class RandomNodes implements IConfiguration {

    @Parameter(isParameter = false, name = "Dimension")
    public int dimension = 2;
    Class<? extends Node> clazz;

    public RandomNodes() {
        this.clazz = Node.class;
    }

    public RandomNodes(int dimension, Class<? extends Node> clazz) {
        this.dimension = dimension;
        this.clazz = clazz;
    }

    @Override
    public Node[] generate(int number) throws ConfigurationInitException {
        Node[] nodes = new Node[number];

        for (int i = 0; i < nodes.length; i++) {
            double[] opinion = new double[dimension];
            DynamicsUtil.fillArrayWithRandomNumbers(opinion, 0);
            try {
                nodes[i] = clazz.getConstructor(double[].class).newInstance(opinion);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                throw new ConfigurationInitException("Failed to create new node: " + e.getMessage());
            }
        }
        return nodes;
    }
    
}
