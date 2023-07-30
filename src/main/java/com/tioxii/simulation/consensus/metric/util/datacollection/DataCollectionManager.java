package com.tioxii.simulation.consensus.metric.util.datacollection;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.tioxii.simulation.consensus.metric.exceptions.DataCollectionException;
import com.tioxii.simulation.consensus.metric.init.Parameter;
import com.tioxii.simulation.consensus.metric.util.ReflectionMethods;

public class DataCollectionManager {
    
    private final String PATH = "results/";

    private ConsensusTimePrinter consensusTimePrinter = null;
    private OpinionPrinter opinionPrinter = null;

    public DataCollectionManager(String directoryName, Parameter parameter) throws DataCollectionException {
        try{
            if(directoryName == null || directoryName.equals(""))
                directoryName = PATH + createUniqueName() + "/";
            else
                directoryName = PATH + directoryName + "/";
            
            File directory = new File(directoryName + "consensusTime/");
            if(!directory.exists())
                directory.mkdirs();

            directory = new File(directoryName + "opinions/");
            if(!directory.exists())
                directory.mkdirs();

            String[] header = extractParamterName(parameter);
            String[] value = extractParamterValue(parameter);

            String fileName = createUniqueName();
            this.consensusTimePrinter = new ConsensusTimePrinter(directoryName + "consensusTime/" + fileName + ".csv", value, header);
            this.opinionPrinter = new OpinionPrinter(directoryName + "opinions/" + fileName + ".csv");
        } catch (IOException | IllegalArgumentException | IllegalAccessException e) {
            throw new DataCollectionException(e);
        }
    }

    private String[] extractParamterName(Parameter parameter) throws IllegalArgumentException, IllegalAccessException {
        Object[] objects = new Object[3];
        objects[0] = parameter.configuration;
        objects[1] = parameter.dynamics;
        objects[2] = parameter.termination;

        String[] names = ReflectionMethods.getParameterNames(objects);
        String[] parameterNames = new String[names.length + 2];
        parameterNames[0] = "Participants";
        parameterNames[1] = "Rounds";
        for(int i = 2; i < parameterNames.length; i++) {
            parameterNames[i] = names[i - 2];
        }
        return parameterNames;
    }

    private String[] extractParamterValue(Parameter parameter) throws IllegalArgumentException, IllegalAccessException {
        Object[] objects = new Object[3];
        objects[0] = parameter.configuration;
        objects[1] = parameter.dynamics;
        objects[2] = parameter.termination;
        return ReflectionMethods.extractParametersFromFields(objects);
    }

    private String createUniqueName() {
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
        LocalDateTime dateTime = LocalDateTime.now();
        String formattedDate = dateTime.format(timeFormat);
        return formattedDate;
    }

    public void collectConsensusTime(int numberOfNodes, int[] consensusTimes) {
        if(consensusTimePrinter == null)
            return;
        
        try {
            consensusTimePrinter.printConsensusTime(numberOfNodes, consensusTimes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void collectOpinionHistory(int round, ArrayList<double[][]> history) {
        if(opinionPrinter == null)
            return;
        
        try {
            opinionPrinter.printOpinion(history);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        if(consensusTimePrinter != null)
            consensusTimePrinter.close();

        if(opinionPrinter != null)
            opinionPrinter.close();
    }
}
