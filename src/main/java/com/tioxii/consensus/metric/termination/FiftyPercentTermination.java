package com.tioxii.consensus.metric.termination;

import java.util.Arrays;

import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.api.ITerminate;

public class FiftyPercentTermination implements ITerminate {

    double[] byzantinePosition = null;
    int termination_value = 10000;

    int round = 0;
    int counter = 0;

    public FiftyPercentTermination(double[] byzantinePosition) {
        this.byzantinePosition = byzantinePosition;
    }

    @Override
    public boolean shouldTerminate(INode[] nodes) {
        if(this.counter > nodes.length * 0.5) {
            return true;
        }
        if(this.round >= nodes.length * termination_value) {
            return true;
        }
        return false;
    }

    private boolean isOnByzantinePosition(INode node) {
        return Arrays.equals(node.getOpinion(), byzantinePosition);
    }

    @Override
    public void synchronous(INode[] nodes, int index) {
        this.round++;
        if(index == 0) {
            this.counter = 0;
        }
        if(isOnByzantinePosition(nodes[index])) {
            this.counter++;
        }   
    }

    private int countNodesOnByzantinePosition(INode[] nodes) {
        return Arrays.stream(nodes)
            .map(node -> {
                return isOnByzantinePosition(node) ? 1 : 0;
            }).mapToInt(Integer::intValue)
            .sum();
    }

    @Override
    public void asynchronous(INode[] nodes, int index, INode oldNode) {
        round++;
        if(counter == 0 && index == 0) {
            counter = countNodesOnByzantinePosition(nodes);
            System.out.println(counter);
        }
        boolean bOldNode = isOnByzantinePosition(oldNode);
        boolean bNewNode = isOnByzantinePosition(nodes[index]);

        if(bOldNode == bNewNode) {
            return;
        }
        if(bOldNode) {
            counter--;
        } else {
            counter++;
        }
    }

    @Override
    public ITerminate copyThis() {
        return new FiftyPercentTermination(byzantinePosition);
    }   
}
