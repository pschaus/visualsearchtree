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

    public void setInstance(TreeVisual instance) {
        this.instance = instance;
    }

    // Init methods
    public  void init(){
        resize();
        alignMenuItemText();
        attachEvent();

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
        initTableInfo();
        initBookMarksTable();
    }
    private void initTableInfo () {
        TableColumn<Map, String> keyColumn = new TableColumn<>("Key");
        keyColumn.setCellValueFactory(new MapValueFactory<>("Key"));
        keyColumn.setMinWidth(150);

        TableColumn<Map, String> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(new MapValueFactory<>("Value"));
        valueColumn.setMinWidth(150);

        infoTableView.getColumns().add(keyColumn);
        infoTableView.getColumns().add(valueColumn);
    }
    public void initBookMarksTable(){
        Map<String, String> bookMarksMap = instance.getBoookMarks();

        TableColumn<Map.Entry<String, String>, String> idColumn = new TableColumn<>("Node Id");
        idColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        idColumn.setMinWidth(100);

        TableColumn<Map.Entry<String, String>, String> valueColumn = new TableColumn<>("BookMarks");
        valueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue()));
        valueColumn.setMinWidth(300);

        ObservableList<Map.Entry<String, String>> items = FXCollections.observableArrayList(bookMarksMap.entrySet());
        bookMarksTableView = new TableView<>(items);
        bookMarksTableView.setMinHeight(150);
        bookMarksTableView.setPlaceholder(new Label("No bookmarks added. Please click CTRL+B to add a bookmark to a node"));
        bookMarksTableView.prefWidthProperty().bind(tableHbox.widthProperty());
        bookMarksTableView.setMaxHeight(Double.MAX_VALUE);

        bookMarksTableView.getColumns().setAll(idColumn, valueColumn);
        tableHbox.getChildren().add(bookMarksTableView);
    }

    // Menu methods
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
    public void saveTree(ActionEvent actionEvent) {
        //
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
    public  void showAllLabels(){
        int length = instance.getLabels().size();
        for (int i = 0; i < length; i++) {
            var element = instance.getLabels().get(i);
            element.setOpacity(element.getOpacity()==1? 0:1);
        }
    }
    public void resize(){
        int depth = instance.getLegendStats().get(3);
        if(depth>4){
            treeroot.setMinHeight(depth*60);
            treeroot.setMinWidth(depth*130);
        }
    }
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

        zoomSlider.valueChangingProperty().addListener((observableValue, aBoolean, t1) -> {
            treeroot.setMinHeight(treeroot.getMinHeight()+zoomSlider.getValue()*ZOOM_COEFFICIENT);
            treeroot.setMinWidth(treeroot.getMinWidth()+zoomSlider.getValue()*ZOOM_COEFFICIENT*3);
            treeroot.setScaleX(1 + zoomSlider.getValue()*SCALE_COEFFICIENT);
            treeroot.setScaleY(1 + zoomSlider.getValue()*SCALE_COEFFICIENT);
        });
    }
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
            item2.put("Key", "param1");
            item2.put("Value"  , info.param1);
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
    public void displayGraph() {
        if(tabPane.getSelectionModel().getSelectedItem()!=graph){
            tabPane.getSelectionModel().select(graph);
        }
    }
    public void addOrRemoveBookMarks() throws IOException {
        Text focusedNodeLabel = (Text) instance.getFocusedRect().get(2);
        var allBookMarks = instance.getBoookMarks();

        if(!Objects.equals(focusedNodeLabel.getText(), " ")){
            if(allBookMarks.containsKey(focusedNodeLabel.getText())){
                removeBookMarks(focusedNodeLabel.getText());
                Rectangle r = (Rectangle) instance.getFocusedRect().get(0);
                r.setStrokeWidth(1);
            }else{
                displayBookMarkForm();
            }

        }else{
            showInformationAlert("BookMarks", "Please select a node first for adding a bookmark");
        }
    }
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
    private void displayBookMarks() {
        if(tabPane.getSelectionModel().getSelectedItem()!=bookMarksTab){
            tabPane.getSelectionModel().select(bookMarksTab);
        }
        bookMarksTableView.getItems().clear();
        for(Map.Entry<String, String> entry : instance.getBoookMarks().entrySet()){
            bookMarksTableView.getItems().add(entry);
        }
    }
    public void removeBookMarks(String key){
        instance.getBoookMarks().remove(key);
        //Delete mark point on the node after here
    }
    public void showInformationAlert(String headerText, String contentText){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("MiniCP-Profiler");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.initOwner(menuBar.getScene().getWindow());
        alert.showAndWait();
    }
    public void alignMenuItemText() {
        showLabels.setText("Show Labels \t\t\t\t L");
        showInfos.setText("Show Infos \t\t\t\t I");
        showGraph.setText("Show Graph \t\t\t\t O");
        showBookMarksItem.setText("Show BookMarks \t\t\t B");
        manageBookMarksItem.setText("Add/ Remove BookMarks \t Ctrl+B");
    }
}