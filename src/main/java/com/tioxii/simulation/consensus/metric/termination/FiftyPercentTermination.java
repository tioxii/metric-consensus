package com.tioxii.simulation.consensus.metric.termination;

import java.util.Arrays;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.ITermination;

public class FiftyPercentTermination implements ITermination {

    double[] byzantinePosition = null;
    int termination_value = 10000;

    int round = 0;
    int counter = 0;

    public FiftyPercentTermination(double[] byzantinePosition) {
        this.byzantinePosition = byzantinePosition;
    }

    @Override
    public boolean shouldTerminate(Node[] nodes) {
        if(this.counter > nodes.length * 0.5) {
            return true;
        }
        if(this.round >= nodes.length * termination_value) {
            return true;
        }
        return false;
    }

    private boolean isOnByzantinePosition(Node node) {
        return Arrays.equals(node.getOpinion(), byzantinePosition);
    }

    @Override
    public void synchronous(Node[] nodes, int index) {
        this.round++;
        if(index == 0) {
            this.counter = 0;
        }
        if(isOnByzantinePosition(nodes[index])) {
            this.counter++;
        }   
    }

    private int countNodesOnByzantinePosition(Node[] nodes) {
        return Arrays.stream(nodes)
            .map(node -> {
                return isOnByzantinePosition(node) ? 1 : 0;
            }).mapToInt(Integer::intValue)
            .sum();
    }

    @Override
    public void asynchronous(Node[] nodes, int index, Node oldNode) {
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
    public ITermination copyThis() {
        return new FiftyPercentTermination(byzantinePosition);
    }   
}
