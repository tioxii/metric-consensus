package com.tioxii.consensus.metric;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tioxii.consensus.metric.api.IDynamic;
import com.tioxii.consensus.metric.api.INodeGenerator;
import com.tioxii.consensus.metric.exception.NetworkGenerationException;
import com.tioxii.consensus.metric.exception.NodeGenerationException;

import com.tioxii.consensus.metric.util.SampleCollection;

public class Simulation {
    
    //Parameters
    public int DIMENSIONS = 2;
    public int SIM_ROUNDS = 1000;
    public int[] PARTICIPATING_NODES = null;
    public IDynamic DYNAMIC = null;
    public boolean SYNCHRONOUS = true;
    public INodeGenerator GENERATOR = null;
    
    //Evaluation-Settings
    public String FILE_NAME = null;
    private String FILE_NAME_POSITIONS = null;
    public String DIR = "results/";
    public boolean RECORD_RESULTS = false;
    public boolean RECORD_POSITIONS = false;
    
    //Utility
    public ResultWriter writer = null;
    public static Logger log = LogManager.getLogger("Simulation");
    public int MAX_THREAD_COUNT = 6;
    public static Semaphore MUTEX; //maximum threads simulating

    public class Data {
        public int consensusTime;
        public double[] startMean;
        public double[] endMean;
    }

    /**
     * Object responsible for collecting the results of the simulation.
     */
    private class ResultWriter {
        private SampleCollection samplePositions = null;
        private SampleCollection sampleResults = null;

        public ResultWriter(String dir, String fileResults, String filePositions) throws IOException {
            if(RECORD_POSITIONS) {
                File f = new File(dir + filePositions);
                f.createNewFile();
                this.samplePositions = new SampleCollection(f, false);
            }

            if(RECORD_RESULTS) {
                File f = new File(dir + fileResults);
                f.createNewFile();
                this.sampleResults = new SampleCollection(f, true);
            }
        }

        public void writeResults(int participants,ArrayList<Data> data) throws IOException {
            if(sampleResults != null)
                sampleResults.writeRoundsToCSV(participants, data);
        }
        public void writePositions(ArrayList<double[][]> positions) throws IOException {
            if(samplePositions != null)
                samplePositions.writePositionsToCSV(positions);
        }

        public void close() throws IOException {
            if(samplePositions != null)
                samplePositions.close();

            if(sampleResults != null)
                sampleResults.close();
        }
    }

    /**
     * Queue for the network elements.
     */
    private class NetworkQ {
        ArrayList<Network> nets = new ArrayList<Network>();
        public static Semaphore QUEUE_ACCESS = new Semaphore(1);

        /*
         * Get the last Element
         */
        public void add(Network e) throws InterruptedException, NetworkGenerationException {
            
            if(e != null) {
                MUTEX.acquire();

                QUEUE_ACCESS.acquire();

                nets.add(e);
                e.t.start();

                QUEUE_ACCESS.release();
            } else {
                throw new NetworkGenerationException("Network is null!");
            }
        }

        /**
         * Get the first element
         * @return
         * @throws InterruptedException
         */
        public Network remove() throws InterruptedException {
            if(nets.size() > 0) {
                QUEUE_ACCESS.acquire();
                
                Network net = nets.remove(0);
                MUTEX.release();

                QUEUE_ACCESS.release();
                
                return net;
            }
            return null;
        }

        public int size() {
            return nets.size();
        }
    }

    public void startSimulate() {
        MUTEX = new Semaphore(MAX_THREAD_COUNT);
        
        log.info("Setting up file for collecting results.");
        log.info("Log positions: " + RECORD_POSITIONS);
        log.info("Log round results: " + RECORD_RESULTS);
        //Generate unique filenames
        if(FILE_NAME == null) {
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
            LocalDateTime dateTime = LocalDateTime.now();
            String formattedDate = dateTime.format(timeFormat);
            FILE_NAME = formattedDate + "_DIM-" + DIMENSIONS + "_R-" + SIM_ROUNDS + "_SYNC-" + SYNCHRONOUS + ".csv";
        } 
        FILE_NAME_POSITIONS = FILE_NAME + "_POSITIONS" + ".csv";
        
        try {
            //Create directory if it doesn't exist.
            File dir = new File(DIR);
            if(!dir.exists()) {
                dir.mkdir();
            }
            
            //Create file.
            this.writer = new ResultWriter(DIR, FILE_NAME, FILE_NAME_POSITIONS);

            for(int i = 0; i < PARTICIPATING_NODES.length; i ++) {
                try {
                    simulate(i);
                } catch (NetworkGenerationException | NodeGenerationException e) {
                    log.error("Failed to simulate round: " + i);
                }
            }
            
            this.writer.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void simulate(int iteration) throws NetworkGenerationException, NodeGenerationException {
        NetworkQ nets = new NetworkQ();
        ArrayList<Data> data = new ArrayList<>();

        log.info("-------Starting Simulation-------");
        log.info("Participants: " + PARTICIPATING_NODES[iteration]);
        log.info("Byzantine-Nodes: " + 0);
        log.info("Generator: " + GENERATOR.getClass().getSimpleName());
        log.info("Dynamic: " + DYNAMIC.getClass().getSimpleName());
        log.info("Simulation-Rounds: " + SIM_ROUNDS);
        log.info("Synchronous: " + SYNCHRONOUS);
        

        Thread evaluation = new Thread(() -> evaluate(nets, data));
        evaluation.setName("Evaluation");
        evaluation.start();

        //Creating Network simulations and add them to the q.
        for(int i = 0; i < SIM_ROUNDS; i++) {
            Network net = new Network(DYNAMIC, GENERATOR.generate(PARTICIPATING_NODES[iteration]), SYNCHRONOUS);

            net.t = new Thread(net);
            net.t.setName(i + "");

            try {
                nets.add(net);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        log.debug("NetworkQ-Length: " + nets.size());
        log.debug("Semaphor-Queue: " + MUTEX.hasQueuedThreads());

        try {
            evaluation.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        //Printing Results to CSV file
        try {
            writer.writeResults(PARTICIPATING_NODES[iteration], data);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        int[] rounds = data.stream().map(elem -> elem.consensusTime).mapToInt(Integer::intValue).toArray();
        double average = Arrays.stream(rounds).average().getAsDouble();

        log.info("-------------RESULTS-------------");
        log.info("Average number of rounds: " + average);
    }

    public void evaluate(NetworkQ nets, ArrayList<Data> data) {
        Network net = null;

        for (int i = 0; i < SIM_ROUNDS; i++) {
            try {
                while((net = nets.remove()) == null) { 
                    Thread.sleep(1000);
                }
                //Waiting for Thread to finish.
                net.t.join();

                //Collecting data
                Data d = new Data();
                d.consensusTime = net.getRounds();
                d.startMean = net.startMean;
                d.endMean = net.endMean;
                data.add(d);

                //Logging the node history of that round
                ArrayList<double[][]> hist = net.getHistory();
                try {
                    writer.writePositions(hist);
                } catch (IOException e) {
                    log.error("Failed to log the node position history: " + e.getMessage());
                }

                log.info("Round " + net.t.getName() + " complete with " + net.getRounds() + " rounds!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }       
        }
        log.info("Evaluation done!");
    }
}
