package com.tioxii.simulation.consensus.metric.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tioxii.simulation.consensus.metric.Node;
import com.tioxii.simulation.consensus.metric.exceptions.ConfigurationInitException;

/**
 * This interface is used to define a configuration.
 * A configuration is the initial starting position for a set of nodes.
 */
public interface IConfiguration {
    public static Logger LOGGER = LogManager.getLogger(IConfiguration.class.getName());

    /**
     * Generates a number of nodes.
     * @param number The number of nodes to generate.
     * @return An array of nodes.
     * @throws ConfigurationInitException
     */
    Node[] generate(int number) throws ConfigurationInitException;
}
