package com.tioxii;

import java.io.IOException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import com.tioxii.consensus.metric.Simulation;
import com.tioxii.consensus.metric.api.IDynamic;
import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.api.INodeGenerator;
import com.tioxii.consensus.metric.dynamics.BaseDynamic;
import com.tioxii.consensus.metric.dynamics.BaseDynamicRandom;
import com.tioxii.consensus.metric.dynamics.MeanValueDynamic;
import com.tioxii.consensus.metric.dynamics.OneMajorityDynamic;
import com.tioxii.consensus.metric.generation.RandomNodes;
import com.tioxii.consensus.metric.nodes.BaseNode;
import com.tioxii.consensus.metric.util.Iterations;
import com.tioxii.consensus.metric.util.Options;

/**
 * Hello world!
 *
 */
public class SimApp {
    //Utility
    public static int MAX_THREAD_COUNT = 6;

    //Logging
    public static final Level LEVEL = Level.INFO;
    public static final Logger log = LogManager.getLogger("Simulation");

    public static void main(String[] args) {
        setUpLoggers();
        log.info("Hello from the metric consensus protocol! :)");
        
        try {
            Options options = new Options();
            log.debug(options.start + " "+ options.end + " " + options.step + " " + options.increment);
            Simulation sim = new Simulation();
            setUpSimulation(sim, options);
            sim.startSimulate();
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
            log.error("Failed to read options");
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
     * Setting up dynamic.
     * @param dynamic
     * @return
     */
    public static IDynamic setUpDynamic(Options option) {
        switch(option.dynamic) {
            case "base": return new BaseDynamic();
            case "base-random": return new BaseDynamicRandom(option.beta);
            case "one-majority": return new OneMajorityDynamic();
            case "mean-value": return new MeanValueDynamic(option.h, 5);
            default: return new BaseDynamic();
        }
    }

    /**
     * Setting up participating nodes.
     * @param incrementType
     * @param start
     * @param end
     * @param steps
     * @return
     */
    public static int[] setUpIterations(String incrementType, int start, int end, int steps) {
        switch(incrementType) {
            case "exponential": return Iterations.iterationsExponential(start, end, steps);
            case "linear": return Iterations.iterationsLinear(start, end, steps);
            default: return new int[0];
        }
    }

    /**
     * Setting up node type.
     * @param type
     * @return
     */
    public static Class<? extends INode> setUpNodeType(String type) {
        switch(type) {
            default: return BaseNode.class;
        }
    }

    public static INodeGenerator setUpNodeGenerator(Options options) {
        switch(options.generator) {
            case "random": return new RandomNodes(options.dimensions, setUpNodeType(options.nodetype));
            default: return new RandomNodes(options.dimensions, setUpNodeType(options.nodetype));
        }
    }

    /**
     * Setup the Simulation
     */
    public static void setUpSimulation(Simulation sim, Options options) {
        sim.DIMENSIONS = options.dimensions;
        sim.SIM_ROUNDS = options.sim_rounds;
        sim.PARTICIPATING_NODES = setUpIterations(options.increment, options.start, options.end, options.step);
        sim.DYNAMIC = setUpDynamic(options);
        sim.SYNCHRONOUS = options.synchronous;
        sim.GENERATOR = setUpNodeGenerator(options);

        sim.RECORD_POSITIONS = options.record_positions;
        sim.RECORD_RESULTS = options.record_results;
        
        sim.MAX_THREAD_COUNT = MAX_THREAD_COUNT;
    }
}
