package org.uclouvain.visualsearchtree.tree;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.uclouvain.visualsearchtree.tree.events.BackToNormalEvent;
import org.uclouvain.visualsearchtree.tree.events.BackToNormalEventHandler;
import org.uclouvain.visualsearchtree.tree.events.CustomEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TreeVisual {

    static List<Integer> legendStats = new ArrayList<>(){{
        add(0);
        add(0);
        add(0);
        add(0);
    }};
    static List<Text> labels = new ArrayList<>(){};
    static String info = "";

    public static List focusedRect = new ArrayList<>(){{
        add(new Rectangle());
        add(" ");
    }};

    public static Group getGroup(Tree.Node<String> node) {
        Group root = new Group();

        Tree.PositionedNode<String> pnode = node.design();
        Text nodeLabel = new Text();
        nodeLabel.setTextAlignment(TextAlignment.RIGHT);
        drawNodeRecur(root, pnode, 0.0, 0, nodeLabel);
        return  root;
    }

    public static Rectangle drawNodeRecur(Group g, Tree.PositionedNode<String> root, double center, int depth, Text nLabel) {
        double absolute = center + root.position;

        Rectangle r = createRectangle(400 + absolute * 40, 50 + depth * 50, root.branch);
        styleLabel(nLabel, absolute, depth, root.label);

        //Add Event to each rectangle
        r.setOnMouseClicked(e -> {
            //root.nodeAction();
            r.fireEvent(new BackToNormalEvent());
            r.setFill(Color.ORANGE);
            nLabel.setOpacity((nLabel.getOpacity())==1? 0:1);
            nLabel.setText(root.label);
            info = String.valueOf(root.label);
            focusedRect.set(0, r);
            focusedRect.set(1, root.branch);
        });

        g.getChildren().add(r);
        g.getChildren().add(nLabel);

        for (Tree.PositionedNode<String> child : root.children) {
            Rectangle childR = drawNodeRecur(g, child, absolute, depth + 1, new Text());
            Line line = connectRectangle(r, childR);
            g.getChildren().add(line);

            //Make rectangle toFront
            r.toFront();
            childR.toFront();
        }

        if (depth > legendStats.get(3)) {
            legendStats.set(3, depth);
        }

        return r;
    }

    private static Rectangle createRectangle(double x, double y, String branch) {
        Rectangle rect = new Rectangle(x,y,20,20);
        rect.setStrokeType(StrokeType.OUTSIDE);
        rect.setStrokeWidth(1);
        rect.setStroke(Color.BLACK);
        rect.addEventHandler(CustomEvent.CUSTOM_EVENT_TYPE, new BackToNormalEventHandler() {
            @Override
            public void unClick() {
                makeNotFocus();
            }
        });

        switch (branch) {
            case "BRANCH" -> {
                rect.setArcHeight(40);
                rect.setArcWidth(40);
                rect.setFill(Color.BLUE);
                legendStats.set(0, legendStats.get(0) +1);
            }
            case "FAILED" -> {
                rect.setFill(Color.RED);
                legendStats.set(1, legendStats.get(1) +1);
            }
            case "SOLVED" -> {
                rect.setFill(Color.GREEN);
                rect.setRotate(45);
                legendStats.set(2, legendStats.get(2) +1);
            }
            default -> {
            }
        }
        rect.setCursor(Cursor.CROSSHAIR);
        return rect;
    }

    private static Rectangle createRectangleForLegendBox(String branch) {
        Rectangle rect = new Rectangle();
        rect.setWidth(18);
        rect.setHeight(18);
        rect.setStrokeType(StrokeType.OUTSIDE);
        rect.setStrokeWidth(1);
        rect.setStroke(Color.BLACK);

        switch (branch) {
            case "BRANCH" -> {
                rect.setArcHeight(36);
                rect.setArcWidth(36);
                rect.setFill(Color.BLUE);
            }
            case "FAILED" -> {
                rect.setFill(Color.RED);
            }
            case "SOLVED" -> {
                rect.setFill(Color.GREEN);
                rect.setRotate(45);
            }
            default -> {
            }
        }
        return rect;
    }

    private static void styleLabel(Text theLabel, double absolute , double depth, String content){

        theLabel.setFont(Font.font("verdana", 10));
        theLabel.setFill(Color.GRAY);
        theLabel.setX(400 + absolute * 40);
        theLabel.setY( 45+ depth * 50);
        theLabel.setOpacity(0);
        theLabel.setText(content);
        labels.add(theLabel);

    }
    private static Line connectRectangle(Rectangle r1, Rectangle r2) {
        Line line = new Line();
        line.setStartX(r1.getX()+r1.getWidth()/2);
        line.setStartY(r1.getY());

        line.setEndX(r2.getX()+r2.getWidth()/2);
        line.setEndY(r2.getY());

        line.setStrokeWidth(1);

        return line;
    }


    public static HBox generateLegendsStack(){
        HBox hbox = new HBox();
        hbox.setMaxWidth(500);
        hbox.setPadding(new Insets(10));
        hbox.setAlignment(Pos.BASELINE_CENTER);
        Rectangle branchRect = createRectangleForLegendBox("BRANCH");
        Rectangle solvedRect = createRectangleForLegendBox("SOLVED");
        Rectangle failedRect = createRectangleForLegendBox("FAILED");
        FlowPane s1 = new FlowPane();
        FlowPane s2 = new FlowPane();
        FlowPane s3 = new FlowPane();
        s1.getChildren().addAll(new  Text("BRANCH => "), branchRect, new Text(" : "+ legendStats.get(0)));
        s2.getChildren().addAll(new  Text("FAILED => "), failedRect, new Text(" : "+ legendStats.get(1)));
        s3.getChildren().addAll(new  Text("SOLVED =>  "), solvedRect, new Text(" : "+ legendStats.get(2)));
        hbox.getChildren().addAll(s1,s2,s3,new  Text("DEPTH : "+ legendStats.get(3)));
        return hbox;
    }


    public static void makeNotFocus(){
        System.out.println("I'm in the event handler");
        var r = (Rectangle) TreeVisual.focusedRect.get(0);
        var branch = (String) TreeVisual.focusedRect.get(1);
        if(!Objects.equals(branch, " ")){
            switch (branch) {
                case "BRANCH" -> {
                    r.setFill(Color.BLUE);
                }
                case "FAILED" -> {
                    r.setFill(Color.RED);
                }
                case "SOLVED" -> {
                    r.setFill(Color.GREEN);
                }
                default -> {
                }
            }
        }

    }
}