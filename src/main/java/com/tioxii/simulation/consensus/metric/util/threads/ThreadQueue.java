package com.tioxii.simulation.consensus.metric.util.threads;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * This Queue doesnt guarantee an order.
 */
public class ThreadQueue<T extends IThreadQueueElement> {

    Semaphore QUEUE_ACCESS = new Semaphore(1);
    Semaphore QUEUE_LENGTH = null;

    ArrayList<T> objects = new ArrayList<>();

    public ThreadQueue(int max_length) {
        this.QUEUE_LENGTH = new Semaphore(max_length);
    }

    /**
     * Adds an element to to the queue and notifies waiting threads.
     * @param object
     * @throws InterruptedException
     */
    public void add(T object) throws InterruptedException {
        QUEUE_LENGTH.acquire();
        
        QUEUE_ACCESS.acquire();
        objects.add(object);
        QUEUE_ACCESS.release();

        object.getThread().start();
        newElement();
    }

    public synchronized void newElement() {
        notifyAll();
    }

    /**
     * Get the first element of the queue.
     * @return
     * @throws InterruptedException
     */
    public T remove() throws InterruptedException {
        waitForObject();

        QUEUE_ACCESS.acquire();
        T object = objects.remove(0);

        QUEUE_ACCESS.release();

        object.getThread().join();
        QUEUE_LENGTH.release();

        return object;
    }

    /**
     * Calling this methods, blocks the thread until there is an element in the queue.
     * @throws InterruptedException
     */
    public synchronized void waitForObject() throws InterruptedException {       
        while(objects.isEmpty()) {
            wait();
        }
    }
}
