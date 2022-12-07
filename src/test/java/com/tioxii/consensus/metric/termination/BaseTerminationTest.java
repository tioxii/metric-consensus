package com.tioxii.consensus.metric.termination;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import com.tioxii.consensus.metric.Network;
import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.dynamics.BaseDynamic;
import com.tioxii.consensus.metric.exceptions.NodeGenerationException;
import com.tioxii.consensus.metric.generation.RandomNodes;
import com.tioxii.consensus.metric.nodes.BaseNode;

public class BaseTerminationTest {
    
    private boolean areNodesEqual(INode[] nodes) {
        for(int i = 1; i < nodes.length; i++) {
            if(!nodes[0].equals(nodes[i])) {
                return false;
            }
        }
        return true;
    }

    public void printNodes(INode[] nodes) {
        Arrays.stream(nodes).forEach(node -> {
            System.out.println(Arrays.toString(node.getOpinion()));
        });
    }

    private boolean areDoubleArraysEqual(double[][] position) {
        for (int i = 1; i < position.length; i++) {
            if(!position[0].equals(position[i])) {
                return false;
            }
        }
        return true;
    }

    private boolean isHistoryUnequal(ArrayList<double[][]> nodes) {
        boolean isEqual = false;
        for (double[][] ds : nodes) {
            isEqual = isEqual || areDoubleArraysEqual(ds);
        }
        return isEqual;
    }

    @Test
    public void testBaseTerminationSynchronous() {
        int TEST_ROUNDS = 1000;
        Network network = null;
        BaseTermination termination = new BaseTermination();
        RandomNodes random = new RandomNodes(2, BaseNode.class);
        BaseDynamic dynamic = new BaseDynamic();

        try {
            for(int i = 0; i < TEST_ROUNDS; i++) {
                network = new Network(dynamic, random.generate(1000), true, termination.copyThis(), true);
                network.setThread(new Thread(network));
                network.getThread().start();

                network.getThread().join();
                INode[] nodes = network.getNodes();
                int rounds = network.getRounds();
                ArrayList<double[][]> history = network.getHistory();
                history.remove(history.size() - 1);
                System.out.println("Round " + i + " complete with " + rounds + " Rounds.");
                
                assertTrue(!isHistoryUnequal(history));
                assertTrue(areNodesEqual(nodes));
            }
        } catch (NodeGenerationException | InterruptedException e) {
            e.printStackTrace();
            network = null;
        }
        assertNotNull(network);
    }

    @Test
    public void testBaseTerminationAsynchronous() {
        int TEST_ROUNDS = 1000;
        Network network = null;
        BaseTermination termination = new BaseTermination();
        RandomNodes random = new RandomNodes(2, BaseNode.class);
        BaseDynamic dynamic = new BaseDynamic();

        try {
            for(int i = 0; i < TEST_ROUNDS; i++) {
                network = new Network(dynamic, random.generate(1000), false, termination.copyThis(), true);
                network.setThread(new Thread(network));
                network.getThread().start();

                network.getThread().join();
                INode[] nodes = network.getNodes();
                int rounds = network.getRounds();
                ArrayList<double[][]> history = network.getHistory();
                history.remove(history.size() - 1);
                System.out.println("Round " + i + " complete with " + rounds + " Rounds.");

                assertTrue(!isHistoryUnequal(history));
                assertTrue(areNodesEqual(nodes));
            }
        } catch (NodeGenerationException | InterruptedException e) {
            e.printStackTrace();
            network = null;
        }
        assertNotNull(network);
    }
}