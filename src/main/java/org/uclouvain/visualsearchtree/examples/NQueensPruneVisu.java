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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.uclouvain.visualsearchtree.tree.*;

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


        NQueensPrune nqueens = new NQueensPrune(4);
        Tree t = new Tree(-1);

        nqueens.dfs(new DFSListener() {
            @Override
            public void solution(int id, int pId) {
                t.createNode(id,pId, Tree.NodeType.SOLUTION,() -> {}, "");
            }

            @Override
            public void fail(int id, int pId) {
                t.createNode(id,pId, Tree.NodeType.FAIL,() -> {}, "");
            }

            @Override
            public void branch(int id, int pId, int nChilds) {
                t.createNode(id,pId, Tree.NodeType.INNER,() -> {}, "");
            }

        });


        Tree.Node n = t.root();


        StackPane p = new StackPane();
        AnimationFactory.zoomOnSCroll(p);

        TreeVisual tv = new TreeVisual(t.root());
        p.getChildren().add(tv.getGroup());


        Scene scene = new Scene(p, 500, 600);
        primaryStage.setScene(scene);


        primaryStage.show();

    }
}