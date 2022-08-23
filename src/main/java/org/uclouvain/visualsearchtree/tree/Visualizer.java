package org.uclouvain.visualsearchtree.tree;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Visualizer {

    public static void show(TreeVisual treeVisual)
    {
        if (Platform.isFxApplicationThread()){
            VisualTree.treeProfilerLauncher(treeVisual);
        }
        else
        {
            Platform.runLater(()->{
                VisualTree.treeProfilerLauncher(treeVisual);
            });
        }
    }

}
