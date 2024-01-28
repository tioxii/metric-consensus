package com.tioxii.simulation;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import com.tioxii.simulation.consensus.metric.SimulationManager;
import com.tioxii.simulation.consensus.metric.init.Parameter;
import com.tioxii.simulation.consensus.metric.init.ParameterManager;


public class SimulationApp {
    
    /* Log Level. TODO: Implement with config file. */
    public static final Level LEVEL = Level.INFO;
    public static final Logger log = LogManager.getLogger("Simulation");
    
    /* Main function */
    public static void main(String[] args) {
        
        /* Setup loggers without config file. */
        setUpLoggers();

        log.info("Hello from the metric consensus protocol! :)"); 

        /* Create an object that consist of all parameter specified in the consensus.properties file. */
        Parameter parameter = ParameterManager.createParameter();

        /* Create a simulation manager */
        SimulationManager simulationManager = new SimulationManager(parameter);

        /* Set the number of threads to 1. TODO: Implement with the config file. */
        simulationManager.maxThreadCount = 1;
        
        /* Start the simulation. */
        simulationManager.startSimulations();

        /* Notifiy the user that the simulations are done. */
        playNotificationSound(3);
    }

    /**
     * Plays a notification sound.
     * @param beeps The number of beeps.
     */
    public static void playNotificationSound(int beeps) {
        
        /* Plays a notification every 1000 ms. */
        try{
            for(int i = 0; i < beeps; i++) {
                java.awt.Toolkit.getDefaultToolkit().beep();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            log.error("Error while playing notification sound.");
        }
    }

    /**
     * Sets up the loggers.
     */
    public static void setUpLoggers() {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration config = context.getConfiguration();
        LoggerConfig rootConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        rootConfig.setLevel(LEVEL);
        
        context.updateLoggers();
    }
}
