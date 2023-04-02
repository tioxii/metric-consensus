package com.tioxii.simulation.consensus.metric.configurations;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.IConfiguration;
import com.tioxii.simulation.consensus.metric.exceptions.ConfigurationInitException;

public class FullCircle implements IConfiguration {

    @Override
    public Node[] generate(int number) throws ConfigurationInitException {
        Circle circle = new Circle(number, Node.class);
        return circle.generate(number);
    }
    
}
