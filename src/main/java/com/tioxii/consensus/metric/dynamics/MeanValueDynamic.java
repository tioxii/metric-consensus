package com.tioxii.consensus.metric.dynamics;

import com.tioxii.consensus.metric.api.IDynamic;
import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.api.Parameter;
import com.tioxii.consensus.metric.util.DynamicUtil;

public class MeanValueDynamic implements IDynamic {

    @Parameter(shouldPrint = true)
    public int h = 1;

    /**
     * Calculates the mean of h other plus itself nodes and jumps to it;
     * @param h how many other nodes are asked
     * @param roundToDigits the precision
     */
    public MeanValueDynamic(int h) {
        this.h = h;
    }
    
    @Override
    public INode applyDynamicOn(int index, INode[] nodes) {
        double[][] opinions = DynamicUtil.selectRandomOpinion(index, h, nodes);
        double[] mean = new double[opinions[h].length];

        for(int i = 0; i < opinions.length; i++) {
            for (int j = 0; j < opinions[i].length; j++) {
                mean[j] += (opinions[i][j]/opinions.length);
            }
        }
        
        return DynamicUtil.createNewNode(nodes[index], mean);
    }
    
}
