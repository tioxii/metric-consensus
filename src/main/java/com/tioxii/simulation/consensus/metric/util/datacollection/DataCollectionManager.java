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
    
    /**
     * The path to the directory where the results are stored.
     */
    private final String PATH = "results/";

    /**
     * The printer for the consensus time.
     */
    private ConsensusTimePrinter consensusTimePrinter = null;

    /**
     * The printer for the opinion/position history.
     */
    private OpinionPrinter opinionPrinter = null;

    /**
     * The constructor of the data collection manager.
     * @param directoryName The name of the directory in the results folder. Can be empty or null. It just "/" then.
     * @param parameter Simulation parameter.
     * @throws DataCollectionException
     */
    public DataCollectionManager(String directoryName, Parameter parameter) throws DataCollectionException {
        try{

            /* Create the directory if it does not exist. */
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

            /* Create the file and the printer. */
            String[] header = extractParamterName(parameter);
            String[] value = extractParamterValue(parameter);

            String fileName = createUniqueName();
            this.consensusTimePrinter = new ConsensusTimePrinter(directoryName + "consensusTime/" + fileName + ".csv", value, header);
            this.opinionPrinter = new OpinionPrinter(directoryName + "opinions/" + fileName + ".csv");
        } catch (IOException | IllegalArgumentException | IllegalAccessException e) {
            throw new DataCollectionException(e);
        }
    }

    /**
     * Extracts the parameter names from the parameter object.
     * @param parameter The parameter object.
     * @return The parameter names.
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
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

    /**
     * Extracts the parameter values from the parameter object.
     * @param parameter The parameter object.
     * @return The parameter values.
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private String[] extractParamterValue(Parameter parameter) throws IllegalArgumentException, IllegalAccessException {
        Object[] objects = new Object[3];
        objects[0] = parameter.configuration;
        objects[1] = parameter.dynamics;
        objects[2] = parameter.termination;
        return ReflectionMethods.extractParametersFromFields(objects);
    }

    /**
     * Creates a unique string for the file name.
     * @return A unique string for the file name.
     */
    private String createUniqueName() {
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
        LocalDateTime dateTime = LocalDateTime.now();
        String formattedDate = dateTime.format(timeFormat);
        return formattedDate;
    }

    /**
     * Collects the consensus time and writes it to the file.
     * @param numberOfNodes The number of nodes in the simulation.
     * @param consensusTimes The consensus time of the simulation process.
     */
    public void collectConsensusTime(int numberOfNodes, int[] consensusTimes) {
        if(consensusTimePrinter == null)
            return;
        
        try {
            consensusTimePrinter.printConsensusTime(numberOfNodes, consensusTimes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Collects the opinion history and writes it to the file.
     * @param round The round of the simulation.
     * @param history The opinion history of the simulation process.
     */
    public void collectOpinionHistory(int round, ArrayList<double[][]> history) {
        if(opinionPrinter == null)
            return;
        
        try {
            opinionPrinter.printOpinion(history);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the data collection.
     * @throws IOException
     */
    public void close() throws IOException {
        if(consensusTimePrinter != null)
            consensusTimePrinter.close();

        if(opinionPrinter != null)
            opinionPrinter.close();
    }
}
