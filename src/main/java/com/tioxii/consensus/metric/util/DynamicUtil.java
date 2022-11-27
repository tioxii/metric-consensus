package com.tioxii.consensus.metric.util;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

import com.tioxii.consensus.metric.api.INode;

public class DynamicUtil {
    
    /**
     * Create a new node based on the old class
     * @param node
     * @return
     */
    public static INode createNewNode(INode node, double[] opinion) {
        INode ret = null;
        
        try {
            ret = node.getClass().getConstructor(double[].class).newInstance(opinion);
        } catch (Exception e) {
           e.printStackTrace();
        }
        return ret;
    }

    @Deprecated
    public static double[][] selectRandomOpinion1(int index, int h, INode[] nodes) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        IntStream.range(0, nodes.length).forEach(val -> list.add(val)); 
        
        double[][] opinions = new double[h+1][];
        Random r = new Random();

        opinions[h] = nodes[(list.remove(index))].getOpinion();

        for(int i = 0; i < h; i++) {
            opinions[i] = nodes[list.remove(r.nextInt(list.size()))].getOpinion();
        }
        return opinions;
    }

    public static double[][] selectRandomOpinion(int index, int h, INode[] nodes) {
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

    public static boolean flipCoin(double beta) {
        Random r = new Random();
        
        if(r.nextDouble() < beta) {
            return true;
        }
        return false;
    }
}
