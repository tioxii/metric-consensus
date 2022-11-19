package com.tioxii;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import com.tioxii.consensus.metric.Simulation;
import com.tioxii.consensus.metric.api.IDynamic;
import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.dynamics.BaseDynamic;
import com.tioxii.consensus.metric.nodes.BaseNode;
import com.tioxii.consensus.metric.util.Preset;




/**
 * Hello world!
 *
 */
public class SimApp {
    
    //Parameters
    public static int DIMENSIONS = 2;
    public static int SIM_ROUNDS = 1;
    public static int[] PARTICIPATING_NODES = {10};
    public static boolean GENERATE_RANDOM = true;
    public static float FRACTION_DISHONEST = 0.0f;
    public static IDynamic DYNAMIC = new BaseDynamic();
    public static Class<? extends INode> NODETYPE = BaseNode.class;
    public static boolean SYNCHRONOUS = true;
    public static Preset PRESET = Preset.RANDOM;
    public static double[][] POSITIONS = null;
    
    //Evaluation
    public static boolean RECORD_RESULTS = false;
    public static boolean RECORD_POSITIONS = true;

    //Utility
    public static int MAX_THREAD_COUNT = 6;

    //Logging
    public static final Level LEVEL = Level.INFO;
    public static final Logger LOGGER = LogManager.getLogger("Simulation");

    public static void main(String[] args) {
        setUpLoggers();
        LOGGER.info("Hello to the metric consensus protocol! :)");
        Simulation sim = new Simulation();
        setUpSimulation(sim);
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

    /**
     * Setup the Simulation
     */
    public static void setUpSimulation(Simulation sim) {
        sim.DIMENSIONS = DIMENSIONS;
        sim.SIM_ROUNDS = SIM_ROUNDS;
        sim.PARTICIPATING_NODES = PARTICIPATING_NODES;
        sim.GENERATE_RANDOM = GENERATE_RANDOM;
        sim.FRACTION_DISHONEST = FRACTION_DISHONEST;
        sim.DYNAMIC = DYNAMIC;
        sim.NODETYPE = NODETYPE;
        sim.SYNCHRONOUS = SYNCHRONOUS;
        sim.PRESET = PRESET;
        sim.POSITIONS = POSITIONS;

        sim.RECORD_POSITIONS = RECORD_POSITIONS;
        sim.RECORD_RESULTS = RECORD_RESULTS;
        
        sim.MAX_THREAD_COUNT = MAX_THREAD_COUNT;
    }
}
