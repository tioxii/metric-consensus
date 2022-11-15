package com.tioxii.consensus.metric.dynamics;

import com.tioxii.consensus.metric.api.IDynamic;
import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.util.DynamicUtil;

public class BaseDynamic implements IDynamic {
    //private String metric = "euclidean";

    @Override
    public INode applyDynamicOn(int index, final INode[] nodes) {

        double[][] opinions = DynamicUtil.selectRandomOpinion(index, 2, nodes);

        double[] newOpinion = new double[opinions[2].length];

        //Adapt new opinion
        if(getDistance(opinions[2], opinions[0]) < getDistance(opinions[2], opinions[1])) {
            for (int i = 0; i < newOpinion.length; i++) {
                newOpinion[i] = (double) opinions[0][i];
            }
        } else {
            for (int i = 0; i < newOpinion.length; i++) {
                newOpinion[i] = opinions[1][i];
            }
        }

        return DynamicUtil.createNewNode(nodes[index], newOpinion);
    }

    /**
     * Calculate the euclidean distance between two points.
     * @param opinion1
     * @param opinion2
     * @return
     */
    double getDistance(double[] opinion1, double[] opinion2) {
        double distance = 0.0f;
        for (int i = 0; i < opinion1.length; i++) {
            distance += Math.pow(opinion1[i] - opinion2[i], 2);
        }
        return (double) Math.sqrt(distance);
    }

}
