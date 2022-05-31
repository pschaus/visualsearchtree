package org.uclouvain.visualsearchtree;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class VisualSearchTree extends Application {
    private String[] flagTitles = {"United States of America", "Canada", "China",
            "Denmark", "France", "Germany", "India"};

    @Override
    public void start(Stage primaryStage) {

        ListView<String> lv = new ListView<>(FXCollections.observableArrayList(flagTitles));
        lv.setPrefSize(270, 320);
        lv.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Create a pane to hold image views
        FlowPane imagePane = new FlowPane(10, 10);
        BorderPane pane = new BorderPane();
        pane.setLeft(new ScrollPane(lv));



        pane.setCenter(imagePane);

//        Button btAdd = new Button("Add");
//        imagePane.setce getCenter(btAdd);

        lv.getSelectionModel().selectedItemProperty().addListener(
        ov -> {
            /*imagePane.getChildren().clear();
            for (Integer i: lv.getSelectionModel().getSelectedIndices()) {
                imagePane.getChildren().add(ImageViews[i]);
            }*/
        });




        //StackPane pane = new StackPane();
//        pane.getChildren().add(new Button("OK"));

        // Create a circle and set its properties
        /*Circle circle = new Circle();
        circle.centerXProperty().bind(pane.widthProperty().divide(2));
        circle.centerYProperty().bind(pane.heightProperty().divide(2));
        circle.setRadius(50);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.WHITE);
        pane.getChildren().add(circle);*/

        Scene scene = new Scene(pane, 270, 350);
        primaryStage.setTitle("VisualSearchTree");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
