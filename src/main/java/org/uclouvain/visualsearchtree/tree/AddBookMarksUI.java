package org.uclouvain.visualsearchtree.tree;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddBookMarksUI {
    public TextArea nodeBookmark;
    private TreeVisual instance;

    public Button cancelButton;


    public void setInstance(TreeVisual instance) {
        this.instance = instance;
    }

    public void addBookMarksToNode(ActionEvent actionEvent){
        String bookMark = nodeBookmark.getText();
        var focusedNode = instance.getFocusedRect();
        String nodeLabel = ((Text) focusedNode.get(2)).getText();
        if(bookMark.trim().equals("")){
            displayAlertError("The BookMark value can not be null");
        }else{
            //key is made by concatening "node", nodeId and nodeLabel
            String key = "node"+focusedNode.get(3)+" "+nodeLabel;
            instance.setBoookMarks(key,bookMark);
            Rectangle r = (Rectangle) focusedNode.get(0);
            r.setStrokeWidth(3);
            closeWindow();
            // Update the bookmarks tableview by simulating pressing the B key
            Robot robot = new Robot();
            robot.keyPress(KeyCode.B);
        }
    }

    public void closeFormWindow(ActionEvent actionEvent) {
        closeWindow();
    }

    public void closeWindow(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void displayAlertError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("MiniCP-Profiler");
        alert.setContentText(message);
        alert.initOwner(cancelButton.getScene().getWindow());
        alert.showAndWait();
    }
}
