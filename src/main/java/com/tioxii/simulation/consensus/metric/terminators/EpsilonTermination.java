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
        if(counter < nodes.length)
            return false;

        counter = 0;
        double[][] opinions = Arrays.stream(nodes)
            .map(node -> node.getOpinion()).toArray(double[][]::new);
        double[] mean = Distance.calculateMean(opinions);

        for(int i = nodes.length - 1; i >= 0 ; i--) {
            if(Distance.getDistanceEuclideanWithOutCheck(mean, nodes[i].getOpinion()) >= this.epsilon) {
                return false;
            }
        }
        return true;
    }

    int counter = 0;

    @Override
    public void synchronous(Node[] nodes, int index) { counter++; }

    @Override
    public void asynchronous(Node[] nodes, int index, Node oldNode) { counter++; }

    @Override
    public ITerminate copyThis() {
        return new EpsilonTermination(this.epsilon);
    }
    
}
