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
    private Thread t = null;

    //Logging
    private static Logger log = LogManager.getLogger(Network.class.getName());

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
    public Network(IDynamic dynamic,
        INode[] nodes, 
        boolean isSynchronous, 
        ITerminate terminator,
        boolean log_history) 
    {
        this.dynamic = dynamic;
        this.nodes = nodes;
        this.isSynchronous = isSynchronous;
        this.terminate = terminator;
        this.LOG_NODEHISTROY = log_history;
    }
    
    /**
     * Runs the Simulation
     * @return State of consensus
     */
    public void run() {
        double[][] opinions = Arrays.stream(nodes).map(node -> node.getOpinion()).toArray(double[][]::new);
        startMean = Distance.calculateMean(opinions);
        if (isSynchronous) {
            //when synchronous, all nodes are updated at the same time
            synchronous(); 
        } else {
            //when asynchronous, nodes are updated one at a time
            asynchronous();
        }
        endMean = Distance.calculateMean(opinions);
    }

    /**
     * One round of the sync process.
     * @param nodes
     * @return
     */
    private INode[] oneRoundSynchronous(INode[] nodes) {
        INode[] newNodes = new INode[nodes.length];
        for(int i = 0; i < nodes.length; i++) {
            if(nodes[i].ishonest()) {
                newNodes[i] = dynamic.applyDynamicOn(i, nodes);
            } else {
                newNodes[i] = nodes[i];
            }
            terminate.synchronous(newNodes, i);
        }
        return newNodes;
    }

    /**
     * All nodes are updated at the same time.
     * @return true if the simulation has converged, false otherwise
     */
    private boolean synchronous() {
        boolean converged = false;
        do {
            INode[] newNodes = oneRoundSynchronous(nodes);
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
     * One round of the asynch process.
     * @param nodes
     * @param i
     * @return
     */
    private INode oneRoundAsynchronous(INode[] nodes, int i) {
        INode newNode;
        if(nodes[i].ishonest()) {
            newNode = dynamic.applyDynamicOn(i, nodes);
            return newNode;
        }
        newNode = nodes[i];
        return newNode;
    }

    /**
     * Nodes are updated one at a time
     * @return
     */
    private boolean asynchronous() {
        //update nodes one at a time
        int i = 0;
        boolean converged = false;
        do {
            INode newNode = oneRoundAsynchronous(nodes, i);
            INode oldNode = nodes[i];
            nodes[i] = newNode;
            terminate.asynchronous(nodes, i, oldNode);
            
            i = (i + 1) % nodes.length;
            if(LOG_NODEHISTROY && i == 0) {
                logHistory();
            }

            rounds++;
            converged = terminate.shouldTerminate(nodes);
        } while (!converged);

        if(LOG_NODEHISTROY) {
            logHistory();
        }
        return converged;
    }

    /**
     * Log node opinions (positions).
     */
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

    public void setThread(Thread thread) {
        this.t = thread;
    }

    @Override
    public Thread getThread() {
        return t;
    }
}
