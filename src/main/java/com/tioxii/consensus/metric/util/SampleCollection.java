package com.tioxii.consensus.metric.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.tioxii.consensus.metric.Simulation.Data;

public class SampleCollection {
    
    CSVPrinter printer;

    public SampleCollection(File f, boolean hasHeader) throws IOException {
        FileWriter out = new FileWriter(f);
        printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        if(hasHeader)
            printer.printRecord("Participants", "Rounds");
    }

    public void writeRoundsToCSV(int participants, ArrayList<Data> data) throws IOException {
        for (Data _data : data) {
            printer.printRecord(participants, _data.consensusTime);
        }
    }

    public void writePositionsToCSV(ArrayList<double[][]> rounds) throws IOException {
        for (double[][] positions : rounds) {
            for(double[] pos : positions) {
                printer.print(Arrays.toString(pos).replace("[", "").replace("]", ""));
            }
            printer.println();
        }
        printer.println();
    }

    public void close() throws IOException {
        printer.close();
    }
}
