package org.uclouvain.visualsearchtree.tree;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
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

import static org.uclouvain.visualsearchtree.util.Constant.*;

public class TreeVisual {
    private Tree.Node<String> node;
    private List<Integer> legendStats;
    private List<Text> labels;
    private String info;
    private List focusedRect;
    private  StackPane treeStackPane;
    private Map<String,String> boookMarks;
    private Map<String, Rectangle> allNodesRects;
    private Map<String, XYChart.Data> allNodesChartDatas;
    private Map<String, Tree.PositionedNode<String>> allNodesPositions;
    private List<Tree.Node> tempList;
    private  Tree tree;
    ///
    NumberAxis xAxis = new NumberAxis();
    NumberAxis yAxis = new NumberAxis();

    private LineChart lineChart;
    private XYChart.Series series;
    private HBox legendhbox;

    public TreeVisual(Tree.Node<String> node) {
        this.node = node;
        this.labels = new ArrayList<>(){};
        this.info = "";
        this.boookMarks = new HashMap<String,String>();
        this.focusedRect = new ArrayList<>(){{
            add(new Rectangle());
            add(Tree.NodeType.INNER);
            add(new Text(" "));
            add(0);
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

    /**
     * Realtime constructor
     */
    public  TreeVisual(){
        this.treeStackPane = new StackPane();
        this.boookMarks = new HashMap<String,String>();
        tree = new Tree(-1);
        this.node = tree.root();
        this.legendhbox = new HBox();

        this.xAxis = new NumberAxis();
        this.yAxis = new NumberAxis();
        lineChart = new LineChart<>(xAxis, yAxis);
        tempList = new ArrayList<>();
        this.labels = new ArrayList<>(){};
        this.info = "";
        this.focusedRect = new ArrayList<>(){{
            add(new Rectangle());
            add(Tree.NodeType.INNER);
            add(new Text(" "));
            add(0);
        }};

        this.legendStats = new ArrayList<>(){{
            add(0);
            add(0);
            add(0);
            add(0);
        }};
        //
        this.allNodesRects = new Hashtable<>();
        this.allNodesPositions = new Hashtable<>();
        this.allNodesChartDatas = new Hashtable<>();
        this.series =  new XYChart.Series();
        lineChart.getData().add(series);
        periodicDrawer();
    }

    public StackPane getTreeStackPane()
    {
        return this.treeStackPane;
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

    public void setFocusedRect(Rectangle r, Tree.NodeType type, Text label, int nodeId) {
        this.focusedRect.set(0, r);
        this.focusedRect.set(1, type);
        this.focusedRect.set(2, label);
        this.focusedRect.set(3, nodeId);
    }

    public Group getGroup() {
        Group root = new Group();

        Tree.PositionedNode<String> pnode = this.getNode().design();
        Text nodeLabel = new Text();
        nodeLabel.setTextAlignment(TextAlignment.RIGHT);
        drawNodeRecur(root, pnode, 0.0, 0, nodeLabel);
        getTreeChart(true);
        generateLegendsStack();
        return root;
    }


    public  Rectangle drawNodeRecur(Group g, Tree.PositionedNode<String> root, double center, int depth, Text nLabel) {
        double absolute = center + root.position;
        Gson gz = new Gson();
        NodeInfoData info = null;

        Rectangle r = createRectangle(400 + absolute * 40, 50 + depth * 50, root.type);
        styleLabel(nLabel, absolute, depth, root.label, root.position, root.children.size());

        //Add Event to each rectangle
        r.setOnMouseClicked(e -> {
            //root.nodeAction();
            r.fireEvent(new BackToNormalEvent());
            r.setFill(Color.ORANGE);
            nLabel.setOpacity((nLabel.getOpacity())==1? 0:1);
            nLabel.setText(root.label);
            this.setInfo(root.info);
            this.setFocusedRect(r, root.type, nLabel, root.nodeId);
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

        info = gz.fromJson(root.info, new TypeToken<NodeInfoData>(){}.getType());
        if (info != null) {
            String nodeID = UUID.randomUUID().toString();
            this.allNodesPositions.put(nodeID, root);
            this.allNodesRects.put(nodeID, r);
        }
        return r;
    }


    private Rectangle createRectangle(double x, double y, Tree.NodeType type) {
        Rectangle rect = new Rectangle(x,y,NODE_SHAPE_SIZE,NODE_SHAPE_SIZE);
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
                rect.setArcHeight(NODE_SHAPE_ARC_VALUE);
                rect.setArcWidth(NODE_SHAPE_ARC_VALUE);
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
        rect.setCursor(Cursor.HAND);
        return rect;
    }

    private Rectangle createRectangleForLegendBox(Tree.NodeType type) {
        Rectangle rect = new Rectangle();
        rect.setWidth(LEGEND_SHAPE_SIZE);
        rect.setHeight(LEGEND_SHAPE_SIZE);
        rect.setStrokeType(StrokeType.OUTSIDE);
        rect.setStrokeWidth(1);
        rect.setStroke(Color.BLACK);

        switch (type) {
            case INNER -> {
                rect.setArcHeight(LEGEND_SHAPE_ARC_VALUE);
                rect.setArcWidth(LEGEND_SHAPE_ARC_VALUE);
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
            theLabel.setX(400 + absolute * LABEL_X_COEFFICIENT);
            theLabel.setY(82 + depth * LABEL_Y_COEFFICIENT);
        } else {
            if (pos == 0.0) {
                theLabel.setX(402 + absolute * LABEL_X_COEFFICIENT);
                theLabel.setY(45 + depth * LABEL_Y_COEFFICIENT);
            } else if (pos < 0) {
                if(pos == -1){
                    theLabel.setX(378 + absolute * LABEL_X_COEFFICIENT);
                    theLabel.setY(50 + depth * LABEL_Y_COEFFICIENT);
                }
                else{
                    theLabel.setX(420 + absolute * LABEL_X_COEFFICIENT);
                    theLabel.setY(50 + depth * LABEL_Y_COEFFICIENT);
                }
            } else {
                theLabel.setX(422 + absolute * LABEL_X_COEFFICIENT);
                theLabel.setY(48 + depth * LABEL_Y_COEFFICIENT);
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

    public HBox generateLegendsStack(){
        legendhbox.setPadding(new Insets(10));
        legendhbox.setAlignment(Pos.BASELINE_LEFT);
        Rectangle branchRect = createRectangleForLegendBox(Tree.NodeType.INNER);
        Rectangle solvedRect = createRectangleForLegendBox(Tree.NodeType.SOLUTION);
        Rectangle failedRect = createRectangleForLegendBox(Tree.NodeType.FAIL);
        FlowPane s1 = new FlowPane();
        FlowPane s2 = new FlowPane();
        FlowPane s3 = new FlowPane();
        s1.getChildren().addAll(branchRect, new Text("  ("+ this.legendStats.get(0)+")"));
        s2.getChildren().addAll(failedRect, new Text("  ("+ this.legendStats.get(1)+")"));
        s3.getChildren().addAll(solvedRect, new Text("  ("+ this.legendStats.get(2)+")"));
        legendhbox.getChildren().addAll(s1,s2,s3,new  Text("DEPTH : ("+ this.legendStats.get(3)+")"));
        return legendhbox;
    }


    public void makeNotFocus(){
        var r = (Rectangle) this.focusedRect.get(0);
        var branch = (Tree.NodeType) this.focusedRect.get(1);
        var label = (Text) this.focusedRect.get(2);
        label.setOpacity(0);
        if(!Objects.equals(branch, " ")){
            switch (branch) {
                case INNER -> r.setFill(Color.CORNFLOWERBLUE);
                case FAIL -> r.setFill(Color.RED);
                case SOLUTION -> r.setFill(Color.GREEN);
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
        this.allNodesChartDatas = new HashMap<>();
        Gson gz = new Gson();
        NodeInfoData _info = null;
        yAxis.setLabel("Cost");
        xAxis.setLabel("Number of Choices");
        //creating the chart
        if (all_sol)
        {
            for (String key: this.allNodesPositions.keySet())
            {
                if (this.allNodesPositions.get(key).type == Tree.NodeType.SOLUTION)
                {
                    _info = gz.fromJson(this.allNodesPositions.get(key).info, new TypeToken<NodeInfoData>(){}.getType());
                    if (_info != null)
                    {
                        this.allNodesChartDatas.put(key, (new XYChart.Data(_info.param1, _info.cost)));
                        series.getData().add(this.allNodesChartDatas.get(key));
                    }
                }
            }
        }
        else
        {
            xAxis.setLabel("Number of Solutions");
            Map<String, Float> only_sol = new HashMap<>();
            for (String key: this.allNodesPositions.keySet())
            {
                if (this.allNodesPositions.get(key).type == Tree.NodeType.SOLUTION)
                {
                    _info = gz.fromJson(this.allNodesPositions.get(key).info, new TypeToken<NodeInfoData>(){}.getType());
                    if (_info != null) {
                        only_sol.put(key, (float) _info.param1);
                    }
                }
            }
            // sort by value
            List<Map.Entry<String, Float>> list = new ArrayList<>(only_sol.entrySet());
            list.sort(Map.Entry.comparingByValue());
            for (int i = 0; i < list.size(); i++) {
                String _key = list.get(i).getKey();
                _info = gz.fromJson(this.allNodesPositions.get(_key).info, new TypeToken<NodeInfoData>(){}.getType());
                this.allNodesChartDatas.put(_key, (new XYChart.Data(i+1, _info.cost)));
                series.getData().add(this.allNodesChartDatas.get(_key));
            }
        }
        return  lineChart;
    }

    public static class NodeInfoData {
        public int cost = 0;
        public int param1 = 0;
        public String other = "";
    }

    /**
     * Use function to add event on chart
     */
    public void addEventOnChart()
    {
        for (String key: this.allNodesChartDatas.keySet())
        {
            if (this.allNodesChartDatas.get(key) != null)
            {
                //variables
                Data tmp_data = (Data)this.allNodesChartDatas.get(key);
                Rectangle tmp_rect = this.allNodesRects.get(key);
                tmp_data.getNode().setCursor(Cursor.HAND);
                Color old_col = (Color) tmp_rect.getFill();

                tmp_data.getNode().setOnMousePressed(event -> {
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

    public void periodicDrawer(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int intv = 0;
            @Override
            public void run() {
                boolean check = false;
                for ( int i = 0; i < 5; i++) {
                    if (i < tempList.size() )
                    {
                        if (tempList.get(i) != null) {
                            tree.attachToParent(tempList.get(i).nodePid, tree.nodeMap.get(tempList.get(i).nodeId));
                            check = true;
                        }
                        tempList.remove(i);
                    }
                }
                if ( (intv > 0) && (!check))
                {
                    refresh(true, timer);
                }else {
                    refresh(false, timer);
                }
                intv ++;
            }
        }, 0, 500);
    }

    public void refresh(Boolean exit, Timer time){
        if (exit)
            time.cancel();
        Platform.runLater(()->{
            if (treeStackPane.getChildren().size() >  0)
            {
                treeStackPane.getChildren().remove(0);
            }
            this.resetAllBeforeRedraw();
            treeStackPane.getChildren().add(getGroup());
        });
    }

    public void createNode(int id, int pId, Tree.NodeType type, NodeAction onClick, String info){
        tree.crateIndNode(id, pId, type, onClick, info);
        this.tempList.add(new Tree.Node(id,pId,"child", type, new LinkedList<>(), new LinkedList(), onClick, info));
    }

    /**
     * Used to reset all parameters
     */
    public void resetAllBeforeRedraw(){
        // Reset all saved nodes data
        this.allNodesRects = new Hashtable<>();
        this.allNodesPositions = new Hashtable<>();
        this.allNodesChartDatas = new Hashtable<>();

        //Empty legendBox
        if (legendhbox.getChildren().size() >  0)
        {
            legendhbox.getChildren().remove(0, legendhbox.getChildren().size());
        }
        this.legendStats = new ArrayList<>(){{
            add(0);
            add(0);
            add(0);
            add(0);
        }};
    }
}