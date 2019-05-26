package com.tokopedia.testproject.problems.algorithm.continousarea;

/**
 * Created by hendry on 18/01/19.
 */
public class Solution {

    static int count;

    public static int maxContinuousArea(int[][] matrix) {
        // TODO, return the largest continuous area containing the same integer, given the 2D array with integers
        // below is stub
        int max_num = 0;

        for (int i=0; i<matrix.length; i++) {
            for (int j=0; j<matrix[i].length; j++) {
                if (matrix[i][j] > max_num) {
                    max_num = matrix[i][j];
                }
            }
        }

        int max = 0;
        int result = 0;
        for (int i=0; i<=max_num; i++) {
            result = largestRegion(matrix, i);
            if (result > max) {
                max = result;
            }
        }

        return max;
    }

    public static int largestRegion(int [][] matrix, int target) {
        int MAX_ROW = matrix.length;
        int MAX_COL = matrix[0].length;

        boolean[][] visited = new boolean[MAX_ROW][MAX_COL];

        int result = 0;
        for (int i = 0; i < MAX_ROW; i++)  {
            for (int j = 0; j < MAX_COL; j++)  {
                if (matrix[i][j] == target && !visited[i][j])  {
                    count = 1;
                    DepthSearch(matrix, i, j, visited, target);

                    result = Math.max(result, count);
                }
            }
        }

        return result;
    }

    public static void DepthSearch(int[][] matrix, int row,
                                   int col, boolean[][] visited, int target) {
        int[] rowNbr = {-1, 0, 0, 1};
        int[] colNbr = { 0, -1, 1, 0};

        visited[row][col] = true;

        for (int k = 0; k < 4; k++)  {
            if (isSafe(matrix, row + rowNbr[k], col + colNbr[k], visited, target)) {
                count++;
                DepthSearch(matrix, row + rowNbr[k], col + colNbr[k], visited, target);
            }
        }
    }

    public static boolean isSafe(int[][] matrix, int row,
                                 int col, boolean[][] visited, int target) {
        int MAX_ROW = matrix.length;
        int MAX_COL = matrix[0].length;

        return ((row >= 0) && (row < MAX_ROW) && (col >= 0)
                && (col < MAX_COL) && (matrix[row][col] == target &&
                !visited[row][col]));
    }

}
