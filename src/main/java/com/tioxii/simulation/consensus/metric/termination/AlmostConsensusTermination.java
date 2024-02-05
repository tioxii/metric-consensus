package com.tioxii.simulation.consensus.metric.termination;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.ITermination;

/**
 * AlmostConsensusTermination criterion.
 * Behaves like the default termination, but has a fallback value, that terminates the process after fixed number of rounds.
 * The maximum number of rounds this termination tolerates is 10000.
 */
public class AlmostConsensusTermination implements ITermination {

    /**
     * Maximum number of rounds, this termination tolerates.
     */
    private final int TERMINATION_CONSTANT = 10000;

    /**
     * Checks if the process should terminate.
     * Behaves like in the ConsensusTermination, but with maxmium number of rounds.
     * @param nodes The nodes in the consensus process.
     * @return True if the process should terminates, false if not.
     */
    @Override
    public boolean shouldTerminate(Node[] nodes) {
        if(round >= TERMINATION_CONSTANT * nodes.length) {
            return true;
        }
        
        return terminate;
    }

    /**
     * Current round.
     */
    private int round = 0;

    /**
     * If the process should terminate.
     */
    boolean terminate = false;

    /**
     * @see com.tioxii.simulation.consensus.metric.termination.ConsensusTermination
     */
    @Override
    public void synchronous(Node[] nodes, int index) {
        round++;

        if(index == 0) {
            terminate = true;
            return;
        }
        terminate = nodes[index].equals(nodes[index-1]) && terminate;
    }

    /**
     * How many nodes had the same opinion in a row.
     */
    private int counter = 0;

    /**
     * @see com.tioxii.simulation.consensus.metric.termination.ConsensusTermination
     */
    @Override
    public void asynchronous(Node[] nodes, int index, Node oldNode) {
        round++;

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
     * Copy this object instance. In this case there is nothing to copy, so it is just a new instance.
     * @return Copy of this object instance.
     */
    @Override
    public ITermination copyThis() {
        return new AlmostConsensusTermination();
    }
    
}
