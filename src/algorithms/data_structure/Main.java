package com.example.demo.algorithms.data_structure;

import java.util.Arrays;
import java.util.List;

public class Main {

    static int[][] distanceMatrix;
    static int numNodes;
    static int sizePopulation;
    static int ITERATIONs;
    static double crossOverRate;
    static int nN;

    static List<Integer> init (List<NodeCoordinate> nodes, int numNodes) {

        sizePopulation = 2000;
        ITERATIONs = 10000;  //số thế hệ
        nN = 2000;          //số lần lai ghép đột biến
        crossOverRate = 0.8;   //xác suất lai ghép

        Main.numNodes = numNodes;
        Main.distanceMatrix = new int [numNodes][numNodes];
        for (int i = 0; i < numNodes; i++) {
            for (int j = 0; j < numNodes; j++) {
                Main.distanceMatrix[i][j] = NodeCoordinate.betweenManhattan(nodes.get(i), nodes.get(j));
            }
        }
        GA ga = new GA(sizePopulation, ITERATIONs, crossOverRate);
        return ga.run(nN);

    }

    public static void main(String[] args) {


        NodeCoordinate nodeCoordinate1 = new NodeCoordinate(0, 0);
        NodeCoordinate nodeCoordinate2 = new NodeCoordinate(12, 1);
        NodeCoordinate nodeCoordinate3 = new NodeCoordinate(12, 5);
        NodeCoordinate nodeCoordinate4 = new NodeCoordinate(8, 15);
        NodeCoordinate nodeCoordinate5 = new NodeCoordinate(20, 23);
        NodeCoordinate nodeCoordinate6 = new NodeCoordinate(20, 24);
        NodeCoordinate nodeCoordinate7 = new NodeCoordinate(8, 31);
        NodeCoordinate nodeCoordinate8 = new NodeCoordinate(4, 36);

        List<NodeCoordinate> nodes = Arrays.asList(nodeCoordinate1, nodeCoordinate2, nodeCoordinate3, nodeCoordinate6, nodeCoordinate7, nodeCoordinate8, nodeCoordinate4, nodeCoordinate5);

        Main.init(nodes, nodes.size());
        GA ga = new GA(sizePopulation, ITERATIONs, crossOverRate);
        ga.run(nN);
    }
}