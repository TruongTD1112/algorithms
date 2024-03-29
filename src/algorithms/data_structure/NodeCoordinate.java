package com.example.demo.algorithms.data_structure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeCoordinate {

    // tọa độ x, y của node
    private int x;
    private int y;

    private int heuristicCost = 0; //Heuristic cost
    private int finalCost = 0; //G+H

    /**
     * calculate distance between two nodes base on Manhattan distance
     *
     * @param from
     * @param to
     * @return
     */
    public static int betweenManhattan(NodeCoordinate from, NodeCoordinate to) {
        return Math.abs(from.x - to.x) + Math.abs(from.y - to.y);
    }

    public static int between;

    NodeCoordinate parent;
    public NodeCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
