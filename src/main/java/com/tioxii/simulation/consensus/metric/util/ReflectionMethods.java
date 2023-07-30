package com.tioxii.simulation.consensus.metric.util;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ReflectionMethods {
    
    public static String[] extractParametersFromFields(Object[] objects) throws IllegalArgumentException, IllegalAccessException {
        ArrayList<String> values = new ArrayList<>();
        
        for(int j = 0; j < objects.length; j++) {
            Field[] fields = objects[j].getClass().getFields();
            
            for(int k = 0; k < fields.length; k++) {
                if(fields[k].getAnnotation(Parameter.class)  != null)
                    if(fields[k].getAnnotation(Parameter.class).isParameter()) {
                        values.add(fields[k].get(objects[j]) + "");
                    }
            }
        }
        return values.stream().toArray(String[]::new);
    }

    /**
     * Get parameter names specified in the annotation: @Parameter
     * @param objects
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static String[] getParameterNames(Object[] objects) throws IllegalArgumentException, IllegalAccessException {
        ArrayList<String> names = new ArrayList<>();

        for(int j = 0; j < objects.length; j++) {
            Field[] fields = objects[j].getClass().getFields();
            
            for(int k = 0; k < fields.length; k++) {
                if(fields[k].getAnnotation(Parameter.class)  != null)
                    if(fields[k].getAnnotation(Parameter.class).isParameter()) {
                        names.add(fields[k].get(objects[j]) + "");
                    }
            }
        }
        return names.stream().toArray(String[]::new);
    }
}
