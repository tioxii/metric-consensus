package com.tioxii.simulation.consensus.metric;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tioxii.simulation.consensus.metric.exceptions.SimulationInitException;
import com.tioxii.simulation.consensus.metric.init.Parameter;
import com.tioxii.simulation.consensus.metric.exceptions.ConfigurationInitException;
import com.tioxii.simulation.consensus.metric.exceptions.DataCollectionException;
import com.tioxii.simulation.consensus.metric.exceptions.EvaluationException;
import com.tioxii.simulation.consensus.metric.util.datacollection.DataCollectionManager;
import com.tioxii.simulation.consensus.metric.util.threads.ThreadQueue;

public class SimulationManager {
    
    //Default Parameters
    private Parameter PARAMETER = null;
    private String dirName = null;
    
    //Utility
    private static Logger log = LogManager.getLogger("Simulation");
    public int MAX_THREAD_COUNT = 6;
    private ThreadQueue<Simulation> QUEUE = null;
    private DataCollectionManager dataCollectionManager = null;

    private double time = 0.0;

    /**
     * For data collection.
     */
    private class Data {
        public int consensusTime;
        public double[] startMean;
        public double[] endMean;
    }

    public SimulationManager(Parameter parameter) {
        this.PARAMETER = parameter;
    }

    private void checkPoint(int iteration) throws IOException {
        if(this.time < 10.0) {
            return;
        }
        
        File file = new File("iteration.txt");
        FileWriter out = new FileWriter(file);
        for(int i = iteration + 1; i < PARAMETER.numberOfNodes.length; i++) {
            out.write(PARAMETER.numberOfNodes[i] + "\n");
        }
        out.close();
    }

    /**
     * Start the simulation.
     */
    public void startSimulations() {
        QUEUE = new ThreadQueue<>(MAX_THREAD_COUNT);
        log.info("-------Starting Simulation-------");
        log.info("Byzantine-Nodes: " + 0);
        log.info("Configuration: " + PARAMETER.configuration.getClass().getSimpleName());
        log.info("Dynamics: " + PARAMETER.dynamics.getClass().getSimpleName());
        log.info("Simulation-Rounds: " + PARAMETER.maxSimulations);
        log.info("Synchronous: " + PARAMETER.isSynchronous);
        log.info("Termination: " + PARAMETER.termination.getClass().getSimpleName());
        
        try {
            dataCollectionManager = new DataCollectionManager(dirName, PARAMETER);
            for(int i = 0; i < PARAMETER.numberOfNodes.length; i++) {
                simulate(i);
                checkPoint(i);
            }
            dataCollectionManager.close();
        } catch (IOException | IllegalArgumentException | SecurityException e) {
            log.error(e.getMessage());
        } catch (SimulationInitException e) {
            e.printStackTrace();
        } catch (ConfigurationInitException e) {
            e.printStackTrace();
        } catch (DataCollectionException e) {
            e.printStackTrace();
        } catch (EvaluationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create networks and start the metric consensus process.
     * @param iteration
     * @throws ConfigurationInitException
     * @throws SimulationInitException
     */
    private void createSimulations(int iteration) throws ConfigurationInitException, SimulationInitException {
        //Creating Network simulations and add them to the q.
        for(int i = 0; i < PARAMETER.maxSimulations; i++) {
            Simulation net = new Simulation(
                PARAMETER.dynamics, 
                PARAMETER.configuration.generate(PARAMETER.numberOfNodes[iteration]), 
                PARAMETER.isSynchronous, 
                PARAMETER.termination.copyThis(), 
                false
            );
            net.setThread(new Thread(net));
            net.getThread().setName(i + "");

            try {
                QUEUE.add(net);
            } catch (InterruptedException e) {
                throw new SimulationInitException(e);
            }
            log.debug("Round " + i + "started!");
        }
    }

    /**
     * Starts a simulation iteration.
     * @param iteration
     * @throws SimulationInitException
     * @throws ConfigurationInitException
     * @throws EvaluationException
     */
    private void simulate(int iteration) throws SimulationInitException, ConfigurationInitException, EvaluationException {
        ArrayList<Data> data = new ArrayList<>();

        log.info("-------Next Iteration: " + iteration +" -------");
        log.info("Number of Nodes: " + PARAMETER.numberOfNodes[iteration]);

        long startTime = System.nanoTime();

        //Start thread for evaluation.
        Thread evaluation = new Thread(() -> evaluate(data));
        evaluation.setName("Evaluation");
        evaluation.start();

        createSimulations(iteration);

        //Waiting for the evaluation to be done.
        try {
            evaluation.join();
        } catch (InterruptedException e) {
            throw new EvaluationException(e);
        }

        //Print Data to file.
        int[] consensusTimes = data.stream().map(elem -> elem.consensusTime).mapToInt(Integer::intValue).toArray();
        dataCollectionManager.collectConsensusTime(iteration, consensusTimes);

        //Summary of the results.
        double average = Arrays.stream(consensusTimes).average().getAsDouble();
        long endTime = System.nanoTime();
        double totalTime = (double) (endTime - startTime) / 1000000000;
        if(this.time < 10.00) {
            this.time = totalTime;
        }
        log.info("-------------RESULTS-------------");
        log.info("Average number of rounds: " + average);
        log.info("Time: " + totalTime + "s");
    }

    /**
     * Wait for metric-consensus process threads and colltect the data.
     * @param data
     */
    private void evaluate(ArrayList<Data> data) {
        Simulation net = null;
        for (int i = 0; i < PARAMETER.maxSimulations; i++) {
            try {
                net = QUEUE.remove();
                
                //Collecting data
                Data d = new Data();
                d.consensusTime = net.getRounds();
                data.add(d);

                //Logging the node history of that round
                dataCollectionManager.collectOpinionHistory(i, net.getHistory());
                log.info("Round " + net.getThread().getName() + " complete with " + net.getRounds() + " rounds!");
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }       
        }
        log.info("Evaluation done!");
    }
}
