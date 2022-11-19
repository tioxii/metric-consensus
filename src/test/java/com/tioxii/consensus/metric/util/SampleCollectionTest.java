package com.tioxii.consensus.metric.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import com.tioxii.consensus.metric.Simulation.Data;

public class SampleCollectionTest {
    
    @Test
    public void testWriteRoundsToCSV() {
        String PATH = "results/results.csv";
        int PARTICIPANTS = 1000;
        int[] rounds = {1,2,3,4,5,6,7};
        ArrayList<Data> data = new ArrayList<>();

        try {
            File f = new File(PATH);
            SampleCollection collection = new SampleCollection(f, false);
            
            collection.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
