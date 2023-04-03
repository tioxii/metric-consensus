package com.tioxii.simulation.consensus.metric.util.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

import org.reflections.Reflections;

import com.tioxii.simulation.consensus.metric.api.IConfiguration;
import com.tioxii.simulation.consensus.metric.api.IDynamics;
import com.tioxii.simulation.consensus.metric.api.ITermination;

public class ParameterManager {
    
    private static String propertyFile = "consensus.properties";
    private static Properties props = new Properties();
    
    private static String defaultDynamics = "com.tioxii.simulation.consensus.metric.dynamics";
    private static String defaultConfigurations = "com.tioxii.simulation.consensus.metric.configurations";
    private static String defaultTerminations = "com.tioxii.simulation.consensus.metric.termination";

    public static Parameter createParameter() {
        try {
            File file = new File(propertyFile);
            props.load(new FileInputStream(file));    
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        String dynamics = props.getProperty("dynamics");
        String configuration = props.getProperty("configuration");
        String termination = props.getProperty("termination");
        String isSynchronous = props.getProperty("synchronous");
        String maxSimulations = props.getProperty("maxSimulations");
        String dimension = props.getProperty("dimension");
        String increment = props.getProperty("increment");
        
        Parameter parameter = new Parameter();

        try {
            parameter.dynamics = setUpDynamics(dynamics);
            parameter.configuration = setUpConfiguration(configuration);
            parameter.termination = setUpTermination(termination);

            parameter.isSynchronous = Boolean.parseBoolean(isSynchronous);
            parameter.maxSimulations = Integer.parseInt(maxSimulations);
            parameter.dimension = Integer.parseInt(dimension);

            parameter.numberOfNodes = setUpNumberOfNodes(increment);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        return parameter;
    }

    public static void checkAnnotation(Object obj, Field field) throws NumberFormatException, IllegalArgumentException, IllegalAccessException {
        if(field.isAnnotationPresent(com.tioxii.util.Parameter.class)) {
            String name = field.getAnnotation(com.tioxii.util.Parameter.class).name();
            String value = props.getProperty("termination" + obj.getClass().getSimpleName().toLowerCase() + name);
            switch(field.getType().getSimpleName()) {
                case "Integer":
                    field.set(obj, Integer.parseInt(value));
                    break;
                case "Double":
                    field.set(obj, Double.parseDouble(value));
                    break;
                case "Boolean":
                    field.set(obj, Boolean.parseBoolean(value));
                    break;
                case "int":
                    field.set(obj, Integer.parseInt(value));
                    break;
                case "double":
                    field.set(obj, Double.parseDouble(value));
                    break;
                case "boolean":
                    field.set(obj, Boolean.parseBoolean(value));
                    break;
            }
        }
    }

    public static void initObject(Object obj) throws NumberFormatException, IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getFields();
        for(Field field : fields) {
            checkAnnotation(obj, field);
        }
    }

    public static ITermination setUpTermination(String termination) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException { 
        Reflections reflections = new Reflections(defaultTerminations);
        Set<Class<? extends ITermination>> allClasses = reflections.getSubTypesOf(ITermination.class);

        for (Class<? extends ITermination> clazz : allClasses) {
            clazz.getSimpleName();
            if(clazz.getSimpleName().equals(termination)) {
                Object obj = clazz.getConstructor().newInstance();
                initObject(obj);
                return (ITermination) obj;
            }
        }
        throw new IllegalArgumentException("Termination " + termination + " not found");
    }

    public static IDynamics setUpDynamics(String dynamics) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException { 
        Reflections reflections = new Reflections(defaultDynamics);
        Set<Class<? extends IDynamics>> allClasses = reflections.getSubTypesOf(IDynamics.class);

        for (Class<? extends IDynamics> clazz : allClasses) {
            clazz.getSimpleName();
            if(clazz.getSimpleName().equals(dynamics)) {
                Object obj = clazz.getConstructor().newInstance();
                initObject(obj);
                return (IDynamics) obj;
                
            }
        }
        throw new IllegalArgumentException("Dynamics " + dynamics + " not found");
    }

    public static IConfiguration setUpConfiguration(String configuration) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException { 
        Reflections reflections = new Reflections(defaultConfigurations);
        Set<Class<? extends IConfiguration>> allClasses = reflections.getSubTypesOf(IConfiguration.class);

        for (Class<? extends IConfiguration> clazz : allClasses) {
            clazz.getSimpleName();
            if(clazz.getSimpleName().equals(configuration)) {
                Object obj = clazz.getConstructor().newInstance();
                initObject(obj);
                return (IConfiguration) obj;
            }
        }
        throw new IllegalArgumentException("Configuration " + configuration + " not found");
    }

    public static int[] setUpNumberOfNodes(String increment) { 
        int steps = Integer.parseInt(props.getProperty("steps"));
        int start = Integer.parseInt(props.getProperty("start"));
        int end = Integer.parseInt(props.getProperty("end"));

        ArrayList<Integer> iterationList = new ArrayList<Integer>(0);
        
        File file = new File("iteration.txt");
        if(file.exists()) {
            try {
                BufferedReader in = new BufferedReader(new FileReader(file));
                while(in.ready()) {
                    int number = Integer.parseInt(in.readLine());
                    iterationList.add(number);
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(iterationList.size() > 0) {
            int[] iterations = new int[iterationList.size()];
            for(int i = 0; i < iterations.length; i++) {
                iterations[i] = iterationList.get(i);
            }
            return iterations;
        }

        switch(increment) {
            case "exponential": return Iterations.iterationsExponential(start, end, steps);
            case "linear": return Iterations.iterationsLinear(start, end, steps);
            case "explin": return Iterations.iterationsExpLin(start, end, steps);
            default: return new int[0];
        }
    }
}
