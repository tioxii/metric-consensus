package com.tioxii.simulation.consensus.metric.configurations;

import java.util.ArrayList;
import java.util.Arrays;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IConfiguration;
import com.tioxii.simulation.consensus.metric.exceptions.ConfigurationInitException;

/**
 * This configuration generates a cluster of nodes on the left side, a cluster of nodes on the right side and a single node in the middle.
 */
public class PointInTheMiddle implements IConfiguration {

    double[] leftCluster = {0.25, 0.5};
    double[] rightCluster = {0.75, 0.5};
    double[] middleCluster = {0.5, 0.5};

    /**
     * Generates a cluster of nodes on the left side, a cluster of nodes on the right side and a single node in the middle.
     * @param number The number of nodes to generate.
     * @return An array of nodes, of size number + 1. (The + 1 is for the middle node.)
     * @throws ConfigurationInitException If the configuration could not be initialized.
     */
    @Override
    public Node[] generate(int number) throws ConfigurationInitException {
        ArrayList<Node> nodes = new ArrayList<Node>();
        int clusterSize = number / 2;

        /* Generate the left cluster. */
        for(int i = 0; i < clusterSize; i++) {
            Node node = new Node(Arrays.copyOf(leftCluster, leftCluster.length));
            nodes.add(node);
        }

        /* Generate the right cluster. */
        for(int i = 0; i < clusterSize; i++) {
            Node node = new Node(Arrays.copyOf(rightCluster, rightCluster.length));
            nodes.add(node);
        }
        
        /* Generate the middle node. */
        Node node = new Node(Arrays.copyOf(middleCluster, middleCluster.length));
        nodes.add(node);

        return nodes.stream().toArray(Node[]::new);
    }
    
}
