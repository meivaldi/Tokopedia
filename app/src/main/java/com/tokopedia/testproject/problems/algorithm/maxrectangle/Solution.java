package com.tokopedia.testproject.problems.algorithm.maxrectangle;

import java.util.Stack;

public class Solution {
    public static int maxRect(int[][] matrix) {
        // TODO, return the largest area containing 1's, given the 2D array of 0s and 1s
        // below is stub

        int row = matrix.length;
        int col = matrix[0].length;
        int result = maxReg(row,col,matrix[0]);

        for (int i = 1; i < row; i++) {
            for (int j = 0; j < col; j++)
                if (matrix[i][j] == 1) matrix[i][j] += matrix[i - 1][j];

            result = Math.max(result, maxReg(row,col,matrix[i]));
        }

        return result;
    }

    public static int maxReg(int row,int col,int matrix[])
    {
        Stack<Integer> result = new Stack<Integer>();
        int top;
        int i = 0;
        int max = 0;
        int area = 0;

        while (i < col)
        {
            if (result.empty() || matrix[result.peek()] <= matrix[i])
                result.push(i++);

            else
            {
                top = matrix[result.peek()];
                result.pop();
                area = top * i;

                if (!result.empty())
                    area = top * (i - result.peek() - 1 );
                max = Math.max(area, max);
            }
        }

        while (!result.empty())
        {
            top = matrix[result.peek()];
            result.pop();
            area = top * i;
            if (!result.empty())
                area = top * (i - result.peek() - 1 );

            max = Math.max(area, max);
        }
        return max;
    }

}

