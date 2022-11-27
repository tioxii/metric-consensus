package com.tioxii.consensus.metric.util;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class SampleCollectionTest {
    
    @Test
    public void testWriteRoundsToCSV() {
        String PATH = "results/results.csv";

        try {
            File f = new File(PATH);
            SampleCollection collection = new SampleCollection(f, false);
            
            collection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
