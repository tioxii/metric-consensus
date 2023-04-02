package com.tioxii.simulation.consensus.metric.util.datacollection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class OpinionPrinter {
    
    CSVPrinter printer = null;

    public OpinionPrinter(String directoryName) throws IOException {
        File file = new File(directoryName + "consensusTime.csv");
        
        file.createNewFile();
        FileWriter out = new FileWriter(file, true);
        printer = new CSVPrinter(out, CSVFormat.DEFAULT);
    }

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

    public void close() throws IOException {
        printer.close();
    }
}
