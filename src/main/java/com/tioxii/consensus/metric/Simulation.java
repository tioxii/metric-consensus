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

import com.tioxii.consensus.metric.dynamics.BaseDynamic;
import com.tioxii.consensus.metric.dynamics.IDynamic;
import com.tioxii.consensus.metric.nodes.BaseNode;
import com.tioxii.consensus.metric.nodes.INode;
import com.tioxii.consensus.metric.util.NodeUtil;
import com.tioxii.consensus.metric.util.Preset;
import com.tioxii.consensus.metric.util.SampleCollection;

public class Simulation {
    
    //Environment-Settings
    public int DIMENSIONS = 2;
    public int SIM_ROUNDS = 1000;
    public int[] PARTICIPATING_NODES = {100, 1000};
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

        /*
         * Get the last Element
         */
        public void add(Network e) {
            nets.add(e);
        }

        /**
         * Get the first element
         * @return
         */
        public Network remove() {
            if(nets.size() >= 1)
                return nets.remove(0);
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
            File dir = new File(DIR);
            if(!dir.exists()) {
                dir.mkdir();
            }

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

        for(int i = 0; i < SIM_ROUNDS; i++) {
            Network net = new Network(DYNAMIC, NodeUtil.generateNodes(PRESET, NODETYPE, PARTICIPATING_NODES[iteration], DIMENSIONS, POSITIONS), SYNCHRONOUS);

            try {
                MUTEX.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
                LOGGER.error(e.getMessage());
            }

            net.t = new Thread(net);
            net.t.setName(i + "");
            net.t.start();
            nets.add(net);
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
            while((net = nets.remove()) == null) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //Waiting for Thread to finish.
            try {
                net.t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //So other Threads can start.

            rounds[i] += net.getRounds();
            LOGGER.info("Simulation-Round " + net.t.getName() + " complete with " + net.getRounds() + " rounds! Start-Mean: " + Arrays.toString(net.startMean) + " End-Mean: " + Arrays.toString(net.endMean));
        }
        LOGGER.info("Evaluation done!");
    }
}
