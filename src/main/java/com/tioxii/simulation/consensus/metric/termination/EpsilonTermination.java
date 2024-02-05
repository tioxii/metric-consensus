package com.tioxii.simulation.consensus.metric.termination;

import java.util.Arrays;

import com.tioxii.simulation.consensus.metric.util.math.Distance;
import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.ITermination;
import com.tioxii.simulation.consensus.metric.util.Parameter;
/**
 * The EpsilonTermination condition.
 * Terimnates, when the distance between the mean opinion and the opinion of every node is less than epsilon.
 */
public class EpsilonTermination implements ITermination {

    /**
     * The epsilon value.
     */
    @Parameter(isParameter = true, name = "Epsilon")
    public double epsilon;
    
    /**
     * Checks if the termination condition is met.
     * @param nodes The nodes in the simulation.
     * @return true if the termination condition is met, false otherwise.
     */
    @Override
    public boolean shouldTerminate(Node[] nodes) {
        /* Checks if the counter has run through. */
        if(counter < nodes.length)
            return false;

        /* Resets the counter. */
        counter = 0;

        /* Calculate the mean. */
        double[][] opinions = Arrays.stream(nodes)
            .map(node -> node.getOpinion()).toArray(double[][]::new);
        double[] mean = Distance.calculateMean(opinions);

        /* Check if the distance between the mean and the opinion of every node is less than epsilon. */
        for(int i = nodes.length - 1; i >= 0 ; i--) {
            if(Distance.getDistanceEuclideanWithOutCheck(mean, nodes[i].getOpinion()) >= this.epsilon) {
                return false;
            }
        }
        return true;
    }

    /**
     * The counter for the termination condition.
     */
    int counter = 0;

    /**
     * The synchronous method for the termination condition.
     * @param nodes The nodes in the simulation.
     * @param index The index of the node we are currently at in the simulation.
     */
    @Override
    public void synchronous(Node[] nodes, int index) { counter++; }

    /**
     * The asynchronous method for the termination condition.
     * @param nodes The nodes in the simulation.
     * @param index The index of the node we are currently at in the simulation.
     * @param oldNode The node that was updated.
     */
    @Override
    public void asynchronous(Node[] nodes, int index, Node oldNode) { counter++; }

    /**
     * The copy method for the termination condition.
     */
    @Override
    public ITermination copyThis() {
        return new EpsilonTermination();
    }
    
}
