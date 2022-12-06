package com.tioxii.consensus.metric;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tioxii.consensus.metric.api.IDynamic;
import com.tioxii.consensus.metric.api.INodeGenerator;
import com.tioxii.consensus.metric.api.ITerminate;
import com.tioxii.consensus.metric.exceptions.NetworkGenerationException;
import com.tioxii.consensus.metric.exceptions.NodeGenerationException;
import com.tioxii.consensus.metric.util.SampleData;
import com.tioxii.util.ReflectionMethods;
import com.tioxii.util.ThreadQueue;

public class Simulation {
    
    //Default Parameters
    private int SIM_ROUNDS = 1000;
    private int[] PARTICIPATING_NODES = null;
    private boolean SYNCHRONOUS = true;

    private INodeGenerator GENERATOR = null;
    private ITerminate TERMINATOR = null;
    private IDynamic DYNAMIC = null;

    private String[] PARAMETER = null;
    private String[] PARAMETER_NAMES = null;

    //Evaluation-Settings
    public String FILE_NAME = null;
    private String FILE_NAME_POSITIONS = null;
    public String DIR = "results/";
    public boolean RECORD_RESULTS = false;
    public boolean RECORD_POSITIONS = false;
    
    //Utility
    private static Logger log = LogManager.getLogger("Simulation");
    public int MAX_THREAD_COUNT = 6;
    private ThreadQueue<Network> QUEUE = null;
    private SampleData dataCollection = null;
    private SampleData positionCollection = null;

    public Simulation(int dimensions, 
                      int sim_rounds, 
                      int[] participating_nodes,
                      IDynamic dynamic,
                      boolean synchronous,
                      INodeGenerator generator,
                      ITerminate terminator) 
    {
        this.SIM_ROUNDS = sim_rounds;
        this.PARTICIPATING_NODES = participating_nodes;
        this.DYNAMIC = dynamic;
        this.SYNCHRONOUS = synchronous;
        this.GENERATOR = generator;
        this.TERMINATOR = terminator;

        //Parameters to be collected
        Object[] objects = new Object[3];

        objects[0] = GENERATOR;
        objects[1] = DYNAMIC;
        objects[2] = TERMINATOR;

        try {
            PARAMETER = ReflectionMethods.extractParametersFromFields(objects);
            String[] names = ReflectionMethods.getParameterNames(objects);
            PARAMETER_NAMES = new String[names.length + 2];
            PARAMETER_NAMES[0] = "Participants";
            PARAMETER_NAMES[1] = "Rounds";
            for(int i = 2; i < PARAMETER_NAMES.length; i++) {
                PARAMETER_NAMES[i] = names[i - 2];
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public class Data {
        public int consensusTime;
        public double[] startMean;
        public double[] endMean;
    }

    /**
     * Start the simulation.
     */
    public void startSimulate() {
        QUEUE = new ThreadQueue<>(MAX_THREAD_COUNT);
        
        log.info("Setting up file for collecting results.");
        log.info("Log positions: " + RECORD_POSITIONS);
        log.info("Log round results: " + RECORD_RESULTS);

        //Generate unique filenames
        if(FILE_NAME == null) {
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
            LocalDateTime dateTime = LocalDateTime.now();
            String formattedDate = dateTime.format(timeFormat);
            FILE_NAME = formattedDate;
        } 
        FILE_NAME += "_R-" + SIM_ROUNDS + "_SYNC-" + SYNCHRONOUS;
        FILE_NAME_POSITIONS = FILE_NAME + "_POSITIONS" + ".csv";
        FILE_NAME += ".csv";

        try {
            //Create directory if it doesn't exist.
            File dir = new File(DIR);
            if(!dir.exists()) {
                dir.mkdir();
            }
            
            if(RECORD_RESULTS)
                dataCollection = new SampleData(new File(DIR + FILE_NAME), PARAMETER_NAMES);

            if(RECORD_POSITIONS)
                positionCollection = new SampleData(new File(DIR + FILE_NAME_POSITIONS), null);
            
            for(int i = 0; i < PARTICIPATING_NODES.length; i ++) {
                try {
                    simulate(i);
                } catch (NetworkGenerationException | NodeGenerationException e) {
                    log.error("Failed to simulate round: " + i);
                }
            }
                
            if(dataCollection != null) 
                this.dataCollection.close();
            if(positionCollection != null) 
                this.positionCollection.close();
        } catch (IOException | IllegalArgumentException | SecurityException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Starts a simulation iteration.
     * @param iteration
     * @throws NetworkGenerationException
     * @throws NodeGenerationException
     */
    public void simulate(int iteration) throws NetworkGenerationException, NodeGenerationException {
        ArrayList<Data> data = new ArrayList<>();

        log.info("-------Starting Simulation-------");
        log.info("Participants: " + PARTICIPATING_NODES[iteration]);
        log.info("Byzantine-Nodes: " + 0);
        log.info("Generator: " + GENERATOR.getClass().getSimpleName());
        log.info("Dynamic: " + DYNAMIC.getClass().getSimpleName());
        log.info("Simulation-Rounds: " + SIM_ROUNDS);
        log.info("Synchronous: " + SYNCHRONOUS);
        log.info("Termination: " + TERMINATOR.getClass().getSimpleName());
        
        long startTime = System.nanoTime();

        //Start thread for evaluation.
        Thread evaluation = new Thread(() -> evaluate(data));
        evaluation.setName("Evaluation");
        evaluation.start();

        //Creating Network simulations and add them to the q.
        for(int i = 0; i < SIM_ROUNDS; i++) {
            Network net = new Network(DYNAMIC, GENERATOR.generate(PARTICIPATING_NODES[iteration]), SYNCHRONOUS, TERMINATOR.copyThis());
            net.t = new Thread(net);
            net.t.setName(i + "");

            try {
                QUEUE.add(net);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("Round " + i + "started!");
        }

        //Waiting for the evaluation to be done.
        try {
            evaluation.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        //Printing Results to CSV file.
        if(dataCollection != null) {
            try {
                dataCollection.print(PARTICIPATING_NODES[iteration], data, PARAMETER);
            } catch (IOException | IllegalArgumentException | IllegalAccessException e) {
                log.error(e.getMessage());
            }
        }

        //Summary of the results.
        int[] rounds = data.stream().map(elem -> elem.consensusTime).mapToInt(Integer::intValue).toArray();
        double average = Arrays.stream(rounds).average().getAsDouble();
        
        long endTime = System.nanoTime();
        double totalTime = (double) (endTime - startTime) / 1000000000;

        log.info("-------------RESULTS-------------");
        log.info("Average number of rounds: " + average);
        log.info("Time: " + totalTime + "s");
    }

    public void evaluate(ArrayList<Data> data) {
        Network net = null;

        for (int i = 0; i < SIM_ROUNDS; i++) {
            try {
                net = QUEUE.remove();

                //Collecting data
                Data d = new Data();
                d.consensusTime = net.getRounds();
                d.startMean = net.startMean;
                d.endMean = net.endMean;
                data.add(d);

                //Logging the node history of that round
                if(positionCollection != null) {
                    ArrayList<double[][]> hist = net.getHistory();
                    try {
                        positionCollection.printPositions(hist);
                    } catch (Exception e) {
                        log.error("Failed to log the node position history: " + e.getMessage());
                    }
                }

                log.info("Round " + net.t.getName() + " complete with " + net.getRounds() + " rounds!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }       
        }
        log.info("Evaluation done!");
    }
}
