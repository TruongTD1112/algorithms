package com.example.demo.algorithms.data_structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Population {
    int sizePopulation; // so luong ca the trong quan the
    ArrayList<IndividualV2> individuals = new ArrayList<>(); // lưu các cá thể trong quần thể

    Population(int n) {
        this.sizePopulation = n; // số lượng cá thể trong quần thể
    }

    public void init() {

        for (int i = 0; i < sizePopulation; i++) {
            ArrayList<Integer> gene = new ArrayList<>();
            for (int j = 1; j < Main.numNodes; j++) {
                gene.add(j);
            }
            Collections.shuffle(gene);
            gene.add(0, 0);
            IndividualV2 ind = new IndividualV2(gene);
            individuals.add(ind);
        }
    }

    public ArrayList<IndividualV2> getIndividuals() {
        return individuals;
    }

    void setIndividuals(ArrayList<IndividualV2> individuals) {
        this.individuals = individuals;
    }

    void add(List<IndividualV2> offsprings) {  //thêm các cá thể con mới tạo ra vào quần thể
        individuals.addAll(offsprings);
    }

    IndividualV2 getBestIndividual() {
        ArrayList<IndividualV2> individualsort = new ArrayList<>(individuals);
        individualsort.sort((i1, i2) -> {
            Double di1 = i1.getFitness();
            Double di2 = i2.getFitness();
            return di1.compareTo(di2);
        });

        return individualsort.get(0);
    }
}
