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
    
    /* Parameter object. Should be initilized with the setting from consensus.properties file. */
    /* Contains all the constraints for the simulation. */
    private Parameter simulationParameter = null;

    /* Directory name for the data collection. */
    private String dirName = null;
    
    /* Logger */
    private static Logger log = LogManager.getLogger("Simulation");

    /* Number of Simulation thread that run concurrently */ 
    /* Take care with memory when having large number of nodes. */
    public int maxThreadCount = 6;

    /* Queue for the simulation threads. */
    private ThreadQueue<Simulation> simulationThreadQueue = null;

    /* Data collection manager. Responsible for collection and writing the simulation results. */
    private DataCollectionManager dataCollectionManager = null;

    /* Time of the last simulation. */
    private double time = 0.0;

    private ArrayList<Data> dataList = new ArrayList<>();

    /**
     * For data collection.
     * This is an object, to add more data easily.
     */
    private class Data {
        public int consensusTime;
    }

    /**
     * Constructor.
     * @param parameter Objects that contains all the constraints for the simulation.
     */
    public SimulationManager(Parameter parameter) {
        this.simulationParameter = parameter;
    }

    /**
     * With a larger number of nodes, the simulation takes a long time.
     * In order to be able to continue the simulation the current number of nodes is saved in a file.
     * @param iteration The iteration of the simulation.
     * @throws IOException When the file can not be written.
     */
    private void checkPoint(int iteration) throws IOException {
        
        /* Checks how long the last simulation took. */
        /* If the last simulation took more than 10 seconds a checkpoint is created. */
        if(this.time < 10.0) {
            return;
        }
        
        /* Write a checkpoint to iterations.txt */
        File file = new File("iteration.txt");
        FileWriter out = new FileWriter(file);
        for(int i = iteration + 1; i < simulationParameter.numberOfNodes.length; i++) {
            out.write(simulationParameter.numberOfNodes[i] + "\n");
        }
        out.close();
    }

    /**
     * Start the simulation processes.
     */
    public void startSimulations() {
        
        /* Initialize the the simulation thread queue. */
        simulationThreadQueue = new ThreadQueue<>(maxThreadCount);
        
        /* Inform the user about the simulation settings. */
        log.info("-------Starting Simulation-------");
        log.info("Byzantine-Nodes: " + 0);
        log.info("Configuration: " + simulationParameter.configuration.getClass().getSimpleName());
        log.info("Dynamics: " + simulationParameter.dynamics.getClass().getSimpleName());
        log.info("Simulation-Rounds: " + simulationParameter.maxSimulations);
        log.info("Synchronous: " + simulationParameter.isSynchronous);
        log.info("Termination: " + simulationParameter.termination.getClass().getSimpleName());
        
        try {

            /* Create the directory for the data collection. */
            dataCollectionManager = new DataCollectionManager(dirName, simulationParameter);
            
            /* Simulation loop. Loops over the different number of nodes. */
            for(int i = 0; i < simulationParameter.numberOfNodes.length; i++) {
                
                /* Start the simulation for the current number of nodes. */
                simulate(i); 

                /* Create a checkpoint if the simulation takes too long. */
                checkPoint(i);
            }

            /* Close the data collection manager. */
            dataCollectionManager.close();
        
        /* Catch all exceptions. */
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
        
        /* Creating simulation instances and add them to the thread queue. */
        for(int i = 0; i < simulationParameter.maxSimulations; i++) {
            
            /* Create a simulation. */
            Simulation simulation = new Simulation(
                simulationParameter.dynamics, /* Update rule. */ 
                simulationParameter.configuration.generate(simulationParameter.numberOfNodes[iteration]), /* Start position/configuration of the nodes. */
                simulationParameter.isSynchronous,  /* Synchronous or asynchronous. */
                simulationParameter.termination.copyThis(), /* Termination condition. */
                false /* No Byzantine nodes. */
            );

            /* Create a thread for the simulation. */
            Thread simulationThread = new Thread(simulation);
            
            /* Tell the simulation which thread it belongs to. */
            simulation.setThread(simulationThread);
            
            /* Set the name of the thread to the current iteration. */
            simulation.getThread().setName(i + "");

            /* Add the simulation to the thread queue. */
            /* The thread queue will start the simulation. */
            try {
                simulationThreadQueue.add(simulation);
            } catch (InterruptedException e) {
                throw new SimulationInitException(e);
            }

            /* For debugging purposes. */
            log.debug("Round " + i + "started!");
        }
    }

    /**
     * Starts a simulation iteration.
     * @param iteration The current iteration of the simulation. Value is used to get the number of nodes.
     * @throws SimulationInitException
     * @throws ConfigurationInitException
     * @throws EvaluationException
     */
    private void simulate(int iteration) throws SimulationInitException, ConfigurationInitException, EvaluationException {
        
        /* Inform the user about the current iteration and the number of nodes. */
        log.info("-------Next Iteration: " + iteration +" -------");
        log.info("Number of Nodes: " + simulationParameter.numberOfNodes[iteration]);

        /* Start the timer. */
        long startTime = System.nanoTime();

        /* Start thread to catch finished simulations from the thread queue. */
        Thread evaluation = new Thread(() -> evaluate(dataList));
        evaluation.setName("Evaluation");
        evaluation.start();

        createSimulations(iteration);

        /* Waiting for the evaluation thread to be done. */
        try {
            evaluation.join();
        } catch (InterruptedException e) {
            throw new EvaluationException(e);
        }

        /* Print the results to the file. */
        int[] consensusTimes = dataList.stream().map(data -> data.consensusTime).mapToInt(Integer::intValue).toArray();
        dataCollectionManager.collectConsensusTime(iteration, consensusTimes);

        /* Calculate a small summary to print it to console */
        double average = Arrays.stream(consensusTimes).average().getAsDouble();
        long endTime = System.nanoTime();
        double totalTime = (double) (endTime - startTime) / 1000000000;
        if(this.time < 10.00) {
            this.time = totalTime;
        }

        /* Print the small summary to the console. */
        log.info("-------------RESULTS-------------");
        log.info("Average number of rounds: " + average);
        log.info("Time: " + totalTime + "s");

        /* Clear the data list. */
        dataList.clear();
    }

    /**
     * Responsible for the evaluation of the simulation.
     * The evaluation is done in a separate thread, so new simulations can be created while the evaluation is running.
     * So multiple simulations can be run concurrently.
     * @param data The data object that contains the data of the simulation.
     */
    private void evaluate(ArrayList<Data> data) {
        
        Simulation simulation = null;

        /* Wait for all simulation for this number of nodes to be done. This depens on the number of simulations option. */
        for (int i = 0; i < simulationParameter.maxSimulations; i++) {
            try {
                /* Get the next simulation from the queue. */
                simulation = simulationThreadQueue.remove();
                
                /* Collect the data of the simulation and add it to the data list. */
                Data simulationData = new Data();
                simulationData.consensusTime = simulation.getRounds();
                data.add(simulationData);

                /* Collect the opinion/position history of the simulation. */
                dataCollectionManager.collectOpinionHistory(i, simulation.getHistory());
                log.info("Round " + simulation.getThread().getName() + " complete with " + simulation.getRounds() + " rounds!");
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }       
        }
        log.info("Evaluation done!");
    }
}
