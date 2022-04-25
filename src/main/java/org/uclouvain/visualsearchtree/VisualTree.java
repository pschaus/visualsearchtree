package org.uclouvain.visualsearchtree;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;




public class VisualTree extends Application {


    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Connected Shapes");

        Tree.Node<String> node = Tree.randomTree();

        Pane sp = new Pane();
        AnimationFactory.zoomOnSCroll(sp);

        sp.getChildren().add(TreeVisual.getGroup(node));

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