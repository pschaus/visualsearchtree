package org.uclouvain.visualsearchtree.tree;

import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;


@ExtendWith(ApplicationExtension.class)
class TreeUIControllerTest {

    static TreeVisual tv;

    @BeforeAll
    public static void before_all(){
        tv = new TreeVisual();
        randomTree(tv);
        Visualizer.show(tv);
        System.out.println("I'm in the before all method");
    }

    @Test
    void correct_stats_for_legend_box(FxRobot robot){
        Assertions.assertThat(robot.lookup("#innerCount").queryAs(Text.class).getText()).isEqualTo("  (3)");
        Assertions.assertThat(robot.lookup("#failCount").queryAs(Text.class).getText()).isEqualTo("  (3)");
        Assertions.assertThat(robot.lookup("#solutionCount").queryAs(Text.class).getText()).isEqualTo("  (1)");
        Assertions.assertThat(robot.lookup("#treeDepth").queryAs(Text.class).getText()).isEqualTo("DEPTH : (2)");
    }

    @Test
    void node_behavior(FxRobot robot) {
        Assertions.assertThat(robot.lookup("#0").queryAs(Rectangle.class).getFill()).isEqualTo(Color.CORNFLOWERBLUE);
        Assertions.assertThat(robot.lookup("#4").queryAs(Rectangle.class).getFill()).isEqualTo(Color.GREEN);
        Assertions.assertThat(robot.lookup("#1").queryAs(Rectangle.class).getFill()).isEqualTo(Color.RED);
        robot.clickOn("#0");
        Assertions.assertThat(robot.lookup("#0").queryAs(Rectangle.class).getFill()).isEqualTo(Color.ORANGE);
        Assertions.assertThat(tv.getFocusedRect().get(3)).isEqualTo(0);
        Assertions.assertThat(tv.getFocusedRect().get(1)).isEqualTo(Tree.NodeType.INNER);
        Assertions.assertThat( ((Text) tv.getFocusedRect().get(2)).getText()).isEqualTo("child");
    }

    @Test
    void check_node_info(FxRobot robot){
        robot.clickOn("#2");
        TableView table = robot.lookup("#infoTableView").queryAs(TableView.class);
        TableColumn valueCol = (TableColumn) table.getColumns().get(1);

        robot.press(KeyCode.I);
        String cost = valueCol.getCellObservableValue(table.getItems().get(0)).getValue().toString();
        String domain = valueCol.getCellObservableValue(table.getItems().get(1)).getValue().toString();
        String other = (String) valueCol.getCellObservableValue(table.getItems().get(2)).getValue();

        Assertions.assertThat(robot.lookup("#tabPane").queryAs(TabPane.class).getSelectionModel().isSelected(1)).isTrue();
        Assertions.assertThat(cost).isEqualTo("2");
        Assertions.assertThat(domain).isEqualTo("2");
        Assertions.assertThat(other).isEqualTo("test");
    }

    @Test
    void check_node_label(FxRobot robot){
        robot.clickOn("#3");
        String node_label = ( (Text) tv.getFocusedRect().get(2)).getText();
        Assertions.assertThat(node_label).isEqualTo("child");
    }

    @Test
    void check_optimization_chart(FxRobot robot){
        robot.press(KeyCode.O);
        Assertions.assertThat(robot.lookup("#tabPane").queryAs(TabPane.class).getSelectionModel().isSelected(0)).isTrue();
        Assertions.assertThat(tv.getSeries().getData().size()).isEqualTo(1);
//        robot.clickOn("#radioOnlySol");
//        robot.clickOn("#radioAllNodes");
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