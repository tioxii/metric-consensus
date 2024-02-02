package com.tioxii.simulation.consensus.metric.dynamics;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IDynamics;
import com.tioxii.simulation.consensus.metric.util.DynamicsUtil;
import com.tioxii.simulation.consensus.metric.util.Parameter;

/**
 * Mean Value Dynamics.
 * A node will ask h other nodes for their opinion/position and adapts to the mean of all opinions/positions.
 */
public class MeanValueDynamics implements IDynamics {

    /* How many other nodes are asked. */
    @Parameter(isParameter = true, name = "h")
    public int h = 1;

    /**
     * Calculates the mean of h other plus itself nodes and jumps to it;
     * @param h how many other nodes are asked
     */
    public MeanValueDynamics() {
        
    }
    
    /**
     * Calculates the mean of h other plus itself nodes and jumps to it.
     * @param index of the node the dynamics is applied on.
     * @param nodes that are part of the simulation.
     * @return New node with new adapted opinion/position.
     */
    @Override
    public Node applyDynamicOn(int index, Node[] nodes) {
        double[][] opinions = DynamicsUtil.selectRandomOpinion(index, h, nodes);
        double[] mean = new double[opinions[h].length];

        for(int i = 0; i < opinions.length; i++) {
            for (int j = 0; j < opinions[i].length; j++) {
                mean[j] += (opinions[i][j]/opinions.length);
            }
        }
        
        return DynamicsUtil.createNewNode(nodes[index], mean);
    }
    
}
