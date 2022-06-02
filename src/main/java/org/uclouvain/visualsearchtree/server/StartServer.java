package org.uclouvain.visualsearchtree.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class StartServer extends Application {
    private int currentAvailablePort = 6666;
    private MultiThreadServer server;
    private static boolean portIsAvailable(int port) {
        try (Socket ignored = new Socket("localhost", port)) {
            return false;
        } catch (IOException ignored) {
            return true;
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        while(!portIsAvailable(currentAvailablePort)) {
            currentAvailablePort++;
        }

        try {
            // THREAD1: SERVER BACKEND
             Runnable r = () -> {
                 server = new MultiThreadServer(currentAvailablePort);
                 /*while (true) {
                     System.out.println("yeah");
                     System.out.println(server);
                 }*/
             };
            new Thread(r).start();
            

            // THREAD2: SERVER UI
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ServerUI.fxml"));
            Parent serverui = loader.load();
            
            Scene scene = new Scene(serverui, 300, 400);
            primaryStage.setTitle("miniCp Profiler");
            primaryStage.setScene(scene);
            Label portLabel = (Label) scene.lookup("#portLabel");
            
            //TODO: Make it dynamic - DONE(✅️)
            portLabel.setText(portLabel.getText() + currentAvailablePort);
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
