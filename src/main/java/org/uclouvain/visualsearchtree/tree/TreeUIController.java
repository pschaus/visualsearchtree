package org.uclouvain.visualsearchtree.tree;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;

public class TreeUIController {

    public Label infoLabel;
    public TabPane tabPane;
    public Tab graph;
    public Tab infoTab;
    public StackPane treeroot;
    public Slider zoomSlider;
    private TreeVisual instance;

    public void setInstance(TreeVisual instance) {
        this.instance = instance;
    }

    public MenuBar menuBar;

    public MenuItem closeMenu;
    public MenuItem showLabels;
    public MenuItem showGaph;
    public MenuItem showInfos;
    public MenuItem about;

    // Menu actions methodes

    public  void init(){
        resize();
        attachEvent();
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
            if(ev.getCode()== KeyCode.Q || ev.getCode() == KeyCode.LEFT || ev.getCode() == KeyCode.RIGHT || ev.getCode() == KeyCode.UP || ev.getCode() == KeyCode.DOWN){
                changeHighlightedNode(ev.getCode());
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

    public void closeWindow(ActionEvent actionEvent) {
        Stage st = (Stage) menuBar.getScene().getWindow();
        st.close();
    }

    public void saveTree(ActionEvent actionEvent) {
        //
    }

    // Helper methodes
    public  void showAllLabels(){
        int length = instance.getLabels().size();
        for (int i = 0; i < length; i++) {
            var element = instance.getLabels().get(i);
            element.setOpacity(element.getOpacity()==1? 0:1);
        }
    }

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
    public void displayGraph() {
        if(tabPane.getSelectionModel().getSelectedItem()!=graph){
            tabPane.getSelectionModel().select(graph);
        }
    }

    public void aboutUs(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("MiniCP-Profiler");
        alert.setHeaderText("About MiniCP-Profiler");
        alert.setContentText("""
                MiniCP-Profiler version 2022.1.0

                Build: javac 18.0.1.1\s
                Runtime version: To be soon precised
                VM: OpenJDK 18 64-Bit IntelliJ IDEA

                Powered by: UC Louvain Belgium
                Copyright © May-June 2022 MiniCP-Profiler
                """);
        alert.initOwner(menuBar.getScene().getWindow());
        alert.showAndWait();
    }


    public void alignMenuItemText(Event event) {
        System.out.println("Been Clicked");
        showLabels.setText("Show Labels \t\t L");
        showInfos.setText("Show Infos \t\t I");
        showGaph.setText("Show graph \t\t O");
    }

    // Aborted functionality: Direction keys are already used for tree navigation.
    // Alternative: Gamer keys.
    // [ lowered priority ]
    private void changeHighlightedNode(KeyCode code) {
        switch (code) {
            case LEFT -> {
                System.out.println(KeyCode.LEFT);
            }
            case RIGHT -> {
                System.out.println(KeyCode.RIGHT);
            }
            case UP -> {
                System.out.println(KeyCode.UP);
            }
            case DOWN -> {
                System.out.println(KeyCode.DOWN);
            }
            default -> {

            }
        }
    }
}