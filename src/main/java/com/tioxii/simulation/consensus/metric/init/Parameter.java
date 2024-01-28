package com.tioxii.simulation.consensus.metric.init;

import com.tioxii.simulation.consensus.metric.api.IConfiguration;
import com.tioxii.simulation.consensus.metric.api.IDynamics;
import com.tioxii.simulation.consensus.metric.api.ITermination;

public class Parameter {

    /* The update rule. */
    public IDynamics dynamics;

    /* The start configuration/position. */
    public IConfiguration configuration;

    /* The termination condition. */
    public ITermination termination;

    /* If the update should happen synchronous or asynchronous. */
    public boolean isSynchronous = true;
    
    public int maxSimulations = 100;

    /* The dimension the opinion/position of each node has. */
    public int dimension = 2;

    /* Array consisting indication, how many nodes should be used for the simulation */
    /* The simulation manager iterates over this array. */
    public int[] numberOfNodes = null;
}
