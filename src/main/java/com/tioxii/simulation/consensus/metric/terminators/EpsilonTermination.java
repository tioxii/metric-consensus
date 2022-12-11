package com.tioxii.simulation.consensus.metric.terminators;

import java.util.Arrays;

import com.tioxii.math.Distance;
import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.ITerminate;
import com.tioxii.util.Parameter;

public class EpsilonTermination implements ITerminate {

    @Parameter(isParameter = true, name = "Epsilon")
    public double epsilon;

    public EpsilonTermination(double epsilon) {
        this.epsilon = epsilon;
    }
    
    @Override
    public boolean shouldTerminate(Node[] nodes) {
        if(counter == nodes.length) {
            return true;
        }
        return false;
    }

    double[] mean = null;

    @Override
    public void synchronous(Node[] nodes, int index) {
        // TODO Auto-generated method stub
    }

    private double[] calculateWeight(Node node, int length) {
        double[] weight = new double[node.getOpinion().length];
        for(int i = 0; i < weight.length; i++) {
            weight[i] = node.getOpinion()[i] / (double) length;
        }
        return weight;
    }

    int counter = 0;

    @Override
    public void asynchronous(Node[] nodes, int index, Node oldNode) {
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
        return new EpsilonTermination(this.epsilon);
    }
    
}
