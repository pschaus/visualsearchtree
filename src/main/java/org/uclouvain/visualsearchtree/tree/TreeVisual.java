package org.uclouvain.visualsearchtree.tree;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.uclouvain.visualsearchtree.tree.events.BackToNormalEvent;
import org.uclouvain.visualsearchtree.tree.events.BackToNormalEventHandler;
import org.uclouvain.visualsearchtree.tree.events.CustomEvent;

import java.util.*;

public class TreeVisual {
    private final Tree.Node<String> node;
    private List<Integer> legendStats;
    private List<Text> labels;
    private String info;
    private List focusedRect;
    private final Map<String,String> boookMarks;


    private Map<String, Rectangle> allNodesRects;
    private Map<String, XYChart.Data> allNodesChartDatas;
    private Map<String, Tree.PositionedNode<String>> allNodesPositions;

    public TreeVisual(Tree.Node<String> node) {
        this.node = node;
        this.labels = new ArrayList<>(){};
        this.info = "";
        this.boookMarks = new HashMap<String,String>();
        this.focusedRect = new ArrayList<>(){{
            add(new Rectangle());
            add(" ");
            add(new Text(" "));
        }};
        this.legendStats = new ArrayList<>(){{
            add(0);
            add(0);
            add(0);
            add(0);
        }};
        this.allNodesRects = new Hashtable<>();
        this.allNodesPositions = new Hashtable<>();
        this.allNodesChartDatas = new Hashtable<>();
    }

    public Tree.Node<String> getNode() {
        return node;
    }

    public List<Text> getLabels() {
        return labels;
    }

    public String getInfo() {
        return info;
    }

    public List<Integer> getLegendStats() {
        return legendStats;
    }
    public List getFocusedRect() {
        return focusedRect;
    }

    public void setLegendStats(int i, int value) {
        this.legendStats.set(i, value);
    }

    public void setLabels(Text label) {
        this.labels.add(label);
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Map<String, String> getBoookMarks() {
        return boookMarks;
    }
    public void setBoookMarks(String key, String value) {
        this.boookMarks.put(key, value);
    }
    public void setFocusedRect(Rectangle r, String branch, Text label) {
        this.focusedRect.set(0, r);
        this.focusedRect.set(1, branch);
        this.focusedRect.set(2, label);
    }

    public void setFocusedRect(Rectangle r, Tree.NodeType type, Text label) {
        this.focusedRect.set(0, r);
        this.focusedRect.set(1, type);
        this.focusedRect.set(2, label);
    }

    public Group getGroup() {
        Group root = new Group();

        Tree.PositionedNode<String> pnode = this.getNode().design();
        Text nodeLabel = new Text();
        nodeLabel.setTextAlignment(TextAlignment.RIGHT);
        drawNodeRecur(root, pnode, 0.0, 0, nodeLabel);
        return root;
    }


//    public  Rectangle drawNodeRecur(Group g, Tree.PositionedNode<String> root, double center, int depth, Text nLabel) {
//        double absolute = center + root.position;
//        Gson gz = new Gson();
//        NodeInfoData info = null;
//
//        Rectangle r = createRectangle(400 + absolute * 40, 50 + depth * 50, root.branch);
//        styleLabel(nLabel, absolute, depth, root.label, root.position, root.children.size());
//
//        //Add Event to each rectangle
//        r.setOnMouseClicked(e -> {
//            //root.nodeAction();
//            r.fireEvent(new BackToNormalEvent());
//            r.setFill(Color.ORANGE);
//            nLabel.setOpacity((nLabel.getOpacity())==1? 0:1);
//            nLabel.setText(root.label);
//            this.setInfo(root.info);
//            this.setFocusedRect(r, root.branch, nLabel);
//        });
//
//        g.getChildren().add(r);
//        g.getChildren().add(nLabel);
//
//        for (Tree.PositionedNode<String> child : root.children) {
//            Rectangle childR = drawNodeRecur(g, child, absolute, depth + 1, new Text());
//            Line line = connectRectangle(r, childR);
//            g.getChildren().add(line);
//
//            //Make rectangle toFront
//            r.toFront();
//            childR.toFront();
//        }
//
//        if (depth > this.legendStats.get(3)) {
//            this.setLegendStats(3, depth);
//        }
//
//        info = gz.fromJson(root.info, new TypeToken<NodeInfoData>(){}.getType());
//        if (info != null) {
//            String nodeID = UUID.randomUUID().toString();
//            this.allNodesPositions.put(nodeID, root);
//            this.allNodesRects.put(nodeID, r);
//            this.allNodesChartDatas.put(nodeID, (new XYChart.Data(info.param1, info.cost)));
//        }
//        return r;
//    }

    public  Rectangle drawNodeRecur(Group g, Tree.PositionedNode<String> root, double center, int depth, Text nLabel) {
        double absolute = center + root.position;

        Rectangle r = createRectangle(400 + absolute * 40, 50 + depth * 50, root.type);
        styleLabel(nLabel, absolute, depth, root.label, root.position, root.children.size());

        //Add Event to each rectangle
        r.setOnMouseClicked(e -> {
            //root.nodeAction();
            r.fireEvent(new BackToNormalEvent());
            r.setFill(Color.ORANGE);
            nLabel.setOpacity((nLabel.getOpacity())==1? 0:1);
            nLabel.setText(root.label);
            this.setInfo(root.label);
            this.setFocusedRect(r, root.type, nLabel);
        });

        g.getChildren().add(r);
        g.getChildren().add(nLabel);

        for (Tree.PositionedNode<String> child : root.children) {
            Rectangle childR = drawNodeRecur(g, child, absolute, depth + 1, new Text());
            Line line = connectRectangle(r, childR);
            g.getChildren().add(line);

            //Make rectangle toFront
            r.toFront();
            childR.toFront();
        }

        if (depth > this.legendStats.get(3)) {
            this.setLegendStats(3, depth);
        }

        return r;
    }

    private Rectangle createRectangle(double x, double y, String branch) {
        Rectangle rect = new Rectangle(x,y,20,20);
        rect.setStrokeType(StrokeType.OUTSIDE);
        rect.setStrokeWidth(1);
        rect.setStroke(Color.BLACK);
        rect.addEventHandler(CustomEvent.CUSTOM_EVENT_TYPE, new BackToNormalEventHandler() {
            @Override
            public void unClick() {
                makeNotFocus();
            }
        });

        switch (branch) {
            case "BRANCH" -> {
                rect.setArcHeight(40);
                rect.setArcWidth(40);
                rect.setFill(Color.CORNFLOWERBLUE);
                this.setLegendStats(0,this.legendStats.get(0) +1);
            }
            case "FAILED" -> {
                rect.setFill(Color.RED);
                this.setLegendStats(1,this.legendStats.get(1) +1);
            }
            case "SOLVED" -> {
                rect.setFill(Color.GREEN);
                rect.setRotate(45);
                this.setLegendStats(2,this.legendStats.get(2) +1);
            }
            default -> {
            }
        }
        rect.setCursor(Cursor.CROSSHAIR);
        return rect;
    }

    private Rectangle createRectangle(double x, double y, Tree.NodeType type) {
        Rectangle rect = new Rectangle(x,y,20,20);
        rect.setStrokeType(StrokeType.OUTSIDE);
        rect.setStrokeWidth(1);
        rect.setStroke(Color.BLACK);
        rect.addEventHandler(CustomEvent.CUSTOM_EVENT_TYPE, new BackToNormalEventHandler() {
            @Override
            public void unClick() {
                makeNotFocus();
            }
        });

        switch (type) {
            case INNER -> {
                rect.setArcHeight(40);
                rect.setArcWidth(40);
                rect.setFill(Color.CORNFLOWERBLUE);
                this.setLegendStats(0,this.legendStats.get(0) +1);
            }
            case FAIL -> {
                rect.setFill(Color.RED);
                this.setLegendStats(1,this.legendStats.get(1) +1);
            }
            case SOLUTION -> {
                rect.setFill(Color.GREEN);
                rect.setRotate(45);
                this.setLegendStats(2,this.legendStats.get(2) +1);
            }
            default -> {
            }
        }
        rect.setCursor(Cursor.CROSSHAIR);
        return rect;
    }

    private Rectangle createRectangleForLegendBox(String branch) {
        Rectangle rect = new Rectangle();
        rect.setWidth(12);
        rect.setHeight(12);
        rect.setStrokeType(StrokeType.OUTSIDE);
        rect.setStrokeWidth(1);
        rect.setStroke(Color.BLACK);

        switch (branch) {
            case "BRANCH" -> {
                rect.setArcHeight(24);
                rect.setArcWidth(24);
                rect.setFill(Color.CORNFLOWERBLUE);
            }
            case "FAILED" -> rect.setFill(Color.RED);
            case "SOLVED" -> {
                rect.setFill(Color.GREEN);
                rect.setRotate(45);
            }
            default -> {
            }
        }
        return rect;
    }

    private Rectangle createRectangleForLegendBox(Tree.NodeType type) {
        Rectangle rect = new Rectangle();
        rect.setWidth(12);
        rect.setHeight(12);
        rect.setStrokeType(StrokeType.OUTSIDE);
        rect.setStrokeWidth(1);
        rect.setStroke(Color.BLACK);

        switch (type) {
            case INNER -> {
                rect.setArcHeight(24);
                rect.setArcWidth(24);
                rect.setFill(Color.CORNFLOWERBLUE);
            }
            case FAIL -> rect.setFill(Color.RED);
            case SOLUTION -> {
                rect.setFill(Color.GREEN);
                rect.setRotate(45);
            }
            default -> {
            }
        }
        return rect;
    }

    private void styleLabel(Text theLabel, double absolute , double depth, String content, double pos, int nChild) {

        theLabel.setFont(Font.font("Roboto", 10));
        theLabel.setFill(Color.rgb(13, 15, 16));
        if (nChild == 0) {
            theLabel.setX(400 + absolute * 40);
            theLabel.setY(82 + depth * 50);
        } else {
            if (pos == 0.0) {
                theLabel.setX(402 + absolute * 40);
                theLabel.setY(45 + depth * 50);
            } else if (pos < 0) {
                if(pos == -1){
                    theLabel.setX(378 + absolute * 40);
                    theLabel.setY(50 + depth * 50);
                }
                else{
                    theLabel.setX(420 + absolute * 40);
                    theLabel.setY(50 + depth * 50);
                }
            } else {
                theLabel.setX(422 + absolute * 40);
                theLabel.setY(48 + depth * 50);
            }
        }
        theLabel.setOpacity(0);
        theLabel.setText(content);
        theLabel.toFront();
        this.setLabels(theLabel);

    }

    private Line connectRectangle(Rectangle r1, Rectangle r2) {
        Line line = new Line();
        line.setStartX(r1.getX()+r1.getWidth()/2);
        line.setStartY(r1.getY());

        line.setEndX(r2.getX()+r2.getWidth()/2);
        line.setEndY(r2.getY());

        line.setStrokeWidth(1);

        return line;
    }


//    public HBox generateLegendsStack(){
//        HBox hbox = new HBox();
//        hbox.setPadding(new Insets(10));
//        hbox.setAlignment(Pos.BASELINE_LEFT);
//        Rectangle branchRect = createRectangleForLegendBox("BRANCH");
//        Rectangle solvedRect = createRectangleForLegendBox("SOLVED");
//        Rectangle failedRect = createRectangleForLegendBox("FAILED");
//        FlowPane s1 = new FlowPane();
//        FlowPane s2 = new FlowPane();
//        FlowPane s3 = new FlowPane();
//        s1.getChildren().addAll(branchRect, new Text("  ("+ this.legendStats.get(0)+")"));
//        s2.getChildren().addAll(failedRect, new Text("  ("+ this.legendStats.get(1)+")"));
//        s3.getChildren().addAll(solvedRect, new Text("  ("+ this.legendStats.get(2)+")"));
//        hbox.getChildren().addAll(s1,s2,s3,new  Text("DEPTH : ("+ this.legendStats.get(3)+")"));
//        return hbox;
//    }

    public HBox generateLegendsStack(){
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10));
        hbox.setAlignment(Pos.BASELINE_LEFT);
        Rectangle branchRect = createRectangleForLegendBox(Tree.NodeType.INNER);
        Rectangle solvedRect = createRectangleForLegendBox(Tree.NodeType.SOLUTION);
        Rectangle failedRect = createRectangleForLegendBox(Tree.NodeType.FAIL);
        FlowPane s1 = new FlowPane();
        FlowPane s2 = new FlowPane();
        FlowPane s3 = new FlowPane();
        s1.getChildren().addAll(branchRect, new Text("  ("+ this.legendStats.get(0)+")"));
        s2.getChildren().addAll(failedRect, new Text("  ("+ this.legendStats.get(1)+")"));
        s3.getChildren().addAll(solvedRect, new Text("  ("+ this.legendStats.get(2)+")"));
        hbox.getChildren().addAll(s1,s2,s3,new  Text("DEPTH : ("+ this.legendStats.get(3)+")"));
        return hbox;
    }


    public void makeNotFocus(){
        var r = (Rectangle) this.focusedRect.get(0);
        var branch = (String) this.focusedRect.get(1);
        var label = (Text) this.focusedRect.get(2);
        label.setOpacity(0);
        if(!Objects.equals(branch, " ")){
            switch (branch) {
                case "BRANCH" -> r.setFill(Color.CORNFLOWERBLUE);
                case "FAILED" -> r.setFill(Color.RED);
                case "SOLVED" -> r.setFill(Color.GREEN);
                default -> {
                }
            }
        }

    }

    /**
     * Create the optimization chart
     * @param all_sol
     * @return
     */
    public LineChart<Number, Number> getTreeChart(boolean all_sol){
        // variables
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        XYChart.Series series = new XYChart.Series();
        String x_label = "Number of Solution";
        yAxis.setLabel("Node Cost");
        if (!all_sol){
            x_label = "Number of Nodes";
        }
        xAxis.setLabel(x_label);
        //creating the chart
        final LineChart<Number,Number> lineChart = new LineChart<>(xAxis, yAxis);
        if (all_sol)
        {
            for (String key: this.allNodesPositions.keySet())
            {
                System.out.println(this.allNodesChartDatas.get(key));
                series.getData().add(this.allNodesChartDatas.get(key));
            }
        }
        else
        {
            for (String key: this.allNodesPositions.keySet())
            {
                if (this.allNodesPositions.get(key).branch == "SOLVED")
                    series.getData().add(this.allNodesChartDatas.get(key));
            }
        }

        lineChart.getData().add(series);
        return  lineChart;
    }

    public static class NodeInfoData {
        public int cost = 0;
        public int param1 = 0;
        public String other = "";
    }

    /*
    private static void getchildRecursvly(XYChart.Series series, Tree.Node<String> node){
        if (node.children.size() > 0) {
            for (Tree.Node<String> child: node.children) {
                getchildRecursvly(series, child);
            }
        }
        Gson g = new Gson();
        NodeInfoData info = g.fromJson(node.getInfo(), new TypeToken<NodeInfoData>(){}.getType());

        if(info != null) {
            try {
                Integer x = Integer.parseInt(info.cost+"");
                Integer y = Integer.parseInt(info.param1+"");
                series.getData().add(new XYChart.Data(x, y));
            } catch (NumberFormatException e) {
            }
        }
    }*/
    /**
     * Use function to add event on chart
     */
    public void addEventOnChart()
    {
        for (String key: this.allNodesPositions.keySet())
        {
            if (this.allNodesChartDatas.get(key) != null)
            {
                //variables
                Data tmp_data = (Data)this.allNodesChartDatas.get(key);
                Rectangle tmp_rect = this.allNodesRects.get(key);

                tmp_data.getNode().setCursor(Cursor.HAND);
                Color old_col = (Color) tmp_rect.getFill();
                tmp_data.getNode().setOnMouseClicked(event -> {
                    //Make focus and animate to ease visibility

                    //Animate
                    ScaleTransition st = new ScaleTransition(Duration.millis(200), tmp_rect);
                    st.setByX(0.5f);
                    st.setByY(0.5f);
                    st.setCycleCount(4);
                    st.setAutoReverse(true);
                    st.play();

                    //focus
                    tmp_rect.getParent().setTranslateX( (tmp_rect.getParent().getScene().getWidth()) - tmp_rect.getX());
                    tmp_rect.setFill(Color.ORANGE);
                });
                tmp_data.getNode().setOnMouseExited(event -> {
                    this.allNodesRects.get(key).setFill(old_col);
                });
            }
        }
    }
}