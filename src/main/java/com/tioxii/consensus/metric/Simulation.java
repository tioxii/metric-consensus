package com.tioxii.consensus.metric;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

import com.tioxii.consensus.metric.dynamics.BaseDynamic;
import com.tioxii.consensus.metric.dynamics.IDynamic;
import com.tioxii.consensus.metric.nodes.BaseNode;
import com.tioxii.consensus.metric.nodes.INode;
import com.tioxii.consensus.metric.util.NodeUtil;
import com.tioxii.consensus.metric.util.SampleCollection;

public class Simulation {
    
    public static int DIMENSIONS = 2;
    public static int SIM_ROUNDS = 1000;
    public static int[] PARTICIPATING_NODES = {1000};
    public static boolean GENERATE_RANDOM = true;
    public static float FRACTION_DISHONEST = 0.0f;
    public static Semaphore MUTEX = new Semaphore(6);
    public static IDynamic DYNAMIC = new BaseDynamic();
    public static Class<? extends INode> NODETYPE = BaseNode.class;
    public static boolean SYNCHRONOUS = true;
    public static String PRESET = "opposing_clusters";
    public static double[][] POSITIONS = null;
    public static String DIR = "results/results.csv";

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

    public static void simulate() {
        NetworkQ nets = new NetworkQ();

        System.out.println("-------Starting Simulation-------");

        int[] rounds = new int[SIM_ROUNDS];

        Thread evaluation = new Thread(() -> evaluate(nets, rounds));
        evaluation.start();

        for(int i = 0; i < SIM_ROUNDS; i++) {
            Network net = new Network(DYNAMIC, NodeUtil.generateNodes(PRESET, NODETYPE, PARTICIPATING_NODES[0], DIMENSIONS, POSITIONS), SYNCHRONOUS);
            
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
            SampleCollection collection = new SampleCollection(DIR);
            collection.writeRoundsToCSV(PARTICIPATING_NODES[0], rounds);
        } catch (IOException e) {
            e.printStackTrace();
        }

        double sum = Arrays.stream(rounds).sum();

        double average = Arrays.stream(rounds).average().getAsDouble(); 

        System.out.println("-------------RESULTS-------------");
        System.out.println("Average number of rounds: " + average);
        System.out.println("Sum of all Rounds: " + sum);
        
    }

    public static void evaluate(NetworkQ nets, int[] rounds) {
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
