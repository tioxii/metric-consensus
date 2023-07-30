package com.tioxii.consensus.metric.init;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

import com.tioxii.simulation.consensus.metric.init.Parameter;
import com.tioxii.simulation.consensus.metric.init.ParameterManager;

public class ParameterManagerTest {
    
    @Test
    public void testCreateParameter() {
        Parameter parameter = ParameterManager.createParameter();
        assertNotNull(parameter);
    }

    
    @Test
    public void testCreateParameterWithProperties() {
        Properties props = new Properties();
        File file = new File("consensus.properties");
        try {
            props.load(new FileInputStream(file));
            Parameter parameter = ParameterManager.createParameter();
            
            String dynamics = props.getProperty("dynamics");
            String configuration = props.getProperty("configuration");
            String termination = props.getProperty("termination");

            assertEquals(dynamics.toLowerCase(), parameter.dynamics.getClass().getSimpleName().toLowerCase());
            assertEquals(configuration.toLowerCase(), parameter.configuration.getClass().getSimpleName().toLowerCase());
            assertEquals(termination.toLowerCase(), parameter.termination.getClass().getSimpleName().toLowerCase());

        } catch (IOException e) {
            e.printStackTrace();
        }

        
    }

}
