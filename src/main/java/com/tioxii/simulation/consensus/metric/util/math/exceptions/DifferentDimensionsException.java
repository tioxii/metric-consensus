package com.tioxii.simulation.consensus.metric.util.math.exceptions;

/**
 * Should be thrown when two arrays have not the same length in the context of dimensions
 */
public class DifferentDimensionsException extends Exception {

    public DifferentDimensionsException() {}

    public DifferentDimensionsException(String message) {
        super(message);
    }

    public DifferentDimensionsException(Throwable throwable) {
        super(throwable);
    }

    public DifferentDimensionsException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
