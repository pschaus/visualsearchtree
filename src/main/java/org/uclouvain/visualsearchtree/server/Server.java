package org.uclouvain.visualsearchtree.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Server extends Application {
    /**
     * <b>Note: </b>This is the main method called to launch our server screen
     * @param stage Stage
     * @throws Exception Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Server.class.getResource("Server.fxml")));
        Scene scene = new Scene(root, 300, 300);
        stage.setScene(scene);
        stage.setTitle("VisualSearchTree - Profiler");
        stage.show();
    }
}
