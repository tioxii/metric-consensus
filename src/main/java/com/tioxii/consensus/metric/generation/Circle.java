package com.tioxii.consensus.metric.generation;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.api.INodeGenerator;
import com.tioxii.consensus.metric.api.Parameter;
import com.tioxii.consensus.metric.exceptions.NodeGenerationException;

public class Circle implements INodeGenerator {

    @Parameter(shouldPrint = true)
    public int numberOfClusters;
    
    private Class<? extends INode> type;

    public Circle(int numberOfClusters, Class<? extends INode> type) {
        this.numberOfClusters = numberOfClusters;
        this.type = type;
    }

    /**
     * Tries to generate equals sized clusters in a circle.
     */
    @Override
    public INode[] generate(int number) throws NodeGenerationException {
        double degree;
        ArrayList<INode> nodes = new ArrayList<>();
        double[] pos = new double[2];

        int remainder = number % numberOfClusters;

        for(int i = 0; i < numberOfClusters; i++) {
            degree = ((double) i/ (double) numberOfClusters) * (double) 360;

            pos[0] = (Math.cos(Math.toRadians(degree)) + 1) * 0.25; //x
            pos[1] = (Math.sin(Math.toRadians(degree)) + 1) * 0.25; //y

            int size = number / numberOfClusters;
            
            for(int j = 0; j < size; j++) {
                try {
                    INode node = type.getConstructor(double[].class).newInstance(Arrays.copyOf(pos, 2));
                    nodes.add(node);
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    throw new NodeGenerationException(e.getMessage());
                }
            }
            if(remainder > 0) {
                try {
                    INode node = type.getConstructor(double[].class).newInstance(Arrays.copyOf(pos, 2));
                    nodes.add(node);
                    remainder--;
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    throw new NodeGenerationException(e.getMessage());
                }
            }
        }
        Collections.shuffle(nodes);
        return nodes.stream().toArray(INode[]::new);
    }
    
}
