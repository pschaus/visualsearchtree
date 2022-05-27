package org.uclouvain.visualsearchtree;


import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Stack;


public class VisualTree extends Application {


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Draw tree - first test");

        //TODO: NEXT STEP BUILD TREE FROM SERVER NOT FOR Tree.staticTree
        VisualTreeServer server = new VisualTreeServer(6666);
        Tree.Node<String> node = server.getNodeTree();

        //Tree.Node<String> node = Tree.randomTree();
        //Tree.Node<String> node = Tree.staticTree();
        StackPane sp = new StackPane();
        AnimationFactory.zoomOnSCroll(sp);
        Group treeGroup = TreeVisual.getGroup(node);
        sp.getChildren().add(treeGroup);
        sp.setAlignment(treeGroup, Pos.CENTER);

        Scene scene = new Scene(sp, 500, 600);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}