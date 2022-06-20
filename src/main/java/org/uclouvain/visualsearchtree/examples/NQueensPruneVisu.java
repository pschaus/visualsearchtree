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
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.uclouvain.visualsearchtree.tree.NodeAction;
import org.uclouvain.visualsearchtree.tree.Tree2;
import org.uclouvain.visualsearchtree.tree.TreePane2;

/**
 * Example that illustrates how to solve the NQueens
 * problem with a chronological backtracking algorithm
 * that backtracks as soon as a decision conflicts with
 * the previous decisions.
 */
public class NQueensPruneVisu extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {


        NQueensPrune nqueens = new NQueensPrune(10);
        Tree2 t = new Tree2(-1);

        nqueens.dfs(new DFSListener() {
            @Override
            public void solution(int pId, int id) {
                t.createNode(pId,id, Tree2.NodeType.SOLUTION,() -> {});
                DFSListener.super.solution(pId, id);
            }

            @Override
            public void fail(int pId, int id) {
                t.createNode(pId,id, Tree2.NodeType.FAIL,() -> {});
                DFSListener.super.fail(pId, id);
            }

            @Override
            public void branch(int pId, int id, int nChilds) {
                t.createNode(pId,id, Tree2.NodeType.INNER,() -> {});
                DFSListener.super.branch(pId, id, nChilds);
            }

        });



        Tree2.Node n = t.root();


        Scene scene = new Scene(new TreePane2(n), 500, 600);
        primaryStage.setScene(scene);


        primaryStage.show();

    }
}