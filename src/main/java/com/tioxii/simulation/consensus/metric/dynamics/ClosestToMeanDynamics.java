package com.tioxii.simulation.consensus.metric.dynamics;

import com.tioxii.simulation.consensus.metric.util.math.Distance;
import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IDynamics;
import com.tioxii.simulation.consensus.metric.util.DynamicsUtil;

public class ClosestToMeanDynamics implements IDynamics {
    int h = 2;

    /**
     * Calculates the mean of 2 other nodes and itself and adapts the opinion of the closest node to the mean.
     * @param index of the node the dynamics is applied on.
     * @param nodes that are part of the simulation.
     * @return New node with new adapted opinion/position.
     */
    @Override
    public Node applyDynamicOn(int index, Node[] nodes) {
        double[][] opinions = DynamicsUtil.selectRandomOpinion(index, h, nodes);
        double[] mean = new double[opinions[h].length];

        /* Calculate the mean. */
        for(int i = 0; i < opinions.length; i++) {
            for (int j = 0; j < opinions[i].length; j++) {
                mean[j] += (opinions[i][j]/opinions.length);
            }
        }

        /* Calculate the distances to the mean. */
        double[] distances = new double[opinions.length];
        /* Keeps track of the shortest distance in the distances array. To avoid running the loop twice. */
        int shortestDistanceIndex = 0; 
        for(int i = 0; i < distances.length; i++) {
            distances[i] = Distance.getDistanceEuclideanWithOutCheck(mean, opinions[i]);
            if(distances[i] < distances[shortestDistanceIndex]) {
                shortestDistanceIndex = i;
            }
        }

        /* Create new node with new opinion/position. */
        return DynamicsUtil.createNewNode(nodes[index], opinions[shortestDistanceIndex]);
    }
    
    

}
