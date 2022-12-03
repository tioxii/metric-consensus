package com.tioxii.consensus.metric.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.tioxii.consensus.metric.Simulation.Data;
import com.tioxii.consensus.metric.api.IDynamic;

public class SampleCollection {
    
    CSVPrinter printer;
    FileWriter out;

    public SampleCollection(File f, boolean hasHeader) throws IOException {
        this.out = new FileWriter(f, true);
        this.printer = new CSVPrinter(out, CSVFormat.DEFAULT);
    }

    public void writeRoundsToCSV(int participants, ArrayList<Data> data, IDynamic dynamic) throws IOException, IllegalArgumentException, IllegalAccessException {  

        for (Data _data : data) {
            printer.print(participants);
            printer.print(_data.consensusTime);
            for(Field field : dynamic.getClass().getFields()) {
                printer.print(field.get(dynamic));
            }
            printer.println();
        }
        out.flush();
    }

    public void writePositionsToCSV(ArrayList<double[][]> rounds) throws IOException {
        for (int i = 0; i < rounds.size(); i++) {
            for(double[] pos : rounds.get(i)) {
                printer.print(i);
                for (double coordinate : pos) {
                    printer.print(coordinate);
                }
                printer.println();
            }
        }
    }

    public void close() throws IOException {
        printer.close();
    }
}
