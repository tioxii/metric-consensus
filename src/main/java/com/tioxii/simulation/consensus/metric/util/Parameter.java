package com.tioxii.simulation.consensus.metric.util;

import com.tioxii.simulation.consensus.metric.api.IConfiguration;
import com.tioxii.simulation.consensus.metric.api.IDynamics;
import com.tioxii.simulation.consensus.metric.api.ITermination;

public class Parameter {
    public IDynamics dynamics;
    public IConfiguration configuration;
    public ITermination termination;
    public boolean isSynchroneous = true;
    
    public int maxSimulations = 100;
    public int dimension = 2;
    public int[] numberOfNodes = null;
}
