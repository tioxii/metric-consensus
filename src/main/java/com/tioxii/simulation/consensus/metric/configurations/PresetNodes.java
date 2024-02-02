package com.tioxii.simulation.consensus.metric.configurations;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IConfiguration;
import com.tioxii.simulation.consensus.metric.exceptions.ConfigurationInitException;

/**
 * PresetNodes configuration.
 * Generates nodes at fixed given positions.
 */
public class PresetNodes implements IConfiguration {

    /**
     * Preset position for Opposing nodes.
     */
    public static final double[][] OPPOSING = {{0.25, 0.5}, {0.75, 0.5}};

    /**
     * Preset position for Square nodes.
     */
    public static final double[][] SQUARE = {{0.25, 0.25}, {0.25, 0.75}, {0.75, 0.25}, {0.75, 0.75}};

    /**
     * The generator that is used to generate the nodes.
     * Code reuse.
     */
    private ClustersAtPositions generator;

    /**
     * The number of nodes that should be generated.
     * Equal to the number of positions.
     */
    private int number;

    public PresetNodes(double[][] positions) {
        this.generator = new ClustersAtPositions(positions);
        this.number = positions.length;
    }

    /**
     * Generates nodes at fixed positions
     * @param number This number has no impact.
     * @return An array of nodes.
     */
    @Override
    public Node[] generate(int number) throws ConfigurationInitException {
        return generator.generate(this.number);
    }
    
}
