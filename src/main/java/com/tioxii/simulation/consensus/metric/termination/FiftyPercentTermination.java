package com.tioxii.simulation.consensus.metric.termination;

import java.util.Arrays;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.ITermination;

/**
 * FiftyPercentTermination. This class implements a termination condition.
 * Terminates, when 50% of the nodes have reached the same opinion, which also equals the opinion of the dishonest nodes.
 * A process is corrupted when 50% of the nodes have reached the opinion of the byzantine nodes.
 * From there is is only a matter of time until all nodes have agreed on the byzantine nodes.
 */
public class FiftyPercentTermination implements ITermination {

    /**
     * The position of the byzantine nodes cluster.
     */
    double[] byzantinePosition = null;

    /**
     * Maximum number of rounds before termination.
     * In this case, it possible that the algorithm never terminates.
     */
    int termination_value = 10000;

    /**
     * The number of rounds that have been executed.
     */
    int round = 0;

    /**
     * The number of nodes that have reached the opinion of the byzantine nodes.
     * Including the byzantine nodes themselves.
     */
    int counter = 0;

    public FiftyPercentTermination(double[] byzantinePosition) {
        this.byzantinePosition = byzantinePosition;
    }

    /**
     * This method checks if the termination condition is met.
     * The termination condition is met, when 50% of the nodes have reached the opinion of the byzantine nodes.
     * @param nodes The nodes that are part of the consensus algorithm.
     * @return True, if the termination condition is met. False, otherwise.
     */
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

    /**
     * This method checks if a node is on the position of the byzantine nodes.
     * @param node The node that is checked.
     * @return True, if the node is on the position of the byzantine nodes. False, otherwise.
     */
    private boolean isOnByzantinePosition(Node node) {
        return Arrays.equals(node.getOpinion(), byzantinePosition);
    }

    /**
     * This method is called when the consensus algorithm is executed synchronously.
     * It increments the round counter and checks if a node is on the position of the byzantine nodes.
     * @param nodes The nodes that are part of the consensus algorithm.
     * @param index The index of the node that is checked.
     */
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

    /**
     * This method counts the number of nodes that are on the position of the byzantine nodes.
     * @param nodes The nodes that are part of the consensus algorithm.
     * @return The number of nodes that are on the position of the byzantine nodes.
     */
    private int countNodesOnByzantinePosition(Node[] nodes) {
        return Arrays.stream(nodes)
            .map(node -> {
                return isOnByzantinePosition(node) ? 1 : 0;
            }).mapToInt(Integer::intValue)
            .sum();
    }

    /**
     * This method is called when the consensus algorithm is executed asynchronously.
     * It increments the round counter and checks if a node is on the position of the byzantine nodes.
     * @param nodes The nodes that are part of the consensus algorithm.
     * @param index The index of the node that is checked.
     * @param oldNode The old state of the node that is checked.
     */
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

    /**
     * This method creates a copy of the termination condition.
     * @return A copy of the termination condition.
     */
    @Override
    public ITermination copyThis() {
        return new FiftyPercentTermination(byzantinePosition);
    }   
}
