package org.uclouvain.visualsearchtree.tree;


import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class TreePane2 extends StackPane {

    public TreePane2(Tree2.Node<String> node) {
        Group root = new Group();
        Tree2.PositionedNode<String> pnode = node.design();
        drawNode(root, pnode, 0.0, 0);
        AnimationFactory.zoomOnSCroll(this);
        this.getChildren().add(root);
    }

    double orgSceneX, orgSceneY;

    private EventHandler<MouseEvent> mousePressedEventHandler = (t) ->
    {
        orgSceneX = t.getSceneX();
        orgSceneY = t.getSceneY();

        // bring the clicked circle to the front

        Circle c = (Circle) (t.getSource());
        c.toFront();
    };

    private EventHandler<MouseEvent> mouseDraggedEventHandler = (t) ->
    {
        double offsetX = t.getSceneX() - orgSceneX;
        double offsetY = t.getSceneY() - orgSceneY;

        Circle c = (Circle) (t.getSource());

        c.setCenterX(c.getCenterX() + offsetX);
        c.setCenterY(c.getCenterY() + offsetY);

        orgSceneX = t.getSceneX();
        orgSceneY = t.getSceneY();
    };

    private Circle createCircle(double x, double y, double r, Color color) {
        Circle circle = new Circle(x, y, r, color);

        circle.setCursor(Cursor.CROSSHAIR);

        circle.setOnMousePressed(mousePressedEventHandler);
        circle.setOnMouseDragged(mouseDraggedEventHandler);

        return circle;
    }

    private Line connect(Circle c1, Circle c2) {
        Line line = new Line();

        line.startXProperty().bind(c1.centerXProperty());
        line.startYProperty().bind(c1.centerYProperty());

        line.endXProperty().bind(c2.centerXProperty());
        line.endYProperty().bind(c2.centerYProperty());

        line.setStrokeWidth(1);
        //line.setStrokeLineCap(StrokeLineCap.BUTT);
        //line.getStrokeDashArray().setAll(1.0, 4.0);

        return line;
    }

    static Random rand = new Random(2);

    public static Tree2.Node<String> randomTree(int depth) {


        int nChildren = 2;


        if ((rand.nextInt(100) < 50 && depth > 3)) {
            nChildren = 0;
        }

        List<Tree2.Node<String>> children = new LinkedList<>();
        List<String> labels = new LinkedList<>();

        for (int i = 0; i < nChildren; i++) {
            children.add(randomTree(depth + 1));
            labels.add("x = " + i);
        }
        return new Tree2.Node<String>("Node" + depth, children, labels, null);
    }

    public Circle drawNode(Group g, Tree2.PositionedNode<String> node, double center, int depth) {
        // circles
        double absolute = center + node.position;
        Circle circle = createCircle(400 + absolute * 5, 50 + depth * 20, 2, Color.RED);
        g.getChildren().add(circle);
        for (Tree2.PositionedNode<String> child : node.children) {
            Circle childCircle = drawNode(g, child, absolute, depth + 1);
            Line line = connect(circle, childCircle);
            g.getChildren().add(line);
        }
        circle.toFront();
        return circle;
    }


}