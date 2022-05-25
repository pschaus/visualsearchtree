package org.uclouvain.visualsearchtree;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TreeVisual {

    public static Group getGroup(Tree.Node<String> node) {
        Group root = new Group();
        Tree.PositionedNode<String> pnode = node.design();
        drawNodeRecur(root, pnode, 0.0, 0);
        return  root;
    }

    public static Rectangle drawNodeRecur(Group g, Tree.PositionedNode<String> root, double center, int depth) {
        // circles
        double absolute = center + root.position;
        System.out.println("root Position: "+root.position);

        Rectangle r = createRectangle(400 + absolute * 10, 50 + depth * 20, root.branch);
        r.setOnMouseClicked(e -> {
            root.nodeAction();
        });

        g.getChildren().add(r);
        for (Tree.PositionedNode<String> child : root.children) {
            Rectangle childR = drawNodeRecur(g, child, absolute, depth + 1);
            Line line = connectRectangle(r, childR);
            g.getChildren().add(line);
            r.toFront();
            childR.toFront();

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
        Rectangle rect = new Rectangle();
        rect.setX(x);
        rect.setY(y);
        rect.setWidth(5);
        rect.setHeight(5);

        switch (branch){
            case "BRANCH":{
                rect.setArcHeight(10);
                rect.setArcWidth(10);
                rect.setFill(Color.BLUE);
                break;
            }
            case "FAILED":{
                rect.setFill(Color.RED);
                break;
            }
            case "SOLVED":{
                rect.setFill(Color.GREEN);
                rect.setRotate(45);
                break;
            }
            default:break;

        }

        rect.setCursor(Cursor.CROSSHAIR);
        return rect;
    }

    private static Line connect(Circle c1, Circle c2) {
        Line line = new Line();

        line.startXProperty().bind(c1.centerXProperty());
        line.startYProperty().bind(c1.centerYProperty());

        line.endXProperty().bind(c2.centerXProperty());
        line.endYProperty().bind(new SimpleDoubleProperty(c2.getCenterY()-2));



        line.setStrokeWidth(0.2);
        return line;
    }
    private static Line connectRectangle(Rectangle r1, Rectangle r2) {
        Line line = new Line();
        line.setStartX(r1.getX()+r1.getWidth()/2);
        line.setStartY(r1.getY());

        line.setEndX(r2.getX()+r2.getWidth()/2);
        line.setEndY(r2.getY());

        line.setStrokeWidth(0.2);

        return line;
    }



}
