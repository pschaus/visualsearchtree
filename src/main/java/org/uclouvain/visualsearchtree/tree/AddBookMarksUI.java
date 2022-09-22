package org.uclouvain.visualsearchtree.tree;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.robot.Robot;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddBookMarksUI {
    public TextArea nodeBookmark;
    private TreeVisual instance;

    public Button cancelButton;

    /**
     * <b>Note: </b>For having an instance of TreeVisual in this class in order to exploit some of its properties
     * @param instance Tree Visual instance
     */
    public void setInstance(TreeVisual instance) {
        this.instance = instance;
    }

    /**
     * <b>Note: </b>Add BookMark to the list of all bookmark and change the node style
     * @param actionEvent event on Add button clicked
     */
    public void addBookMarksToNode(ActionEvent actionEvent){
        String bookMark = nodeBookmark.getText();
        var focusedNode = instance.getFocusedRect();
        String nodeLabel = ((Text) focusedNode.get(2)).getText();
        if(bookMark.trim().equals("")){
            displayAlertError("The BookMark value can not be null");
        }else{
            //key is made by concatening "node", nodeId and nodeLabel
            String key = "node"+focusedNode.get(3)+" "+nodeLabel;
            instance.setBookMarks(key,bookMark);
            Anchor r = (Anchor) focusedNode.get(0);
            r.setStrokeWidth(3);
            closeWindow();
            // Update the bookmarks tableview by simulating pressing the B key --- Only for Windows and Linux
            if(!System.getProperty("os.name").contains("MAC")){
                Robot robot = new Robot();
                robot.keyPress(KeyCode.B);
            }
        }
    }

    /**
     * <b>Note: </b>Close the form window one time Cancel Button been clicked
     * @param actionEvent event on Close Button clicked
     */
    public void closeFormWindow(ActionEvent actionEvent) {
        closeWindow();
    }

    public void closeWindow(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * <b>Note: </b>Display Error relative to this functionality
     * @param message Error message
     */
    public void displayAlertError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("VisualSearchTree-Profiler");
        alert.setContentText(message);
        alert.initOwner(cancelButton.getScene().getWindow());
        alert.showAndWait();
    }
}
