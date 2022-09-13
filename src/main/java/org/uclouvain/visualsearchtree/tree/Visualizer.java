package org.uclouvain.visualsearchtree.tree;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Visualizer{

    TreeVisual tv;
    public Visualizer(TreeVisual tv){this.tv = tv;}

    public static void show(TreeVisual tv) throws Exception {

        Platform.runLater(()->{
            Parent root = null;
            try {
                root = FXMLLoader.load((Objects.requireNonNull(Visualizer.class.getResource("TreeUI.fxml"))));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage = new Stage();
            Scene scene = new Scene(tv.getTreeStackPane(), 500, 700);
            stage.setScene(scene);
            stage.setTitle("miniCp Profiler");
            stage.show();
        });
    }
}
