package org.uclouvain.visualsearchtree.tree;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
                    scene.setOnKeyPressed(ev ->{
                        if(ev.getCode()== KeyCode.L){
                            for (int i = 0; i < TreeVisual.getLabels().size(); i++) {
                                var element = TreeVisual.getLabels().get(i);
                                element.setOpacity(element.getOpacity()==1? 0:1);

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
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}