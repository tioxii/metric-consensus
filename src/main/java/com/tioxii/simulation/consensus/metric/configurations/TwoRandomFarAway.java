package com.tioxii.simulation.consensus.metric.configurations;

import java.util.ArrayList;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IConfiguration;
import com.tioxii.simulation.consensus.metric.exceptions.ConfigurationInitException;
import com.tioxii.simulation.consensus.metric.util.DynamicsUtil;

public class TwoRandomFarAway implements IConfiguration {

    double offset = 100;
    int dimension = 2;

    private void createNodes(ArrayList<Node> nodes, int number) {
        for(int i = 0; i < number; i++) {
            double[] position = new double[dimension];
            DynamicsUtil.fillArrayWithRandomNumbers(position, offset);
            Node node = new Node(position);
            nodes.add(node);
        }
    }

    @Override
    public Node[] generate(int number) throws ConfigurationInitException {
        int firstBatchSize = number / 2;
        int secondBatchSize = number - firstBatchSize;
        ArrayList<Node> nodes = new ArrayList<>();

        createNodes(nodes, firstBatchSize);

        this.offset = 0;
        createNodes(nodes, secondBatchSize);

        return nodes.stream().toArray(Node[]::new);
    }
    
}
