package com.tioxii.consensus.metric.generator;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.configurations.OneByzantineCluster;
import com.tioxii.simulation.consensus.metric.exceptions.ConfigurationInitException;

public class OneByzantineClusterTest {
    
    private boolean isOnByzantinePosition(Node node, double[] byzantinePosition) {
        return Arrays.equals(node.getOpinion(), byzantinePosition);
    }

    private int countNodesOnByzantinePosition(Node[] nodes, double[] byzantinePosition) {
        return Arrays.stream(nodes)
            .map(node -> {
                return isOnByzantinePosition(node, byzantinePosition) ? 1 : 0;
            }).mapToInt(Integer::intValue)
            .sum();
    }

    @Test
    public void testgenerateNodes() {
        int number = 1000;
        double[] honestClusterPosition = {0.25,0.5};
        double[] byzantineClusterPosition = {0.75, 0.5};
        Class<? extends Node> type = Node.class;
        Node[] nodes = null;

        OneByzantineCluster generator = new OneByzantineCluster(0.5, honestClusterPosition, byzantineClusterPosition, type);

        try {
            nodes = generator.generate(number);
            int byzantineNodes = countNodesOnByzantinePosition(nodes, byzantineClusterPosition);
            System.out.println(byzantineNodes);

        } catch (ConfigurationInitException e) {
            e.printStackTrace();
        }   
        int l = -1;
        if(nodes != null)
            l = nodes.length;

        assertEquals(number, l);
    }
}
