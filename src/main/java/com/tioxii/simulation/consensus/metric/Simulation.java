package com.tioxii.simulation.consensus.metric;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tioxii.simulation.consensus.metric.api.IDynamics;
import com.tioxii.simulation.consensus.metric.api.ITermination;
import com.tioxii.simulation.consensus.metric.util.threads.IThreadQueueElement;

/**
 * The Simulation class is responsible for running the simulation.
 * It can run the simulation synchronously or asynchronously.
 * It also logs the history of the nodes if the isLoggingNodeHistory is set to true.
 */
public class Simulation implements Runnable, IThreadQueueElement {
    
    /**
     * The dynamics to be used in the simulation.
     */
    private IDynamics dynamics;
    
    /**
     * The termination condition.
     */
    private ITermination termination = null;
    
    /**
     * The nodes in the simulation.
     */
    private Node[] nodes = null;

    /**
     * True if the simulation is synchronous, false if it is asynchronous.
     */
    private boolean isSynchronous = true;
    
    /**
     * Logger for the Simulation class.
     */
    private static Logger log = LogManager.getLogger(Simulation.class.getName());

    /**
     * The thread the simulation belongs to.
     */
    private Thread t = null;

    /**
     * Number of rounds it took to reach the termination condition.
     */
    private int rounds = 0;

    /**
     * Log the history of the nodes. Position of every node for each round.
     * Default is false. Set to true if the history of the nodes should be logged.
     */
    public boolean isLoggingNodeHistory = false;

    /**
     * History of the nodes. Position of every node for each round.
     */
    private ArrayList<double[][]> nodesHistroy = new ArrayList<double[][]>();

    /**
     * Constructor.
     * @param dynamic The dynamics to be used in the simulation.
     * @param nodes The start configuration of the nodes.
     * @param isSynchronous True if the simulation is synchronous, false if it is asynchronous.
     * @param termination The termination condition.
     * @param log_history True if the history of the nodes should be logged, false otherwise.
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
        this.isLoggingNodeHistory = log_history;
    }
    
    /**
     * Runs the Simulation.
     * If the simulation is synchronous, it will run the synchronous process.
     * If the simulation is asynchronous, it will run the asynchronous process.
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
            if(isLoggingNodeHistory) {
                logHistory();
            }
            nodes = newNodes; //update nodes
            rounds++;
            log.debug("Round: " + rounds);
            converged = termination.shouldTerminate(nodes); //check if converged
        } while (!converged);

        if(isLoggingNodeHistory) {
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
            if(isLoggingNodeHistory && i == 0) {
                logHistory();
            }

            rounds++;
            converged = termination.shouldTerminate(nodes); //test if consensus is reached
        } while (!converged);

        if(isLoggingNodeHistory) {
            logHistory();
        }
    }

    /**
     * Log the nodes' opinions (positions).
     */
    private void logHistory() {
        double[][] round = Arrays.stream(nodes).map(node -> node.getOpinion()).toArray(double[][]::new);
        nodesHistroy.add(round);
    }

    /**
     * Get the Number of Rounds, that where necessary to reach the termination condition.
     * @return The number of rounds it took to reach the termination condition.
     */
    public int getRounds() {
        return this.rounds;
    }

    /**
     * Get the node history. Position of every node for each round.
     * @return The history of the nodes.
     */
    public ArrayList<double[][]> getHistory() {
        return this.nodesHistroy;
    }

    /**
     * Get the nodes.
     * @return The nodes.
     */
    public Node[] getNodes() {
        return nodes;
    }

    /**
     * Tell the simulation which thread it belongs to.
     * Set during the creation of the thread.
     * @param thread The thread the simulation belongs to.
     */
    public void setThread(Thread thread) {
        this.t = thread;
    }

    /**
     * Get the thread the simulation belongs to.
     * @return The thread the simulation belongs to.
     */
    @Override
    public Thread getThread() {
        return t;
    }
}
