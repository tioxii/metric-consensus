package com.tioxii.simulation;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import com.tioxii.simulation.consensus.metric.SimulationManager;
import com.tioxii.simulation.consensus.metric.init.ParameterManager;


public class SimulationApp {
    //Logging
    public static final Level LEVEL = Level.INFO;
    public static final Logger log = LogManager.getLogger("Simulation");

    public static void main(String[] args) {
        setUpLoggers();

        log.info("Hello from the metric consensus protocol! :)"); 
        SimulationManager simulationManager = new SimulationManager(ParameterManager.createParameter());
        simulationManager.MAX_THREAD_COUNT = 1;
        simulationManager.startSimulations();

        // Notifiy the user that the simulations are done.
        playNotificationSound(3);
    }

    public static void playNotificationSound(int beeps) {
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
     * Setting up the Logger.
     */
    public static void setUpLoggers() {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration config = context.getConfiguration();
        LoggerConfig rootConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        rootConfig.setLevel(LEVEL);
        
        context.updateLoggers();
    }
}
