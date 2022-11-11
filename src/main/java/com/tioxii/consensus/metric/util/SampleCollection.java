package com.tioxii.consensus.metric.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class SampleCollection {
    
    FileWriter out;

    public SampleCollection(String path) throws IOException {
        this.out = new FileWriter(path);
    }

    public void writeRoundsToCSV(int participants, int[] rounds) throws IOException {
        
        String[] strArray = Arrays.stream(rounds).mapToObj(String::valueOf).toArray(String[]::new);

        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT)) {
            printer.printRecord(participants, String.join(",",strArray));
        }
    }
}
