package org.uclouvain.visualsearchtree.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.uclouvain.visualsearchtree.tree.Tree;
import org.uclouvain.visualsearchtree.tree.VisualTree;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

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
            // SERVER DATA
            final ProfilingData pData = new ProfilingData(new ArrayList<>(), new ArrayList<>());

            // THREAD1: SERVER BACKEND
            Runnable r = () -> {
             server = new MultiThreadServer(currentAvailablePort, pData);
            };
            new Thread(r).start();
            new  Thread(r).interrupt();


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
            ServerUIController uiController = loader.getController();

            pData.addProfilingDataListener(new ProfilingDataListener() {
                @Override
                public void newProfilingDetected(String name) {
                    uiController.addProfilingName(name);
                }

                @Override
                public void newProfilingNodeReady(Tree.Node<String> node) {
                    VisualTree.treeProfilerLauncher(node, primaryStage);
                }
            });

            //Show Server UI
            primaryStage.show();
            uiController.addProfilingDataListener(pData);

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    public static void main(String[] args) {
        launch(StartServer.class, args);
    }
}
