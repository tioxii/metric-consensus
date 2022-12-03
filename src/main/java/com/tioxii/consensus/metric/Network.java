package com.tioxii.consensus.metric;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tioxii.consensus.metric.api.IDynamic;
import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.api.ITerminate;

public class Network implements Runnable {
    //Environment constraints
    private IDynamic dynamic;
    private INode[] nodes = null;
    private ITerminate terminate = null;
    private boolean isSynchronous = true;
    public Thread t = null;

    //Logging
    public static Logger log = LogManager.getLogger(Network.class.getName());

    //Measurements
    private int rounds = 0;
    public double[] startMean = null;
    public double[] endMean = null;
    public boolean LOG_NODEHISTROY = true;
    private ArrayList<double[][]> nodesHistroy = new ArrayList<double[][]>();

    /**
     * Constructor
     * @param dynamic
     * @param nodes
     * @param isSynchronous
     */
    public Network(IDynamic dynamic, INode[] nodes, boolean isSynchronous, ITerminate terminator) {
        this.dynamic = dynamic;
        this.nodes = nodes;
        this.isSynchronous = isSynchronous;
        this.terminate = terminator;
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
                if(nodes[i].ishonest()) {
                    newNodes[i] = dynamic.applyDynamicOn(i, nodes);
                } else {
                    newNodes[i] = nodes[i];
                }
            }
            
            if(LOG_NODEHISTROY) {
                logHistory();
            }

            //update nodes
            nodes = newNodes;
            rounds++;
            log.debug("Round: " + rounds);

            //check if converged
            converged = terminate.shouldTerminate(nodes);
        } while (!converged);

        if(LOG_NODEHISTROY) {
            logHistory();
        }

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
            if(nodes[i].ishonest()) {
                nodes[i] = dynamic.applyDynamicOn(i, nodes);
            }
            
            i = (i + 1) % nodes.length;

            if(LOG_NODEHISTROY && i == 0) {
                logHistory();
            }

            rounds++;
            
            //check if converged
            converged = terminate.shouldTerminate(nodes);
        } while (!converged);

        if(LOG_NODEHISTROY) {
            logHistory();
        }

        return converged;
    }

    private void logHistory() {
        double[][] round = new double[nodes.length][];
        for (int i = 0; i < nodes.length; i++) {
            round[i] = new double[nodes[i].getOpinion().length];
            for (int j = 0; j < round[i].length; j++) {
                round[i][j] = nodes[i].getOpinion()[j];
            }
            
        }
        nodesHistroy.add(round);
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
    public ArrayList<double[][]> getHistory() {
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
