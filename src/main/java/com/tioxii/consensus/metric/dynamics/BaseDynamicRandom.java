package com.tioxii.consensus.metric.dynamics;

import com.tioxii.consensus.metric.api.IDynamic;
import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.util.DynamicUtil;

public class BaseDynamicRandom implements IDynamic {

    double beta;

    public BaseDynamicRandom(double beta) {

    }

    /**
     * Decides at random if to use the BaseDynamic or use OneMajorityDynamic
     */
    @Override
    public INode applyDynamicOn(int index, INode[] nodes) {

        if(DynamicUtil.flipCoin(this.beta)) {
            return new OneMajorityDynamic().applyDynamicOn(index, nodes);
        } else {
            return new BaseDynamic().applyDynamicOn(index, nodes);
        }
    }

    
    
}
