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
        final double SCALE_DELTA = 1.1;
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

                    //Slider sl =(Slider) scene.lookup("#zoomSlider");

                    StackPane sp = (StackPane) scene.lookup("#treeroot");
                    sp.getChildren().add(treeGroup);
                    //sp.translateYProperty().bind(sl.valueProperty());

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

}