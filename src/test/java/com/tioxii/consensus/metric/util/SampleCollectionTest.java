package com.tioxii.consensus.metric.util;

import java.io.IOException;

import org.junit.Test;

public class SampleCollectionTest {
    
    @Test
    public void testWriteRoundsToCSV() {
        String PATH = "results/results.csv";
        int PARTICIPANTS = 1000;
        int[] rounds = {1,2,3,4,5,6,7};
        

        try {
            SampleCollection collection = new SampleCollection(PATH);
            collection.writeRoundsToCSV(PARTICIPANTS, rounds);
            collection.writeRoundsToCSV(PARTICIPANTS, rounds);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
