package com.tioxii.consensus.metric;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tioxii.consensus.metric.dynamics.BaseDynamic;
import com.tioxii.consensus.metric.dynamics.IDynamic;
import com.tioxii.consensus.metric.dynamics.MeanValueDynamic;
import com.tioxii.consensus.metric.nodes.BaseNode;
import com.tioxii.consensus.metric.nodes.INode;
import com.tioxii.consensus.metric.util.NodeUtil;
import com.tioxii.consensus.metric.util.Preset;
import com.tioxii.consensus.metric.util.SampleCollection;

public class Simulation {
    
    public int DIMENSIONS = 2;
    public int SIM_ROUNDS = 1000;
    public int[] PARTICIPATING_NODES = {100, 200, 300, 400, 500, 600, 700, 800, 900, 1000};
    public boolean GENERATE_RANDOM = true;
    public float FRACTION_DISHONEST = 0.0f;
    public static int MAX_THREAD_COUNT = 1;
    public static Semaphore MUTEX = new Semaphore(MAX_THREAD_COUNT); //maximum threads simulating
    public IDynamic DYNAMIC = new BaseDynamic();
    public Class<? extends INode> NODETYPE = BaseNode.class;
    public boolean SYNCHRONOUS = true;
    public Preset PRESET = Preset.RANDOM;
    public double[][] POSITIONS = null;
    public String DIR = "results/results.csv";
    public SampleCollection sample;
    public static Logger LOGGER = LogManager.getLogger("Simulation");


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
    }

    public void startSimulate() {
        try {
            this.sample = new SampleCollection(DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < PARTICIPATING_NODES.length; i ++) {
            simulate(i);
        }

        try {
            this.sample.close();
        } catch (IOException e) {   
            e.printStackTrace();
        }
    }

    public void simulate(int iteration) {
        NetworkQ nets = new NetworkQ();

        System.out.println("-------Starting Simulation-------");

        int[] rounds = new int[SIM_ROUNDS];

        Thread evaluation = new Thread(() -> evaluate(nets, rounds));
        evaluation.start();

        for(int i = 0; i < SIM_ROUNDS; i++) {
            Network net = new Network(DYNAMIC, NodeUtil.generateNodes(PRESET, NODETYPE, PARTICIPATING_NODES[iteration], DIMENSIONS, POSITIONS), SYNCHRONOUS);
            
            try {
                Network.mutex.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            net.t = new Thread(net);
            net.t.setName(i + "");
            net.t.start();
            nets.add(net);
            //System.out.println("[Thread-Starting] Simulation-Round: " + net.t.getName());
        }

        try {
            evaluation.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            sample.writeRoundsToCSV(PARTICIPATING_NODES[iteration], rounds);
        } catch (Exception e) {
            // TODO: handle exception
        }

        double sum = Arrays.stream(rounds).sum();

        double average = Arrays.stream(rounds).average().getAsDouble(); 

        System.out.println("-------------RESULTS-------------");
        System.out.println("Average number of rounds: " + average);
        System.out.println("Sum of all Rounds: " + sum);
        
    }

    public void evaluate(NetworkQ nets, int[] rounds) {
        Network net = null;

        for (int i = 0; i < SIM_ROUNDS; i++) {
            while((net = nets.remove()) == null) {
                try {
                    //System.out.println("[Evaluation] Waiting...");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                net.t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            rounds[i] += net.getRounds();
            System.out.println("[Evaluation]: Simulation-Round " + net.t.getName() + " complete with " + net.getRounds() + "!");
        }
    }
}
