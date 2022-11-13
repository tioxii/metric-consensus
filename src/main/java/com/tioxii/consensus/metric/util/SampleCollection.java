package com.tioxii.consensus.metric.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class SampleCollection {
    
    FileWriter out;
    CSVPrinter printer;

    public SampleCollection(String path) throws IOException {
        this.out = new FileWriter(path);
        printer = new CSVPrinter(out, CSVFormat.DEFAULT);
    }

    public void writeRoundsToCSV(int participants, int[] rounds) throws IOException {
        printer.print(participants);

        Arrays.stream(rounds).forEach(i -> {
            try {
                printer.print(i);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        printer.println();
    }

    public void close() throws IOException {
        printer.close();
    }
}
