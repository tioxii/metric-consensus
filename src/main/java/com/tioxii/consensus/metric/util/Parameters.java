package com.tioxii.consensus.metric.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import org.reflections.Reflections;

import com.tioxii.consensus.metric.api.DynamicName;
import com.tioxii.consensus.metric.api.IDynamic;

public class Parameters {
    public IDynamic DYNAMIC = null;

    private static String DYNAMICS_PACKAGE = "com.tioxii.consensus.metric.dynamics";

    public static Parameters[] createParameters() throws ClassNotFoundException, FileNotFoundException, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        String consensusConfigPath = "consensus.properties";
        Properties consensusProps = new Properties();
        File f = new File(consensusConfigPath);
        consensusProps.load(new FileInputStream(f));

        String dynamic = consensusProps.getProperty("dynamic");
        //Setting up reflection

        Reflections reflections = new Reflections(DYNAMICS_PACKAGE);
        Set<Class<?>> type = reflections
            .getTypesAnnotatedWith(DynamicName.class);
        
        //Get class by property
        Optional<Class<?>> result = type.stream()
            .filter(cls -> {
                //TODO
                return /*(cls.isInstance(IDynamic.class)) &&*/ cls.getAnnotation(DynamicName.class).name().equals(dynamic);
            }).findFirst(); 
            
        Class<?> clazz;
        if(result.isPresent()) {
            clazz = result.get();
        } else {
            System.out.println("Not found");
            return null;
        }

        //Read in properties
        Field[] fields = clazz.getFields();
        List<String[]> propValues = Arrays.stream(fields)
            .map(fld -> {
                    return consensusProps.getProperty("dynamic."
                            + clazz.getAnnotation(DynamicName.class).name()
                            + "."
                            + fld.getName())
                        .replace(" ", "")
                        .split(",");
            })
            .toList();

        //Array to store the params
        ArrayList<Object> instances = new ArrayList<>(); 
        //Array with indecies to iterate through propValues
        int[] indecies = new int[propValues.size()];
        do {
            //Creating instances
            Object instance = clazz.getConstructor().newInstance();
            for(int i = 0; i < propValues.size(); i++) {
                for (Field field : fields) {
                    Object obj = transform((String) propValues.get(i)[indecies[i]],field.getType());
                    field.set(instance, obj);
                }
            }

            instances.add(instance);

            //Handling indecies
            for(int i = 0; i < indecies.length; i++) {
                indecies[i]++;
                if(indecies[i] < propValues.get(i).length) {
                    break;
                }
                indecies[i] = 0;
            }
        } while(Arrays.stream(indecies).sum() != 0); //Termination
        
        ArrayList<Parameters> params = new ArrayList<>();
        instances.stream().forEach(x -> {
            Parameters param = new Parameters();
            param.DYNAMIC = (IDynamic) x;
            params.add(param);
        });

        return params.stream().toArray(Parameters[]::new);
    }

    /**
     * Convert String to data type.
     * @param str
     * @param clazz
     * @return
     */
    public static Object transform(String str, Class<?> clazz) {
        switch(clazz.getSimpleName()) {
            default: return Double.parseDouble(str);
        }
    }
}
