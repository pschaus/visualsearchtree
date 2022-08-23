package org.uclouvain.visualsearchtree.tree;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.uclouvain.visualsearchtree.util.Constant.*;

public class TreeUIController {
    public TabPane tabPane;
    public Tab graph;
    public Tab infoTab;
    public StackPane treeroot;
    public Slider zoomSlider;
    public TableView infoTableView;
    private TreeVisual instance;
    private final double stackPaneMinWidth = 400;
    private double stackPaneMinHeight = 400;

    //Menu items variables
    public MenuBar menuBar;
    public MenuItem manageBookMarksItem;
    public MenuItem showBookMarksItem;
    public MenuItem closeMenu;
    public MenuItem showLabels;
    public MenuItem showGraph;
    public MenuItem showInfos;
    public MenuItem about;
    public HBox tableHbox;
    public Tab bookMarksTab;
    public TableView<Map.Entry<String,String>> bookMarksTableView;

    @FXML
    public ToggleGroup graphType;
    @FXML
    public RadioButton radioOnlySol;
    @FXML
    public RadioButton radioAllNodes;
    @FXML
    public VBox chartUI;

    /**
     * @param instance TreeVisual instance
     */
    public void setInstance(TreeVisual instance) {
        this.instance = instance;
    }

    // Init methods
    public  void init(){
        resize();
        alignMenuItemText();
        attachEvent();
        initTableInfo();
        initBookMarksTable();
    }
    public void resize(){
        int depth = instance.getLegendStats().get(3);
        if(depth>5){
            treeroot.setMinHeight(depth*60);
            stackPaneMinHeight = treeroot.getMinHeight();
        }
    }

    private void initTableInfo () {
        TableColumn<Map, String> keyColumn = new TableColumn<>("Key");
        keyColumn.setCellValueFactory(new MapValueFactory<>("Key"));
        keyColumn.setMinWidth(150);

        TableColumn<Map, String> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(new MapValueFactory<>("Value"));
        valueColumn.setMinWidth(350);

        valueColumn.prefWidthProperty().bind(infoTableView.widthProperty().add(-keyColumn.getWidth()));
        infoTableView.getColumns().add(keyColumn);
        infoTableView.getColumns().add(valueColumn);
    }
    public void initBookMarksTable(){
        Map<String, String> bookMarksMap = instance.getBookMarks();

        TableColumn<Map.Entry<String, String>, String> idColumn = new TableColumn<>("Node Id");
        idColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        idColumn.setMinWidth(150);

        TableColumn<Map.Entry<String, String>, String> valueColumn = new TableColumn<>("BookMarks");
        valueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue()));

        ObservableList<Map.Entry<String, String>> items = FXCollections.observableArrayList(bookMarksMap.entrySet());
        bookMarksTableView = new TableView<>(items);
        bookMarksTableView.setMinHeight(150);
        bookMarksTableView.setPlaceholder(new Label("No bookmarks added. Please press CTRL+B to add a bookmark to a node"));
        bookMarksTableView.prefWidthProperty().bind(tableHbox.widthProperty());
        bookMarksTableView.setMaxHeight(Double.MAX_VALUE);

        valueColumn.prefWidthProperty().bind(bookMarksTableView.widthProperty().add(-idColumn.getWidth()));
        bookMarksTableView.getColumns().setAll(idColumn, valueColumn);
        tableHbox.getChildren().add(bookMarksTableView);
    }

    // Menu nodeAction methods
    public void showNodeLabels(ActionEvent actionEvent) {
        showAllLabels();
    }
    public void showNodeInfos(Event actionEvent) {
        displayNodeInfos();
    }
    public void showGraph(Event actionEvent){
        displayGraph();
    }
    public void closeWindow(ActionEvent actionEvent) {
        Stage st = (Stage) menuBar.getScene().getWindow();
        st.close();
    }
    public void manageBookMarks(Event actionEvent) throws IOException {
        addOrRemoveBookMarks();
    }
    public void showBookMarks(Event actionEvent) {
        displayBookMarks();
    }
    public void aboutUs(ActionEvent actionEvent) {
        String message = """
                MiniCP-Profiler version 2022.1.0

                Build: javac 18.0.1.1\s
                Runtime version: To be soon precised
                VM: OpenJDK 18 64-Bit IntelliJ IDEA

                Powered by: UC Louvain Belgium
                Copyright © May-June 2022 MiniCP-Profiler
                """;
        showInformationAlert("About MiniCP-Profiler", message);
    }

    // Helper methods

    /**
     * Display all node's Label
     */
    public  void showAllLabels(){
        int length = instance.getLabels().size();
        for (int i = 0; i < length; i++) {
            var element = instance.getLabels().get(i);
            element.setOpacity(element.getOpacity()==1? 0:1);
        }
    }

    /**
     * Attach keyEvent on the scene
     */
    public void attachEvent(){
        menuBar.getScene().setOnKeyPressed(ev ->{
            if(ev.getCode()== KeyCode.L){
                showAllLabels();
            }
            if(ev.getCode()== KeyCode.I){
                displayNodeInfos();
            }
            if(ev.getCode()== KeyCode.O){
                displayGraph();
            }
            if(ev.getCode()== KeyCode.B){
                if(ev.isControlDown()){
                    try {
                        addOrRemoveBookMarks();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    displayBookMarks();
                }
            }
        });

        // Check if radio btn changed
        graphType.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                RadioButton tmp = (RadioButton)newValue;

                chartUI.getChildren().remove(0);
                if (tmp.getText() == radioAllNodes.getText()) {
                    chartUI.getChildren().add(instance.getTreeChart(true));
                    instance.addEventOnChart();
                }else {
                    chartUI.getChildren().add(instance.getTreeChart(false));
                    instance.addEventOnChart();
                }
            }
        });

        zoomSlider.setOnMouseClicked(e ->{
            double zoomValue = zoomSlider.getValue();
            if (zoomValue == DEFAULT_SLIDER_VALUE) {
                treeroot.setMinHeight(stackPaneMinHeight);
                treeroot.setMinWidth(stackPaneMinWidth);
                treeroot.setScaleY(1);
                treeroot.setScaleY(1);
            }else if (zoomValue <DEFAULT_SLIDER_VALUE) {
                zoomOut(zoomValue);
            }else {
                zoomIn(zoomValue);
            }
        });
    }

    /**
     * Zoom In StackPane containing the tree
     * @param value slider current value
     */
    public void zoomIn(double value){
        treeroot.setScaleX(1 + value*SCALE_COEFFICIENT);
        treeroot.setScaleY(1 + value*SCALE_COEFFICIENT);
        if(value<=DEFAULT_SLIDER_VALUE+7){
            treeroot.setMinHeight(stackPaneMinHeight * (value/ZOOM_Y_COEFFICIENT));
            treeroot.setMinWidth(stackPaneMinWidth * (value/ZOOM_X_COEFFICIENT));
        }else if(value<=DEFAULT_SLIDER_VALUE+15){
            treeroot.setMinHeight(stackPaneMinHeight * (value/(ZOOM_Y_COEFFICIENT+10)));
            treeroot.setMinWidth(stackPaneMinWidth * (value/(ZOOM_X_COEFFICIENT+1)));
        }else if(value<=DEFAULT_SLIDER_VALUE+20){
            treeroot.setMinHeight(stackPaneMinHeight * (value/(ZOOM_Y_COEFFICIENT+10)));
            treeroot.setMinWidth(stackPaneMinWidth * (value/(ZOOM_X_COEFFICIENT+2)));
        }else{
            treeroot.setMinHeight(stackPaneMinHeight * (value/(ZOOM_Y_COEFFICIENT+10)));
            treeroot.setMinWidth(stackPaneMinWidth * (value/(ZOOM_X_COEFFICIENT+3)));
        }
    }

    /**
     * Zoom Out StackPane containing the tree
     * @param value slider current value
     */
    public void zoomOut(double value){
        treeroot.setMinHeight(stackPaneMinHeight);
        treeroot.setMinWidth(stackPaneMinWidth);
        treeroot.setScaleX(1 + (-DEFAULT_SLIDER_VALUE + value)*SCALE_COEFFICIENT);
        treeroot.setScaleY(1 + (-DEFAULT_SLIDER_VALUE + value)*SCALE_COEFFICIENT);
    }

    /**
     * Switch Tab pane and display the current Node selected infos
     */
    public void displayNodeInfos(){
        if(tabPane.getSelectionModel().getSelectedItem() != infoTab){
            tabPane.getSelectionModel().select(infoTab);
        }
        Gson g = new Gson();
        TreeVisual.NodeInfoData info = g.fromJson(instance.getInfo(), new TypeToken<TreeVisual.NodeInfoData>(){}.getType());
        infoTableView.getItems().clear();
        if(info != null) {
            ObservableList<Map<String, Object>> items = FXCollections.<Map<String, Object>>observableArrayList();
            Map<String, Object> item1 = new HashMap<>();
            item1.put("Key", "cost");
            item1.put("Value" , info.cost);
            items.add(item1);
            Map<String, Object> item2 = new HashMap<>();
            item2.put("Key", "domain");
            item2.put("Value"  , info.domain);
            items.add(item2);
            Map<String, Object> item3 = new HashMap<>();
            item3.put("Key", "other");
            item3.put("Value" , info.other);
            items.add(item3);
            infoTableView.getItems().addAll(items);
        }
        else{
            infoTableView.setPlaceholder(new Label("Select one Node and press 'I' to display this Node infos."));
        }
    }

    /**
     * Switch Tab pane and display the optimization graph
     */
    public void displayGraph() {
        if(tabPane.getSelectionModel().getSelectedItem()!=graph){
            tabPane.getSelectionModel().select(graph);
        }
    }

    /**
     * Display window to add a bookmark if the seleted node has not yet, otherwise remove it
     */
    public void addOrRemoveBookMarks() throws IOException {

        var allBookMarks = instance.getBookMarks();
        var focusedNode = instance.getFocusedRect();
        String focusedNodeLabel = ( (Text) focusedNode.get(2) ).getText();

        //key is made by concatening "node", nodeId and nodeLabel
        String key = "node"+focusedNode.get(3)+" "+focusedNodeLabel;

        if(!Objects.equals(focusedNodeLabel, " ")){
            if(allBookMarks.containsKey(key)){
                removeBookMarks(key);
                Rectangle r = (Rectangle) instance.getFocusedRect().get(0);
                r.setStrokeWidth(1);
                showBookMarksItem.fire();
            }else{
                displayBookMarkForm();
            }

        }else{
            showInformationAlert("BookMarks", "Please select a node first for adding a bookmark");
        }
    }

    /**
     * Display form to add a bookMark
     */
    public void displayBookMarkForm() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddBookMarksUI.fxml"));
        Parent formRoot = fxmlLoader.load();
        AddBookMarksUI boookMarkController = fxmlLoader.getController();
        boookMarkController.setInstance(instance);

        Scene scene = new Scene(formRoot, 400, 200);

        Stage bookMarkStage = new Stage();
        bookMarkStage.setTitle("BookMarks");
        bookMarkStage.setScene(scene);
        bookMarkStage.toFront();
        bookMarkStage.show();
    }

    /**
     * Switch Tab pane and display the current Node selected bookmark
     */
    public void displayBookMarks() {
        if(tabPane.getSelectionModel().getSelectedItem()!=bookMarksTab){
            tabPane.getSelectionModel().select(bookMarksTab);
        }
        bookMarksTableView.getItems().clear();
        for(Map.Entry<String, String> entry : instance.getBookMarks().entrySet()){
            bookMarksTableView.getItems().add(entry);
        }
    }

    /**
     * Remove a node bookMark
     */
    public void removeBookMarks(String key){
        instance.getBookMarks().remove(key);
    }

    /**
     * General method for showing Alert Information
     * @param headerText Title text
     * @param contentText message to display
     */
    public void showInformationAlert(String headerText, String contentText){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("MiniCP-Profiler");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.initOwner(menuBar.getScene().getWindow());
        alert.showAndWait();
    }

    /**
     * Modify menu text for add the shortcut key
     */
    public void alignMenuItemText() {
        showLabels.setText("Show Labels \t\t\t\t L");
        showInfos.setText("Show Infos \t\t\t\t I");
        showGraph.setText("Show Graph \t\t\t\t O");
        showBookMarksItem.setText("Show BookMarks \t\t\t B");
        manageBookMarksItem.setText("Add/ Remove BookMarks \t Ctrl+B");
    }
}