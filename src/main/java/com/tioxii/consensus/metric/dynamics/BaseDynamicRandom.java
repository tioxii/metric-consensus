package com.tioxii.consensus.metric.dynamics;

import java.util.Random;

import com.tioxii.consensus.metric.nodes.INode;

public class BaseDynamicRandom implements IDynamic {

    /**
     * Decides at random if to use the BaseDynamic or use OneMajorityDynamic
     */
    @Override
    public INode applyDynamicOn(int index, INode[] nodes) {
        Random r = new Random();

        
        if(r.nextBoolean()) {
            return new OneMajorityDynamic().applyDynamicOn(index, nodes);
        } else {
            return new BaseDynamic().applyDynamicOn(index, nodes);
        }
    }
    
}
