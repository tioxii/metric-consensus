package com.tioxii.consensus.metric.generation;

import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.api.INodeGenerator;
import com.tioxii.consensus.metric.exceptions.NodeGenerationException;

public class PresetNodes implements INodeGenerator {

    //Presets
    public static final double[][] OPPOSING = {{0.25, 0.5}, {0.75, 0.5}};
    public static final double[][] SQUARE = {{0.25, 0.25}, {0.25, 0.75}, {0.75, 0.25}, {0.75, 0.75}};

    //Variables
    private ClustersAtPositions generator;
    private int number;

    public PresetNodes(double[][] positions, Class<? extends INode> clazz) {
        this.generator = new ClustersAtPositions(positions, clazz);
        this.number = positions.length;
    }

    /**
     * Generates nodes at fixed positions
     * @param number This number has no impact.
     */
    @Override
    public INode[] generate(int number) throws NodeGenerationException {
        return generator.generate(this.number);
    }
    
}
