package com.tioxii.simulation.consensus.metric.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.tioxii.simulation.consensus.metric.api.IConfiguration;
import com.tioxii.simulation.consensus.metric.api.IDynamics;
import com.tioxii.simulation.consensus.metric.api.ITermination;

public class ParameterManager {
    
    private static String propertyFile = "consensus.properties";
    
    private static String defaultDynamics = "";
    private static String defaultConfigurations = "";
    private static String defaultTerminations = "";

    public static Parameter createParameter() throws FileNotFoundException, IOException {
        Properties props = new Properties();
        File file = new File(propertyFile);
        props.load(new FileInputStream(file));

        String dynamics = props.getProperty("dynamics");
        String configuration = props.getProperty("configuration");
        String termination = props.getProperty("termination");
        String isSynchronous = props.getProperty("synchronous");
        String maxSimulations = props.getProperty("maxSimulations");
        String dimension = props.getProperty("dimension");
        String increment = props.getProperty("increment");
        
        return null;
    }

    public static ITermination setUpTermination(String termination) { return null; }

    public static IDynamics setUpDynamics(String dynamics) { return null; }

    public static IConfiguration setUpConfiguration(String configuration) { return null; }

    public static int[] setUpNumberOfNodes(String increment) { return null; }
}
