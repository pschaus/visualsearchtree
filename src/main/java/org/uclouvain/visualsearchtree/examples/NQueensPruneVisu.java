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

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.uclouvain.visualsearchtree.tree.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Example that illustrates how to solve the NQueens
 * problem with a chronological backtracking algorithm
 * that backtracks as soon as a decision conflicts with
 * the previous decisions.
 */
public class NQueensPruneVisu {

    public static void main(String[] args) {

        Platform.startup(() -> {
            NQueensPrune nqueens = new NQueensPrune(7);
            Tree t = new Tree(-1);
            TreeVisual tv = new TreeVisual();

            tv.setRealtimeItv(1000);
            tv.setRealtimeNbNodeDrawer(5);
            // TEST: TO SIMULATE OPTIMIZATION GRAPH
            VisualTree.treeProfilerLaucher(tv);

            Thread t2 = new Thread(() -> nqueens.dfs(new DFSListener() {
                @Override
                public void solution(int id, int pId) {
                    System.out.println("solution");
                    tv.createNode(id,pId, Tree.NodeType.SOLUTION,(nodeInfoData) -> {}, "{\"cost\": "+id+", \"param1\": "+id+", \"other\": \""+ getNodeValue(nqueens.q)+"\"}");
                }
                @Override
                public void fail(int id, int pId) {
                    System.out.println("fail");
                    tv.createNode(id,pId, Tree.NodeType.FAIL,(nodeInfoData) -> {}, "{\"cost\": "+id+", \"param1\": "+id+", \"other\": \""+ getNodeValue(nqueens.q)+"\"}");
                }
                @Override
                public void branch(int id, int pId, int nChilds) {
                    System.out.println("branch");
                    tv.createNode(id,pId, Tree.NodeType.INNER,(nodeInfoData) -> {}, "{\"cost\": "+id+", \"param1\": "+id+", \"other\": \""+ getNodeValue(nqueens.q)+"\"}");
                }
            }));
            t2.start();
        });
    }

    /**
     * Smple function to return the node value as string during the search
     * tab of "index" : value (0: q[0] | 1: q[1] |... n: q[n])
     * @param q: int tab
     * @return : String value
     */
    public static String getNodeValue(int[] q){
        StringBuilder value = new StringBuilder("{");
        for (int i = 0; i < q.length; i++) {
            value.append(i).append(":").append(q[i]);
            if (i != (q.length - 1))
                value.append(",");
        }
        value.append("}");
        return value.toString();
    }
}
