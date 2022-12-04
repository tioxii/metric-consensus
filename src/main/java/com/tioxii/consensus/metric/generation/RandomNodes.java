package com.tioxii.consensus.metric.generation;

import java.lang.reflect.InvocationTargetException;

import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.api.INodeGenerator;
import com.tioxii.consensus.metric.exceptions.NodeGenerationException;
import com.tioxii.util.Parameter;

public class RandomNodes implements INodeGenerator {

    @Parameter(isParameter = false, name = "Dimension")
    public int dimension;
    Class<? extends INode> clazz;

    public RandomNodes(int dimension, Class<? extends INode> clazz) {
        this.dimension = dimension;
        this.clazz = clazz;
    }

    @Override
    public INode[] generate(int number) throws NodeGenerationException {
        INode[] nodes = new INode[number];
        
        for (int i = 0; i < nodes.length; i++) {
            
            try {
                nodes[i] = clazz.getConstructor(int.class).newInstance(this.dimension);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                throw new NodeGenerationException("Failed to create new node: " + e.getMessage());
            }
        }
        return nodes;
    }
    
}
