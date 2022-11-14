package com.tioxii;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import com.tioxii.consensus.metric.Simulation;




/**
 * Hello world!
 *
 */
public class SimApp {
    
    public static final Level LEVEL = Level.INFO;

    public static final Logger LOGGER = LogManager.getLogger("Simulation");

    public static void main(String[] args) {
        setUpLoggers();
        LOGGER.info("Hello World");
        Simulation sim = new Simulation();
        sim.startSimulate();
    }

    /**
     * Setting up the Logger
     */
    public static void setUpLoggers() {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration config = context.getConfiguration();
        LoggerConfig rootConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        rootConfig.setLevel(LEVEL);
        
        context.updateLoggers();
    }
}
