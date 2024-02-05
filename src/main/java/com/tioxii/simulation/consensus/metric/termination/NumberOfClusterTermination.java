package com.tioxii.simulation.consensus.metric.termination;

import java.util.ArrayList;
import java.util.Arrays;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.ITermination;
import com.tioxii.simulation.consensus.metric.util.Parameter;

/**
 * NumberOfClusterTermination condition.
 * Terminates the process, when the specified number of clusters remains.
 */
public class NumberOfClusterTermination implements ITermination {
    
    /**
     * Number of clusters, at which the process should terminate.
     */
    @Parameter(name = "clusters", isParameter = true)
    public int clusters = 2;

    /**
     * Terminates when the specified number of clusters remains. Default value is 2.
     * @param nodes The nodes of the consensus process.
     * @return True if process should end, false if not.
     */
    @Override
    public boolean shouldTerminate(Node[] nodes) {
        ArrayList<double[]> set = new ArrayList<double[]>();
        boolean hasElement = true;

        /* Collects all opinions in a (unique) set. If the set size is bigger than clusters it returns false, otherwise true. */
        for(int i = 0; i < nodes.length; i++) {
            for(int j = 0; j < set.size(); j++) {
                if(Arrays.equals(set.get(j), nodes[i].getOpinion())) {
                    hasElement = true;
                    break;
                }
            }
            if(!hasElement) {
                set.add(nodes[i].getOpinion());
            }
            if(set.size() > clusters)
                return false;

            hasElement = false;
        }
        return true;
    }

    /**
     * Does nothing in this termination condition.
     */
    @Override
    public void synchronous(Node[] nodes, int index) {
        return;
    }

    /**
     * Does nothing in this termination condition.
     */
    @Override
    public void asynchronous(Node[] nodes, int index, Node oldNode) {
        return;
    }

    /**
     * Creates a copy of this object.
     * In this case its just a new instance, since no parameters need to be copied.
     * @return Copy of this object.
     */
    @Override
    public ITermination copyThis() {
        return new NumberOfClusterTermination();
    }
    
}
