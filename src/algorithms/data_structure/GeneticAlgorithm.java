package com.example.demo.algorithms.data_structure;

import java.util.*;

public class GeneticAlgorithm {


    int numNodes;
    Integer[] nodes;
    int generations = 2000;
    int population = 100;
    int thresHoldRate = 100;
    double retentionRate = 0.7;
    double mutationRate = 0.2;
    double crossOverRate = 0.7;
    int timeReset = 100;
    Individual bestIndividual;

    List<Individual> pool = new ArrayList<>();


    int[][] distance_matrix;

    public GeneticAlgorithm(int[] nodes, int numNodes, int[][] distance_matrix) {
        Integer[] nodesArray = new Integer[numNodes];
        for (int i = 0; i < numNodes; i++) {
            nodesArray[i] = nodes[i];
        }
        this.nodes = new Integer[numNodes + 1];
        System.arraycopy(nodesArray, 0, this.nodes, 0, nodes.length);
        this.nodes[numNodes] = 0;
        this.numNodes = numNodes + 1;
        this.distance_matrix = distance_matrix;
    }

    public void initPopulation() {
        for (int i = 0; i < population; i++) {
            int x1 = getRandomNumber(1, this.numNodes - 1);
            int x2 = getRandomNumber(1, this.numNodes - 1);
            while (x1 == x2) {
                x1 = getRandomNumber(1, this.numNodes - 1);
                x2 = getRandomNumber(1, this.numNodes - 1);
            }
            int tmp = nodes[x1];
            nodes[x1] = nodes[x2];
            nodes[x2] = tmp;
            int fitness = calcFitness(nodes);
            Integer[] result = new Integer[numNodes];
            for (int j = 1; j < numNodes; j++) {
                result[j] = nodes[j];
            }
            result[0] = 0;
            pool.add(new Individual(result, fitness));
            pool.sort(Comparator.comparingInt(t -> t.fitness));
            bestIndividual = pool.get(0);
        }
    }

    public void initPopulationAgain() {
        List<Individual> newPool = new ArrayList<>();
        for (int i = 0; i < population; i++) {
            int x1 = getRandomNumber(1, this.numNodes - 1);
            int x2 = getRandomNumber(1, this.numNodes - 1);
            while (x1 == x2) {
                x1 = getRandomNumber(1, this.numNodes - 1);
                x2 = getRandomNumber(1, this.numNodes - 1);
            }
            int tmp = nodes[x1];
            nodes[x1] = nodes[x2];
            nodes[x2] = tmp;
            int fitness = calcFitness(nodes);
            Integer[] result = new Integer[numNodes];
            for (int j = 1; j < numNodes; j++) {
                result[j] = nodes[j];
            }
            result[0] = 0;
            newPool.add(new Individual(result, fitness));
        }
        newPool.sort(Comparator.comparingInt(t -> t.fitness));
        pool = newPool.subList(0, population);
    }

    public List<Individual> takeMutation(List<Individual> offSprings) {
        for (Individual individual : offSprings) {
            if ((double) getRandomNumber(1, 100) / 100 <= mutationRate) {
                int x1 = getRandomNumber(1, numNodes - 1);
                int x2 = getRandomNumber(1, numNodes - 1);
                while (x1 == x2) {
                    x1 = getRandomNumber(1, this.numNodes - 1);
                    x2 = getRandomNumber(1, this.numNodes - 1);
                }
                int tmp = individual.getGnome()[x1];
                individual.getGnome()[x1] = individual.getGnome()[x2];
                individual.getGnome()[x2] = tmp;
            }
        }
        offSprings.sort(Comparator.comparingInt(t -> t.fitness));
        return offSprings;
    }

    public void takeMutationIndividual(Individual individual) {
        int x1 = getRandomNumber(1, numNodes - 1);
        int x2 = getRandomNumber(1, numNodes - 1);
        while (x1 == x2) {
            x1 = getRandomNumber(1, this.numNodes - 1);
            x2 = getRandomNumber(1, this.numNodes - 1);
        }
        int tmp = individual.getGnome()[x1];
        individual.getGnome()[x1] = individual.getGnome()[x2];
        individual.getGnome()[x2] = tmp;
    }

    public List<Individual> crossOver() {
        List<Individual> offSprings = new ArrayList<>();
        for (Individual individual : pool) {
            if ((double) getRandomNumber(1, 100) / 100 <= crossOverRate) {
                int rand1 = getRandomNumber(1, population);
                int rand2 = getRandomNumber(1, population);
                while (rand1 == rand2 && Arrays.equals(pool.get(rand1).getGnome(), pool.get(rand2).getGnome())) {
                    rand1 = getRandomNumber(1, this.numNodes - 1);
                    rand2 = getRandomNumber(1, this.numNodes - 1);
                }
                Integer[] parent1 = pool.get(rand1).getGnome();
                Integer[] parent2 = pool.get(rand2).getGnome();
                int crossOverPoint = getRandomNumber(1, numNodes - 1);
                Integer[] child1 = new Integer[numNodes];
                Integer[] child2 = new Integer[numNodes];
                for (int i = 1; i <= crossOverPoint; i++) {
                    child1[i] = parent2[i];
                    child2[i] = parent1[i];
                }
                child1[0] = 0;
                child2[0] = 0;
                child1[numNodes - 1] = 0;
                child2[numNodes - 1] = 0;
                int tmp = crossOverPoint;
                int tmp1 = crossOverPoint;
                int tmp2 = crossOverPoint;
                int tmp3 = crossOverPoint;
                for (int i = 1; i < numNodes - 1; i++) {
                    int k = ++tmp;
                    if (k >= numNodes - 1) {
                        k = k - numNodes + 2;
                    }
                    if (!contains(child1, parent1[k])) {
                        child1[++tmp1] = parent1[k];
                    }
                }
                for (int i = 1; i < numNodes - 1; i++) {
                    int k = ++tmp3;
                    if (k >= numNodes - 1) {
                        k = k - numNodes + 2;
                    }
                    if (!contains(child2, parent2[k])) {
                        child2[++tmp2] = parent2[k];

                    }
                }

                Individual offSpring1 = new Individual(child1, calcFitness(child1));
                Individual offSpring2 = new Individual(child2, calcFitness(child2));

                offSprings.add(offSpring1);
                offSprings.add(offSpring2);
            } else {
                takeMutationIndividual(individual);
                offSprings.add(individual);
            }
        }
        return offSprings;
    }

    public void selectParents(List<Individual> offSprings) {
        pool.addAll(offSprings);
        pool.sort(Comparator.comparingInt(t -> t.fitness));
        int m = (int) (0.7 * population);

        List<Individual> newIndividuals = new ArrayList<>();
        int l = pool.size() - 1;
        for (int i = 0; i < m; i++) {
            newIndividuals.add(pool.get(i));
        }

        for (int i = 0; i < population - m; i++) {
            newIndividuals.add(pool.get(l - i));
        }

        pool = newIndividuals;
    }

    public int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public int[] findTSPBYGA() {
        initPopulation();
        int changeBest = 0;
        for (int i = 0; i < generations; i++) {
            System.out.println("gen:" + " " + i);
            for (int j = 0; j < thresHoldRate; j++) {
                List<Individual> offSprings = crossOverV2();
                takeMutation(offSprings);
                selectParents(offSprings);
            }
            Individual bestPerIteration = pool.get(0);
            if (bestPerIteration.getFitness() < bestIndividual.getFitness()) {
                bestIndividual = bestPerIteration;
                changeBest = 0;
            }
            changeBest++;

            if (changeBest > timeReset) {
                initPopulationAgain();
                System.out.println("INIT AGAIN POPULATION ---------------------------------------------------------");
                changeBest = 0;
            }
            System.out.println("gen:" + " " + i);
            System.out.println("bestResult" + " " + bestIndividual.getFitness());
            System.out.println("bestIndividual" + " " + bestIndividual.getGnome().toString());
        }
        int[] result = new int[numNodes];
        for (int i = 0; i < numNodes; i++) {
            result[i] = bestIndividual.getGnome()[i];
        }
        return result;
    }

    public int calcFitness(Integer[] genome) {
        int length = genome.length;
        int result = 0;
        for (int i = 0; i < length - 2; i++) {
            int node1 = genome[i];
            int node2 = genome[i + 1];
            result += distance_matrix[node1][node2];
        }
        result += distance_matrix[genome[length - 2]][genome[length - 1]];
        return result;
    }

    private boolean contains(Integer[] array, int element) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                continue;
            }
            if (array[i] == element) {
                return true;
            }
        }
        return false;
    }

    public List<Individual> crossOverV2() {
        List<Individual> offSprings = new ArrayList<>();
        for (Individual individual : pool) {
            if ((double) getRandomNumber(1, 100) / 100 <= crossOverRate) {
                int rand1 = getRandomNumber(1, population);
                int rand2 = getRandomNumber(1, population);
                while (rand1 == rand2 && Arrays.equals(pool.get(rand1).getGnome(), pool.get(rand2).getGnome())) {
                    rand1 = getRandomNumber(1, this.numNodes - 1);
                    rand2 = getRandomNumber(1, this.numNodes - 1);
                }
                Integer[] parent1 = pool.get(rand1).getGnome();
                Integer[] parent2 = pool.get(rand2).getGnome();

                Integer[] child1 = new Integer[numNodes];
                Integer[] child2 = new Integer[numNodes];

                int m1 = getRandomNumber(1, numNodes - 1);
                int m2 = getRandomNumber(1, numNodes - 1);
                while (m1 >= m2) {
                    m1 = getRandomNumber(1, numNodes - 1);
                    m2 = getRandomNumber(1, numNodes - 1);
                }


                for (int i = m1; i <= m2; i++) {
                    child1[i] = parent2[i];
                    child2[i] = parent1[i];
                }

                child1[0] = 0;
                child2[0] = 0;
                child1[numNodes - 1] = 0;
                child2[numNodes - 1] = 0;
                int k1 = m2 + 1;
                int k2 = m2;
                int k3 = m2 + 1;
                int k4 = m2;

                for (int i = 0; i < numNodes - 2 - (m2 - m1 + 1); i++) {
                    if (k1 == numNodes - 1) {
                        k1 = 1;
                    }
                    for (int j = k1; j< numNodes -1; j++) {
                        if (!contains(child1, parent1[j])) {
                            child1[k1] = parent1[j];
                            k1++;
                            break;
                        }
                        if (j == numNodes - 2) {
                            j = 1;
                        }
                    }
                }

                for (int i = 0; i < numNodes - 2 - (m2 - m1 + 1); i++) {
                    if (k3 == numNodes - 1) {
                        k3 = 1;
                    }
                    for (int j = k3; j< numNodes -1; j++) {
                        if (!contains(child2, parent2[j])) {
                            child2[k3] = parent2[j];
                            k3++;
                            break;
                        }
                        if (j == numNodes - 2) {
                            j = 1;
                        }
                    }
                }

                Individual offSpring1 = new Individual(child1, calcFitness(child1));
                Individual offSpring2 = new Individual(child2, calcFitness(child2));
                offSprings.add(offSpring1);
                offSprings.add(offSpring2);
                System.out.println("hu");
            } else {
                takeMutationIndividual(individual);
                offSprings.add(individual);
            }
        }

        return offSprings;

}

    void makeIndividualValid(ArrayList<Integer> a) {
        ArrayList<Integer> check = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            check.add(i);
        }

        for (int i = 0; i < a.size(); i++) {
            boolean ok = true;
            for (int j = 0; j < i; j++) {
                if (a.get(i) == a.get(j)) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                check.remove(check.indexOf(a.get(i)));
            }
        }

        Collections.shuffle(check); // tăng độ ngẫu nhiên khi sửa gen
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (a.get(i) == a.get(j)) {
                    a.set(i, check.get(0));
                    check.remove(0);
                }
            }
        }
    }

    boolean checkIndividualValid(List<Integer> a) { // kiểm tra gen có trùng lặp node
        for (int i = 0; i < a.size() - 1; i++) {
            for (int j = i + 1; j < a.size(); j++) {
                if (a.get(i) == a.get(j) && a.get(i) != 0) {
                    return false;
                }
            }
        }

        return true;
    }
}