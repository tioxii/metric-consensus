package com.tioxii.simulation.consensus.metric.dynamics;

import com.tioxii.math.Distance;
import com.tioxii.math.exceptions.DifferentDimensionsException;
import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IDynamic;
import com.tioxii.simulation.consensus.metric.util.DynamicUtil;

public class BaseDynamic implements IDynamic {
    //private String metric = "euclidean";

    @Override
    public Node applyDynamicOn(int index, final Node[] nodes) {

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
