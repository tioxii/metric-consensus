package com.tioxii.simulation.consensus.metric.configurations;

import java.lang.reflect.InvocationTargetException;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IConfiguration;
import com.tioxii.simulation.consensus.metric.exceptions.ConfigurationInitException;
import com.tioxii.simulation.consensus.metric.util.DynamicsUtil;
import com.tioxii.simulation.consensus.metric.util.Parameter;

public class RandomNodes implements IConfiguration {

    /**
     * The dimension of the opinion/position.
     */
    @Parameter(isParameter = false, name = "Dimension")
    public int dimension = 2;

    /**
     * The class of the node that should be generated.
     * Exists to not break the code. Has no use.
     */
    Class<? extends Node> clazz;

    public RandomNodes() {
        this.clazz = Node.class;
    }

    /**
     * Generates nodes with random opinions/positions.
     * @param number The number of nodes that should be generated.
     * @return An array of nodes.
     */
    @Override
    public Node[] generate(int number) throws ConfigurationInitException {
        Node[] nodes = new Node[number];

        /* Generate nodes */
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
