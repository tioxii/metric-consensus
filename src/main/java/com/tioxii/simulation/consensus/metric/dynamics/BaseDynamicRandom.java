package com.tioxii.simulation.consensus.metric.dynamics;

import com.tioxii.math.RandomMethods;
import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IDynamic;
import com.tioxii.util.Parameter;

public class BaseDynamicRandom implements IDynamic {

    @Parameter(isParameter = true, name = "Beta")
    public double beta;

    public BaseDynamicRandom() {}

    public BaseDynamicRandom(double beta) {
        this.beta = beta;
    }

    /**
     * Decides at random if to use the BaseDynamic or use OneMajorityDynamic
     */
    @Override
    public Node applyDynamicOn(int index, Node[] nodes) {

        if(!RandomMethods.flipCoin(this.beta)) {
            return new OneMajorityDynamic().applyDynamicOn(index, nodes);
        } else {
            return new BaseDynamic().applyDynamicOn(index, nodes);
        }
    }

    
    
}
