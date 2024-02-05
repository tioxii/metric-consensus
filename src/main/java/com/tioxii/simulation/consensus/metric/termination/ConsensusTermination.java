package com.tioxii.simulation.consensus.metric.termination;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.ITermination;

/**
 * ConsensusTermination.
 * This is the standard termination condition for a consensus process.
 * The process terminates, when all nodes have the same opinion/position.
 * A consenus is reached.
 */
public class ConsensusTermination implements ITermination {

    /**
     * This method checks if the consensus process should terminate.
     * @param nodes The nodes of the consensus process.
     * @return True if the consensus process should terminate, false otherwise.
     */
    @Override
    public boolean shouldTerminate(Node[] nodes) {
        return terminate;
    }

    /**
     * 
     */
    private boolean terminate = false;

    /**
     * In the synchronous case all nodes update at the same time.
     * But the calculation are done sequentially.
     * This methods checks during the calculation if the consensus process should terminate.
     * By checking if the current node is equal to the previous node.
     * @param nodes The nodes of the consensus process.
     * @param index The index of the current node.
     */
    public void synchronous(Node[] nodes, int index) {
        if(index == 0) {
            terminate = true;
            return;
        }
        terminate = nodes[index].equals(nodes[index-1]) && terminate;
    }

    private int counter = 0;

    /**
     * In the asynchronous case all nodes update at different times.
     * This methods checks during the calculation if the consensus process should terminate.
     * By checking if all nodes have the same opinion.
     * @param nodes The nodes of the consensus process.
     * @param index The index of the current node.
     * @param oldNode The node before it got updated.
     */
    public void asynchronous(Node[] nodes, int index, Node oldNode) {
        int indexSecond = index - 1;
        if(indexSecond == -1) {
            indexSecond += nodes.length;
        }
        if(!nodes[index].equals(nodes[indexSecond])) {
            counter = 0;
            return;
        }
        counter++;
        if(counter == nodes.length) {
            terminate = true;
        }
    }

    /**
     * This method creates a copy of the current termination condition.
     */
    public ITermination copyThis() {
        return new ConsensusTermination();
    }
}
