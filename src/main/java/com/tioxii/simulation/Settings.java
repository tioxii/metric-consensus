package com.tioxii.simulation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Settings {
    //Parameters
    public int sim_rounds;
    public int dimensions;
    public String dynamic;
    public String nodetype;
    public boolean synchronous;
    public String generator;
    public String terminator;

    //Participating nodes
    public int start;
    public int end;
    public int step;
    public String increment;

    //Dynamic specific parameters
    public double beta;
    public int h;

    //Generation specific parameters
    public int clusters;
    public double oneLargeClusterSize;
    public float fraction_dishonest;

    //Collection of results
    public boolean record_positions;
    public boolean record_results;
    public String preset;
    public String file_name;
    public int max_thread_count;

    //Termination specific parameters
    public double epsilon;

    public Settings() throws FileNotFoundException, IOException, NumberFormatException {
        String consensusConfigPath = "consensus.properties";

        Properties consensusProps = new Properties();
        File f = new File(consensusConfigPath);

        if(f.exists()) {
            SimulationApp.log.info("File exists");
            consensusProps.load(new FileInputStream(f));

            this.sim_rounds = Integer.parseInt(consensusProps.getProperty("sim_rounds").replaceAll(" ", ""));
            this.dimensions = Integer.parseInt(consensusProps.getProperty("dimensions").replaceAll(" ", ""));
            this.dynamic = consensusProps.getProperty("dynamic");
            this.nodetype = consensusProps.getProperty("nodetype");
            this.synchronous = Boolean.parseBoolean((String) consensusProps.getOrDefault("synchronous", "true"));
            this.generator = consensusProps.getProperty("generator");
            this.start = Integer.parseInt(consensusProps.getProperty("start").replaceAll(" ", ""));
            this.end = Integer.parseInt(consensusProps.getProperty("end").replaceAll(" ", ""));
            this.step = Integer.parseInt(consensusProps.getProperty("step").replaceAll(" ", ""));
            this.increment = consensusProps.getProperty("increment");
            this.beta = Double.parseDouble(consensusProps.getProperty("beta").replaceAll(" ", ""));
            this.h = Integer.parseInt(consensusProps.getProperty("h").replaceAll(" ", ""));
            this.fraction_dishonest = (float) Double.parseDouble(consensusProps.getProperty("fraction_byzantine").replaceAll(" ", ""));
            this.preset = consensusProps.getProperty("preset");
            this.record_positions = Boolean.parseBoolean(((String) consensusProps.getOrDefault("record_positions", "false")).replaceAll(" ", ""));
            this.record_results = Boolean.parseBoolean(((String) consensusProps.getOrDefault("record_results", "false")).replaceAll(" ", ""));
            this.terminator = consensusProps.getProperty("terminator");
            this.epsilon = Double.parseDouble(consensusProps.getProperty("epsilon").replaceAll(" ", ""));
            this.file_name = (String) consensusProps.getOrDefault("filename", "none");
            this.max_thread_count = Integer.parseInt(((String) consensusProps.getOrDefault("max_thread_count", "1")).replaceAll(" ", ""));
            this.clusters = Integer.parseInt(consensusProps.getProperty("clusters"));
            this.oneLargeClusterSize = Double.parseDouble(consensusProps.getProperty("oneLargeClusterSize"));
        }
    }
}
