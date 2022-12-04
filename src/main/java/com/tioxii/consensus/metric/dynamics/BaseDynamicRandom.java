package com.tioxii.consensus.metric.dynamics;

import com.tioxii.consensus.metric.api.DynamicName;
import com.tioxii.consensus.metric.api.IDynamic;
import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.api.Parameter;
import com.tioxii.math.RandomMethods;

@DynamicName(name = "base-random")
public class BaseDynamicRandom implements IDynamic {

    @Parameter(shouldPrint = true)
    public double beta;

    public BaseDynamicRandom() {}

    public BaseDynamicRandom(double beta) {
        this.beta = beta;
    }

    /**
     * Decides at random if to use the BaseDynamic or use OneMajorityDynamic
     */
    @Override
    public INode applyDynamicOn(int index, INode[] nodes) {

        if(RandomMethods.flipCoin(this.beta)) {
            return new OneMajorityDynamic().applyDynamicOn(index, nodes);
        } else {
            return new BaseDynamic().applyDynamicOn(index, nodes);
        }
    }

    
    
}
