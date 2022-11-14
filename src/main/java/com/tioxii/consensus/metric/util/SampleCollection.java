package com.tioxii.consensus.metric.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class SampleCollection {
    
    CSVPrinter printer;

    public SampleCollection(File f) throws IOException {
        FileWriter out = new FileWriter(f);
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
