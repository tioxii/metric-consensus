package com.tioxii.consensus.metric.generator;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.tioxii.consensus.metric.api.INode;
import com.tioxii.consensus.metric.exceptions.NodeGenerationException;
import com.tioxii.consensus.metric.generation.Circle;
import com.tioxii.consensus.metric.nodes.BaseNode;

public class CircleTest {
    
    @Test
    public void testgenerateNodes() {
        int number = 1000;
        Class<? extends INode> type = BaseNode.class;
        Circle generator = new Circle(11, type);
        INode[] nodes = null;

        try {
            nodes = generator.generate(number);

            INode node = nodes[0];
            System.out.println(nodes.length);
            assertEquals(number, nodes.length);
            assertEquals(node.getClass(), type);

            Arrays.stream(nodes).forEach(x -> {
                System.out.println(Arrays.toString(x.getOpinion()));
            });
        } catch (NodeGenerationException e) {
            e.printStackTrace();
        }
    }
}
