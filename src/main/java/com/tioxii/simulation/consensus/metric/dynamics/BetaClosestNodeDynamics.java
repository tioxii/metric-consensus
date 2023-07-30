package com.tioxii.simulation.consensus.metric.dynamics;

import com.tioxii.simulation.consensus.metric.util.math.RandomMethods;
import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IDynamics;
import com.tioxii.simulation.consensus.metric.util.Parameter;

public class BetaClosestNodeDynamics implements IDynamics {

    @Parameter(isParameter = true, name = "Beta")
    public double beta;

    public BetaClosestNodeDynamics() {}

    public BetaClosestNodeDynamics(double beta) {
        this.beta = beta;
    }

    /**
     * Decides at random if to use the BaseDynamic or use OneMajorityDynamic
     */
    @Override
    public Node applyDynamicOn(int index, Node[] nodes) {

        if(!RandomMethods.flipCoin(this.beta)) {
            return new VoterDynamics().applyDynamicOn(index, nodes);
        } else {
            return new ClosestNodeDynamics().applyDynamicOn(index, nodes);
        }
    }

    
    
}
