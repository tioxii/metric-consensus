package com.tioxii.consensus.metric.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.tioxii.SimApp;

public class Options {
    //Parameters
    public int sim_rounds;
    public int dimensions;
    public float fraction_dishonest;
    public String dynamic;
    public String nodetype;
    public boolean synchronous;
    public String generator;

    //Participating nodes
    public int start;
    public int end;
    public int step;
    public String increment;

    //Dynamic specific parameters
    public double beta;
    public int h;

    //Collection of results
    public boolean record_positions;
    public boolean record_results;
    public String preset;
    

    public Options() throws FileNotFoundException, IOException, NumberFormatException {
        String consensusConfigPath = "consensus.properties";

        Properties consensusProps = new Properties();
        File f = new File(consensusConfigPath);

        if(f.exists()) {
            SimApp.log.info("File exists");
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
        }
    }
}
