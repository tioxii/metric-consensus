package com.tioxii.consensus.metric;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tioxii.consensus.metric.api.IDynamic;
import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.api.INodeGenerator;
import com.tioxii.consensus.metric.dynamics.BaseDynamic;
import com.tioxii.consensus.metric.exception.NetworkGenerationException;
import com.tioxii.consensus.metric.exception.NodeGenerationException;
import com.tioxii.consensus.metric.nodes.BaseNode;
import com.tioxii.consensus.metric.util.NodeUtil;
import com.tioxii.consensus.metric.util.Preset;
import com.tioxii.consensus.metric.util.SampleCollection;

public class Simulation {
    
    //Environment-Settings
    public int DIMENSIONS = 2;
    public int SIM_ROUNDS = 1000;
    public int[] PARTICIPATING_NODES = {1000};
    public boolean GENERATE_RANDOM = true;
    public float FRACTION_DISHONEST = 0.0f;
    public IDynamic DYNAMIC = new BaseDynamic();
    public Class<? extends INode> NODETYPE = BaseNode.class;
    public boolean SYNCHRONOUS = true;
    public Preset PRESET = Preset.RANDOM;
    public double[][] POSITIONS = null;
    public INodeGenerator GENERATOR = null;
    
    //Evaluation-Settings
    public String FILE_NAME = null;
    public String DIR = "results/";
    
    //Utility
    public SampleCollection sample;
    public static Logger LOGGER = LogManager.getLogger("Simulation");
    public int MAX_THREAD_COUNT = 6;
    public static Semaphore MUTEX; //maximum threads simulating

    private class Data {
        ArrayList<Integer> consensusTime = new ArrayList<>();
        ArrayList<Double[]> startMean = new ArrayList<>();
        ArrayList<Double[]> endMean = new ArrayList<>();
    }

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
        
        //Generate unique filenames
        if(FILE_NAME == null) {
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
            LocalDateTime dateTime = LocalDateTime.now();
            String formattedDate = dateTime.format(timeFormat);
            FILE_NAME = formattedDate + "_DIM-" + DIMENSIONS + "_R-" + SIM_ROUNDS + "_SYNC-" + SYNCHRONOUS + ".csv";
        }
        
        try {
            //Create directory if it doesn't exist.
            File dir = new File(DIR);
            if(!dir.exists()) {
                dir.mkdir();
            }

            //Create file.
            File f = new File(DIR + FILE_NAME);
            f.createNewFile();
            this.sample = new SampleCollection(f);

            for(int i = 0; i < PARTICIPATING_NODES.length; i ++) {
                try {
                    simulate(i);
                } catch (NetworkGenerationException | NodeGenerationException e) {
                    LOGGER.error("Failed to simulate round: " + i);
                }
            }
            
            this.sample.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public void simulate(int iteration) throws NetworkGenerationException, NodeGenerationException {
        NetworkQ nets = new NetworkQ();

        LOGGER.info("-------Starting Simulation-------");

        Data data = new Data();

        Thread evaluation = new Thread(() -> evaluate(nets, data));
        evaluation.setName("Evaluation");
        evaluation.start();

        //Creating Network simulations and add them to the q.
        for(int i = 0; i < SIM_ROUNDS; i++) {
            Network net = new Network(DYNAMIC, NodeUtil.generateNodes(PRESET, NODETYPE, PARTICIPATING_NODES[iteration], DIMENSIONS, POSITIONS), SYNCHRONOUS);

            net.t = new Thread(net);
            net.t.setName(i + "");

            try {
                nets.add(net);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        LOGGER.debug("NetworkQ-Length: " + nets.size());
        LOGGER.debug("Semaphor-Queue: " + MUTEX.hasQueuedThreads());

        try {
            evaluation.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }

        //Printing Results to CSV file
        try {
            sample.writeRoundsToCSV(PARTICIPATING_NODES[iteration], rounds);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

        double sum = Arrays.stream(data.consensusTime).sum();
        double average = data.consensusTime.stream().average

        LOGGER.info("-------------RESULTS-------------");
        LOGGER.info("Average number of rounds: " + average);
        LOGGER.info("Sum of all Rounds: " + sum);
    }

    public void evaluate(NetworkQ nets, Data data) {
        Network net = null;

        for (int i = 0; i < SIM_ROUNDS; i++) {
            try {
                while((net = nets.remove()) == null) { 
                    Thread.sleep(1000);
                }
                //Waiting for Thread to finish.
                net.t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //Collecting data
            if(net.startMean.length == net.endMean.length) {
                Double[] startMean = new Double[net.startMean.length];
                Double[] endMean = new Double[net.endMean.length];
                for (int j = 0; j < net.startMean.length; j++) {
                    startMean[j] = (Double) net.startMean[j];
                    endMean[j] = (Double) net.endMean[j];
                }
                data.startMean.add(startMean);
                data.endMean.add(endMean);
            }
            data.consensusTime.add(net.getRounds());

            LOGGER.info("Round " + net.t.getName() + " complete with " + net.getRounds() + " rounds!");
        }
        LOGGER.info("Evaluation done!");
    }
}
