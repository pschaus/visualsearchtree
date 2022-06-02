package org.uclouvain.visualsearchtree.tree;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.uclouvain.visualsearchtree.server.VisualTreeServer;

import java.io.IOException;


public class VisualTree extends Application {
    private Tree.Node<String> node;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Draw tree");
        //VisualTreeServer server = new VisualTreeServer(6666);
        //Tree.Node<String> node  = server.getNodeTree();
        this.node  = Tree.staticTree();

        try {
            // THE SERVER DRAWING......
            Parent root = FXMLLoader.load(getClass().getResource("TreeUI.fxml"));
            Group treeGroup = TreeVisual.getGroup(this.node);
    
            Scene scene = new Scene(root, 500, 700);
            scene.setOnKeyPressed(ev ->{
                if(ev.getCode()== KeyCode.L){
                    for (int i = 0; i < TreeVisual.labels.size(); i++) {
                        var element = TreeVisual.labels.get(i);
                        element.setOpacity(element.getOpacity()==1? 0:1);

                    }
                }
            });
            primaryStage.setScene(scene);
            primaryStage.show();

            StackPane sp = (StackPane) scene.lookup("#treeroot");
            sp.getChildren().add(treeGroup);

            VBox legendbox = (VBox) scene.lookup("#legendbox");
            legendbox.getChildren().add(TreeVisual.generateLegendsStack());

        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}