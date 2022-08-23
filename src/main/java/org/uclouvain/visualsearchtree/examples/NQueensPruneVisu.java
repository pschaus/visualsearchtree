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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.uclouvain.visualsearchtree.tree.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Example that illustrates how to solve the NQueens
 * problem with a chronological backtracking algorithm
 * that backtracks as soon as a decision conflicts with
 * the previous decisions.
 */
public class NQueensPruneVisu {

    public static void main(String[] args) {

        Platform.startup(() -> {
            NQueensPrune nqueens = new NQueensPrune(4);
            Tree t = new Tree(-1);
            TreeVisual tv = new TreeVisual();
            Gson gson = new Gson();

            tv.setRealtimeItv(1000);
            tv.setRealtimeNbNodeDrawer(5);
            // TEST: TO SIMULATE OPTIMIZATION GRAPH
            VisualTree.treeProfilerLauncher(tv);

            Thread t2 = new Thread(() -> nqueens.dfs(new DFSListener() {
                @Override
                public void solution(int id, int pId) {
                    System.out.println("solution");
                    String info = "{\"cost\": "+id+", \"domain\": "+id+", \"other\": \""+ getNodeValue(nqueens.q)+"\"}";
                    TreeVisual.NodeInfoData infoData = gson.fromJson(info, new TypeToken<TreeVisual.NodeInfoData>(){}.getType());
                    tv.createNode(id,pId, Tree.NodeType.SOLUTION,() -> {showNewVisualisation(infoData,Tree.NodeType.SOLUTION);}, info);
                }
                @Override
                public void fail(int id, int pId) {
                    System.out.println("fail");
                    String info = "{\"cost\": "+id+", \"domain\": "+id+", \"other\": \""+ getNodeValue(nqueens.q)+"\"}";
                    TreeVisual.NodeInfoData infoData = gson.fromJson(info, new TypeToken<TreeVisual.NodeInfoData>(){}.getType());
                    tv.createNode(id,pId, Tree.NodeType.FAIL,() -> {showNewVisualisation(infoData,Tree.NodeType.FAIL);}, info);
                }
                @Override
                public void branch(int id, int pId, int nChilds) {
                    System.out.println("branch");
                    String info = "{\"cost\": "+id+", \"domain\": "+id+", \"other\": \""+ getNodeValue(nqueens.q)+"\"}";
                    TreeVisual.NodeInfoData infoData = gson.fromJson(info, new TypeToken<TreeVisual.NodeInfoData>(){}.getType());
                    tv.createNode(id,pId, Tree.NodeType.INNER,() -> {showNewVisualisation(infoData, Tree.NodeType.INNER);}, info);
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

    /**
     * Draw Rectangle for Chess Visualisation for Nqueens problem
     * @param isFixed boolean which indicate if a variable has its value fixed
     * @return Rectangle which represent a case
     */
    private static Rectangle createRectangleForVisualisation(boolean isFixed, Tree.NodeType type){
        Rectangle r = new Rectangle(50,50);
        Color c = (type == Tree.NodeType.FAIL)? Color.RED : (type == Tree.NodeType.SOLUTION)? Color.GREEN : Color.CORNFLOWERBLUE;
        r.setFill(isFixed ? c : Color.WHITE);
        r.setStrokeType(StrokeType.OUTSIDE);
        r.setStrokeWidth(.4);
        r.setStroke(Color.BLACK);
        return r;
    }

    /**
     * Draw a visualisation : Here a chess with fixed value of node is drawn
     * @param nodeInfoData info parse to gson object of the concerned node
     */
    public static void showNewVisualisation(TreeVisual.NodeInfoData nodeInfoData, Tree.NodeType type){
        int n = NQueensPrune.nVisu;
        Map<Integer, Integer> coordinates = new Gson().fromJson(nodeInfoData.other, new TypeToken<HashMap<Integer, Integer>>() {}.getType());
        GridPane chess = new GridPane();
        Scene chessScene = new Scene(chess, n*50 +n, n*50 +n);
        Stage chessWindow = new Stage();

        chessWindow.setTitle("Nqueens Visualisation Board");
        chessWindow.setScene(chessScene);

        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                if(coordinates.get(i) == j){
                    chess.add(createRectangleForVisualisation(true, type), j, i);
                }else{
                    chess.add(createRectangleForVisualisation(false,type), j, i);
                }
            }
        }
        chessWindow.initModality(Modality.WINDOW_MODAL);
        chessWindow.initOwner(VisualTree.pStage);
        chessWindow.setX(VisualTree.pStage.getX());
        chessWindow.setY(VisualTree.pStage.getY());

        chessWindow.show();
    }
}
