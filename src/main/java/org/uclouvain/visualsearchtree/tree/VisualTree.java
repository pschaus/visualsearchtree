package org.uclouvain.visualsearchtree.tree;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
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

import java.io.IOException;


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
                    primaryStage.setAlwaysOnTop(true);
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

                    /** GRAPH **/
                    //creating the chart
                    final LineChart<Number,Number> lineChart = instance.getTreeChart(true);
                    VBox chart = (VBox) scene.lookup("#chartUI");
                    chart.getChildren().add(lineChart);

                    instance.addEventOnChart();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     *
     * @param instance
     */
    public static void treeProfilerLaucher(TreeVisual instance)
    {
        //TreeVisual instance = new TreeVisual();
        Platform.runLater(new Runnable() {
            /**
             *
             */
            @Override
            public void run() {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TreeUI.fxml"));
                Parent root = null;
                try {
                    root = fxmlLoader.load();
                    TreeUIController treeController = fxmlLoader.getController();
                    treeController.setInstance(instance);

                    Scene scene = new Scene(root, 500, 700);
                    StackPane p = instance.getTreeStackPane();

                    Stage outputStage = new Stage();

                    outputStage.setScene(scene);
                    outputStage.show();


                    StackPane sp = (StackPane) scene.lookup("#treeroot");
                    sp.getChildren().add(p);

                    AnimationFactory.zoomOnSCroll(sp);

                    VBox legendbox = (VBox) scene.lookup("#legendbox");
                    legendbox.getChildren().add(instance.generateLegendsStack());
                    treeController.init();

                    /** GRAPH **/
                    //creating the chart
                    VBox chart = (VBox) scene.lookup("#chartUI");
                    chart.getChildren().add(instance.getTreeChart(true));
                    instance.addEventOnChart();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}