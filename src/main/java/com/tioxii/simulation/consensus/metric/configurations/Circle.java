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
    
    @Parameter(isParameter = false, name = "Cluster")
    public int numberOfClusters;
    
    private Class<? extends Node> type;

    public Circle(int numberOfClusters, Class<? extends Node> type) {
        this.numberOfClusters = numberOfClusters;
        this.type = type;
    }

    /**
     * Tries to generate equals sized clusters in a circle.
     */
    @Override
    public Node[] generate(int number) throws ConfigurationInitException {
        double degree;
        ArrayList<Node> nodes = new ArrayList<>();
        double[] pos = new double[2];

        int remainder = number % numberOfClusters;

        for(int i = 0; i < numberOfClusters; i++) {
            degree = ((double) i/ (double) numberOfClusters) * (double) 360;

            pos[0] = (Math.cos(Math.toRadians(degree)) + 2) * 0.25; //x
            pos[1] = (Math.sin(Math.toRadians(degree)) + 2) * 0.25; //y

            int size = number / numberOfClusters;
            
            for(int j = 0; j < size; j++) {
                try {
                    Node node = type.getConstructor(double[].class).newInstance(Arrays.copyOf(pos, 2));
                    nodes.add(node);
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    throw new ConfigurationInitException(e.getMessage());
                }
            }
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
        Collections.shuffle(nodes);
        return nodes.stream().toArray(Node[]::new);
    }
    
}
