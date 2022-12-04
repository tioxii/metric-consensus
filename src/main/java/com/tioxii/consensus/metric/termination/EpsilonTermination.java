package com.tioxii.consensus.metric.termination;

import java.util.Arrays;

import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.api.ITerminate;
import com.tioxii.consensus.metric.api.Parameter;
import com.tioxii.math.Distance;
import com.tioxii.math.exceptions.DifferentDimensionsException;

public class EpsilonTermination implements ITerminate {

    @Parameter(shouldPrint = true)
    public double epsilon;

    public EpsilonTermination(double epsilon) {
        this.epsilon = epsilon;
    }
    
    @Override
    public boolean shouldTerminate(INode[] nodes) {
        double[][] opinions = Arrays.stream(nodes)
            .map(node -> node.getOpinion())
            .toArray(double[][]::new);

        double[] mean = Distance.calculateMean(opinions);
        
        for (int i = 1; i < nodes.length; i++) {
            try {
                if(Distance.getDistanceEuclidean(mean, nodes[i].getOpinion()) > epsilon)
                return false;
            } catch (DifferentDimensionsException e) {
                return true;
            }
        }
        return true;
    }
    
}
