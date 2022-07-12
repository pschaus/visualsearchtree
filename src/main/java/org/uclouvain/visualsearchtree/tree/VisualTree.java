package org.uclouvain.visualsearchtree.tree;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class VisualTree {
    public static void treeProfilerLauncher(Tree.Node<String> node, Stage primaryStage) {
        TreeVisual instance = new TreeVisual(node);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here.
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TreeUI.fxml"));
                    Parent root = fxmlLoader.load();
                    TreeUIController treeController = fxmlLoader.getController();
                    treeController.setInstance(instance);

                    Group treeGroup = instance.getGroup();
                    Scene scene = new Scene(root, 500, 700);

                    Stage outputStage = new Stage();

                    outputStage.initOwner(primaryStage);
                    outputStage.setScene(scene);
                    outputStage.show();

                    StackPane sp = (StackPane) scene.lookup("#treeroot");
                    sp.getChildren().add(treeGroup);

                    AnimationFactory.zoomOnSCroll(sp);

                    VBox legendbox = (VBox) scene.lookup("#legendbox");
                    legendbox.getChildren().add(instance.generateLegendsStack());
                    treeController.init();

                    /** TEST GRAPH **/
                    final NumberAxis xAxis = new NumberAxis();
                    final NumberAxis yAxis = new NumberAxis();
                    yAxis.setLabel("Node Cost");
                    xAxis.setLabel("Number of Solution");
                    //creating the chart
                    final LineChart<Number,Number> lineChart = TreeVisual.getTreeChart(node);

                    VBox chartbox = (VBox) scene.lookup("#chartUI");
                    chartbox.getChildren().add(lineChart);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

}