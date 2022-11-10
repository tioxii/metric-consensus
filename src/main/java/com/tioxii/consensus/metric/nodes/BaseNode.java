package com.tioxii.consensus.metric.nodes;

import java.util.Arrays;
import java.util.Random;

public class BaseNode implements INode {
    double[] opinion = null;

    public BaseNode(double[] newOpinion) {
        this.opinion = newOpinion;
    }

    /**
     * @param dim
     */
    public BaseNode(int dim) {
        Random r = new Random();

        opinion = new double[dim];

        for(int i = 0; i < dim; i++) {
            opinion[i] = r.nextDouble();
        }
    }

    @Override
    public double[] getOpinion() {
        return opinion;
    }

    @Override
    public boolean equals(INode node) {
        return Arrays.equals(opinion, node.getOpinion());
    }
}
