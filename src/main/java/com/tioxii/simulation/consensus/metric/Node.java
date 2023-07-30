package com.tioxii.simulation.consensus.metric;

import java.util.Arrays;

public class Node {
    private double[] opinion = null;

    //Is the node honest?
    private boolean isHonest = true;

    public Node(double[] newOpinion) {
        this.opinion = Arrays.copyOf(newOpinion, newOpinion.length);
    }

    /**
     * Used in the evaluation
     */
    final public double[] getOpinion() {
        return opinion;
    }

    /**
     * Used in dynamics
     */
    public double[] askOpinion() {
        return opinion;
    }

    /**
     * Checks if another node has the same opinion as this one.
     * @param node Other node.
     * @return True when equal.
     */
    public boolean equals(Node node) {
        return Arrays.equals(opinion, node.getOpinion());
    }

    /**
     * Makes this node dishonest.
     */
    public void setDishonest() {
        this.isHonest = false;
    }

    /**
     * Tests if this node is honest.
     * @return True when honest. False when dishonest.
     */
    public boolean ishonest() {
        return this.isHonest;
    }
}
