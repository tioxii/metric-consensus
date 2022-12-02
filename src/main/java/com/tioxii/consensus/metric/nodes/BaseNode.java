package com.tioxii.consensus.metric.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.tioxii.consensus.metric.api.INode;

public class BaseNode implements INode {
    private double[] opinion = null;
    
    //If null full graph is assumed.
    public ArrayList<INode> neighbors = null;

    //Is the node honest?
    private boolean isHonest = true;

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

    /**
     * Used in the evaluation
     */
    @Override
    public double[] getOpinion() {
        return opinion;
    }

    /**
     * Used in dynamics
     */
    @Override
    public double[] askOpinion() {
        return opinion;
    }

    /**
     * Checks if another node has the same opinion as this one.
     * @param node Other node.
     * @return True when equal.
     */
    @Override
    public boolean equals(INode node) {
        return Arrays.equals(opinion, node.getOpinion());
    }

    /** 
     * Adds a neighbor. Useful for graphs.
     */
    public void addNeighbor(INode node) {
        if(neighbors != null) {
            neighbors.add(node);
        }
    }

    /**
     * Makes this node dishonest.
     */
    @Override
    public void setDishonest() {
        this.isHonest = false;
    }

    /**
     * Tests if this node is honest.
     * @return False when dishonest.
     */
    @Override
    public boolean ishonest() {
        return this.isHonest;
    }
}
