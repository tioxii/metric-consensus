package com.tioxii.consensus.metric.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.tioxii.consensus.metric.api.Parameter;

public class SampleData {

    CSVPrinter printer = null;

    SampleData(File file, String[] headers) {
        try {
            FileWriter out = new FileWriter(file, true);
            printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void print(int participating_nodes, int[] rounds, Object[] parameters) throws IOException, IllegalArgumentException, IllegalAccessException {
        for(int i = 0; i < rounds.length; i++) {
            printer.print(participating_nodes);
            printer.print(rounds[i]);

            for(int j = 0; j < parameters.length; j++) {
                Field[] fields = parameters[j].getClass().getFields();
                
                for(int k = 0; k < fields.length; k++) {
                    if(fields[k].getAnnotation(Parameter.class).shouldPrint()) {
                        printer.print(fields[k].get(parameters[j]));
                    }
                }
            }
        }
    }

    public void printPositions(double[][] positions) {
        
    }
}
