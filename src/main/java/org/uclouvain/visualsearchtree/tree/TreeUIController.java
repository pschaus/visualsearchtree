package org.uclouvain.visualsearchtree.tree;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class TreeUIController {


    private TreeVisual instance;
    public StackPane treeroot;
    public Slider zoomSlider;

    //Menu items variables
    public MenuBar menuBar;
    public MenuItem manageBookMarksItem;
    public MenuItem showBookMarksItem;
    public MenuItem closeMenu;
    public MenuItem showLabels;
    public MenuItem showGaph;
    public MenuItem showInfos;
    public MenuItem about;

    //Tab variables
    public TabPane tabPane;
    public Tab graph;
    public Tab infoTab;
    public HBox tableHbox;
    public Tab bookMarksTab;
    public Label infoLabel;
    public TableView<Map.Entry<String,String>> allBookMarks;



    public void setInstance(TreeVisual instance) {
        this.instance = instance;
    }

    /**
     * Initialize window
     */
    public  void init(){
        resize();
        alignMenuItemText();
        attachEvent();
        initBookMarksTable();
    }

    /**
     * Resize the window in terms of the tree size
     */
    public void resize(){
        int depth = instance.getLegendStats().get(3);
        if(depth>4){
            treeroot.setMinHeight(depth*60);
            treeroot.setMinWidth(depth*130);
        }
    }

    /**
     * Attaching Event for Menu items and for Slider for zoom Effect
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

        zoomSlider.valueChangingProperty().addListener((observableValue, aBoolean, t1) -> {
            treeroot.setPrefSize(400+zoomSlider.getValue(), 400+zoomSlider.getValue());
            treeroot.setScaleX(1 + zoomSlider.getValue()*0.01);
            treeroot.setScaleY(1 + zoomSlider.getValue()*0.01);
        });
    }

    public void showNodeLabels(ActionEvent actionEvent) {
        showAllLabels();
    }
    public void showNodeInfos(Event actionEvent) {
        displayNodeInfos();
    }
    public void showGraph(Event actionEvent){
        displayGraph();
    }
    public void saveTree(ActionEvent actionEvent) {
        //
    }
    /**
     * Show Form for adding bookmark or remove bookmark from a node if it has already been marked
     * @param actionEvent Event
     * @throws IOException Exception
     */
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

    public void closeWindow(ActionEvent actionEvent) {
        Stage st = (Stage) menuBar.getScene().getWindow();
        st.close();
    }

    /* Intermediates methodes */
    public  void showAllLabels(){
        int length = instance.getLabels().size();
        for (int i = 0; i < length; i++) {
            var element = instance.getLabels().get(i);
            element.setOpacity(element.getOpacity()==1? 0:1);
        }
    }

    /*Change selection for TabPane and display infos for selected Node*/
    public void displayNodeInfos(){
        if(tabPane.getSelectionModel().getSelectedItem()!=infoTab){
            tabPane.getSelectionModel().select(infoTab);
        }
        Gson g = new Gson();
        TreeVisual.NodeInfoData info = g.fromJson(instance.getInfo(), new TypeToken<TreeVisual.NodeInfoData>(){}.getType());
        if(info!=null){
            infoLabel.setText(info.other);
        }
        else{
            if(Objects.equals(((Text) instance.getFocusedRect().get(2)).getText(), "root")){
                infoLabel.setText("Root node selected\n No infos to display");
            }
            else{
                infoLabel.setText("No node selected!\n Please select a node yet.");
            }
        }
    }

    /* Change selection for TabPane and display graph optimization*/
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
        allBookMarks.getItems().clear();
        for(Map.Entry<String, String> entry : instance.getBoookMarks().entrySet()){
            allBookMarks.getItems().add(entry);
        }
    }
    /**
     * @param key String
     */
    public void removeBookMarks(String key){
        instance.getBoookMarks().remove(key);
        //Delete mark point on the node after here
    }
    /**
     *Show Alert for displaying information
     * @param headerText String
     * @param contentText String
     */
    public void showInformationAlert(String headerText, String contentText){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("MiniCP-Profiler");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.initOwner(menuBar.getScene().getWindow());
        alert.showAndWait();
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
        allBookMarks = new TableView<>(items);
        allBookMarks.setMaxWidth(400);
        allBookMarks.setPrefHeight(200);

        allBookMarks.getColumns().setAll(idColumn, valueColumn);
        tableHbox.getChildren().add(allBookMarks);
    }
    public void alignMenuItemText() {
        showLabels.setText("Show Labels \t\t\t\t L");
        showInfos.setText("Show Infos \t\t\t\t I");
        showGaph.setText("Show Graph \t\t\t\t O");
        showBookMarksItem.setText("Show BookMarks \t\t\t B");
        manageBookMarksItem.setText("Add/ Remove BookMarks \t Ctrl+B");
    }
    

}