package com.example.demo.algorithms.data_structure;

import java.util.ArrayList;

public class IndividualV2 {
    ArrayList<Integer> gene;
    double fitness;

    IndividualV2(ArrayList<Integer> gene) {
        this.gene = gene;
        setFitness();
    }

    public void setFitness() {
        fitness = 0.0;
        for (int i = 0; i < Main.numNodes - 1; i++) {
            fitness += Main.distanceMatrix[gene.get(i)][gene.get(i + 1)];
        }

        fitness += Main.distanceMatrix[gene.get(Main.numNodes - 1)][0];
    }

    public double getFitness() {
        return fitness;
    }

    public ArrayList<Integer> getGene() {
        return gene;
    }
}

