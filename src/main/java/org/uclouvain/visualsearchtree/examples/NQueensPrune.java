/*
 * mini-cp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License  v3
 * as published by the Free Software Foundation.
 *
 * mini-cp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY.
 * See the GNU Lesser General Public License  for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mini-cp. If not, see http://www.gnu.org/licenses/lgpl-3.0.en.html
 *
 * Copyright (c)  2018. by Laurent Michel, Pierre Schaus, Pascal Van Hentenryck
 */

package org.uclouvain.visualsearchtree.examples;

import java.util.ArrayList;

/**
 * Example that illustrates how to solve the NQueens
 * problem with a chronological backtracking algorithm
 * that backtracks as soon as a decision conflicts with
 * the previous decisions.
 */
public class NQueensPrune {

    int [] q;
    int n = 0;
    static  int nVisu = 0;
    static int nRecur = 0;

    public NQueensPrune(int n) {
        this.n = n;
        nVisu = n;
        q = new int[n];
    }

    private int nodeId = 0;

    public void dfs(DFSListener listener) {
        nodeId = -1;
        try {
            dfs(0,listener, -1);
        } catch (RuntimeException exception) {

        }
    }

    private void dfs(int idx, DFSListener listener, int pId) {
        nRecur++;
        int id = ++nodeId;
        if (idx == n) {
            listener.solution(id, pId);
            // throw new RuntimeException("solution found");
        } else {
            listener.branch(id, pId, n);
            for (int i = 0; i < n; i++) {
                q[idx] = i;
                if (constraintsSatisfied(idx)) {
                    dfs(idx + 1, listener, id);
                } else {
                    nodeId++;
                    listener.fail(nodeId, pId);
                }
            }
        }
    }

    public boolean constraintsSatisfied(int j) {
        for (int i = 0; i < j; i++) {
            // no two queens on the same row
            if (q[i] == q[j]) return false;
            // no two queens on the diagonal
            if (Math.abs(q[j] - q[i]) == j - i) {
                return false;
            }
        }
        return true;
    }

    public static void solve(int n) {
        NQueensPrune q = new NQueensPrune(n);
        ArrayList<int []> solutions = new ArrayList<>();
        // collect all the solutions
        long t0 = System.currentTimeMillis();
        //q.dfs(0, solution -> solutions.add(solution));
        long t1 = System.currentTimeMillis();
        System.out.println("# solutions: " + solutions.size());
        System.out.println("# recurs: " + nRecur);
        System.out.println("# time(ms): " + (t1-t0));
    }

    public static void main(String[] args) {
        solve(5);
    }
}
