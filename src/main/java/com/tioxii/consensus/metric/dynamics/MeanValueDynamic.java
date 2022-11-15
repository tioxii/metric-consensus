package com.tioxii.consensus.metric.dynamics;

import org.apache.commons.math3.util.Precision;

import com.tioxii.consensus.metric.api.IDynamic;
import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.util.DynamicUtil;

public class MeanValueDynamic implements IDynamic {

    int h = 1;
    int roundToDigits = 18;

    /**
     * Calculates the mean of h other plus itself nodes and jumps to it;
     * @param h how many other nodes are asked
     * @param roundToDigits the precision
     */
    public MeanValueDynamic(int h, int roundToDigits) {
        this.h = h;
        this.roundToDigits = roundToDigits;
    }
    
    
    @Override
    public INode applyDynamicOn(int index, INode[] nodes) {
        double[][] opinions = DynamicUtil.selectRandomOpinion(index, h, nodes);
        double[] mean = new double[opinions[h].length];

        for(int i = 0; i < opinions.length; i++) {
            for (int j = 0; j < opinions[i].length; j++) {
                mean[j] += Precision.round((opinions[i][j]/opinions.length), roundToDigits);
            }
        }
        
        return DynamicUtil.createNewNode(nodes[index], mean);
    }
    
}
