package com.example.demo.algorithms.data_structure;

import com.example.demo.algorithms.static_algorithms.AStar;

import java.util.*;

public class TSP {

    public TSP() {
    }

    AStar aStar;

    int totalDistance = Integer.MAX_VALUE;
    Map<Integer, List<NodeCoordinate>> possibleSolutionResult = new HashMap<>();
    int numNodes;
    Map<Integer, NodeCoordinate> nodeMap;

    Integer[] solution;
    int[][] distanceMatrix;
    boolean[] mark;
    int total = 0;
    List<NodeCoordinate> nodeCoordinates;

    GeneticAlgorithm ga;

    public void init(List<NodeCoordinate> nodeCoordinates) {
        this.nodeCoordinates = nodeCoordinates;
        numNodes = nodeCoordinates.size();
        nodeMap = new HashMap<>();
        int[] nodes = new int[numNodes];
        mark = new boolean[numNodes];
        solution = new Integer[numNodes];
        for (int i = 0; i < numNodes; i++) {
            nodeMap.put(i, nodeCoordinates.get(i));
            nodes[i] = i;
        }
        distanceMatrix = new int[numNodes][numNodes];
        for (int i = 0; i < numNodes; i++) {
            for (int j = 0; j < numNodes; j++) {
                distanceMatrix[i][j] = NodeCoordinate.betweenManhattan(nodeMap.get(i), nodeMap.get(j));
            }
        }

        Arrays.fill(mark, false);

        // init genetic algorithm constructor here
        ga = new GeneticAlgorithm(nodes, numNodes, distanceMatrix);
    }

    void trace(int i, int pos) {
        if (!mark[i]) {
            mark[i] = true;
            solution[pos] = i;
            if (pos == numNodes - 1) {
                int finalTotal = total + distanceMatrix[i][0];
                mark[i] = false;
                if (finalTotal > totalDistance) {
                    return;
                }
                if (total < totalDistance) {
                    totalDistance = finalTotal;
                    List<NodeCoordinate> nodeCoordinates = new ArrayList<>();
                    for (Integer integer : solution) {
                        NodeCoordinate nodeCoordinate = nodeMap.get(integer);
                        nodeCoordinates.add(nodeCoordinate);
                    }
                    possibleSolutionResult.put(total, nodeCoordinates);
                }
            } else {
                for (int k = 0; k < numNodes; k++) {
                    total += distanceMatrix[i][k];
                    trace(k, pos + 1);
                    total -= distanceMatrix[i][k];
                }
            }
            mark[i] = false;
        }
    }

    public List<NodeCoordinate> findBestSolution() {
        List<Integer> keys = new ArrayList<>(possibleSolutionResult.keySet());
        Collections.sort(keys);
        List<NodeCoordinate> result = possibleSolutionResult.get(keys.get(0));
        List<NodeCoordinate> subList = result.subList(1, result.size());
        Collections.reverse(subList);
        subList.add(0, result.get(0));
        for (NodeCoordinate nodeCoordinate : subList) {
            System.out.printf("(%d, %d)", nodeCoordinate.getX(), nodeCoordinate.getY());
        }
        return subList;
    }

    void traceV2(int k) {
        for (int i = 0; i < numNodes; i++) {
            if (!mark[i]) {
                solution[k] = i;
                total += distanceMatrix[solution[k - 1]][i];
                mark[i] = true;
                if (k == numNodes - 1) {
                    if (total + distanceMatrix[solution[k]][0] < totalDistance) {
                        totalDistance = total + distanceMatrix[solution[k]][0];
                        List<NodeCoordinate> nodeCoordinates = new ArrayList<>();
                        for (Integer integer : solution) {
                            NodeCoordinate nodeCoordinate = nodeMap.get(integer);
                            nodeCoordinates.add(nodeCoordinate);
                        }
                        possibleSolutionResult.put(total + distanceMatrix[solution[k]][0], nodeCoordinates);
                    }
                } else {
                    traceV2(k + 1);
                }
                mark[i] = false;
                total -= distanceMatrix[solution[k - 1]][i];
            }
        }
    }

    public List<NodeCoordinate> findTSP() {

        //by backtracking
        if (numNodes < 13) {
            mark[0] = true; // keep
            solution[0] = 0; // keep
            traceV2(1); // keep
            return findBestSolution(); // keep
        }

        // by genetic algorithm
        List<Integer> resultGA = Main.init(nodeCoordinates, nodeCoordinates.size());
        int[] orderList = new int [numNodes];
        for (int i = 0; i < resultGA.size(); i++) {
            orderList[i] = resultGA.get(i);
        }
        List<NodeCoordinate> result = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) {
            result.add(nodeMap.get(orderList[i]));
        }
        return result;

    }


    public static void main(String[] args) {
        AStar aStar = new AStar(20, 24);
        aStar.init();
        NodeCoordinate nodeCoordinate1 = new NodeCoordinate(0, 0);
//        Node node5 = new Node(7, 7);
//        Node node2 = new Node(3, 5);
//        Node node3 = new Node(2, 1);
//        Node node4 = new Node(3, 6);
        NodeCoordinate nodeCoordinate2 = new NodeCoordinate(12, 1);
        NodeCoordinate nodeCoordinate3 = new NodeCoordinate(12, 5);
        NodeCoordinate nodeCoordinate4 = new NodeCoordinate(8, 15);
        NodeCoordinate nodeCoordinate5 = new NodeCoordinate(20, 23);
        NodeCoordinate nodeCoordinate6 = new NodeCoordinate(20, 24);
        NodeCoordinate nodeCoordinate7 = new NodeCoordinate(8, 31);
        NodeCoordinate nodeCoordinate8 = new NodeCoordinate(4, 36);

        NodeCoordinate block1 = new NodeCoordinate(2, 1);
        NodeCoordinate block2 = new NodeCoordinate(2, 2);
        NodeCoordinate block3 = new NodeCoordinate(2, 3);
        NodeCoordinate block4 = new NodeCoordinate(2, 4);
        NodeCoordinate block5 = new NodeCoordinate(2, 5);
        NodeCoordinate block6 = new NodeCoordinate(2, 6);
        NodeCoordinate block7 = new NodeCoordinate(1, 1);
        NodeCoordinate block8 = new NodeCoordinate(1, 2);
        NodeCoordinate block9 = new NodeCoordinate(1, 3);
        NodeCoordinate block10 = new NodeCoordinate(1, 4);
        NodeCoordinate block11 = new NodeCoordinate(1, 5);
        NodeCoordinate block12 = new NodeCoordinate(1, 6);
        List<NodeCoordinate> blockNodeCoordinateList = Arrays.asList(block1, block2, block3, block4, block5, block6, block7, block8, block9, block10, block11, block12);
        aStar.setBlockedList(blockNodeCoordinateList);
        TSP tsp = new TSP();
        tsp.init(Arrays.asList(nodeCoordinate1, nodeCoordinate2, nodeCoordinate3, nodeCoordinate4, nodeCoordinate5, nodeCoordinate6, nodeCoordinate7, nodeCoordinate8));
//        tsp.trace(0, 0);
        List<NodeCoordinate> nodeCoordinates = tsp.findTSP();
        for (int i = 0; i < nodeCoordinates.size() - 1; i++) {
            aStar.findShortestPath(nodeCoordinates.get(i), nodeCoordinates.get(i + 1));
        }
        aStar.findShortestPath(nodeCoordinates.get(nodeCoordinates.size() - 1), nodeCoordinate1);
//        aStar.findShortestPath(nodes.get);
    }
}
