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
import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.dynamics.BaseDynamic;
import com.tioxii.consensus.metric.exception.NetworkGenerationException;
import com.tioxii.consensus.metric.nodes.BaseNode;
import com.tioxii.consensus.metric.util.NodeUtil;
import com.tioxii.consensus.metric.util.Preset;
import com.tioxii.consensus.metric.util.SampleCollection;

public class Simulation {
    
    //Environment-Settings
    public int DIMENSIONS = 2;
    public int SIM_ROUNDS = 1000;
    public int[] PARTICIPATING_NODES = {100,200,300,400,500,600,700,800,900,1000,1100,1200,1300,1400,1500,1600,1700,1800,1900,2000};
    public boolean GENERATE_RANDOM = true;
    public float FRACTION_DISHONEST = 0.0f;
    public IDynamic DYNAMIC = new BaseDynamic();
    public Class<? extends INode> NODETYPE = BaseNode.class;
    public boolean SYNCHRONOUS = true;
    public Preset PRESET = Preset.RANDOM;
    public double[][] POSITIONS = null;
    
    //Evaluation-Settings
    public String FILE_NAME = null;
    public String DIR = "results/";
    
    //Utility
    public SampleCollection sample;
    public static Logger LOGGER = LogManager.getLogger("Simulation");
    public int MAX_THREAD_COUNT = 6;
    public static Semaphore MUTEX; //maximum threads simulating

    public static class NetworkQ {
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
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy__HH-mm-ss");
            LocalDateTime dateTime = LocalDateTime.now();
            String formattedDate = dateTime.format(timeFormat);
            FILE_NAME = formattedDate + "__DIM-" + DIMENSIONS + "__R-" + SIM_ROUNDS + "__SYNC-" + SYNCHRONOUS + ".csv";
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
                simulate(i);
            }
            
            this.sample.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public void simulate(int iteration) {
        NetworkQ nets = new NetworkQ();

        LOGGER.info("-------Starting Simulation-------");

        int[] rounds = new int[SIM_ROUNDS];

        Thread evaluation = new Thread(() -> evaluate(nets, rounds));
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
            } catch (NetworkGenerationException e) {
                LOGGER.error(e.getMessage());
            }
        }

        LOGGER.info("NetworkQ-Length: " + nets.size());
        LOGGER.info("Semaphor-Queue: " + MUTEX.hasQueuedThreads());

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

        double sum = Arrays.stream(rounds).sum();
        double average = Arrays.stream(rounds).average().getAsDouble(); 

        LOGGER.info("-------------RESULTS-------------");
        LOGGER.info("Average number of rounds: " + average);
        LOGGER.info("Sum of all Rounds: " + sum);
    }

    public void evaluate(NetworkQ nets, int[] rounds) {
        Network net = null;

        for (int i = 0; i < SIM_ROUNDS; i++) {
            try {
                while((net = nets.remove()) == null) { 
                    Thread.sleep(5000);
                }

                //Waiting for Thread to finish.
                net.t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //So other Threads can start.

            rounds[i] += net.getRounds();
            LOGGER.info("Simulation-Round " + net.t.getName() + " complete with " + net.getRounds() + " rounds! Q-Length: " + nets.size() + " i: " + i);
        }
        LOGGER.info("Evaluation done!");
    }
}
