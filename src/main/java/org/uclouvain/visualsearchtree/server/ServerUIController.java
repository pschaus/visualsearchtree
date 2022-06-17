package org.uclouvain.visualsearchtree.server;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.uclouvain.visualsearchtree.tree.Tree;
import javax.swing.*;
import com.google.gson.Gson;
import org.uclouvain.visualsearchtree.tree.VisualTree;

public  class ServerUIController {
    private ArrayList comingTreelist = new ArrayList();
    private ObservableList observableList = FXCollections.observableArrayList();
    private ProfilingData pData;
    private List<Tree.Node> treeListCurrentlyDraw = new ArrayList<>();
    private Stage primaryStage;
    @FXML
    private Label portLabel;

    
    @FXML
    public ListView threelistView;
    @FXML
    public VBox listViewcontainer;
    @FXML
    public void initialize() {
        System.out.println("Profiler UI Started ....");
    }

    // Add a public no-args constructor
    public ServerUIController(){}

    public void addProfilingName(String s){
        comingTreelist.add(s);
        observableList.setAll(comingTreelist);
        threelistView.setItems(observableList);
    }
    public void addProfilingDataListener(ProfilingData pData, Stage primaryStage) {
        this.pData = pData;
        this.primaryStage = primaryStage;
    }
    public void addToTreeDrawList(Tree.Node node) {
        treeListCurrentlyDraw.add(node);
    }

    @FXML
    public void showTree(Event e){
        if(!threelistView.getSelectionModel().isEmpty()) {
            int nodeKey = threelistView.getSelectionModel().getSelectedIndex();
            Tree.Node<String> profilingNode = pData.getProfilingNodesList().get(nodeKey);
            if(!treeListCurrentlyDraw.contains(profilingNode)) {
                VisualTree.treeProfilerLauncher(profilingNode, primaryStage);
                addToTreeDrawList(profilingNode);
            }
        }
    }

    public void fileSaver(Tree.Node<String> profilingNode) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose location To Save profiling Data");
        fileChooser.requestFocus();

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String absPath = fileToSave.getAbsolutePath();

            File file = new File(absPath + ".mcp");
            PrintWriter outFile = null;
            try {
                outFile = new PrintWriter(file);
            } catch (FileNotFoundException ev) {
                ev.printStackTrace();
            }
            System.out.println(profilingNode.toString());
            // TODO : SAVE DATA ON CORRECT FORMAT HERE - DONE(✅️)
            outFile.println(new Gson().toJson(profilingNode,  new TypeToken<Tree.Node<String>>(){}.getType()));
            // ---------------------------------------
            outFile.close();
        }
    }
    @FXML
    public void saveTree(Event e){
        if(!threelistView.getSelectionModel().isEmpty()) {
            int nodeKey = threelistView.getSelectionModel().getSelectedIndex();
            Tree.Node<String> profilingNode = pData.getProfilingNodesList().get(nodeKey);
            System.out.println(profilingNode.toString());

            // SAVE FILE
            fileSaver(profilingNode);
        }
    }

    /*
    * Load an old file
    * */
    private void fileLoader() {
        // TODO : DECODE FILE HERE AND CONVERT TO TREE.NODE - DONE(✅️)
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select minicp profiling backup file");
        fileChooser.requestFocus();
        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToRead = fileChooser.getSelectedFile();

            try {
                Reader reader = Files.newBufferedReader(Paths.get(fileToRead.getAbsolutePath()));
                Gson gson = new Gson();
                Tree.Node<String> nodeFromFile = gson.fromJson(reader, new TypeToken<Tree.Node<String>>(){}.getType());

                pData.addToProfilingNameList("<old> " + nodeFromFile.getLabel());
                if(!pData.getProfilingNodesList().contains(nodeFromFile)) {
                    pData.addSilentlyToProfilingNodesList(nodeFromFile);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @FXML
    public void loadTree(Event e){
        fileLoader();
    }
}