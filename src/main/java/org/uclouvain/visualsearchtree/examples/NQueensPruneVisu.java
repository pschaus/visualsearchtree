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

import java.util.Random;

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

        // TEST: TO SIMULATE OPTIMIZATION GRAPH
        Random rand = new Random();

        nqueens.dfs(new DFSListener() {
            @Override
            public void solution(int id, int pId) {
                t.createNode(id,pId, Tree.NodeType.SOLUTION,() -> {}, "{\"cost\": "+rand.nextInt(40)+", \"param1\": "+id+", \"other\": \"Some info on node\"}");
            }

            @Override
            public void fail(int id, int pId) {
                t.createNode(id,pId, Tree.NodeType.FAIL,() -> {}, "{\"cost\": "+rand.nextInt(60)+", \"param1\": "+id+", \"other\": \"Some info on node\"}");
            }

            @Override
            public void branch(int id, int pId, int nChilds) {
                t.createNode(id,pId, Tree.NodeType.INNER,() -> {}, "{\"cost\": "+rand.nextInt(30)+", \"param1\": "+id+", \"other\": \"Some info on node\"}");
            }
        });


        Tree.Node n = t.root();

        // Let use visualTree screen to visualize both
        // tree, optimization graph, node info and bookmarks
        VisualTree.treeProfilerLauncher(n, primaryStage);

        // StackPane p = new StackPane();
        // AnimationFactory.zoomOnSCroll(p);
        // TreeVisual tv = new TreeVisual(t.root());
        // p.getChildren().add(tv.getGroup());
        // Scene scene = new Scene(p, 500, 600);
        // primaryStage.setScene(scene);
        // primaryStage.show();

    }
}