package com.tioxii.simulation.consensus.metric.dynamics;

import com.tioxii.simulation.consensus.metric.util.math.Distance;
import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IDynamics;
import com.tioxii.simulation.consensus.metric.util.DynamicsUtil;

public class ClosestToMeanDynamics implements IDynamics {
    int h = 2;

    @Override
    public Node applyDynamicOn(int index, Node[] nodes) {
        double[][] opinions = DynamicsUtil.selectRandomOpinion(index, h, nodes);
        double[] mean = new double[opinions[h].length];

        for(int i = 0; i < opinions.length; i++) {
            for (int j = 0; j < opinions[i].length; j++) {
                mean[j] += (opinions[i][j]/opinions.length);
            }
        }
        double[] distances = new double[opinions.length];
        int shortestDistanceIndex = 0; //Keeps track of the shortest distance in the distances array. To avoid running the loop twice.
        for(int i = 0; i < distances.length; i++) {
            distances[i] = Distance.getDistanceEuclideanWithOutCheck(mean, opinions[i]);
            if(distances[i] < distances[shortestDistanceIndex]) {
                shortestDistanceIndex = i;
            }
        }

        return DynamicsUtil.createNewNode(nodes[index], opinions[shortestDistanceIndex]);
    }
    
    

}
