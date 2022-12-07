package com.tioxii.consensus.metric.termination;

import java.util.Arrays;

import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.api.ITerminate;
import com.tioxii.math.Distance;
import com.tioxii.math.exceptions.DifferentDimensionsException;
import com.tioxii.util.Parameter;

public class EpsilonTermination implements ITerminate {

    @Parameter(isParameter = true, name = "Epsilon")
    public double epsilon;

    public EpsilonTermination(double epsilon) {
        this.epsilon = epsilon;
    }
    
    @Override
    public boolean shouldTerminate(INode[] nodes) {
        if(counter == nodes.length) {
            return true;
        }
        return false;
    }

    double[] mean = null;

    @Override
    public void synchronous(INode[] nodes, int index) {
        // TODO Auto-generated method stub
    }

    private double[] calculateWeight(INode node, int length) {
        return null;
    }

    int counter = 0;

    @Override
    public void asynchronous(INode[] nodes, int index, INode oldNode) {
        if(mean == null) {
            double[][] opinions = Arrays.stream(nodes)
                .map(node -> node.getOpinion()).toArray(double[][]::new);
            mean = Distance.calculateMean(opinions);
        }
        double[] oldWeight = calculateWeight(oldNode, nodes.length);
        double[] newWeight = calculateWeight(nodes[index], nodes.length);
        for(int i = 0; i < mean.length; i++) {
            mean[i] -= oldWeight[i];
            mean[i] += newWeight[i];
        }
        if(Distance.getDistanceEuclideanWithOutCheck(mean, nodes[index].getOpinion()) < epsilon) {
            counter++;
        } else {
            counter = 0;
        }
    }

    @Override
    public ITerminate copyThis() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
