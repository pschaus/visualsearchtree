package org.uclouvain.visualsearchtree.tree;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class ExampleTextFx extends Scene {

    private int numberOfClicks = 0;

    public ExampleTextFx() {
        super(new GridPane(), 300, 300);
        GridPane grid = (GridPane)getRoot();

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // This button will toggle to a different label once it has been clicked
        Button firstButton = new Button("click me to change my name!");
        firstButton.setId("first-button");
        firstButton.setOnAction(actionEvent -> firstButton.setText("clicked!"));

        grid.add(firstButton, 0, 0);

        // this button will report the number of times it has been clicked.
        Button secondButton = new Button("0 clicks");
        secondButton.setId("second-button");
        secondButton.setOnAction(actionEvent -> secondButton.setText( ++numberOfClicks + " clicks"));

        grid.add(secondButton, 0, 1);

    }
}
