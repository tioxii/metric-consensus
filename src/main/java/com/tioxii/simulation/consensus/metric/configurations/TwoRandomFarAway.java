package com.tioxii.simulation.consensus.metric.configurations;

import java.util.ArrayList;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IConfiguration;
import com.tioxii.simulation.consensus.metric.exceptions.ConfigurationInitException;
import com.tioxii.simulation.consensus.metric.util.DynamicsUtil;

/**
 * TwoRandomFarAway configuration.
 * Create two batches of nodes where the mean of the two batches have a distance of 100 between them.
 */
public class TwoRandomFarAway implements IConfiguration {

    /**
     * The offset for the second batch of nodes.
     * Although this value changes in code, it only takes two values.
     * Therfore, I decided to mark it as a constant.
     */
    double OFFSET = 100;

    /**
     * The dimension of the space in which the nodes are placed.
     */
    int dimension = 2;

    /**
     * Helper function to create a batch of nodes.
     * @param nodes The list of nodes to which the new nodes are added.
     * @param number The size of the batch.
     */
    private void createNodes(ArrayList<Node> nodes, int number) {
        for(int i = 0; i < number; i++) {
            double[] position = new double[dimension];
            DynamicsUtil.fillArrayWithRandomNumbers(position, OFFSET);
            Node node = new Node(position);
            nodes.add(node);
        }
    }

    /**
     * Generates a set of nodes.
     * Create two batches of two nodes where two batches have a distance of 100 between them.
     * @param number The number of nodes to be generated.
     * @return An array of nodes.
     */
    @Override
    public Node[] generate(int number) throws ConfigurationInitException {
        
        /* Calculate the number of nodes in each batch. */
        int firstBatchSize = number / 2;
        int secondBatchSize = number - firstBatchSize;
        ArrayList<Node> nodes = new ArrayList<>();

        /* The first batch of nodes is placed in a space with an offset of 100. */
        this.OFFSET = 100;
        createNodes(nodes, firstBatchSize);

        /* The second batch of nodes is placed in a space with an offset of 0. */
        this.OFFSET = 0;
        createNodes(nodes, secondBatchSize);

        return nodes.stream().toArray(Node[]::new);
    }
    
}
