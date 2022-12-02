package com.tioxii.consensus.metric.generation;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.api.INodeGenerator;
import com.tioxii.consensus.metric.exceptions.NodeGenerationException;

public class ClustersAtPositions implements INodeGenerator {
    
    private Class<? extends INode> clazz;
    private double[][] positions;

    public ClustersAtPositions(double[][] positions, Class<? extends INode> clazz) {
        this.positions = positions;
        this.clazz = clazz;
    }

    @Override
    public INode[] generate(int number) throws NodeGenerationException {
        if(number % positions.length != 0) {
            throw new NodeGenerationException("Failed to create new node: " + number + " is not divisible by the number of positions");
        }
        int clusterSize = number / positions.length;
        ArrayList<INode> nodes = new ArrayList<INode>();
        
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
        INode[] array = new INode[nodes.size()];

        return nodes.toArray(array);
    }
    
}
