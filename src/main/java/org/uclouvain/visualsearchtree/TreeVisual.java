package org.uclouvain.visualsearchtree;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class TreeVisual {

    public static Group getGroup(Tree.Node<String> node) {
        Group root = new Group();
        Tree.PositionedNode<String> pnode = node.design();
        drawNodeRecur(root, pnode, 0.0, 0);
        return  root;
    }

    public static Circle drawNodeRecur(Group g, Tree.PositionedNode<String> root, double center, int depth) {
        // circles
        double absolute = center + root.position;
        Circle circle = createCircle(400 + absolute * 5, 50 + depth * 10, 2, Color.RED);
        g.getChildren().add(circle);
        for (Tree.PositionedNode<String> child : root.children) {
            Circle childCircle = drawNodeRecur(g, child, absolute, depth + 1);
            Line line = connect(circle, childCircle);
            g.getChildren().add(line);
        }
        circle.toFront();
        return circle;
    }

    private static Circle createCircle(double x, double y, double r, Color color) {
        Circle circle = new Circle(x, y, r, color);
        circle.setCursor(Cursor.CROSSHAIR);
        return circle;
    }

    private static Line connect(Circle c1, Circle c2) {
        Line line = new Line();

        line.startXProperty().bind(c1.centerXProperty());
        line.startYProperty().bind(c1.centerYProperty());

        line.endXProperty().bind(c2.centerXProperty());
        line.endYProperty().bind(c2.centerYProperty());

        line.setStrokeWidth(1);

        return line;
    }

}
