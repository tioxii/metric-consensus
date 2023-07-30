package com.tioxii.simulation.consensus.metric.util.threads;

public interface IThreadQueueElement {
    
    /**
     * Get the executing thread corresponding to the object.
     * @return The thread.
     */
    Thread getThread();
}
