package org.uclouvain.visualsearchtree.tree;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.*;

public class Anchor extends Rectangle {
    DoubleProperty x, y;
    Tree.Node<String> node;
    int nbChild;
    int depth;
    NodeAction nodeAction;
    Anchor parent = null;
    List<Anchor> children = new ArrayList<>();

    Anchor()
    {
        super();
    }
    /**
     *
     * @param x
     * @param y
     */
    Anchor(DoubleProperty x, DoubleProperty y, Tree.Node<String> mNode) {
        super(x.get(), y.get(), 20, 20);
        this.x = x;
        this.y = y;
        nbChild = 0;
        depth = 0;
        this.node = mNode;
        //setFill(color.deriveColor(1, 1, 1, 0.5));
        setFill(Color.OLIVE);
        setStroke(Color.BLACK.deriveColor(0, 1, 1, 0.5));
        setStrokeWidth(1);
        setStrokeType(StrokeType.OUTSIDE);
        x.bind(xProperty());
        y.bind(yProperty());
        enableDrag();
    }


    /**
     * Add new child to the node and return a Group
     * @param child_node
     * @return
     */
    public Group addChild(Tree.Node<String> child_node)
    {
        nbChild++;
        DoubleProperty endX = new SimpleDoubleProperty(10);
        DoubleProperty endY   = new SimpleDoubleProperty(10);
        Anchor child = new Anchor(endX, endY, child_node);
        Line line = new BoundLine(x.add(child.getWidth()/2), y.add(0), endX.add(child.getWidth()/2), endY.add(0));
        child.depth = depth + 1;
        child.parent = this;
        children.add(child);
        return new Group(child, line);
    }

    /**
     *
     * @param root
     * @param anchMap
     * @param center
     * @param depth
     */
    public static void positionNode(Tree.PositionedNode<String> root, Map<Integer, Anchor> anchMap, double center, int depth)
    {
        double absolute = center + root.position;
        if (anchMap.get(root.nodeId) == null)
            return;
        anchMap.get(root.nodeId).setX(400 + absolute * 40);
        anchMap.get(root.nodeId).setY(depth * 50);
        for (Tree.PositionedNode<String> child : root.children)
        {
            positionNode(child, anchMap, absolute, depth+1);
        }
    }

    /**
     * make a node movable by dragging it around with the mouse.
     */
    private void enableDrag() {
        final Delta dragDelta = new Delta();
        setOnMousePressed(mouseEvent -> {
            // record a delta distance for the drag and drop operation.
            dragDelta.x = getX() - mouseEvent.getX();
            dragDelta.y = getY() - mouseEvent.getY();
            getScene().setCursor(Cursor.MOVE);
        });
        setOnMouseReleased(mouseEvent -> getScene().setCursor(Cursor.HAND));
        setOnMouseDragged(mouseEvent -> {
            double newX = mouseEvent.getX() + dragDelta.x;
            if (newX > 0 && newX < getScene().getWidth()) {
                setX(newX);
            }
            double newY = mouseEvent.getY() + dragDelta.y;
            if (newY > 0 && newY < getScene().getHeight()) {
                setY(newY);
            }
        });
        setOnMouseEntered(mouseEvent -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.HAND);
            }
        });
        setOnMouseExited(mouseEvent -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.DEFAULT);
            }
        });
    }
    private class Delta { double x, y; }
}

class BoundLine extends Line {
    BoundLine(DoubleBinding startX, DoubleBinding startY, DoubleBinding endX, DoubleBinding endY) {
        startXProperty().bind(startX);
        startYProperty().bind(startY);
        endXProperty().bind(endX);
        endYProperty().bind(endY);
        setStrokeWidth(2);
        setStroke(Color.BLACK.deriveColor(0, 1, 1, 0.5));
        setStrokeLineCap(StrokeLineCap.BUTT);
        setMouseTransparent(true);
    }
}