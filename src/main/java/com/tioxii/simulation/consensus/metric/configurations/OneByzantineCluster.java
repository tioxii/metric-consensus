package com.tioxii.simulation.consensus.metric.configurations;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IConfiguration;
import com.tioxii.simulation.consensus.metric.exceptions.ConfigurationInitException;
import com.tioxii.simulation.consensus.metric.util.Parameter;

public class OneByzantineCluster implements IConfiguration {
    
    @Parameter(isParameter = true, name = "Byzantine")
    public double fraction_byzantine = 0.0f;
    double[] honestClusterPosition = null;
    double[] byzantineClusterPosition = null;
    Class<? extends Node> type; 

    public OneByzantineCluster(double fraction_byzantine, double[] honestClusterPosition, double[] byzantineClusterPosition, Class<? extends Node> type) {
        this.fraction_byzantine = fraction_byzantine;
        this.honestClusterPosition = honestClusterPosition;
        this.byzantineClusterPosition = byzantineClusterPosition;
        this.type = type;
    }

    @Override
    public Node[] generate(int number) throws ConfigurationInitException {
        int number_byzantine = (int) (number * fraction_byzantine);
        int number_honestNodes = number - number_byzantine;
        ArrayList<Node> nodes = new ArrayList<Node>();

        //Byzantine-nodes
        for(int i = 0; i < number_byzantine; i++) {
            try {
                Node node = this.type.getConstructor(double[].class).newInstance(byzantineClusterPosition);
                node.setDishonest();
                nodes.add(node);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                throw new ConfigurationInitException(e.getMessage());
            }
        }
        //Honest-nodes
        for(int i = 0; i < number_honestNodes; i++) {
            try {
                Node node = this.type.getConstructor(double[].class).newInstance(honestClusterPosition);
                nodes.add(node);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                throw new ConfigurationInitException(e.getMessage());
            }
        }

        Collections.shuffle(nodes);
        return nodes.stream().toArray(Node[]::new);
    }
    
}