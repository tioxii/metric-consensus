package com.tioxii.consensus.metric.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.tioxii.consensus.metric.Simulation.Data;

public class SampleCollection {
    
    CSVPrinter printer;

    public SampleCollection(File f) throws IOException {
        FileWriter out = new FileWriter(f);
        printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.printRecord("Participants", "Rounds");
    }

    public void writeRoundsToCSV(int participants, ArrayList<Data> data) throws IOException {

        data.stream().forEach(i -> {
            try {
                printer.printRecord(participants, i.consensusTime);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void close() throws IOException {
        printer.close();
    }
}
