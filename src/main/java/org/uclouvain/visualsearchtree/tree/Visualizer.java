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

            addTreeStackPane(scene, tv);
            addOptimizationChart(scene, tv);
            addTreeLegendBox(scene, tv);

            treeController.init();
        });
    }

    /**
     * add Legend to tree
     * @param scene Scene
     * @param tv TreeVisual
     */
    private static void addTreeLegendBox(Scene scene, TreeVisual tv)
    {
        VBox legendbox = (VBox) scene.lookup("#legendbox");
        tv.onNodeDrawn(()->{
            legendbox.getChildren().add(tv.generateLegendsStack());
        });
    }

    /**
     * add the tree to UI
     * @param scene Scene
     * @param tv TreeVisual
     */
    private static void addTreeStackPane(Scene scene, TreeVisual tv)
    {
        StackPane sp = (StackPane) scene.lookup("#treeroot");
        if (sp != null)
            sp.getChildren().add(tv.getTreeStackPane());

        AnimationFactory.zoomOnSCroll(sp);
    }

    /**
     * add
     * @param scene
     * @param tv
     */
    private static void addOptimizationChart(Scene scene, TreeVisual tv)
    {
        VBox vBox = (VBox) scene.lookup("#chartUI");
        if (vBox != null)
            vBox.getChildren().add(tv.getOptimizationChart());
    }
}
