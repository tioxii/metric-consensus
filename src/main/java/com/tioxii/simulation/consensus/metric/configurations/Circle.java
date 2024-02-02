package com.tioxii.simulation.consensus.metric.configurations;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IConfiguration;
import com.tioxii.simulation.consensus.metric.exceptions.ConfigurationInitException;
import com.tioxii.simulation.consensus.metric.util.Parameter;

public class Circle implements IConfiguration {
    
    /**
     *  How many cluster there are in the circle. 
     */
    @Parameter(isParameter = false, name = "Cluster")
    public int numberOfClusters;
    
    /** 
     * The type of node that is generated. 
     * Does not have a purpose at the moment. Stays here so the code does not break. 
     */
    private Class<? extends Node> type = Node.class;

    /**
     * Tries to generate equals sized clusters in a circle.
     * @param number The number of nodes that should be generated.
     * @return An array of nodes.
     */
    @Override
    public Node[] generate(int number) throws ConfigurationInitException {
        
        /* For calculating the position of each cluster. */
        double degree;
        double[] pos = new double[2];

        /* List of nodes that will be returned. Later converted to an array. */
        ArrayList<Node> nodes = new ArrayList<>();

        /* If the number of nodes is not divisible by the number of clusters, the remainder is shared between the clusters. */
        int remainder = number % numberOfClusters;

        /* Generate nodes. */
        for(int i = 0; i < numberOfClusters; i++) {
            
            /* Calculate the position of each cluster. */
            degree = ((double) i/ (double) numberOfClusters) * (double) 360;
            pos[0] = (Math.cos(Math.toRadians(degree)) + 2) * 0.25; //x
            pos[1] = (Math.sin(Math.toRadians(degree)) + 2) * 0.25; //y

            /* Determine size of the cluster */
            int size = number / numberOfClusters;
            
            /* Create nodes for that custer */
            for(int j = 0; j < size; j++) {
                try {
                    Node node = type.getConstructor(double[].class).newInstance(Arrays.copyOf(pos, 2));
                    nodes.add(node);
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    throw new ConfigurationInitException(e.getMessage());
                }
            }

            /* If there is a remainder, create one more node for that cluster. */
            if(remainder > 0) {
                try {
                    Node node = type.getConstructor(double[].class).newInstance(Arrays.copyOf(pos, 2));
                    nodes.add(node);
                    remainder--;
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    throw new ConfigurationInitException(e.getMessage());
                }
            }
        }

        /* Shuffel the nodes */
        Collections.shuffle(nodes);
        return nodes.stream().toArray(Node[]::new);
    }
    
}
