package org.uclouvain.visualsearchtree.tree;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.uclouvain.visualsearchtree.tree.events.BackToNormalEvent;
import org.uclouvain.visualsearchtree.tree.events.BackToNormalEventHandler;
import org.uclouvain.visualsearchtree.tree.events.CustomEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.uclouvain.visualsearchtree.util.Constant.*;

/**
 * <p><b>TreeVisual</b>: This Class instance return very basic profiling screen</p>
 * <p>This screen contain: </p>
 * <ul>
 *   <li>
 *       The <b>Search Tree</b>: [{@link org.uclouvain.visualsearchtree.tree.Tree Tree}]
 *   </li>
 *   <li>
 *       The layout oft his screen is TreePane Object [{@link org.uclouvain.visualsearchtree.tree.TreePane TreePane}]
 *   </li>
 * </ul>
 */
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

    private  Tree tree;

    NumberAxis xAxis;
    NumberAxis yAxis;

    private LineChart lineChart;
    private XYChart.Series series;
    private HBox legendbox;

    private Group treeGroup;

    private Map<Integer, Group> rootNodes;
    private List<DrawListener> dfsListeners = new LinkedList<DrawListener>();

    public void onDrawFinished(Procedure listener)
    {
        dfsListeners.add(new DrawListener() {
            /**
             * Will be called at the end of draw
             */
            @Override
            public void onFinish() {
                listener.call();
            }
        });
    }

    /**
     * Will be called at end
     */
    private void notifyEndDraw()
    {
        dfsListeners.forEach(l-> l.onFinish());
    }

    private void notifyNewUINodeCreated(int id, int pId, Tree.NodeType type, NodeAction nodeAction, String info)
    {
        dfsListeners.forEach(l->l.onUINodeCreated(id,pId,type,nodeAction,info));
    }


    /**
     * <b>Note: </b> Create an instance of the search tree from a {@link org.uclouvain.visualsearchtree.tree.Tree.Node Node} object.
     * @param tree
     */

    public TreeVisual(Tree tree, boolean isFx)
    {
        this.tree = tree;
        initTreeVisual_(isFx);
        treeStackPane = new StackPane();
        treeStackPane.getChildren().add(treeGroup);
        //Refresh UI : ADD parameters (time and nb nodes)
        periodicUIRefresher();

        // Listen to new node added on the Tree : ADD a listener that return the Node UI element
        tree.addListener(new TreeListener() {
            @Override
            public void onNodeCreated(int id, int pId, Tree.NodeType type, NodeAction nodeAction, String info) {
                if (rootNodes.get(pId) == null){return;}
                System.out.println("in it");
                Anchor parent = (Anchor)rootNodes.get(pId).getChildren().get(0);
                Group temp = drawNode(parent, id, pId, type, nodeAction, info);
                rootNodes.put(id, temp);
            }
        });

    }



    public TreeVisual(Tree.Node<String> node) {
        this.node = node;
        this.labels = new ArrayList<>(){};
        this.info = "";
        this.boookMarks = new HashMap<String,String>();
        this.focusedRect = new ArrayList<>(){{
            add(new Rectangle());
            add(Tree.NodeType.INNER);
            Text  t = new Text(" ");
            add(t);
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
     * Create an instance of the search without {@link org.uclouvain.visualsearchtree.tree.Tree.Node Node} object pass argument.
     * <br/><br/>
     * <p>
     *     <b>Use case: </b> Used for  Realtime constructor
     * </p>
     */
    public TreeVisual(Procedure procedure , Tree tree, boolean isFx){

        this.tree = tree;
        initTreeVisual_(isFx);
        treeStackPane = new StackPane();
        treeStackPane.getChildren().add(treeGroup);

        //
        startExploringTask(procedure);

        //Refresh UI : ADD parameters (time and nb nodes)
        periodicUIRefresher();

        // Listen to new node added on the Tree : ADD a listener that return the Node UI element
        tree.addListener(new TreeListener() {
            @Override
            public void onNodeCreated(int id, int pId, Tree.NodeType type, NodeAction nodeAction, String info) {
                if (rootNodes.get(pId) == null){return;}
                Anchor parent = (Anchor)rootNodes.get(pId).getChildren().get(0);
                Group temp = drawNode(parent, id, pId, type, nodeAction, info);
                rootNodes.put(id, temp);
            }
        });
    }

    /**
     * Draw a new node from its parent
     * @param parent
     * @return
     */
    private Group drawNode(Anchor parent, int id, int pId, Tree.NodeType type, NodeAction nodeAction, String info)
    {
        Group child_group = parent.addChild();
        //To get the child node as Circle(Anchor)=>
        Anchor child = (Anchor) child_group.getChildren().get(0);
        // ADD EVENT ON IT
        return child_group;
    }

    /**
     * Perform callback exploring task in background
     * @param p
     */
    private void startExploringTask(Procedure p)
    {
        ExplorerTask explore = new ExplorerTask(p);
        Thread th = new Thread(explore);
        th.setDaemon(true);
        th.start();
    }

    /**
     * Periodically add node on UI
     */
    private void periodicUIRefresher()
    {
        final int[] pos = {1};
        Timeline fiveSecondsWonder = new Timeline(
            new KeyFrame(Duration.seconds(1),
                event -> {
                    int i;
                    for (i = pos[0]; i < pos[0] + 7; i++) {
                        if (rootNodes.get(i) == null) {
                            return;
                        }
                        if (!treeGroup.getChildren().contains(rootNodes.get(i)))
                            treeGroup.getChildren().add(rootNodes.get(i));
                    }
                    pos[0] = i;
                    if (treeStackPane.getChildren().size() > 0) {
                        treeStackPane.getChildren().clear();
                    }
                    treeStackPane.getChildren().add(treeGroup);
            })
        );
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();
    }

    /**
     * Used to initialized parameters
     */
    public void initTreeVisual_(Boolean isFx)
    {
        boookMarks = new HashMap<>();
        rootNodes = new HashMap<>();
        DoubleProperty startX = new SimpleDoubleProperty(0);
        DoubleProperty startY = new SimpleDoubleProperty(50);
        Anchor start   = new Anchor(Color.PALEGREEN, startX, startY);
        Group _start = new Group(start);
        rootNodes.put(tree.root().nodeId, _start);
        treeGroup = new Group();
        treeGroup.getChildren().add(_start);

        initTreeVisual(isFx);
    }

    public void initTreeVisual(Boolean isFx)
    {
        if (!isFx) {
            fxInitializer();
        }
        else{
            nonFxInitializer();
        }
    }

    /**
     * Init these parameters on FX thread
     */
    public void fxInitializer()
    {
        Platform.startup(()->{
            nonFxInitializer();
        });
    }

    /**
     * Init these parameters on FX thread
     */
    public void nonFxInitializer()
    {
            treeStackPane = new StackPane();
            boookMarks = new HashMap<String,String>();
            legendbox = new HBox();
            xAxis = new NumberAxis();
            yAxis = new NumberAxis();
            lineChart = new LineChart<>(xAxis, yAxis);
            labels = new ArrayList<>(){};
            info = "";
            focusedRect = new ArrayList<>(){{
                add(new Rectangle());
                add(Tree.NodeType.INNER);
                Text  t = new Text(" ");
                add(t);
                add(0);
            }};

            legendStats = new ArrayList<>(){{
                add(0);
                add(0);
                add(0);
                add(0);
            }};
            //
            allNodesRects = new Hashtable<>();
            allNodesPositions = new Hashtable<>();
            allNodesChartDatas = new Hashtable<>();
            series =  new XYChart.Series();
            lineChart.getData().add(series);
    }
    /**
     * <b>Note: </b> Return the stack Pane that contain visualization search Tree
     * @return {@link javafx.scene.layout.StackPane}
     */
    public StackPane getTreeStackPane()
    {
        return this.treeStackPane;
    }


    /**
     * <b>Note: </b> Return the root node used to draw search tree
     * @return {@link org.uclouvain.visualsearchtree.tree.Tree.Node Node}
     */
    public Tree.Node<String> getNode() {
        return node;
    }

    /**
     * <b>Notes: </b> Get the list of label defined on each {@link org.uclouvain.visualsearchtree.tree.Tree.Node Node} of Search {@link org.uclouvain.visualsearchtree.tree.Tree}
     * @return {@link java.util.List}
     */
    public List<Text> getLabels() {
        return labels;
    }

    // TODO: DO NOT UNDERSTAND
    public String getInfo() {
        return info;
    }

    // TODO: DO NOT UNDERSTAND
    public List<Integer> getLegendStats() {
        return legendStats;
    }

    /**
     * <b>Note: </b>Get Tree Node on which user click.
     * @return {@link java.util.List}
     */
    public List getFocusedRect() {
        return focusedRect;
    }

    // TODO: DO NOT UNDERSTAND
    public void setLegendStats(int i, int value) {
        this.legendStats.set(i, value);
    }

    /**
     * <b>Note: </b> Add label
     * @param label
     */
    public void setLabels(Text label) {
        this.labels.add(label);
    }

    /**
     * <b>Note: </b> Add info
     * @param info
     */
    public void setInfo(String info) {
        this.info = info;
    }


    /**
     * <b>Note: </b> Get bookMarks from Tree
     * @return
     */
    public Map<String, String> getBookMarks() {
        return boookMarks;
    }


    /**
     * <b>Note: </b> Define nookmark on specific {@link org.uclouvain.visualsearchtree.tree.Tree.Node Node}
     * @param key
     * @param value
     */
    public void setBookMarks(String key, String value) {
        this.boookMarks.put(key, value);
    }

    /**
     * <b>Note: </b> Focus on a Tree {@link org.uclouvain.visualsearchtree.tree.Tree.Node Node}
     * @param r
     * @param type
     * @param label
     * @param nodeId
     */
    public void setFocusedRect(Rectangle r, Tree.NodeType type, Text label, int nodeId) {
        this.focusedRect.set(0, r);
        this.focusedRect.set(1, type);
        this.focusedRect.set(2, label);
        this.focusedRect.set(3, nodeId);
    }

    /**
     * <b>Note: </b> Use {@link org.uclouvain.visualsearchtree.tree.Tree.PositionedNode PositionedNode} to build tree and return it as {@link javafx.scene.Group Group}
     * @return {@link javafx.scene.Group}
     */
    public Group getGroup() {
//        Group root = new Group();
//        Tree.PositionedNode<String> pnode = this.getNode().design();
//        Text nodeLabel = new Text();
//        nodeLabel.setTextAlignment(TextAlignment.RIGHT);
//        drawNodeRecur(root, pnode, 0.0, 0, nodeLabel);
        //getTreeChart(true);
        //generateLegendsStack();
        //return root;
        return treeGroup;
    }

    /**
     * <b>Note: </b>Draw Node recursively
     * @param g group to add node
     * @param root root node
     * @param center double value for centering the tree
     * @param depth depth of the tree
     * @param nLabel Text widget for containing the node Label
     * @return Rectangle which can be circle or losange representing node and depending of its type
     */
    public  Rectangle drawNodeRecur(Group g, Tree.PositionedNode<String> root, double center, int depth, Text nLabel) {
        double absolute = center + root.position;
        Gson gson = new Gson();
        NodeInfoData info= null;

        Rectangle r = createRectangle(400 + absolute * 40, 50 + depth * 50, root.type);
        styleLabel(nLabel, absolute, depth, root.label, root.position, root.children.size());

        //Add Event to each rectangle
        r.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 || e.getButton()== MouseButton.SECONDARY) {
                root.nodeAction.nodeAction();
            }
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
        info = gson.fromJson(root.info, new TypeToken<NodeInfoData>(){}.getType());
        if (info != null) {
            String nodeID = UUID.randomUUID().toString();
            this.allNodesPositions.put(nodeID, root);
            this.allNodesRects.put(nodeID, r);
        }
        return r;
    }

    /**
     <b>Note: </b>This Method is used to create Tre Nodes objects.
     * @param x width of the rectangle
     * @param y height of the rectangle
     * @param type Node Type
     * @return Rectangle representing the node
     */
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
        treeStackPane.getChildren().add(rect);
        return rect;
    }

    /**
     * <b>Note: </b>Draw Rectangle for Legend HBox
     * @param type Node Type
     * @return Rectangle representing a type of node
     */
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

    /**
     * <b>Note: </b>Style Each node Label by setting its content and positioning it to a suitable position on the scene
     * @param theLabel Text Widget
     * @param absolute absolute position value
     * @param depth tree depth
     * @param content Node label to show
     * @param pos position value
     * @param nChild number of children the current node has
     */
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

    /**
     * <b>Note: </b>Create a line for connecting two Nodes represented by a rectangle
     * @param r1 first rectangle
     * @param r2 second rectangle
     * @return Line for connecting rectangles
     */
    private Line connectRectangle(Rectangle r1, Rectangle r2) {
        Line line = new Line();
        line.setStartX(r1.getX()+r1.getWidth()/2);
        line.setStartY(r1.getY());

        line.setEndX(r2.getX()+r2.getWidth()/2);
        line.setEndY(r2.getY());

        line.setStrokeWidth(1);

        return line;
    }

    /**
     * <b>Note: </b>Generate Legend Box for specify number of each type of nodes
     * @return HBox
     */
    public HBox generateLegendsStack(){
        legendbox.setPadding(new Insets(10));
        legendbox.setAlignment(Pos.BASELINE_LEFT);
        Rectangle branchRect = createRectangleForLegendBox(Tree.NodeType.INNER);
        Rectangle solvedRect = createRectangleForLegendBox(Tree.NodeType.SOLUTION);
        Rectangle failedRect = createRectangleForLegendBox(Tree.NodeType.FAIL);
        FlowPane s1 = new FlowPane();
        FlowPane s2 = new FlowPane();
        FlowPane s3 = new FlowPane();
        Text  t1 = new Text("  ("+ this.legendStats.get(0)+")");
        Text  t2 = new Text("  ("+ this.legendStats.get(1)+")");
        Text  t3 = new Text("  ("+ this.legendStats.get(2)+")");
        Text  t4 = new  Text("DEPTH : ("+ this.legendStats.get(3)+")");
        s1.getChildren().addAll(branchRect, t1);
        s2.getChildren().addAll(failedRect, t2);
        s3.getChildren().addAll(solvedRect, t3);
        legendbox.getChildren().addAll(s1,s2,s3,t4);
        return legendbox;
    }

    /**
     * <b>Note: </b>Make the previous node selected to it initial state : No more focus color...
     */
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
     * <b>Note: </b>Create the optimization chart
     * @param all_sol boolean
     * @return LineChart
     */
    public LineChart<Number, Number> getTreeChart(boolean all_sol){
        // variables
        series.getData().clear();
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
                        this.allNodesChartDatas.put(key, (new XYChart.Data(_info.domain, _info.cost)));
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
                        only_sol.put(key, (float) _info.domain);
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

    /**
     * <b>Note: </b> Node Info Data
     */
    public static class NodeInfoData {
        public int cost = 0;
        public int domain = 0;
        public String other = "";
    }

    /**
     * <b>Note: </b>Use function to add event on chart
     */
    public void addEventOnChart() {
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
                    //tmp_rect.getParent().setTranslateX( (tmp_rect.getParent().getScene().getWidth()) - tmp_rect.getX());
                    tmp_rect.getParent().setTranslateX(tmp_rect.getParent().getLayoutX() -  tmp_rect.getX());
                    tmp_rect.getParent().setTranslateY(tmp_rect.getParent().getLayoutY() -  tmp_rect.getY());
                    tmp_rect.setFill(Color.ORANGE);
                });
                tmp_data.getNode().setOnMouseExited(event -> {
                    this.allNodesRects.get(key).setFill(old_col);
                });
            }
        }
    }

    /**
     * Used to call dfs task in background
     */
    class ExplorerTask extends Task<Boolean>
    {
        private final Procedure action;

        /**
         *
         * @param action is callback function to be performed by the task
         */
        public ExplorerTask(Procedure action)
        {
            this.action = action;
        }
        @Override
        protected Boolean call() throws Exception {
            this.action.call();
            return true;
        }
    }


}