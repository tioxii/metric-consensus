package com.tioxii.simulation.consensus.metric.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.INodeGenerator;
import com.tioxii.simulation.consensus.metric.exceptions.NodeGenerationException;

public class ClustersAtPositions implements INodeGenerator {
    
    private Class<? extends Node> clazz;
    private double[][] positions;

    public ClustersAtPositions(double[][] positions, Class<? extends Node> clazz) {
        this.positions = positions;
        this.clazz = clazz;
    }

    @Override
    public Node[] generate(int number) throws NodeGenerationException {
        if(number % positions.length != 0) {
            throw new NodeGenerationException("Failed to create new node: " + number + " is not divisible by the number of positions");
        }
        int clusterSize = number / positions.length;
        ArrayList<Node> nodes = new ArrayList<Node>();
        
        Arrays.stream(positions).forEach(pos -> {
            for (int i = 0; i < clusterSize; i++) {    
                try {
                    nodes.add(clazz.getConstructor(double[].class).newInstance(Arrays.copyOf(pos, pos.length)));
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    INodeGenerator.LOGGER.error("Failed to create new Instance.");
                }
            }
        });

        if(!(nodes.size() == number)) {
            throw new NodeGenerationException("Failed to create nodes.");
        }

        Collections.shuffle(nodes);
        Node[] array = new Node[nodes.size()];

        return nodes.toArray(array);
    }
    
}