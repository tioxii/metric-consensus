package com.tioxii.simulation.consensus.metric.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.tioxii.simulation.consensus.metric.Node;

public class DynamicsUtil {
    
    /**
     * Create a new node based on the old class
     * @param node
     * @return
     */
    public static Node createNewNode(Node node, double[] opinion) {
        Node ret = null;
        
        try {
            ret = node.getClass().getConstructor(double[].class).newInstance(Arrays.copyOf(opinion, opinion.length));
        } catch (Exception e) {
           e.printStackTrace();
        }
        return ret;
    }

    public static double[][] selectRandomOpinion(int index, int h, Node[] nodes) {
        double[][] opinions = new double[h+1][];
        ArrayList<Integer> indecies = new ArrayList<Integer>();
        Random r = new Random();

        opinions[h] = nodes[index].getOpinion();

        for (int i = 0; i < h; i++) {
            int r1 = r.nextInt(nodes.length);
            while(indecies.contains(r1)) {
                r1 = r.nextInt(nodes.length);
            }
            indecies.add(r1);
            opinions[i] = nodes[r1].getOpinion();
        }

        return opinions;
    }

    public static Random r = new Random();

    /**
     * Fills the given array with random doubles between 0 (inclusive) and 1 (exclusive)
     * @param array to be filled.
     */
    public static void fillArrayWithRandomNumbers(double[] array, double offset) {
        for(int i = 0; i < array.length; i++) {
            array[i] = r.nextDouble() + offset;
        }
    }
}
