package com.tioxii.simulation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.SimulationManager;
import com.tioxii.simulation.consensus.metric.api.IDynamic;
import com.tioxii.simulation.consensus.metric.api.INodeGenerator;
import com.tioxii.simulation.consensus.metric.api.ITerminate;
import com.tioxii.simulation.consensus.metric.dynamics.BaseDynamic;
import com.tioxii.simulation.consensus.metric.dynamics.BaseDynamicRandom;
import com.tioxii.simulation.consensus.metric.dynamics.ClosestToMeanDynamic;
import com.tioxii.simulation.consensus.metric.dynamics.MeanValueDynamic;
import com.tioxii.simulation.consensus.metric.dynamics.OneMajorityDynamic;
import com.tioxii.simulation.consensus.metric.generators.Circle;
import com.tioxii.simulation.consensus.metric.generators.ClustersAtPositions;
import com.tioxii.simulation.consensus.metric.generators.FullCircle;
import com.tioxii.simulation.consensus.metric.generators.OneByzantineCluster;
import com.tioxii.simulation.consensus.metric.generators.OneLargeCluster;
import com.tioxii.simulation.consensus.metric.generators.RandomNodes;
import com.tioxii.simulation.consensus.metric.generators.RandomNodesPreset100;
import com.tioxii.simulation.consensus.metric.generators.RandomSizedClustersAtRandomPositions;
import com.tioxii.simulation.consensus.metric.generators.RandomSizedClustersPreset100;
import com.tioxii.simulation.consensus.metric.generators.TwoRandomFarAway;
import com.tioxii.simulation.consensus.metric.terminators.BaseTermination;
import com.tioxii.simulation.consensus.metric.terminators.BaseTerminationDishonest;
import com.tioxii.simulation.consensus.metric.terminators.EpsilonTermination;
import com.tioxii.simulation.consensus.metric.terminators.FiftyPercentTermination;
import com.tioxii.simulation.consensus.metric.terminators.NumberOfClusterTermination;
import com.tioxii.simulation.consensus.metric.util.Iterations;

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
            sim.startSimulate();

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
     * Setting up dynamic.
     * @param dynamic
     * @return
     */
    public static IDynamic setUpDynamic(Settings option) {
        switch(option.dynamic) {
            case "base": return new BaseDynamic();
            case "base-random": return new BaseDynamicRandom(option.beta);
            case "one-majority": return new OneMajorityDynamic();
            case "mean-value": return new MeanValueDynamic(option.h);
            case "closest-to-mean": return new ClosestToMeanDynamic();
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
        ArrayList<Integer> iterationList = new ArrayList<Integer>(0);
        
        File file = new File("iteration.txt");
        if(file.exists()) {
            try {
                BufferedReader in = new BufferedReader(new FileReader(file));
                while(in.ready()) {
                    int number = Integer.parseInt(in.readLine());
                    iterationList.add(number);
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(iterationList.size() > 0) {
            int[] iterations = new int[iterationList.size()];
            for(int i = 0; i < iterations.length; i++) {
                iterations[i] = iterationList.get(i);
            }
            return iterations;
        }

        switch(incrementType) {
            case "exponential": return Iterations.iterationsExponential(start, end, steps);
            case "linear": return Iterations.iterationsLinear(start, end, steps);
            case "explin": return Iterations.iterationsExpLin(start, end, steps);
            default: return new int[0];
        }
    }

    /**
     * Setting up node type.
     * @param type
     * @return
     */
    public static Class<? extends Node> setUpNodeType(String type) {
        switch(type) {
            default: return Node.class;
        }
    }

    /**
     * Set-up node generator.
     * @param options
     * @return
     */
    public static INodeGenerator setUpNodeGenerator(Settings options) {
        Class<? extends Node> clazz = setUpNodeType(options.nodetype);
        double[][] opposing = {{0.25, 0.5}, {0.75, 0.5}};

        switch(options.generator) {
            case "random": return new RandomNodes(options.dimensions, clazz);
            case "opposing": return new ClustersAtPositions(opposing, clazz);
            case "circle": return new Circle(options.clusters, clazz);
            case "fullcircle": return new FullCircle();
            case "byzantine": return new OneByzantineCluster(options.fraction_dishonest, opposing[0], opposing[1], clazz);
            case "onelarge": return new OneLargeCluster(options.oneLargeClusterSize);
            case "faraway": return new TwoRandomFarAway();
            case "randomSizedClusters": return new RandomSizedClustersAtRandomPositions();
            case "randomNodesPreset100": return new RandomNodesPreset100();
            case "randomSizedClusterPreset100": return new RandomSizedClustersPreset100();
            default: return new RandomNodes(options.dimensions, setUpNodeType(options.nodetype));
        }
    }

    public static ITerminate setUpTerminator(Settings options) {
        double[] byzantine_position = {0.75, 0.5};
        switch(options.terminator) {
            case "two-clusters": return new NumberOfClusterTermination();
            case "base": return new BaseTermination();
            case "fifty": return new FiftyPercentTermination(byzantine_position);
            case "epsilon": return new EpsilonTermination(options.epsilon);
            case "basedishonest": return new BaseTerminationDishonest();
            default: return new BaseTermination();
        }
    }

    /**
     * Setup the Simulation
     * @param options
     */
    public static SimulationManager setUpSimulation(Settings options) {
        SimulationManager sim = new SimulationManager(
            options.dimensions,
            options.sim_rounds,
            setUpIterations(options.increment, options.start, options.end, options.step),
            setUpDynamic(options),
            options.synchronous,
            setUpNodeGenerator(options),
            setUpTerminator(options)
        );

        sim.RECORD_POSITIONS = options.record_positions;
        sim.RECORD_RESULTS = options.record_results;
        
        if(!options.file_name.equals("none"))
            sim.FILE_NAME = options.file_name;

        sim.MAX_THREAD_COUNT = options.max_thread_count;
        return sim;
    }
}
