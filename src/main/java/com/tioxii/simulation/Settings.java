package com.tioxii.simulation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Settings {
    
    //Collection of results
    public boolean record_positions;
    public boolean record_results;
    public String preset;
    public String file_name;
    public int max_thread_count;

    public Settings() throws FileNotFoundException, IOException, NumberFormatException {
        String consensusConfigPath = "consensus.properties";

        Properties consensusProps = new Properties();
        File f = new File(consensusConfigPath);

        if(f.exists()) {
            SimulationApp.log.info("Settings File exists");
            consensusProps.load(new FileInputStream(f));

            this.record_positions = Boolean.parseBoolean(((String) consensusProps.getOrDefault("record_positions", "false")).replaceAll(" ", ""));
            this.record_results = Boolean.parseBoolean(((String) consensusProps.getOrDefault("record_results", "false")).replaceAll(" ", ""));
            this.file_name = (String) consensusProps.getOrDefault("filename", "none");
            this.max_thread_count = Integer.parseInt(((String) consensusProps.getOrDefault("max_thread_count", "1")).replaceAll(" ", ""));
        }
    }
}
