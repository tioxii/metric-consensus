package com.tioxii.simulation.consensus.metric.configurations;

import java.util.ArrayList;
import java.util.Arrays;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IConfiguration;
import com.tioxii.simulation.consensus.metric.exceptions.ConfigurationInitException;
import com.tioxii.simulation.consensus.metric.util.DynamicsUtil;
import com.tioxii.simulation.consensus.metric.util.Parameter;

/**
 * OneLargeCluster configuration.
 * Create a cluster of nodes with a size of clusterSizeAsFraction * number of nodes.
 * The rest of the nodes are placed randomly, with a cluster size of 1.
 */
public class OneLargeCluster implements IConfiguration {

    /**
     * The size of the cluster as a fraction of the total number of nodes.
     */
    @Parameter(isParameter = true, name = "Cluster Size")
    public double clusterSizeAsFraction = 0.0f;

    /**
     * The dimension of the space in which the nodes are placed.
     * The value of the variable stays constant, might change in the future.
     */
    int dimension = 2;

    /**
     * Generates a set of nodes.
     * The nodes are placed in a single cluster, depending on the value of the clusterSizeAsFraction.
     * The rest of the nodes are placed randomly, with a cluster size of 1.
     * @param number The number of nodes to be generated.
     * @return An array of nodes.
     */
    @Override
    public Node[] generate(int number) throws ConfigurationInitException {
        
        /* Caluclate the absolute size of the cluster. */
        int clusterSize = (int) (clusterSizeAsFraction * (double) number);
        
        /* Create the cluster opionion/position. */
        double[] position = new double[dimension];
        DynamicsUtil.fillArrayWithRandomNumbers(position, 0);

        /* Create the cluster. */
        ArrayList<Node> nodes = new ArrayList<>();
        for(int i = 0; i < clusterSize; i++) {
            Node node = new Node(Arrays.copyOf(position, 2));
            nodes.add(node);
        }

        /* Create the rest of the nodes (with cluster size of 1).  */
        for(int i = 0; i < number - clusterSize; i++) {
            position = new double[dimension];
            DynamicsUtil.fillArrayWithRandomNumbers(position, 0);
            Node node = new Node(position);
            nodes.add(node);
        }

        return nodes.stream().toArray(Node[]::new);
    }
    
}
