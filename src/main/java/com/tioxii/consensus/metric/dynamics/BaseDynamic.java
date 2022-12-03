package com.tioxii.consensus.metric.dynamics;

import com.tioxii.consensus.metric.api.DynamicName;
import com.tioxii.consensus.metric.api.IDynamic;
import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.util.DynamicUtil;
import com.tioxii.math.Distance;
import com.tioxii.math.exceptions.DifferentDimensionsException;

@DynamicName(name = "base")
public class BaseDynamic implements IDynamic {
    //private String metric = "euclidean";

    @Override
    public INode applyDynamicOn(int index, final INode[] nodes) {

        double[][] opinions = DynamicUtil.selectRandomOpinion(index, 2, nodes);

        double[] newOpinion = new double[opinions[2].length];

        try {
            //Adapt new opinion
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
            e.printStackTrace();
        }   

        return DynamicUtil.createNewNode(nodes[index], newOpinion);
    }
}
