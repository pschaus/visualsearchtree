package org.uclouvain.visualsearchtree.tree;

import javafx.application.Platform;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

@ExtendWith(ApplicationExtension.class)
class TreeVisualTest {

    static TreeVisual tv;

    @BeforeAll
    public static void before_all(){
        tv = new TreeVisual();
        randomTree(tv);
        Visualizer.show(tv);
    }

    @AfterAll
    static void check_all_is_ok(){
        Assertions.assertEquals(0, tv.getLabels().size());
        Assertions.assertEquals(0, tv.getAllNodesPositions().size());
        Assertions.assertEquals(0, tv.getAllNodesChartDatas().size());
        Assertions.assertEquals(0, tv.getAllNodesRects().size());
        Assertions.assertEquals(0, tv.getLegendStats().get(0));
        Assertions.assertEquals(0, tv.getLegendStats().get(1));
        Assertions.assertEquals(0, tv.getLegendStats().get(2));
        Assertions.assertEquals(0, tv.getLegendStats().get(3));
    }

    @Test
    void resetAllBeforeRedraw() {
        Assertions.assertEquals(7, tv.getLabels().size());
        Assertions.assertEquals(6, tv.getAllNodesPositions().size());
        Assertions.assertEquals(1, tv.getAllNodesChartDatas().size());
        Assertions.assertEquals(6, tv.getAllNodesRects().size());
        Assertions.assertEquals(3, tv.getLegendStats().get(0));
        Assertions.assertEquals(3, tv.getLegendStats().get(1));
        Assertions.assertEquals(1, tv.getLegendStats().get(2));
        Assertions.assertEquals(2, tv.getLegendStats().get(3));
        Platform.runLater(()->{
            if (tv.getTreeStackPane().getChildren().size() >  0)
            {
                tv.getTreeStackPane().getChildren().remove(0);
            }
            tv.resetAllBeforeRedraw();
        });
    }

    private static void randomTree(TreeVisual tv){
        tv.createNode(0,-1, Tree.NodeType.INNER,()->{System.out.println(0);},"{\"cost\": "+0+", \"domain\": "+0+", \"other\": test}");
        tv.createNode(1,0, Tree.NodeType.FAIL,()->{System.out.println(1);},"{\"cost\": "+1+", \"domain\": "+1+", \"other\": test}");
        tv.createNode(2,0, Tree.NodeType.FAIL,()->{System.out.println(2);},"{\"cost\": "+2+", \"domain\": "+2+", \"other\": test}");
        tv.createNode(3,-1, Tree.NodeType.INNER,()->{System.out.println(3);},"{\"cost\": "+3+", \"domain\": "+3+", \"other\": test}");
        tv.createNode(4,3, Tree.NodeType.SOLUTION,()->{System.out.println(4);},"{\"cost\": "+4+", \"domain\": "+4+", \"other\": test}");
        tv.createNode(5,3, Tree.NodeType.FAIL,()->{System.out.println(5);},"{\"cost\": "+5+", \"domain\": "+5+", \"other\": test}");
    }
}