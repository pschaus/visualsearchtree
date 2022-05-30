package org.uclouvain.visualsearchtree;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class StartServer extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        try {
            
            // Start server on a new thread
            // Runnable r = new Runnable() {
            //     public void run() {
            //         VisualTreeServer server = new VisualTreeServer(6666);
            //     }
            // };
            // new Thread(r).start();
            

            //LOAD Server UI
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ServerUI.fxml"));
            Parent serverui = loader.load();
            
            Scene scene = new Scene(serverui, 300, 400);
            primaryStage.setTitle("PROFILER");
            primaryStage.setScene(scene);
            
            Label portLabel = (Label) scene.lookup("#portLabel");
            
            //TODO: Make it dynamic.
            portLabel.setText(portLabel.getText() + 6666);
            primaryStage.setResizable(false);

            //Show Server UI
            primaryStage.show();
            
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        
    }

    public static void main(String[] args) {

        launch(StartServer.class, args);
    }

    
}
