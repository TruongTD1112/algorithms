//package com.example.demo.algorithms.data_structure;
//
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//public class Warehouse {
//
//    private int width;
//    private int height;
//
//    public Warehouse(int width, int height) {
//        this.width = width;
//        this.height = height;
//    }
//
//    public int[][] initGridLayout() {
//        int[][] grid = new int[width][height];
//        for (int i = 0; i < width; i++) {
//            boolean check = false;
//            for (int j = 0; j < height; j++) {
//                if (i == 0 || i == width - 1) {
//                    grid[i][j] = 0;
//                    continue;
//                }
//                if (j == 0 || j == height - 1) {
//                    grid[i][j] = 0;
//                    continue;
//                }
//                if (j % 2 == 1) {
//                    if (j + 1 < height - 1) {
//                        grid[i][j] = 1;
//                        grid[i][j + 1] = 1;
//                        j += 2;
//                        continue;
//                    }
//                }
//            }
//
//        }
//        return grid;
//    }
//
//    public static void main(String[] args) {
//        Warehouse warehouse = new Warehouse(16, 16);
//        int[][] grid = warehouse.initGridLayout();
//        for (int i = 0; i < 16; i++) {
//            for (int j = 0; j < 16; j++) {
//                System.out.print(grid[i][j] + " ");
//            }
//            System.out.println();
//        }
//    }
//}
