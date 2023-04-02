package com.tioxii.simulation.consensus.metric.util.datacollection;

import java.io.IOException;
import java.util.ArrayList;

public class DataCollectionManager {
    
    private String directoryName = "";
    private ConsensusTimePrinter consensusTimePrinter = null;

    public DataCollectionManager(String directoryName, String fileName, String[] parameter, String[] header) throws IOException {
        this.consensusTimePrinter = new ConsensusTimePrinter(directoryName, parameter, header);
    }

    public void collectConsensusTime(int numberOfNodes, int consensusTime) {
        try {
            consensusTimePrinter.printConsensusTime(numberOfNodes, consensusTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void collectOpinionHistory(int round, ArrayList<double[][]> history) {}

    public void close() throws IOException {
        consensusTimePrinter.close();
    }
}
