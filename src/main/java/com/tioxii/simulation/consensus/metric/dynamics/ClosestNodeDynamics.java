package com.tioxii.simulation.consensus.metric.dynamics;

import com.tioxii.simulation.consensus.metric.util.math.Distance;
import com.tioxii.simulation.consensus.metric.util.math.exceptions.DifferentDimensionsException;
import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IDynamics;
import com.tioxii.simulation.consensus.metric.util.DynamicsUtil;

/**
 * Closest Node Dynamics.
 * A node will ask two random nodes for their opinion and adapt the opinion/position of the closest node.
 */
public class ClosestNodeDynamics implements IDynamics {
    

    @Override
    public Node applyDynamicOn(int index, final Node[] nodes) {

        /* Select two unique random nodes. */
        double[][] opinions = DynamicsUtil.selectRandomOpinion(index, 2, nodes);

        /* Array that should later contain the new opinion. */
        double[] newOpinion = new double[opinions[2].length];

        try {
            /* Check distances */
            /* Adapat opinion of the closest node. */
            if(Distance.getDistanceEuclidean(opinions[2], opinions[0]) < Distance.getDistanceEuclidean(opinions[2], opinions[1])) {
                for (int i = 0; i < newOpinion.length; i++) {
                    newOpinion[i] = (double) opinions[0][i];
                }
            } else {
                for (int i = 0; i < newOpinion.length; i++) {
                    newOpinion[i] = opinions[1][i];
                }
            }
        } catch(DifferentDimensionsException e) {
            /* Does nothing really in case of exception. TODO: Change that. */
            e.printStackTrace();
        }   

        /* Create new node with new opinion. */
        return DynamicsUtil.createNewNode(nodes[index], newOpinion);
    }
}
