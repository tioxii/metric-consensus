package com.tioxii.simulation.consensus.metric.configurations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IConfiguration;
import com.tioxii.simulation.consensus.metric.exceptions.ConfigurationInitException;
import com.tioxii.simulation.consensus.metric.util.DynamicsUtil;

public class RandomSizedClustersPreset100 implements IConfiguration  {

    double[][] opinions = new double[100][];

    public RandomSizedClustersPreset100() {
        int number = 100;
        int dimensions = 2;

        Random r = new Random();
        int numberOfClusters = r.nextInt(number + 1);

        ArrayList<Integer> numbers = new ArrayList<>(0);
        for(int i = 0; i < number; i++) {
            numbers.add(i + 1);
        }

        int[] clusterSizes = new int[numberOfClusters + 1];
        for(int i = 0; i < numberOfClusters; i++) {
            int index = r.nextInt(numbers.size());
            clusterSizes[i] = numbers.remove(index);
        }
        clusterSizes[numberOfClusters] = number;
        Arrays.sort(clusterSizes);

        Node[] nodes = new Node[number];
        double[] opinion = new double[dimensions];
        DynamicsUtil.fillArrayWithRandomNumbers(opinion, 0);
        int j = 0;
        for (int i = 0; i < nodes.length; i++) {
            if(i > clusterSizes[j]) {
                j++;
                DynamicsUtil.fillArrayWithRandomNumbers(opinion, 0);
            }
            opinions[i] = Arrays.copyOf(opinion, opinion.length);
        }
    }

    @Override
    public Node[] generate(int number) throws ConfigurationInitException {
        Node[] nodes = new Node[100];
        for(int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node(opinions[i]);
        }
        return nodes;
    }
    
}
