package com.tioxii.consensus.metric.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.tioxii.consensus.metric.nodes.INode;

public class NodeUtil {
    public static double[][] OPPOSING = {{0.25, 0.5},{0.75, 0.5}};

    /**
     * Generate nodes according to the preset
     * @param preset
     * @param clazz
     * @param participants
     * @param dimensions
     * @param positions
     * @return
     */
    public static INode[] generateNodes(Preset _preset, Class<? extends INode> clazz, int participants, int dimensions, double[][] positions) {
        //TODO enum
        switch(_preset) {
            case PRESET: return generatePreset(clazz, positions);
            case OPPOSING_CLUSTERS:
                if(participants % 2 == 1) {
                    //TODO throw exception
                    System.out.println("Hello");
                    return null;
                }
                participants /= 2;
                return generateClusters(OPPOSING, clazz, participants);
            case RANDOM: return generateRandom(participants, clazz, dimensions);
            default: return generateRandom(participants, clazz, dimensions);
        }
    }

    /**
     * Generate clusters at fixed positions with the given size
     * @param positions
     * @param clazz
     * @param size
     * @return
     */
    public static INode[] generateClusters(double[][] positions, Class<? extends INode> clazz, int size) {
        ArrayList<INode> nodes = new ArrayList<INode>();

        Arrays.stream(positions).forEach(pos -> {
            for (int i = 0; i < size; i++) {
                try {
                    nodes.add(clazz.getConstructor(double[].class).newInstance(Arrays.copyOf(pos, pos.length)));
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }
            }
        });

        Collections.shuffle(nodes);

        INode[] array = new INode[nodes.size()];

        return nodes.toArray(array);
    }

    /**
     * Generates nodes at given preset positions
     * @param clazz
     * @param positions
     * @return
     */
    public static INode[] generatePreset(Class<? extends INode> clazz, double[][] positions) {
        INode[] nodes = new INode[positions.length];

        for (int i = 0; i < positions.length; i++) {
            try {
                nodes[i] = clazz.getConstructor(double[].class).newInstance(Arrays.copyOf(positions[i], positions.length));
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
        }
        return nodes;
    }

    /**
     * Generate nodes at random positions
     * @param number
     * @param clazz
     * @param dimensions
     * @return
     */
    public static INode[] generateRandom(int number, Class<? extends INode> clazz, int dimensions) {
        INode[] nodes = new INode[number];
        
        for (int i = 0; i < nodes.length; i++) {
            try {
                nodes[i] = clazz.getConstructor(int.class).newInstance(dimensions);
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
        return nodes;
    }
}
