package com.tioxii.simulation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * This class is still an artifact of the original project.
 * Some methods are still used in the current project.
 * I want to remove this class in the future, and move the contents to the ParameterManager class.
 * The class is used to read the settings from the consensus.properties file.
 */
public class Settings {
    
    //Collection of results
    public boolean record_positions;
    public boolean record_results;
    public String preset;
    public String file_name;

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
        }
    }
}
