package org.uclouvain.visualsearchtree.tree;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TreeUIController {
    public Label infoLabel;
    public TabPane tabPane;
    public Tab graph;
    public Tab infoTab;
    public StackPane treeroot;
    public Slider zoomSlider;
    public TableView tableView;
    private TreeVisual instance;

    @FXML
    public ToggleGroup graphType;

    @FXML
    public RadioButton radioOnlySol;

    @FXML
    public RadioButton radioAllNods;

    @FXML
    public VBox chartUI;

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

        // Check if radio btn changed
        graphType.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                RadioButton tmp = (RadioButton)newValue;

                chartUI.getChildren().remove(0);
                if (tmp.getText() == radioAllNods.getText()) {
                    chartUI.getChildren().add(instance.getTreeChart(true));
                    instance.addEventOnChart();
                }else {
                    chartUI.getChildren().add(instance.getTreeChart(false));
                    instance.addEventOnChart();
                }
            }
        });
        initTableInfo();
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

    private void initTableInfo () {
        TableColumn<Map, String> keyColumn = new TableColumn<>("Key");
        keyColumn.setCellValueFactory(new MapValueFactory<>("Key"));
        keyColumn.setMinWidth(150);

        TableColumn<Map, String> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(new MapValueFactory<>("Value"));
        valueColumn.setMinWidth(150);

        tableView.getColumns().add(keyColumn);
        tableView.getColumns().add(valueColumn);
    }

    public void displayNodeInfos(){
        if(tabPane.getSelectionModel().getSelectedItem() != infoTab){
            tabPane.getSelectionModel().select(infoTab);
        }
        Gson g = new Gson();
        TreeVisual.NodeInfoData info = g.fromJson(instance.getInfo(), new TypeToken<TreeVisual.NodeInfoData>(){}.getType());
        tableView.getItems().clear();
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
            tableView.getItems().addAll(items);
        }
        else{
            tableView.setPlaceholder(new Label("Select one Node and press 'I' to display this Node infos."));
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