package com.tioxii.simulation.consensus.metric.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileInputStream;
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


    /**
     * Creates a parameter object from the properties file.
     * @return
     */
    public static Parameter createParameter() {
        // Load the properties file.
        try {
            File file = new File(propertyFile);
            props.load(new FileInputStream(file));    
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Get the properties from the file.
        String dynamics = props.getProperty("dynamics");
        String configuration = props.getProperty("configuration");
        String termination = props.getProperty("termination");
        String isSynchronous = props.getProperty("synchronous");
        String maxSimulations = props.getProperty("maxSimulations");
        String dimension = props.getProperty("dimensions");
        String increment = props.getProperty("increment");
        
        // Create a new parameter object.
        Parameter parameter = new Parameter();

        // Call seperate methods to set up the parameter object.
        try {
            parameter.dynamics = setUpDynamics(dynamics);
            parameter.configuration = setUpConfiguration(configuration);
            parameter.termination = setUpTermination(termination);

            parameter.isSynchronous = Boolean.parseBoolean(isSynchronous);
            parameter.maxSimulations = Integer.parseInt(maxSimulations);
            parameter.dimension = Integer.parseInt(dimension);

            parameter.numberOfNodes = setUpNumberOfNodes(increment);
        } catch (InstantiationException 
            | IllegalAccessException 
            | IllegalArgumentException 
            | InvocationTargetException 
            | NoSuchMethodException 
            | SecurityException e) {
            e.printStackTrace();
        }

        return parameter;
    }

    public static void checkAnnotation(Object obj, Field field, String objectType) throws NumberFormatException, IllegalArgumentException, IllegalAccessException {
        if(!field.isAnnotationPresent(com.tioxii.simulation.consensus.metric.util.Parameter.class)) 
            return;

        String name = field.getAnnotation(com.tioxii.simulation.consensus.metric.util.Parameter.class).name();
        String value = props.getProperty(objectType.toLowerCase() + "." + obj.getClass().getSimpleName().toLowerCase() + "." + name.toLowerCase());

        System.out.println("[ParameterManager]  Setting " + name + " to " + value);
        
        switch(field.getType().getSimpleName()) {
            case "Integer":
                field.set(obj, Integer.parseInt(value));
                break;
            case "Double":
                field.set(obj, Double.parseDouble(value));
                break;
            case "Float":
                field.set(obj, Float.parseFloat(value));
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
            case "float":
                field.set(obj, Float.parseFloat(value));
                break;
            case "boolean":
                field.set(obj, Boolean.parseBoolean(value));
                break;
            default: 
                throw new IllegalArgumentException("Type " + field.getType().getSimpleName() + " not supported");
            }
    }

    /**
     * Initializes the object with the values from the properties file.
     * @param obj
     * @throws NumberFormatException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void initObject(Object obj, String objectType) throws NumberFormatException, IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getFields();
        for(Field field : fields) {
            checkAnnotation(obj, field, objectType);
        }
    }

    /**
     * Sets up the the termination criterion object.
     * @param termination
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public static ITermination setUpTermination(String termination) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException { 
        Reflections reflections = new Reflections(defaultTerminations);
        Set<Class<? extends ITermination>> allClasses = reflections.getSubTypesOf(ITermination.class);

        for (Class<? extends ITermination> clazz : allClasses) {
            clazz.getSimpleName();
            if(clazz.getSimpleName().equals(termination)) {
                Object obj = clazz.getConstructor().newInstance();
                initObject(obj, "termination");
                return (ITermination) obj;
            }
        }
        throw new IllegalArgumentException("Termination " + termination + " not found");
    }

    /**
     * Sets up the dynamics object.
     * @param dynamics
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public static IDynamics setUpDynamics(String dynamics) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException { 
        Reflections reflections = new Reflections(defaultDynamics);
        Set<Class<? extends IDynamics>> allClasses = reflections.getSubTypesOf(IDynamics.class);

        for (Class<? extends IDynamics> clazz : allClasses) {
            clazz.getSimpleName();

            if(clazz.getSimpleName().equals(dynamics)) {
                Object obj = clazz.getConstructor().newInstance();
                initObject(obj, "dynamics");
                return (IDynamics) obj;  
            }
        }
        throw new IllegalArgumentException("Dynamics " + dynamics + " not found");
    }

    /**
     * Sets up the starting position of the nodes. Here called configuration.
     * @param configuration
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public static IConfiguration setUpConfiguration(String configuration) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException { 
        Reflections reflections = new Reflections(defaultConfigurations);
        Set<Class<? extends IConfiguration>> allClasses = reflections.getSubTypesOf(IConfiguration.class);

        for (Class<? extends IConfiguration> clazz : allClasses) {
            clazz.getSimpleName();
            if(clazz.getSimpleName().equals(configuration)) {
                Object obj = clazz.getConstructor().newInstance();
                initObject(obj, "configuration");
                return (IConfiguration) obj;
            }
        }
        throw new IllegalArgumentException("Configuration " + configuration + " not found");
    }

    /**
     * Sets up the number of nodes and the increment to the next simulation.
     * @param increment
     * @return
     */
    public static int[] setUpNumberOfNodes(String increment) { 
        int steps = Integer.parseInt(props.getProperty("step"));
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
