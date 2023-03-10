package com.tioxii.simulation.consensus.metric.generators;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.api.INodeGenerator;
import com.tioxii.simulation.consensus.metric.exceptions.NodeGenerationException;

public class FullCircle implements INodeGenerator {

    @Override
    public Node[] generate(int number) throws NodeGenerationException {
        Circle circle = new Circle(number, Node.class);
        return circle.generate(number);
    }
    
}
