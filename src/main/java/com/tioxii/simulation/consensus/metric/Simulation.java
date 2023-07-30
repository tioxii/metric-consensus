package com.tioxii.simulation.consensus.metric;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tioxii.simulation.consensus.metric.api.IDynamics;
import com.tioxii.simulation.consensus.metric.api.ITermination;
import com.tioxii.simulation.consensus.metric.util.threads.IThreadQueueElement;

public class Simulation implements Runnable, IThreadQueueElement {
    //Environment constraints
    private IDynamics dynamics;
    private ITermination termination = null;
    private Node[] nodes = null;
    private boolean isSynchronous = true;
    
    //Utility
    private static Logger log = LogManager.getLogger(Simulation.class.getName());
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
    public Simulation(
        IDynamics dynamics,
        Node[] nodes, 
        boolean isSynchronous, 
        ITermination termination,
        boolean log_history) 
    {
        this.dynamics = dynamics;
        this.nodes = nodes;
        this.isSynchronous = isSynchronous;
        this.termination = termination;
        this.LOG_NODEHISTROY = log_history;
    }
    
    /**
     * Runs the Simulation
     * @return State of consensus
     */
    public void run() {
        if (isSynchronous) {
            synchronousProcess(); 
        } else {
            asynchronousProcess();
        }
    }

    /**
     * One round of the sync process.
     * @param nodes
     * @return
     */
    private Node[] synchronousRound(Node[] nodes) {
        Node[] newNodes = new Node[nodes.length];
        for(int i = 0; i < nodes.length; i++) {
            if(nodes[i].ishonest()) {
                newNodes[i] = dynamics.applyDynamicOn(i, nodes);
            } else {
                newNodes[i] = nodes[i];
            }
            termination.synchronous(newNodes, i);
        }
        return newNodes;
    }

    /**
     * All nodes are updated at the same time.
     * @return true if the simulation has converged, false otherwise
     */
    private void synchronousProcess() {
        boolean converged = false;
        do {
            Node[] newNodes = synchronousRound(nodes);
            if(LOG_NODEHISTROY) {
                logHistory();
            }
            nodes = newNodes; //update nodes
            rounds++;
            log.debug("Round: " + rounds);
            converged = termination.shouldTerminate(nodes); //check if converged
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
    private Node asynchronousActivation(Node[] nodes, int i) {
        Node newNode;
        if(nodes[i].ishonest()) {
            newNode = dynamics.applyDynamicOn(i, nodes);
            return newNode;
        }
        newNode = nodes[i];
        return newNode;
    }

    /**
     * Nodes are updated one at a time
     * @return
     */
    private void asynchronousProcess() {
        int i = 0;
        boolean converged = false;
        do {
            Node newNode = asynchronousActivation(nodes, i);
            Node oldNode = nodes[i];
            nodes[i] = newNode;
            termination.asynchronous(nodes, i, oldNode); //executes every iteration
            
            i = (i + 1) % nodes.length;
            if(LOG_NODEHISTROY && i == 0) {
                logHistory();
            }

            rounds++;
            converged = termination.shouldTerminate(nodes); //test if consensus is reached
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
