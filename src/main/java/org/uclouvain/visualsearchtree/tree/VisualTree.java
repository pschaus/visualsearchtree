package org.uclouvain.visualsearchtree.tree;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * <h1>Complete Visualization Object Class</h1>
 * <p>
 *     VisualTree is a Graphic Component that hel the user to visualize the look of it search Algorithm
 *     and interact with it of need. It allow the user to perfrom the following action:
 * </p>
 * <ul>
 *     <li>
 *         <b>Show Tree</b>: [{@link org.uclouvain.visualsearchtree.tree.VisualTree:treeProfilerLaucher() treeProfilerLaucher}] Show the research tree, in real time or not, it can be display lonely
 *         or with all other features.
 *     </li>
 *     <li>
 *         <b>Show optimization graph</b>: Display the optimization graph is the research Algorithm is about optimization.
 *         , it can aolso bedisplay with other features.
 *     </li>
 *     <li>
 *         <b>Show Legend</b>: display the number of solution or fail of all Nodes after the research.
 *     </li>
 *     <li>
 *         <b>Add bookMark</b>: to set a reference point in search tree node for after.
 *     </li>
 * </ul>
 */
public class VisualTree {
    public static Stage pStage = new Stage();

    /**
     * <p>
     *     It render new screen instance of {@link org.uclouvain.visualsearchtree.tree.VisualTree VisualTree}
     * </p>
     * @param node
     * @param primaryStage
     * @see #treeProfilerLauncher(TreeVisual)
     */
    public static void treeProfilerLauncher(Tree.Node<String> node, Stage primaryStage) {
        TreeVisual instance = new TreeVisual(node);
        pStage = primaryStage;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here.
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TreeUI.fxml"));
                    Parent root = fxmlLoader.load();
                    TreeUIController treeController = fxmlLoader.getController();
                    treeController.setInstance(instance);

                    Group treeGroup = instance.getGroup();
                    primaryStage.setAlwaysOnTop(true);
                    Scene scene = new Scene(root, 500, 700);

                    Stage outputStage = new Stage();
                    outputStage.initOwner(primaryStage);
                    outputStage.setScene(scene);
                    outputStage.show();

                    StackPane sp = (StackPane) scene.lookup("#treeroot");
                    sp.getChildren().add(treeGroup);

                    AnimationFactory.zoomOnSCroll(sp);

                    VBox legendbox = (VBox) scene.lookup("#legendbox");
                    legendbox.getChildren().add(instance.generateLegendsStack());
                    treeController.init();

                    /** GRAPH **/
                    //creating the chart
                    final LineChart<Number,Number> lineChart = instance.getTreeChart(true);
                    VBox chart = (VBox) scene.lookup("#chartUI");
                    chart.getChildren().add(lineChart);

                    instance.addEventOnChart();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * <p>
     *     It render new screen instance of {@link org.uclouvain.visualsearchtree.tree.VisualTree VisualTree}
     * </p>
     * @param instance TreeVisual instance
     */
    public static void treeProfilerLauncher(TreeVisual instance)
    {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TreeUI.fxml"));
                Parent root = null;
                try {
                    root = fxmlLoader.load();
                    TreeUIController treeController = fxmlLoader.getController();
                    treeController.setInstance(instance);

                    Scene scene = new Scene(root, 500, 700);
                    StackPane p = instance.getTreeStackPane();

                    Stage outputStage = new Stage();

                    outputStage.setScene(scene);
                    outputStage.show();

                    StackPane sp = (StackPane) scene.lookup("#treeroot");
                    sp.getChildren().add(p);

                    AnimationFactory.zoomOnSCroll(sp);

                    VBox legendbox = (VBox) scene.lookup("#legendbox");
                    legendbox.getChildren().add(instance.generateLegendsStack());
                    treeController.init();

                    /** GRAPH **/
                    //creating the chart
                    VBox chart = (VBox) scene.lookup("#chartUI");
                    chart.getChildren().add(instance.getTreeChart(true));
                    instance.addEventOnChart();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}