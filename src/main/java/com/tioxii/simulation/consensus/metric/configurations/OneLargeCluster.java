package com.tioxii.simulation.consensus.metric.configurations;

import java.util.ArrayList;
import java.util.Arrays;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IConfiguration;
import com.tioxii.simulation.consensus.metric.exceptions.ConfigurationInitException;
import com.tioxii.simulation.consensus.metric.util.DynamicsUtil;
import com.tioxii.simulation.consensus.metric.util.Parameter;

public class OneLargeCluster implements IConfiguration {

    @Parameter(isParameter = true, name = "Cluster Size")
    public double clusterSizeAsFraction = 0.0f;

    int dimension = 2;

    public OneLargeCluster(double clusterSizeAsFraction) {
        this.clusterSizeAsFraction = clusterSizeAsFraction;
    }

    @Override
    public Node[] generate(int number) throws ConfigurationInitException {
        int clusterSize = (int) (clusterSizeAsFraction * (double) number);
        double[] position = new double[dimension];
        DynamicsUtil.fillArrayWithRandomNumbers(position, 0);

        ArrayList<Node> nodes = new ArrayList<>();
        for(int i = 0; i < clusterSize; i++) {
            Node node = new Node(Arrays.copyOf(position, 2));
            nodes.add(node);
        }
        for(int i = 0; i < number - clusterSize; i++) {
            position = new double[dimension];
            DynamicsUtil.fillArrayWithRandomNumbers(position, 0);
            Node node = new Node(position);
            nodes.add(node);
        }

        return nodes.stream().toArray(Node[]::new);
    }
    
}
