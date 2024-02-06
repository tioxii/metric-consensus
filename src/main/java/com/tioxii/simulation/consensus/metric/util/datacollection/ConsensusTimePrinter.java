package com.tioxii.simulation.consensus.metric.util.datacollection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 * Writes/prints the consensus times of the consensus processes to a .csv file.
 * @throws IOException If the file cannot be created.
 */
public class ConsensusTimePrinter {
    
    CSVPrinter printer = null;
    String[] parameter = null;

    /**
     * Constructor. Creates a new file in the specified directory.
     * @param fileName The name of the file where the consensus times should be printed.
     * @param parameter The parameters of the consensus process.
     * @param header The header of the file. The first row of the CSV file.
     * @throws IOException If the file cannot be created.
     */
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

    /**
     * Prints the header of the file.
     * @param header The header of the file. The first row of the CSV file.
     * @throws IOException If the file cannot be written to.
     */
    private void printHeader(String[] header) throws IOException {
        for(int i = 0; i < header.length; i++) {
            printer.print(header[i]);
        }
        printer.println();
    }

    /**
     * Prints the consensus times of the consensus processes to the file.
     * @param numberOfNodes The number of nodes in the consensus process.
     * @param consensusTimes The consensus times of the consensus processes.
     * @throws IOException If the file cannot be written to.
     */
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

    /**
     * Closes the file.
     * @throws IOException If the file cannot be closed.
     */
    public void close() throws IOException {
        printer.close();
    }
}
