package com.tioxii.consensus.metric.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.tioxii.consensus.metric.nodes.BaseNode;
import com.tioxii.consensus.metric.nodes.INode;

public class NodeUtil {
    public static double[][] OPPOSING = {{0.25, 0.5},{0.75, 0.5}};

    public static INode[] generateClusters(double[][] positions, int size) {
        ArrayList<INode> nodes = new ArrayList<INode>();

        Arrays.stream(positions).forEach(pos -> {
            for (int i = 0; i < size; i++) {
                nodes.add(new BaseNode(Arrays.copyOf(pos, pos.length)));
            }
        });

        Collections.shuffle(nodes);

        INode[] array = new INode[1000];

        return nodes.toArray(array);
    }
}
