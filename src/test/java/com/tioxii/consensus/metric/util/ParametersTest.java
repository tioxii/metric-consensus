package com.tioxii.consensus.metric.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.tioxii.consensus.metric.dynamics.BaseDynamicRandom;

public class ParametersTest {
    
    @Test
    public void testcreateParameters() {
        try {
            Parameters[] params = Parameters.createParameters();
            for (Object parameters : params) {
                Parameters param = (Parameters) parameters;
                System.out.println(((BaseDynamicRandom) param.DYNAMIC).beta);
            }
        } catch (ClassNotFoundException | IOException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }
}
