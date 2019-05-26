package com.tokopedia.testproject.problems.algorithm.waterJug;

public class Solution {

    public static int minimalPourWaterJug(int jug1, int jug2, int target) {
        // TODO, return the smallest number of POUR action to do the water jug problem
        // below is stub, replace with your implementation!
        if (jug1 > jug2) {
            int temp = jug1;
            jug1 = jug2;
            jug2 = temp;
        }

        if (target > jug2) {
            return -1;
        }

        if ((target % gcd(jug1, jug2)) != 0) {
            return -1;
        }

        return Math.min(pour(jug2,jug1,target), pour(jug1,jug2,target));
    }

    public static int pour(int jug1, int jug2, int target) {
        int from = jug1;
        int to = 0;

        int step = 1;

        while (from != target && to != target) {
            int temp = Math.min(from, jug2 - to);

            to += temp;
            from -= temp;

            step++;

            if (from == target || to == target) {
                break;
            }

            if (from == 0) {
                from = jug1;
            }

            if (to == jug2) {
                to = 0;
            }
        }

        return step-1;
    }

    public static int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }

        return gcd(b, a%b);
    }

}

