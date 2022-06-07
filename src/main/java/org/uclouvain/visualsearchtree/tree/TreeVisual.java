package org.uclouvain.visualsearchtree.tree;

import javafx.beans.property.SimpleDoubleProperty;
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

import java.util.ArrayList;
import java.util.List;

public class TreeVisual {

    static List<Integer> legendStats = new ArrayList<>(){{
        add(0);
        add(0);
        add(0);
        add(0);
    }};
    static List<Text> labels = new ArrayList<>(){};

    public static Group getGroup(Tree.Node<String> node) {
        Group root = new Group();

        Tree.PositionedNode<String> pnode = node.design();
        Text nodeLabel = new Text();
        nodeLabel.setTextAlignment(TextAlignment.RIGHT);
        drawNodeRecur(root, pnode, 0.0, 0, nodeLabel);
        return  root;
    }

    public static List<Text> getLabels() {
        return labels;
    }

    public static Rectangle drawNodeRecur(Group g, Tree.PositionedNode<String> root, double center, int depth, Text nLabel) {
        double absolute = center + root.position;

        Rectangle r = createRectangle(400 + absolute * 40, 50 + depth * 50, root.branch);
        styleLabel(nLabel, absolute, depth, root.label);

        //Add Event to each rectangle
        r.setOnMouseClicked(e -> {
            r.setFill(Color.ORANGE);
            //root.nodeAction();
            nLabel.setOpacity((nLabel.getOpacity())==1? 0:1);
            nLabel.setText(root.label);
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
    private static Circle createCircle(double x, double y, double r, Color color) {
        Circle circle = new Circle(x, y, r, color);
        circle.setCursor(Cursor.CROSSHAIR);
        System.out.println("x:"+x +
                "y:"+y);
        return circle;
    }
    private static Rectangle createRectangle(double x, double y, String branch) {
        Rectangle rect = new Rectangle(x,y,20,20);
        rect.setStrokeType(StrokeType.OUTSIDE);
        rect.setStrokeWidth(1);
        rect.setStroke(Color.BLACK);
        rect.setOnMouseEntered(e -> rect.setFill(Color.ORANGE));

        switch (branch) {
            case "BRANCH" -> {
                rect.setArcHeight(40);
                rect.setArcWidth(40);
                rect.setFill(Color.BLUE);
                rect.setOnMouseExited(e -> rect.setFill(Color.BLUE));
                rect.setOnMouseReleased(e -> rect.setFill(Color.BLUE));
                legendStats.set(0, legendStats.get(0) +1);
            }
            case "FAILED" -> {
                rect.setFill(Color.RED);
                rect.setOnMouseExited(e -> rect.setFill(Color.RED));
                rect.setOnMouseReleased(e -> rect.setFill(Color.RED));
                legendStats.set(1, legendStats.get(1) +1);
            }
            case "SOLVED" -> {
                rect.setFill(Color.GREEN);
                rect.setOnMouseExited(e -> rect.setFill(Color.GREEN));
                rect.setOnMouseReleased(e -> rect.setFill(Color.GREEN));
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
        rect.setWidth(20);
        rect.setHeight(20);
        rect.setStrokeType(StrokeType.OUTSIDE);
        rect.setStrokeWidth(1);
        rect.setStroke(Color.BLACK);

        switch (branch) {
            case "BRANCH" -> {
                rect.setArcHeight(40);
                rect.setArcWidth(40);
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
    private static Line connect(Circle c1, Circle c2) {
        Line line = new Line();

        line.startXProperty().bind(c1.centerXProperty());
        line.startYProperty().bind(c1.centerYProperty());

        line.endXProperty().bind(c2.centerXProperty());
        line.endYProperty().bind(new SimpleDoubleProperty(c2.getCenterY()-2));



        line.setStrokeWidth(3);
        return line;
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



}
