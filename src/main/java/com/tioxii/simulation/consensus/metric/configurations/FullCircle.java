package com.tioxii.simulation.consensus.metric.configurations;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IConfiguration;
import com.tioxii.simulation.consensus.metric.exceptions.ConfigurationInitException;

/**
 * FullCircle configuration.
 * Generates a circle of nodes, where the number of nodes is equal to the number of clusters.
 * So each cluster has exactly one node.
 */
public class FullCircle implements IConfiguration {

    /**
     * Genrates a circle of nodes, where the number of nodes is equal to the number of clusters.
     * So each cluster has exactly one node.
     * @param number The number of nodes that should be generated.
     * @return An array of nodes.
     */
    @Override
    public Node[] generate(int number) throws ConfigurationInitException {
        Circle circle = new Circle();
        circle.numberOfClusters = number;
        return circle.generate(number);
    }
    
}
