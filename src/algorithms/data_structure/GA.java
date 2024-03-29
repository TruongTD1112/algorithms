package com.example.demo.algorithms.data_structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GA {
    Random rd = new Random();
    Population population;
    double pOfMutation; // ngưỡng xác suất để lai ghép hoặc đột biến
    int ITERATIONs;
    IndividualV2 bestSolution;
    int timeReset = 300;

    public GA(int sizePopulation, int iterations, double pOfMutation) {
        population = new Population(sizePopulation);
        ITERATIONs = iterations;
        this.pOfMutation = pOfMutation;
    }

    public List<Integer> run(int nN) { //nN số lần thực hiện lai ghép và đột biến
        population.init();
        //bestSolution = population.getBestIndividual(); //lấy ngẫu nhiên một cá thể coi như tốt nhất
        bestSolution = population.getIndividuals().get(0);
        int changeBest = 0; // Nếu sau timeReset thế hệ mà bestSolution không đổi thì khởi tạo lại quần thể

        for (int iter = 0; iter < ITERATIONs; iter++) {
            List<IndividualV2> individuals = population.getIndividuals();
            ArrayList<IndividualV2> children = new ArrayList<>();

            for (int i = 0; i < nN; i++) {
                IndividualV2 a = individuals.get(rd.nextInt(individuals.size()));
                IndividualV2 b = individuals.get(rd.nextInt(individuals.size()));

                while (a == b) { // == so sánh địa chỉ object trên memory
                    b = individuals.get(rd.nextInt(individuals.size()));
                }

                double p = rd.nextDouble();
                if (p > pOfMutation) {
                    children.addAll(crossOver(a, b));
                } else {
                    IndividualV2 ia = mutation(a);
                    IndividualV2 ib = mutation(b);
                    children.add(ia);
                    children.add(ib);
                }
            }

            // nếu muốn thực hiện hàm selection3() thì phải tách riêng các cá thể con mới tạo ra
            //Individual bestInter = selection3(children);

            population.add(children);
            IndividualV2 bestInter = selection(); // thực hiện chọn lọc

            if (bestInter.getFitness() < bestSolution.getFitness()) {
                bestSolution = bestInter;
                changeBest = 0;
            }

            changeBest++;
            if (changeBest > timeReset) {
                population.init();
                changeBest = 0;
            }

            System.out.println("Thế hệ: " + (iter + 1));
            System.out.println("Best Fitness: " + bestSolution.getFitness());
            System.out.println("Gen: " + bestSolution.getGene());
            System.out.println("--------------------------------------------------------");
        }

        List<Integer> result = new ArrayList<>();
        List<Integer> best = population.getBestIndividual().getGene();
        int pivot = 0;
        for (int i= 0; i< best.size(); i++) {
            if (best.get(i) == 0) {
                pivot = i;
                break;
            }
        }
        result.addAll(best.subList(pivot, best.size()));
        result.addAll(best.subList(0, pivot));
        return result;
    }

    // Cross over ------------------------------------------------------------------------------------------------------
    ArrayList<IndividualV2> crossOver(IndividualV2 a, IndividualV2 b) {
        ArrayList<IndividualV2> children = new ArrayList<>();
        ArrayList<Integer> ca = new ArrayList<>();
        ArrayList<Integer> cb = new ArrayList<>();
        //chọn một điểm cắt trên gen
        /*
        int t = rd.nextInt(a.getGene().size()-1);
        for(int i=0; i<t; i++){
            ca.add(a.getGene().get(i));
            cb.add(b.getGene().get(i));
        }

        for(int i=t; i<a.getGene().size(); i++){
            ca.add(b.getGene().get(i));
            cb.add(a.getGene().get(i));
        }
        */

        // chọn hai điểm cắt
        int m1, m2;
        do {
            m1 = rd.nextInt(a.getGene().size());
            m2 = rd.nextInt(a.getGene().size());
        } while (m1 > m2 || m1 == m2);

        for (int i = 0; i < m1; i++) {
            ca.add(b.getGene().get(i));
            cb.add(a.getGene().get(i));
        }

        for (int i = m1; i <= m2; i++) {
            ca.add(a.getGene().get(i));
            cb.add(b.getGene().get(i));
        }

        for (int i = m2 + 1; i < a.getGene().size(); i++) {
            ca.add(b.getGene().get(i));
            cb.add(a.getGene().get(i));
        }

        if (!checkIndividualValid(ca)) {
            makeIndividualValid(ca);
        }

        if (!checkIndividualValid(cb)) {
            makeIndividualValid(cb);
        }

        IndividualV2 ind1 = new IndividualV2(ca);
        IndividualV2 ind2 = new IndividualV2(cb);

        children.add(ind1);
        children.add(ind2);

        return children;
    }

    // Mutation --------------------------------------------------------------------------------------------------------
    IndividualV2 mutation(IndividualV2 a) { // đảo hai vị trí bất kì trên gen
        int t1;

        do {
            t1 = rd.nextInt(a.getGene().size());
        }
        while (t1 == 0);

        int t2;
        do {
            t2 = rd.nextInt(a.getGene().size());
        }
        while (t2 == 0 || t2 == t1);

        ArrayList<Integer> ca = new ArrayList<>(a.getGene());
        ca.set(t1, a.getGene().get(t2));
        ca.set(t2, a.getGene().get(t1));

        IndividualV2 ind = new IndividualV2(ca);
        return ind;
    }

    IndividualV2 mutation2(IndividualV2 a) { // đảo ngược một đoạn gen
        int m1, m2;
        do {
            m1 = rd.nextInt(a.getGene().size());
            m2 = rd.nextInt(a.getGene().size());
        } while (m1 > m2 || m1 == m2);

        ArrayList<Integer> ca = new ArrayList<>();
        for (int i = 0; i < m1; i++) {
            ca.add(a.getGene().get(i));
        }

        for (int i = m2; i >= m1; i--) {
            ca.add(a.getGene().get(i));
        }

        for (int i = m2 + 1; i < a.getGene().size(); i++) {
            ca.add(a.getGene().get(i));
        }

        IndividualV2 ind = new IndividualV2(ca);
        return ind;
    }

    // Selection -------------------------------------------------------------------------------------------------------
    IndividualV2 selection() { // chọn 70% tốt nhất và 20% tồi nhất trong quần thể có cả parent + children
        population.getIndividuals().sort((i1, i2) -> {
            Double di1 = i1.getFitness();
            Double di2 = i2.getFitness();
            return di1.compareTo(di2);
        });

        ArrayList<IndividualV2> newIndividuals = new ArrayList<>();
        int m = (int) (0.7 * Main.sizePopulation);

        int l = population.getIndividuals().size() - 1;
        for (int i = 0; i < m; i++) {
            newIndividuals.add(population.getIndividuals().get(i));
        }

        for (int i = 0; i < Main.sizePopulation - m; i++) {
            newIndividuals.add(population.getIndividuals().get(l - i));
        }

//        int l = population.getIndividuals().size()-1;
//        for(int i=0; i<population.sizePopulation/2; i++){
//            newIndividuals.add(population.getIndividuals().get(i));
//            newIndividuals.add(population.getIndividuals().get(l-i));
//        }

        population.setIndividuals(newIndividuals);
        return population.getIndividuals().get(0); // trả về cá thể tốt nhất trong quần thể hiện tại
    }

    IndividualV2 selection2() {
        // lựa chọn theo bánh xe roulett

        IndividualV2 bestInd = population.getBestIndividual(); // lấy cá thể tốt nhất trong quần thể gồm parent + child để trả về
        ArrayList<Double> pI = new ArrayList<>();
        double sumP = 0.0;
        for (int i = 0; i < population.getIndividuals().size(); i++) {
            sumP += population.getIndividuals().get(i).getFitness();
        }

        pI.add(population.getIndividuals().get(0).getFitness() * 1.0 / sumP);
        for (int i = 1; i < population.getIndividuals().size(); i++) {
            pI.add(pI.get(i - 1) + population.getIndividuals().get(i).getFitness() * 1.0 / sumP);
        }

        ArrayList<IndividualV2> newIndividuals = new ArrayList<>();
        for (int i = 0; i < Main.sizePopulation; i++) {
            double r = rd.nextDouble();
            for (int j = 0; j < population.getIndividuals().size(); j++) {
                if (pI.get(j) > r) {
                    newIndividuals.add(new IndividualV2(population.getIndividuals().get(j).getGene()));
                    break;
                }
            }
        }
        population.setIndividuals(newIndividuals);
        return bestInd;
    }

    IndividualV2 selection3(ArrayList<IndividualV2> children) {
        // lựa chọn 50% cá thể cũ tốt nhất và 50% cá thể mới tốt nhất
        ArrayList<IndividualV2> newIndividuals = new ArrayList<>();
        IndividualV2 bestInd = population.getBestIndividual();

        population.getIndividuals().sort((i1, i2) -> {
            Double di1 = i1.getFitness();
            Double di2 = i2.getFitness();
            return di1.compareTo(di2);
        });

        bestInd = population.getIndividuals().get(0);
        int m = (int) (0.2 * Main.sizePopulation);
        for (int i = 0; i < m; i++) {
            newIndividuals.add(population.getIndividuals().get(i));
        }

        population.setIndividuals(children);
        population.getIndividuals().sort((i1, i2) -> {
            Double di1 = i1.getFitness();
            Double di2 = i2.getFitness();
            return di1.compareTo(di2);
        });
        for (int i = 0; i < Main.sizePopulation - m; i++) {
            newIndividuals.add(population.getIndividuals().get(i));
        }

        if (bestInd.getFitness() >= population.getIndividuals().get(0).getFitness()) {
            bestInd = population.getIndividuals().get(0);
        }

        population.setIndividuals(newIndividuals);
        return bestInd;
    }

    // Check and Make individual valid ---------------------------------------------------------------------------------
    boolean checkIndividualValid(ArrayList<Integer> a) { // kiểm tra gen có trùng lặp node
        for (int i = 0; i < a.size() - 1; i++) {
            for (int j = i + 1; j < a.size(); j++) {
                if (a.get(i) == a.get(j)) {
                    return false;
                }
            }
        }

        return true;
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

}
