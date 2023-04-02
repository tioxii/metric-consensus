package com.tioxii.consensus.metric.generator;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.configurations.Circle;
import com.tioxii.simulation.consensus.metric.exceptions.ConfigurationInitException;

public class CircleTest {
    
    @Test
    public void testgenerateNodes() {
        int number = 1000;
        Class<? extends Node> type = Node.class;
        Circle generator = new Circle(11, type);
        Node[] nodes = null;

        try {
            nodes = generator.generate(number);

            
            System.out.println(nodes.length);
            
            

            Arrays.stream(nodes).forEach(x -> {
                System.out.println(Arrays.toString(x.getOpinion()));
            });
        } catch (ConfigurationInitException e) {
            e.printStackTrace();
        }
        int l = -1; 
        Class<? extends Node> clazz = null;

        if(nodes != null) {
            l = nodes.length;
            if(l > 0)
                clazz = nodes[0].getClass();
        }
            
        
        assertEquals(number, l);
        assertEquals(clazz, type);
    }
}
