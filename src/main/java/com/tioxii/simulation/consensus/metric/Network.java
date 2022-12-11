package com.tioxii.simulation.consensus.metric;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tioxii.simulation.consensus.metric.api.IDynamic;
import com.tioxii.simulation.consensus.metric.api.ITerminate;
import com.tioxii.util.IThreadQueueElement;

public class Network implements Runnable, IThreadQueueElement {
    //Environment constraints
    private IDynamic dynamic;
    private ITerminate terminate = null;
    private Node[] nodes = null;
    private boolean isSynchronous = true;
    
    //Utility
    private static Logger log = LogManager.getLogger(Network.class.getName());
    private Thread t = null;

    //Data
    private int rounds = 0;
    public boolean LOG_NODEHISTROY = false;
    private ArrayList<double[][]> nodesHistroy = new ArrayList<double[][]>();

    /**
     * Constructor
     * @param dynamic
     * @param nodes
     * @param isSynchronous
     */
    public Network(
        IDynamic dynamic,
        Node[] nodes, 
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
        if (isSynchronous) {
            synchronous(); //when synchronous, all nodes are updated at the same time
        } else {
            asynchronous(); //when asynchronous, nodes are updated one at a time
        }
    }

    /**
     * One round of the sync process.
     * @param nodes
     * @return
     */
    private Node[] oneRoundSynchronous(Node[] nodes) {
        Node[] newNodes = new Node[nodes.length];
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
    private void synchronous() {
        boolean converged = false;
        do {
            Node[] newNodes = oneRoundSynchronous(nodes);
            if(LOG_NODEHISTROY) {
                logHistory();
            }
            nodes = newNodes; //update nodes
            rounds++;
            log.debug("Round: " + rounds);
            converged = terminate.shouldTerminate(nodes); //check if converged
        } while (!converged);

        if(LOG_NODEHISTROY) {
            logHistory();
        }
    }

    /**
     * One round of the asynch process.
     * @param nodes
     * @param i
     * @return
     */
    private Node oneRoundAsynchronous(Node[] nodes, int i) {
        Node newNode;
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
    private void asynchronous() {
        int i = 0;
        boolean converged = false;
        do {
            Node newNode = oneRoundAsynchronous(nodes, i);
            Node oldNode = nodes[i];
            nodes[i] = newNode;
            terminate.asynchronous(nodes, i, oldNode); //executes every iteration
            
            i = (i + 1) % nodes.length;
            if(LOG_NODEHISTROY && i == 0) {
                logHistory();
            }

            rounds++;
            converged = terminate.shouldTerminate(nodes); //test if consensus is reached
        } while (!converged);

        if(LOG_NODEHISTROY) {
            logHistory();
        }
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

    public Node[] getNodes() {
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
