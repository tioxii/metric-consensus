package com.tioxii.simulation.consensus.metric.termination;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.ITermination;

public class AlmostConsensusTermination implements ITermination {

    private final int terminationConstant = 10000;

    @Override
    public boolean shouldTerminate(Node[] nodes) {
        if(round >= terminationConstant * nodes.length) {
            return true;
        }
        
        return terminate;
    }

    private int round = 0;
    boolean terminate = false;

    @Override
    public void synchronous(Node[] nodes, int index) {
        round++;

        if(index == 0) {
            terminate = true;
            return;
        }
        terminate = nodes[index].equals(nodes[index-1]) && terminate;
    }

    private int counter = 0;

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

    @Override
    public ITermination copyThis() {
        return new AlmostConsensusTermination();
    }
    
}
