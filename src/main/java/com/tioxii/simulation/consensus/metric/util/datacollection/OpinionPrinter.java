package com.tioxii.simulation.consensus.metric.util.datacollection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 * Writes/prints the opinions/positions of the nodes to a .csv file.
 * @throws IOException If the file cannot be created.
 */
public class OpinionPrinter {
    
    CSVPrinter printer = null;

    /**
     * Constructor. Creates a new file in the specified directory.
     * @param directoryName The name of the directory where the file should be created.
     * @throws IOException If the file cannot be created.
     */
    public OpinionPrinter(String directoryName) throws IOException {
        File file = new File(directoryName + "consensusTime.csv");
        
        file.createNewFile();
        FileWriter out = new FileWriter(file, true);
        printer = new CSVPrinter(out, CSVFormat.DEFAULT);
    }

    /**
     * Prints the opinions of the nodes to the file.
     * @param positions The positions of the nodes.
     * @throws IOException If the file cannot be written to.
     */
    public void printOpinion(ArrayList<double[][]> positions) throws IOException {
        for(int i = 0; i < positions.size(); i++) {
            for(int j = 0; j < positions.get(i).length; j++) {
                printer.print(i);
                for(int k = 0; k < positions.get(i)[j].length; k++) {
                    printer.print(positions.get(i)[j][k]);
                }
                printer.println();
            }
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
