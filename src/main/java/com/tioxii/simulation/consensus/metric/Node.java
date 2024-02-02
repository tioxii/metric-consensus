package com.tioxii.simulation.consensus.metric;

import java.util.Arrays;

/**
 * Node class, used in the consensus simulation.
 * Each node holds a multidimensional opinion/position represented by a double array.
 */
public class Node {

    /**
     * The opinion/position of the node.
     */
    private double[] opinion = null;

    /**
     * Tells if the node is honest and tells the truth and behaves according to the protocol.
     * True when the node is honest. False when dishonest.
     */
    private boolean isHonest = true;

    public Node(double[] newOpinion) {
        this.opinion = Arrays.copyOf(newOpinion, newOpinion.length);
    }

    /**
     * Get the opinion/position of the node.
     * @return The opinion/position of the node.
     */
    final public double[] getOpinion() {
        return opinion;
    }

    /**
     * Used in dynamics.
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
     * Base value is true, so this method is used to make the node dishonest.
     * For initialization purposes.
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
