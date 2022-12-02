package com.tioxii.consensus.metric.termination;

import java.util.Arrays;

import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.api.ITerminate;

public class FiftyPercentTermination implements ITerminate {

    double[] byzantinePosition = null;

    public FiftyPercentTermination(double[] byzantinePosition) {
        this.byzantinePosition = byzantinePosition;
    }

    @Override
    public boolean shouldTerminate(INode[] nodes) {
        int counter = 0;
        for(int i = 0; i < nodes.length; i++) {
            if(Arrays.equals(byzantinePosition, nodes[i].getOpinion())) {
                counter++;

                if(counter > nodes.length * 0.5) {
                    return true;
                }
            }
        }
        return false;
    }   
}
