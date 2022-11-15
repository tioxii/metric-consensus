package com.tioxii.consensus.metric;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tioxii.consensus.metric.api.IDynamic;
import com.tioxii.consensus.metric.api.INode;

public class Network implements Runnable {
    //Environment constraints
    private IDynamic dynamic;
    private INode[] nodes = null;
    private boolean isSynchronous = true;
    public Thread t = null;

    //Logging
    public static Logger LOGGER = LogManager.getLogger(Network.class.getName());
    public static Semaphore MUTEX = Simulation.MUTEX;

    //Measurements
    private int rounds = 0;
    public double[] startMean = null;
    public double[] endMean = null;
    private ArrayList<double[]> nodesHistroy = new ArrayList<double[]>();

    /**
     * Constructor
     * @param dynamic
     * @param nodes
     * @param isSynchronous
     */
    public Network(IDynamic dynamic, INode[] nodes, boolean isSynchronous) {
        this.dynamic = dynamic;
        this.nodes = nodes;
        this.isSynchronous = isSynchronous;
    }
    
    /**
     * Runs the Simulation
     * @return State of consensus
     */
    public void run() {
        
        startMean = calculateMean();
        
        //when synchronous, all nodes are updated at the same time
        //when asynchronous, nodes are updated one at a time
        if (isSynchronous) {
            synchronous(); 
        } else {
            asynchronous();
        }
        
        endMean = calculateMean();

        //MUTEX.release();
    }

    /**
     * Is Consensus Reached?
     * @return
     */
    public boolean isConsensusReached() {
        //checks if the opinion of all nodes is the same
        for (int i = 1; i < nodes.length; i++) {
            if(!nodes[0].equals(nodes[i]))
                return false;    
        }
        return true;
    }

    /**
     * All nodes are updated at the same time.
     * @return true if the simulation has converged, false otherwise
     */
    public boolean synchronous() {
        boolean converged = false;

        do {

            INode[] newNodes = new INode[nodes.length];

            for(int i = 0; i < nodes.length; i++) {
                newNodes[i] = dynamic.applyDynamicOn(i, nodes);
            }

            //update nodes
            nodes = newNodes;

            rounds++;
            LOGGER.debug("Round: " + rounds);

            //check if converged
            converged = isConsensusReached();
        } while (!converged);

        return converged;
    }

    /**
     * Nodes are updated one at a time
     * @return
     */
    public boolean asynchronous() {
        //update nodes one at a time
        int i = 0;
        boolean converged = false;

        do {

            nodes[i] = dynamic.applyDynamicOn(i, nodes);
            i = (i + 1) % nodes.length;

            rounds++;
            
            //System.out.println("Round: " + rounds);
            
            //check if converged
            converged = isConsensusReached();

        } while (!converged);

        return converged;
    }

    /**
     * Write nodes to CSV file
     */
    public void writeNodesToCSV() {
        //TODO write to CSV file
    }

    /**
     * Return the Number of Rounds
     */
    public int getRounds() {
        return this.rounds;
    }

    /**
     * Return History
     */
    public ArrayList<?> getHistory() {
        return this.nodesHistroy;
    }

    /**
     * Calculate the mean position of all nodes
     * @return
     */
    public double[] calculateMean() {
        double[] mean = new double[nodes[0].getOpinion().length];

        for(int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[i].getOpinion().length; j++) {
                mean[j] += (nodes[i].getOpinion()[j] / nodes.length);
            }
        }

        return mean;
    }

    public INode[] getNodes() {
        return nodes;
    }
}
