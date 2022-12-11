package com.tioxii.simulation;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.tioxii.math.Distance;

public class Evaluation {
    
    ArrayList<double[][]> positionHistory = new ArrayList<>();

    public Evaluation(ArrayList<double[][]> list) {
        this.positionHistory = list;
    }

    public Evaluation(String path) throws IOException {
        File file = new File(path);
        if(file.exists()) {
            FileReader in = new FileReader(file);
            try (CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT)) {
                List<CSVRecord> records = parser.getRecords();
                convertToPositions(records);
            }
        }
    }

    private void convertToPositions(List<CSVRecord> records) {
        int i = 1;
        do {
            List<CSVRecord> oneRound = getOneRound(i, records);
            if(oneRound.isEmpty() || oneRound == null) {
                break;
            }
            double[][] positionsOneRound = oneRoundToArray(oneRound);
            positionHistory.add(positionsOneRound);
            i++;
        } while(true);
    }

    private double[][] oneRoundToArray(List<CSVRecord> oneRound) {
        return oneRound.stream()
            .map(record -> {
                int dim = record.size() - 1;
                double[] point = new double[dim];
                for(int j = 0; j < dim; j++) {
                    point[j] = Double.parseDouble(record.get(j + 1));
                } 
                return point;
            }).toArray(double[][]::new);
    }

    private List<CSVRecord> getOneRound(int index, List<CSVRecord> records) {
        return (List<CSVRecord>) records.stream()
            .filter(record -> {
                String str = record.get(0);
                return str.equals(index + "");
            }).toList();
    }

    public int size() {
        return positionHistory.size();
    }

    public double calculatePotential1(int round) {
        double[][] positions = positionHistory.get(round);
        double[] mean = Distance.calculateMean(positions);
        double[] distanceToMean = new double[positions.length];
        
        for(int i = 0; i < distanceToMean.length; i++) {
            distanceToMean[i] = Distance.getDistanceEuclideanWithOutCheck(positions[i], mean);
        }

        return Arrays.stream(distanceToMean).sum() / (double) distanceToMean.length;
    }

    public double calculatePotential2(int round) {
        double[][] positions = positionHistory.get(round);
        double[] mean = Distance.calculateMean(positions);
        double[] distanceToMean = new double[positions.length];
        
        for(int i = 0; i < distanceToMean.length; i++) {
            distanceToMean[i] = Distance.getDistanceEuclideanWithOutCheck(positions[i], mean);
        }
        double max = Arrays.stream(distanceToMean).max().getAsDouble();
        double min = Arrays.stream(distanceToMean).min().getAsDouble();
        double average = Arrays.stream(distanceToMean).sum() / (double) distanceToMean.length;

        System.out.println("Average: " + average);
        System.out.println("Maximum: " + max);
        System.out.println("Minimum: " + min);

        return 0;
    }
}