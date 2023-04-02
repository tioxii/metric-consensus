package com.tioxii.simulation.consensus.metric.exceptions;

public class SimulationInitException extends Exception {

    String message;

    public SimulationInitException(String message) {
        this.message = message;
    }

    public SimulationInitException(InterruptedException e) {
    }

    @Override
    public String getMessage() {
        return message;
    }
}
