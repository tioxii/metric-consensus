package com.tioxii.consensus.metric.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

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
