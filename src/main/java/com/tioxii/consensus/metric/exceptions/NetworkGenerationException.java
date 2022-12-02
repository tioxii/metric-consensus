package com.tioxii.consensus.metric.exceptions;

public class NetworkGenerationException extends Exception {

    String message;

    public NetworkGenerationException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
