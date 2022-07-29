package com.example.demo.algorithms.static_algorithms;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(2, 3, 7, 1,0);
        Collections.sort(integers);
        System.out.println(integers.get(0));
    }
}
