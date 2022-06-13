package org.uclouvain.visualsearchtree.tree;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class VisualTree {
    public static void treeProfilerLauncher(Tree.Node<String> node, Stage primaryStage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here.
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("TreeUI.fxml"));
                    Group treeGroup = TreeVisual.getGroup(node);
                    Scene scene = new Scene(root, 500, 700);

                    Label label = new Label();
                    label.setAlignment(Pos.CENTER);
                    label.setFont(Font.font("roboto", 13));
                    label.setStyle("-fx-border-style: solid outside;"
                            + "-fx-border-width: 0.1;"
                            + "-fx-border-radius: 4;"
                            + "-fx-border-color: black;");
                    label.setWrapText(true);
                    label.setPadding(new Insets(2.0));
                    label.setMaxWidth(500);


                    scene.setOnKeyPressed(ev ->{
                        if(ev.getCode()== KeyCode.L){
                            for (int i = 0; i < TreeVisual.labels.size(); i++) {
                                var element = TreeVisual.labels.get(i);
                                element.setOpacity(element.getOpacity()==1? 0:1);
                            }
                        }
                        if(ev.getCode()== KeyCode.I){
                            if(!TreeVisual.info.isEmpty()){
                                label.setText(TreeVisual.info);
                            }
                            else{
                                label.setText("No node selected");
                            }
                        }
                    });

                    Stage outputStage = new Stage();

                    outputStage.initOwner(primaryStage);
                    outputStage.setScene(scene);
                    outputStage.show();

                    StackPane sp = (StackPane) scene.lookup("#treeroot");
                    sp.getChildren().add(treeGroup);

                    VBox legendbox = (VBox) scene.lookup("#legendbox");
                    legendbox.getChildren().add(TreeVisual.generateLegendsStack());

//                    VBox infoBox = (VBox) scene.lookup("#infobox");
//                    infoBox.getChildren().add(label);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}