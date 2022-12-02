package com.tioxii.consensus.metric.generation;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;

import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.api.INodeGenerator;
import com.tioxii.consensus.metric.exceptions.NodeGenerationException;

public class OneByzantineCluster implements INodeGenerator {

    double fraction_byzantine = 0.0f;
    double[] honestClusterPosition = null;
    double[] byzantineClusterPosition = null;
    Class<? extends INode> type; 

    public OneByzantineCluster(double fraction_byzantine, double[] honestClusterPosition, double[] byzantineClusterPosition, Class<? extends INode> type) {
        this.fraction_byzantine = fraction_byzantine;
        this.honestClusterPosition = honestClusterPosition;
        this.byzantineClusterPosition = byzantineClusterPosition;
        this.type = type;
    }

    @Override
    public INode[] generate(int number) throws NodeGenerationException {
        int number_byzantine = (int) (number * fraction_byzantine);
        int number_honestNodes = number - number_byzantine;
        ArrayList<INode> nodes = new ArrayList<INode>();

        //Byzantine-nodes
        for(int i = 0; i < number_byzantine; i++) {
            try {
                INode node = this.type.getConstructor(double[].class).newInstance(byzantineClusterPosition);
                node.setDishonest();
                nodes.add(node);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                throw new NodeGenerationException(e.getMessage());
            }
        }
        //Honest-nodes
        for(int i = 0; i < number_honestNodes; i++) {
            try {
                INode node = this.type.getConstructor(double[].class).newInstance(honestClusterPosition);
                nodes.add(node);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                throw new NodeGenerationException(e.getMessage());
            }
        }

        Collections.shuffle(nodes);
        return nodes.stream().toArray(INode[]::new);
    }
    
}