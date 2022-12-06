package com.tioxii.consensus.metric;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tioxii.consensus.metric.api.IDynamic;
import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.api.ITerminate;
import com.tioxii.math.Distance;
import com.tioxii.util.IThreadQueueElement;

public class Network implements Runnable, IThreadQueueElement {
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
    public boolean LOG_NODEHISTROY = false;
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
        double[][] opinions = Arrays.stream(nodes).map(node -> node.getOpinion()).toArray(double[][]::new);
        
        startMean = Distance.calculateMean(opinions);
        
        //when synchronous, all nodes are updated at the same time
        //when asynchronous, nodes are updated one at a time
        if (isSynchronous) {
            synchronous(); 
        } else {
            asynchronous();
        }

        endMean = Distance.calculateMean(opinions);
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
                terminate.synchronous(newNodes, i);
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
            INode newNode;
            if(nodes[i].ishonest()) {
                newNode = dynamic.applyDynamicOn(i, nodes);
            } else {
                newNode = nodes[i];
            }

            nodes[i] = newNode;
            i = (i + 1) % nodes.length;

            if(LOG_NODEHISTROY && i == 0) {
                logHistory();
            }

            rounds++;
            
            terminate.asynchronous(nodes, i);
            converged = terminate.shouldTerminate(nodes);
        } while (!converged);

        if(LOG_NODEHISTROY) {
            logHistory();
        }

        return converged;
    }

    private void logHistory() {
        double[][] round = Arrays.stream(nodes).map(node -> node.getOpinion()).toArray(double[][]::new);
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

    public INode[] getNodes() {
        return nodes;
    }

    @Override
    public Thread getThread() {
        return t;
    }
}
