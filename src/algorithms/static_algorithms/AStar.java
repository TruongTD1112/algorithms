package com.example.demo.algorithms.static_algorithms;

import java.util.*;
import com.example.demo.algorithms.data_structure.NodeCoordinate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AStar {

    public AStar(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void init() {
        grid = new NodeCoordinate[width][height];
        closed = new boolean[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                try {
                    grid[i][j] = new NodeCoordinate(i, j);
                    closed[i][j] = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    int height, width;
    final static int DISTANCE = 1;
    static int startI, startJ;
    static int endI, endJ;
    static NodeCoordinate start, end;

    NodeCoordinate[][] grid = new NodeCoordinate[width][height];
    PriorityQueue<NodeCoordinate> open = new PriorityQueue<>(Comparator.comparingInt(NodeCoordinate::getFinalCost));
    boolean[][] closed = new boolean[width][height];

    public void setBlocked(NodeCoordinate nodeCoordinate) {
        grid[nodeCoordinate.getX()][nodeCoordinate.getY()] = null;
    }

    public void setBlockedList(List<NodeCoordinate> nodeCoordinates) {
        for (NodeCoordinate nodeCoordinate : nodeCoordinates) {
            setBlocked(nodeCoordinate);
        }
    }

    private void checkAndUpdateCost(NodeCoordinate current, NodeCoordinate candidate, NodeCoordinate to) {
        if (candidate == null) {
            return;
        }
        int finalCost = current.getFinalCost() + DISTANCE + NodeCoordinate.betweenManhattan(candidate, to);
        if (open.contains(candidate) && finalCost < candidate.getFinalCost()) {
            candidate.setParent(current);
            candidate.setFinalCost(finalCost);
        }
        if (!open.contains(candidate) && closed[candidate.getX()][candidate.getY()] && finalCost < candidate.getFinalCost()) {
            candidate.setParent(current);
            candidate.setFinalCost(finalCost);
            closed[candidate.getX()][candidate.getY()] = false;
            open.add(candidate);
        }
        if (!open.contains(candidate) && !closed[candidate.getX()][candidate.getY()]) {
            candidate.setParent(current);
            candidate.setFinalCost(finalCost);
            open.add(candidate);
        }
    }

    private void reset() {
        open.clear();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                NodeCoordinate nodeCoordinate = grid[i][j];
                if (nodeCoordinate != null) {
                    nodeCoordinate.setFinalCost(0);
                    nodeCoordinate.setParent(null);
                    nodeCoordinate.setHeuristicCost(0);
                    closed[i][j] = false;
                }
            }
        }
    }

    public List<NodeCoordinate> findShortestPath(NodeCoordinate from, NodeCoordinate to) {
        from = grid[from.getX()][from.getY()];
        to = grid[to.getX()][to.getY()];
        open.add(from);
        while (true) {
            try {
                NodeCoordinate current = open.poll();
                if (current == null) {
                    break;
                }
                closed[current.getX()][current.getY()] = true;
                if (current.getX() == to.getX() && current.getY() == to.getY()) {
                    break;
                }

                NodeCoordinate candidate;
                if (current.getX() - 1 >= 0) {
                    candidate = grid[current.getX() - 1][current.getY()];
                    checkAndUpdateCost(current, candidate, to);
                }

                if (current.getY() - 1 >= 0) {
                    candidate = grid[current.getX()][current.getY() - 1];
                    checkAndUpdateCost(current, candidate, to);
                }

                if (current.getY() + 1 < grid[0].length) {
                    candidate = grid[current.getX()][current.getY() + 1];
                    checkAndUpdateCost(current, candidate, to);
                }

                if (current.getX() + 1 < grid.length) {
                    candidate = grid[current.getX() + 1][current.getY()];
                    checkAndUpdateCost(current, candidate, to);
                }
            }
            catch(Exception ex) {
                throw ex;
            }
        }

        List<NodeCoordinate> result = new ArrayList<>();
        if (closed[to.getX()][to.getY()]) {
            //Trace back the path
            result.add(to);
            NodeCoordinate t = grid[to.getX()][to.getY()].getParent();
            while (t != null) {
                result.add(t);
                t = t.getParent();
            }
        } else {
            System.out.println("No possible path");
        }
        reset();
        Collections.reverse(result);
        System.out.println("Path: ");
        int last = result.size() - 1;
        result.remove(last);
        for (NodeCoordinate nodeCoordinate : result) {
            System.out.print("[" + nodeCoordinate.getX() + "," + nodeCoordinate.getY() + "]" + " -> ");
        }
        return result;
    }

    public int findShortestPathV2(NodeCoordinate from, NodeCoordinate to) {
        from = grid[from.getX()][from.getY()];
        to = grid[to.getX()][to.getY()];
        open.add(from);
        while (true) {
            try {
                NodeCoordinate current = open.poll();
                if (current == null) {
                    break;
                }
                closed[current.getX()][current.getY()] = true;
                if (current.getX() == to.getX() && current.getY() == to.getY()) {
                    break;
                }

                NodeCoordinate candidate;
                if (current.getX() - 1 >= 0) {
                    candidate = grid[current.getX() - 1][current.getY()];
                    checkAndUpdateCost(current, candidate, to);
                }

                if (current.getY() - 1 >= 0) {
                    candidate = grid[current.getX()][current.getY() - 1];
                    checkAndUpdateCost(current, candidate, to);
                }

                if (current.getY() + 1 < grid[0].length) {
                    candidate = grid[current.getX()][current.getY() + 1];
                    checkAndUpdateCost(current, candidate, to);
                }

                if (current.getX() + 1 < grid.length) {
                    candidate = grid[current.getX() + 1][current.getY()];
                    checkAndUpdateCost(current, candidate, to);
                }
            }
            catch(Exception ex) {
                throw ex;
            }
        }

        List<NodeCoordinate> result = new ArrayList<>();
        if (closed[to.getX()][to.getY()]) {
            //Trace back the path
            result.add(to);
            NodeCoordinate t = grid[to.getX()][to.getY()].getParent();
            while (t != null) {
                result.add(t);
                t = t.getParent();
            }
        } else {
            System.out.println("No possible path");
        }
        reset();
        Collections.reverse(result);
        System.out.println("Path: ");
        int last = result.size() - 1;
        result.remove(last);
        for (NodeCoordinate nodeCoordinate : result) {
            System.out.print("[" + nodeCoordinate.getX() + "," + nodeCoordinate.getY() + "]" + " -> ");
        }
        return result.size();
    }


    public static void main(String[] args) {
        AStar aStar = new AStar(20, 24);
        aStar.init();
        NodeCoordinate from = new NodeCoordinate(3, 21);
        NodeCoordinate to = new NodeCoordinate(19, 18);
//        Node block = new Node(5,4);
//        Node block2 = new Node(4,5);
//        Node block3 = new Node(6,5);
//        Node block4 = new Node(5,6);
//        AStar.setBlockedList(Arrays.asList(block, block2, block3, block4));
        List<NodeCoordinate> nodeCoordinates = aStar.findShortestPath(from, to);
    }
}