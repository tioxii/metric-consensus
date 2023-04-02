package com.tioxii.simulation.consensus.metric.configurations;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IConfiguration;
import com.tioxii.simulation.consensus.metric.exceptions.ConfigurationInitException;

public class PresetNodes implements IConfiguration {

    //Presets
    public static final double[][] OPPOSING = {{0.25, 0.5}, {0.75, 0.5}};
    public static final double[][] SQUARE = {{0.25, 0.25}, {0.25, 0.75}, {0.75, 0.25}, {0.75, 0.75}};

    //Variables
    private ClustersAtPositions generator;
    private int number;

    public PresetNodes(double[][] positions, Class<? extends Node> clazz) {
        this.generator = new ClustersAtPositions(positions, clazz);
        this.number = positions.length;
    }

    /**
     * Generates nodes at fixed positions
     * @param number This number has no impact.
     */
    @Override
    public Node[] generate(int number) throws ConfigurationInitException {
        return generator.generate(this.number);
    }
    
}
