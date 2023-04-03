package com.tioxii.simulation;

import java.io.IOException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import com.tioxii.simulation.consensus.metric.SimulationManager;
import com.tioxii.simulation.consensus.metric.util.init.ParameterManager;

/**
 * Hello world!
 *
 */
public class SimulationApp {
    //Logging
    public static final Level LEVEL = Level.INFO;
    public static final Logger log = LogManager.getLogger("Simulation");

    public static void main(String[] args) {
        setUpLoggers();
        log.info("Hello from the metric consensus protocol! :)"); 
        try {
            Settings options = new Settings();
            log.debug(options.start + " "+ options.end + " " + options.step + " " + options.increment);
            SimulationManager sim = setUpSimulation(options);
            sim.startSimulations();

            Thread.sleep(1000);
        } catch (NumberFormatException | IOException | InterruptedException e) {
            e.printStackTrace();
            log.error("Failed to read options");
        }
        notifiySound(3);
    }

    public static void notifiySound(int beeps) {
        try{
            for(int i = 0; i < beeps; i++) {
                java.awt.Toolkit.getDefaultToolkit().beep();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setting up the Logger.
     */
    public static void setUpLoggers() {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration config = context.getConfiguration();
        LoggerConfig rootConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        rootConfig.setLevel(LEVEL);
        
        context.updateLoggers();
    }

    /**
     * Setup the Simulation
     * @param options
     */
    public static SimulationManager setUpSimulation(Settings options) {
        SimulationManager sim = new SimulationManager(ParameterManager.createParameter());

        /*sim.RECORD_POSITIONS = options.record_positions;
        sim.RECORD_RESULTS = options.record_results;
        
        if(!options.file_name.equals("none"))
            sim.FILE_NAME = options.file_name;
        */
        sim.MAX_THREAD_COUNT = options.max_thread_count;
        return sim;
    }
}
