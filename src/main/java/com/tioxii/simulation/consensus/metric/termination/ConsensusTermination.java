package com.tioxii.simulation.consensus.metric.termination;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.ITermination;

public class ConsensusTermination implements ITermination {

    @Override
    public boolean shouldTerminate(Node[] nodes) {
        return terminate;
    }

    private boolean terminate = false;

    public void synchronous(Node[] nodes, int index) {
        if(index == 0) {
            terminate = true;
            return;
        }
        terminate = nodes[index].equals(nodes[index-1]) && terminate;
    }

    private int counter = 0;

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

    public ITermination copyThis() {
        return new ConsensusTermination();
    }
}
