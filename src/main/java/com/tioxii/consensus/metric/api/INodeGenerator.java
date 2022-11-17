package com.tioxii.consensus.metric.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tioxii.consensus.metric.exception.NodeGenerationException;

public interface INodeGenerator {
    public static Logger LOGGER = LogManager.getLogger(INodeGenerator.class.getName());

    INode[] generate(int number) throws NodeGenerationException;
}
