package com.tokopedia.testproject.problems.androidView.waterJugSimulation;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    public static List<WaterJugAction> simulateWaterJug(int jug1, int jug2, int target) {
        // TODO, simulate the smallest number of action to do the water jug problem
        // below is stub, replace with your implementation!
        List<WaterJugAction> list = new ArrayList<>();
        List<WaterJugAction> list2 = new ArrayList<>();

        minimalPourWaterJug(jug1, jug2, target, list, list2);

        return list.size() > list2.size() ? list2 : list;
    }

    public static int minimalPourWaterJug(int jug1, int jug2, int target,
                                          List<WaterJugAction> list, List<WaterJugAction> list2) {
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

        return Math.min(pourFromjug1(jug1,jug2,target, list), pourFromjug2(jug1,jug2,target, list2));
    }

    public static int pourFromjug1(int jug1, int jug2, int target, List<WaterJugAction> list) {
        int from = jug1;
        int to = 0;

        int step = 0;
        list.add(new WaterJugAction(WaterJugActionEnum.FILL, 1));

        while (from != target && to != target) {
            int temp = Math.min(from, jug2 - to);

            to += temp;
            from -= temp;
            list.add(new WaterJugAction(WaterJugActionEnum.POUR, 2));

            step++;

            if (from == target || to == target) {
                break;
            }

            if (from == 0) {
                from = jug1;
                list.add(new WaterJugAction(WaterJugActionEnum.FILL, 1));
            }

            if (to == jug2) {
                to = 0;
                list.add(new WaterJugAction(WaterJugActionEnum.EMPTY, 2));
            }
        }

        return step;
    }

    public static int pourFromjug2(int jug1, int jug2, int target, List<WaterJugAction> list) {
        int from = jug2;
        int to = 0;

        int step = 0;
        list.add(new WaterJugAction(WaterJugActionEnum.FILL, 2));

        while (from != target && to != target) {
            int temp = Math.min(from, jug1 - to);

            to += temp;
            from -= temp;
            list.add(new WaterJugAction(WaterJugActionEnum.POUR, 1));

            step++;

            if (from == target || to == target) {
                break;
            }

            if (from == 0) {
                from = jug2;
                list.add(new WaterJugAction(WaterJugActionEnum.FILL, 2));
            }

            if (to == jug1) {
                to = 0;
                list.add(new WaterJugAction(WaterJugActionEnum.EMPTY, 1));
            }
        }

        return step;
    }

    public static int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }

        return gcd(b, a%b);
    }
}


