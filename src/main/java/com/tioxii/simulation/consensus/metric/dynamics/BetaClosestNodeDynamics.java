package com.tioxii.simulation.consensus.metric.dynamics;

import com.tioxii.simulation.consensus.metric.util.math.RandomMethods;
import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IDynamics;
import com.tioxii.simulation.consensus.metric.util.Parameter;

/**
 * Beta Closest Node Dynamics.
 * Depending on the probability beta, the node will either use the ClosestNodeDynamics or the VoterDynamics.
 */
public class BetaClosestNodeDynamics implements IDynamics {

    /* The probability of using the ClosestNodeDynamics. */
    @Parameter(isParameter = true, name = "Beta")
    public double beta;

    /**
     * Decides at random if to use the ClosestNodeDynamics or use VoterDynamics.
     * @param index of the node the dynamics is applied on.
     * @param nodes that are part of the simulation.
     * @return New node with new adapted opinion/position.
     */
    @Override
    public Node applyDynamicOn(int index, Node[] nodes) {
        /* Flip a coin. */
        if(RandomMethods.flipCoin(this.beta)) {
            return new ClosestNodeDynamics().applyDynamicOn(index, nodes);
        } else {
            return new VoterDynamics().applyDynamicOn(index, nodes);
        }
    }

    
    
}
