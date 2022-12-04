package com.tioxii.consensus.metric.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.tioxii.consensus.metric.Simulation.Data;

public class SampleData {

    CSVPrinter printer = null;

    public SampleData(File file, String[] headers) {
        try {
            file.createNewFile();
            FileWriter out = new FileWriter(file, true);
            printer = new CSVPrinter(out, CSVFormat.DEFAULT);

            if(headers != null && !file.exists()) {
                for(int i = 0; i < headers.length; i++) {
                    printer.print(headers[i]);
                }
            }
            printer.println();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void print(int participating_nodes, ArrayList<Data> data, String[] parameter) throws IOException, IllegalArgumentException, IllegalAccessException {
        for(int i = 0; i < data.size(); i++) {
            printer.print(participating_nodes);
            printer.print(data.get(i).consensusTime);

            for(int j = 0; j < parameter.length; j++) {
                printer.print(parameter[j]);
            }
            printer.println();
        }
        printer.flush();
    }

    public void printPositions(ArrayList<double[][]> positions) {

    }

    public void close() throws IOException {
        printer.close();
    }
}
