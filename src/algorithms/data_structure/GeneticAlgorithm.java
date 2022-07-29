package algorithms.data_structure;

import java.util.*;

public class GeneticAlgorithm {


    int numNodes;
    int[] nodes;
    int timesOfLoop = 100;
    int population = 50;
    double retentionRate = 0.7;
    double mutationRate = 0.05;
    double crossOverRate = 0.8;

    List<Individual> pool = new ArrayList<>();


    int[][] distance_matrix;

    public GeneticAlgorithm(int[] nodes, int numNodes, int[][] distance_matrix) {
        this.nodes = new int[numNodes + 1];
        System.arraycopy(nodes, 0, this.nodes, 0, nodes.length);
        this.nodes[numNodes] = 0;
        this.numNodes = numNodes + 1;
        this.distance_matrix = distance_matrix;
    }

    public void initPopulation() {
        for (int i = 0; i < population; i++) {
            int x1 = getRandomNumber(1, this.numNodes - 1);
            int x2 = getRandomNumber(1, this.numNodes - 1);
            int tmp = nodes[x1];
            nodes[x1] = nodes[x2];
            nodes[x2] = tmp;
            int fitness = calcFitness(nodes);
            int[] result = new int[numNodes];
            for (int j = 1; j < numNodes; j++) {
                result[j] = nodes[j];
            }
            pool.add(new Individual(result, fitness));
        }
    }

    public List<Individual> takeMutation(List<Individual> offSprings) {
        for (Individual individual : offSprings) {
            if ((double) getRandomNumber(1, 100) / 100 < mutationRate) {
                int x1 = getRandomNumber(1, numNodes - 1);
                int x2 = getRandomNumber(1, numNodes - 1);
                int tmp = individual.getGnome()[x1];
                individual.getGnome()[x1] = individual.getGnome()[x2];
                individual.getGnome()[x2] = tmp;
            }
        }
        offSprings.sort(Comparator.comparingInt(t -> t.fitness));
        return offSprings;
    }

    public List<Individual> crossOver() {
        List<Individual> offSprings = new ArrayList<>();
        for (Individual individual : pool) {
            if ((double) getRandomNumber(1, 100) / 100 < crossOverRate) {
                int rand1 = getRandomNumber(1, population);
                int rand2 = getRandomNumber(1, population);
                int[] parent1 = pool.get(rand1).getGnome();
                int[] parent2 = pool.get(rand2).getGnome();
                int crossOverPoint = getRandomNumber(1, numNodes - 1);
                int[] child1 = new int[numNodes];
                int[] child2 = new int[numNodes];
                for (int i = 1; i <= crossOverPoint; i++) {
                    child1[i] = parent2[i];
                    child2[i] = parent1[i];
                }
                int tmp = crossOverPoint;
                int tmp1 = crossOverPoint;
                int tmp2 = crossOverPoint;
                int tmp3 = crossOverPoint;
                for (int i = 1; i < numNodes - 1; i++) {
                    int k = ++tmp;
                    if (k >= numNodes - 1) {
                        k = k - numNodes + 2;
                    }
                    if (!contains(child1, parent2[k])) {
                        child1[++tmp1] = parent2[k];
                    }
                }
                for (int i = 1; i < numNodes - 1; i++) {
                    int k = ++tmp3;
                    if (k >= numNodes - 1) {
                        k = k - numNodes + 2;
                    }
                    if (!contains(child2, parent1[k])) {
                        child2[++tmp2] = parent1[k];

                    }
                }

                Individual offSpring1 = new Individual(child1, calcFitness(child1));
                Individual offSpring2 = new Individual(child2, calcFitness(child2));

                offSprings.add(offSpring1);
                offSprings.add(offSpring2);
            }
        }
        return offSprings;
    }

    public void selectParents(List<Individual> offSprings) {
        pool.addAll(offSprings);
        pool.sort(Comparator.comparingInt(t -> t.fitness));
        pool = pool.subList(0, population);
    }

    public int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public int[] findTSPBYGA() {
        initPopulation();
        int best = 100000;
        for (int i = 0; i < timesOfLoop; i++) {
//            System.out.println("gen:" + " " + i);
            List<Individual> offSprings = crossOver();
            takeMutation(offSprings);
            selectParents(offSprings);
//            if (pool.get(0).fitness < best) {
            best = pool.get(0).fitness;
            System.out.println(best);
            System.out.println(Arrays.toString(pool.get(0).gnome));
            System.out.println("------------------------------------------------------------------------");
//            }
        }
        System.out.println("_______________finish_________________");

        return pool.get(0).gnome;
    }

    public int calcFitness(int[] genome) {
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

    private boolean contains(int[] array, int element) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == element) {
                return true;
            }
        }
        return false;
    }
}
