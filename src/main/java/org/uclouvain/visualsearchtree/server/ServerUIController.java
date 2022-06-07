package org.uclouvain.visualsearchtree.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import org.uclouvain.visualsearchtree.tree.Tree;

public  class ServerUIController {
    private ArrayList comingTreelist = new ArrayList();
    private ObservableList observableList = FXCollections.observableArrayList();
    private ProfilingData pData;
    
    
    @FXML
    public ListView threelistView;

    @FXML
    private Label portLabel;

    @FXML
    public VBox listViewcontainer;
    
    @FXML
    public void initialize() {
        System.out.println("Profiler UI Started ....");
    }

    // Add a public no-args constructor
    public ServerUIController()
    {
    }
    
    @FXML
    public void showTree(Event e){
        comingTreelist.add("THREE : TEST INPUT");
        observableList.setAll(comingTreelist);
        threelistView.setItems(observableList);
    }

    public void addProfilingName(String s){
        comingTreelist.add(s);
        observableList.setAll(comingTreelist);
        threelistView.setItems(observableList);
    }

    @FXML
    public void saveTree(Event e){
        if(!threelistView.getSelectionModel().isEmpty()) {
            int nodeKey = threelistView.getSelectionModel().getSelectedIndex();
            Tree.Node<String> profilingNode = pData.getProfilingNodesList().get(nodeKey);
            System.out.println(profilingNode.toString());
        }
        // return nodeKey;
    }
    
    public void addProfilingDataListener(ProfilingData pData) {
        this.pData = pData;
    }

    @FXML
    public void loadTree(Event e){
        
    }
}