package com.tioxii.simulation.consensus.metric.configurations;

import java.util.ArrayList;
import java.util.Arrays;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IConfiguration;
import com.tioxii.simulation.consensus.metric.exceptions.ConfigurationInitException;

public class PointInTheMiddle implements IConfiguration {

    double[] leftCluster = {0.25, 0.5};
    double[] rightCluster = {0.75, 0.5};
    double[] middleCluster = {0.5, 0.5};

    @Override
    public Node[] generate(int number) throws ConfigurationInitException {
        ArrayList<Node> nodes = new ArrayList<Node>();
        int clusterSize = number / 2;

        for(int i = 0; i < clusterSize; i++) {
            Node node = new Node(Arrays.copyOf(leftCluster, leftCluster.length));
            nodes.add(node);
        }
        for(int i = 0; i < clusterSize; i++) {
            Node node = new Node(Arrays.copyOf(rightCluster, rightCluster.length));
            nodes.add(node);
        }
        Node node = new Node(Arrays.copyOf(middleCluster, middleCluster.length));
        nodes.add(node);

        return nodes.stream().toArray(Node[]::new);
    }
    
}
