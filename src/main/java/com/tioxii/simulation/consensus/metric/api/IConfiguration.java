package com.tioxii.simulation.consensus.metric.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.exceptions.NodeGenerationException;

public interface IConfiguration {
    public static Logger LOGGER = LogManager.getLogger(IConfiguration.class.getName());

    Node[] generate(int number) throws NodeGenerationException;
}
