package com.tioxii.simulation.consensus.metric.dynamics;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IDynamics;
import com.tioxii.simulation.consensus.metric.util.DynamicsUtil;
import com.tioxii.simulation.consensus.metric.util.Parameter;

public class MeanValueDynamics implements IDynamics {

    @Parameter(isParameter = true, name = "h")
    public int h = 1;

    /**
     * Calculates the mean of h other plus itself nodes and jumps to it;
     * @param h how many other nodes are asked
     * @param roundToDigits the precision
     */
    public MeanValueDynamics(int h) {
        this.h = h;
    }
    
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
