package com.tioxii.consensus.metric.termination;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import com.tioxii.simulation.consensus.metric.Simulation;
import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.configurations.RandomNodes;
import com.tioxii.simulation.consensus.metric.dynamics.ClosestNodeDynamics;
import com.tioxii.simulation.consensus.metric.exceptions.ConfigurationInitException;
import com.tioxii.simulation.consensus.metric.termination.ConsensusTermination;

public class BaseTerminationTest {
    
    private boolean areNodesEqual(Node[] nodes) {
        for(int i = 1; i < nodes.length; i++) {
            if(!nodes[0].equals(nodes[i])) {
                return false;
            }
        }
        return true;
    }

    public void printNodes(Node[] nodes) {
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
        Simulation network = null;
        ConsensusTermination termination = new ConsensusTermination();
        RandomNodes random = new RandomNodes(2, Node.class);
        ClosestNodeDynamics dynamic = new ClosestNodeDynamics();

        try {
            for(int i = 0; i < TEST_ROUNDS; i++) {
                network = new Simulation(dynamic, random.generate(1000), true, termination.copyThis(), true);
                network.setThread(new Thread(network));
                network.getThread().start();

                network.getThread().join();
                Node[] nodes = network.getNodes();
                int rounds = network.getRounds();
                ArrayList<double[][]> history = network.getHistory();
                history.remove(history.size() - 1);
                System.out.println("Round " + i + " complete with " + rounds + " Rounds.");
                
                assertTrue(!isHistoryUnequal(history));
                assertTrue(areNodesEqual(nodes));
            }
        } catch (ConfigurationInitException | InterruptedException e) {
            e.printStackTrace();
            network = null;
        }
        assertNotNull(network);
    }

    @Test
    public void testBaseTerminationAsynchronous() {
        int TEST_ROUNDS = 1000;
        Simulation network = null;
        ConsensusTermination termination = new ConsensusTermination();
        RandomNodes random = new RandomNodes(2, Node.class);
        ClosestNodeDynamics dynamic = new ClosestNodeDynamics();

        try {
            for(int i = 0; i < TEST_ROUNDS; i++) {
                network = new Simulation(dynamic, random.generate(1000), false, termination.copyThis(), true);
                network.setThread(new Thread(network));
                network.getThread().start();

                network.getThread().join();
                Node[] nodes = network.getNodes();
                int rounds = network.getRounds();
                ArrayList<double[][]> history = network.getHistory();
                history.remove(history.size() - 1);
                System.out.println("Round " + i + " complete with " + rounds + " Rounds.");

                assertTrue(!isHistoryUnequal(history));
                assertTrue(areNodesEqual(nodes));
            }
        } catch (ConfigurationInitException | InterruptedException e) {
            e.printStackTrace();
            network = null;
        }
        assertNotNull(network);
    }
}
