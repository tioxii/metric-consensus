package com.tioxii.simulation.consensus.metric;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tioxii.simulation.consensus.metric.api.IDynamics;
import com.tioxii.simulation.consensus.metric.api.IConfiguration;
import com.tioxii.simulation.consensus.metric.api.ITermination;
import com.tioxii.simulation.consensus.metric.exceptions.NetworkGenerationException;
import com.tioxii.simulation.consensus.metric.exceptions.NodeGenerationException;
import com.tioxii.simulation.consensus.metric.util.SampleData;
import com.tioxii.util.ReflectionMethods;
import com.tioxii.util.ThreadQueue;

public class SimulationManager {
    
    //Default Parameters
    private int SIM_ROUNDS = 1000;
    private int[] NUMBER_OF_NODES = null;
    private boolean SYNCHRONOUS = true;

    private IConfiguration GENERATOR = null;
    private ITermination TERMINATOR = null;
    private IDynamics DYNAMIC = null;

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
    private ThreadQueue<Simulation> QUEUE = null;
    private SampleData dataCollection = null;
    private SampleData positionCollection = null;

    private double time = 0.0;

    /**
     * For data collection.
     */
    public class Data {
        public int consensusTime;
        public double[] startMean;
        public double[] endMean;
    }

    public SimulationManager(int dimensions, 
                      int sim_rounds, 
                      int[] participating_nodes,
                      IDynamics dynamic,
                      boolean synchronous,
                      IConfiguration generator,
                      ITermination terminator) 
    {
        this.SIM_ROUNDS = sim_rounds;
        this.NUMBER_OF_NODES = participating_nodes;
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

    /**
     * Create unique filenames for CSV files.
     */
    private void createFileName() {
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
    }

    /**
     * Create CSV files and corresponding directory.
     */
    private void createFiles() {
        //Create directory if it doesn't exist.
        File dir = new File(DIR);
        if(!dir.exists()) {
            dir.mkdir();
        }
        File dirPos = new File(DIR);
        if(!dirPos.exists()) {
            dirPos.mkdirs();
        }
        if(RECORD_RESULTS)
            dataCollection = new SampleData(new File(DIR + FILE_NAME), PARAMETER_NAMES);
        if(RECORD_POSITIONS)
            positionCollection = new SampleData(new File(DIR + FILE_NAME_POSITIONS), null);
    }

    /**
     * Close all CSV files.
     * @throws IOException
     */
    private void closeFiles() throws IOException {
        if(dataCollection != null) 
            this.dataCollection.close();
        if(positionCollection != null) 
            this.positionCollection.close();
    }

    private void checkPoint(int iteration) throws IOException {
        if(this.time < 10.0) {
            return;
        }
        
        File file = new File("iteration.txt");
        FileWriter out = new FileWriter(file);
        for(int i = iteration + 1; i < NUMBER_OF_NODES.length; i++) {
            out.write(NUMBER_OF_NODES[i] + "\n");
        }
        out.close();
    }

    /**
     * Iterate over the simulation.
     */
    private void iterateOverSimulations() {
        for(int i = 0; i < NUMBER_OF_NODES.length; i ++) {
            try {
                simulate(i);
                checkPoint(i);
            } catch (NetworkGenerationException | NodeGenerationException | IOException e) {
                e.printStackTrace();
                log.error("Failed to simulate round: " + i);
            }
        }
    }

    /**
     * Start the simulation.
     */
    public void startSimulate() {
        QUEUE = new ThreadQueue<>(MAX_THREAD_COUNT);
        createFileName();
        try {
            createFiles();
            iterateOverSimulations();
            closeFiles();
        } catch (IOException | IllegalArgumentException | SecurityException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Create networks and start the metric consensus process.
     * @param iteration
     * @throws NodeGenerationException
     */
    private void createNetworks(int iteration) throws NodeGenerationException {
        //Creating Network simulations and add them to the q.
        for(int i = 0; i < SIM_ROUNDS; i++) {
            Simulation net = new Simulation(
                DYNAMIC, 
                GENERATOR.generate(NUMBER_OF_NODES[iteration]), 
                SYNCHRONOUS, 
                TERMINATOR.copyThis(), 
                RECORD_POSITIONS
            );
            net.setThread(new Thread(net));
            net.getThread().setName(i + "");

            try {
                QUEUE.add(net);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("Round " + i + "started!");
        }
    }

    /**
     * Collect data from the metric consensus process.
     * @param data
     * @param iteration
     */
    private void collectData(ArrayList<Data> data, int iteration) {
        //Printing Results to CSV file.
        if(dataCollection != null) {
            try {
                dataCollection.print(NUMBER_OF_NODES[iteration], data, PARAMETER);
            } catch (IOException | IllegalArgumentException | IllegalAccessException e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * Starts a simulation iteration.
     * @param iteration
     * @throws NetworkGenerationException
     * @throws NodeGenerationException
     */
    private void simulate(int iteration) throws NetworkGenerationException, NodeGenerationException {
        ArrayList<Data> data = new ArrayList<>();

        log.info("-------Starting Simulation-------");
        log.info("Participants: " + NUMBER_OF_NODES[iteration]);
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

        createNetworks(iteration);

        //Waiting for the evaluation to be done.
        try {
            evaluation.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        collectData(data, iteration);

        //Summary of the results.
        int[] rounds = data.stream().map(elem -> elem.consensusTime).mapToInt(Integer::intValue).toArray();
        double average = Arrays.stream(rounds).average().getAsDouble();
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
     * Collect positions from the metric consensus process.
     * @param net
     */
    private void handlePositions(Simulation net) {
        if(positionCollection != null) {
            ArrayList<double[][]> hist = net.getHistory();
            try {
                positionCollection.printPositions(hist);
            } catch (Exception e) {
                log.error("Failed to log the node position history: " + e.getMessage());
            }
        }
    }

    /**
     * Wait for metric-consensus process threads and colltect the data.
     * @param data
     */
    private void evaluate(ArrayList<Data> data) {
        Simulation net = null;

        for (int i = 0; i < SIM_ROUNDS; i++) {
            try {
                net = QUEUE.remove();

                //Collecting data
                Data d = new Data();
                d.consensusTime = net.getRounds();
                data.add(d);

                //Logging the node history of that round
                String zero = "0";
                if(zero.equals(net.getThread().getName()))
                    handlePositions(net);

                log.info("Round " + net.getThread().getName() + " complete with " + net.getRounds() + " rounds!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }       
        }
        log.info("Evaluation done!");
    }
}
