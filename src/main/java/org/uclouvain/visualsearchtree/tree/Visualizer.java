package org.uclouvain.visualsearchtree.tree;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Visualizer{

    TreeVisual tv;
    public Visualizer(TreeVisual tv){this.tv = tv;}

    public static void show(TreeVisual tv){
        Platform.runLater(()->{
            Parent root = null;
            FXMLLoader fxmlLoader;
            try {
                fxmlLoader = new FXMLLoader(Objects.requireNonNull(Visualizer.class.getResource("TreeUI.fxml")));
                root = fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Init Controller
            TreeUIController treeController = fxmlLoader.getController();
            treeController.setInstance(tv);

            Stage stage = new Stage();
            Scene scene = new Scene(root, 500, 700);
            stage.setScene(scene);
            stage.setTitle("miniCp Profiler");
            stage.show();

            StackPane sp = (StackPane) scene.lookup("#treeroot");
            sp.getChildren().add(tv.getTreeStackPane());

            AnimationFactory.zoomOnSCroll(sp);


            tv.onDrawFinished(()->{
                VBox legendbox = (VBox) scene.lookup("#legendbox");
                legendbox.getChildren().add(tv.generateLegendsStack());
            });
            treeController.init();
        });
    }
}
