package com.tioxii.consensus.metric.termination;

import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.api.ITerminate;

public class BaseTermination implements ITerminate {

    @Override
    public boolean shouldTerminate(INode[] nodes) {
        return terminate;
    }

    private boolean terminate = false;

    public void synchronous(INode[] nodes, int index) {
        if(index == 0) {
            terminate = true;
            return;
        }
        terminate = nodes[index].equals(nodes[index-1]) && terminate;
    }

    private int counter = 0;

    public void asynchronous(INode[] nodes, int index, INode oldNode) {
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

    public ITerminate copyThis() {
        return new BaseTermination();
    }
}
