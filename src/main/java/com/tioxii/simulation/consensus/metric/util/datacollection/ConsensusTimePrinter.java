package com.tioxii.simulation.consensus.metric.util.datacollection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class ConsensusTimePrinter {
    
    CSVPrinter printer = null;
    String[] parameter = null;

    public ConsensusTimePrinter(String fileName, String[] parameter, String[] header) throws IOException {
        this.parameter = parameter;
        File file = new File(fileName);
        
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

    public void printConsensusTime(int numberOfNodes, int[] consensusTimes) throws IOException {
        for(int i = 0; i < consensusTimes.length; i++) {
            printer.print(numberOfNodes);
            printer.print(consensusTimes[i]);
            for(int j = 0; j < parameter.length; i++) {
                printer.print(parameter[j]);
            }
            printer.println();
        }
    }

    public void close() throws IOException {
        printer.close();
    }
}
