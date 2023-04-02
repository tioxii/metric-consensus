package com.tioxii.simulation.consensus.metric.util.datacollection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class ConsensusTimePrinter {
    
    CSVPrinter printer = null;
    String[] parameter = null;

    public ConsensusTimePrinter(String directoryName, String[] parameter, String[] header) throws IOException {
        this.parameter = parameter;
        File file = new File(directoryName + "consensusTime.csv");
        
        file.createNewFile();
        FileWriter out = new FileWriter(file, true);
        printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        if(!file.exists()) {
            printHeader(header);
        }
    }

    private void printHeader(String[] header) throws IOException {
        for(int i = 0; i < header.length; i++) {
            printer.print(header[i]);
        }
        printer.println();
    }

    public void printConsensusTime(int numberOfNodes, int consensusTime) throws IOException {
        printer.print(numberOfNodes);
        printer.print(consensusTime);
        for(int i = 0; i < parameter.length; i++) {
            printer.print(parameter[i]);
        }
        printer.println();
    }

    public void close() throws IOException {
        printer.close();
    }
}
