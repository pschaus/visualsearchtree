package org.uclouvain.visualsearchtree.tree;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;

import java.util.ArrayList;
import java.util.List;

public class Anchor extends Circle {
    DoubleProperty x, y;
    int nbChild;
    int depth;
    Anchor parent = null;
    List<Anchor> children = new ArrayList<>();
    List<DoubleProperty> levels = null;
    int position = 0;

    Anchor()
    {
        super();
    }
    /**
     *
     * @param color
     * @param x
     * @param y
     */
    Anchor(Color color, DoubleProperty x, DoubleProperty y) {
        super(x.get(), y.get(), 10);
        this.x = x;
        this.y = y;
        nbChild = 0;
        depth = 0;
        setFill(color.deriveColor(1, 1, 1, 0.5));
        setStroke(color);
        setStrokeWidth(2);
        setStrokeType(StrokeType.OUTSIDE);
        levels = new ArrayList<>();
        levels.add(this.x);
        x.bind(centerXProperty());
        y.bind(centerYProperty());
        enableDrag();
    }

    public Group addChild()
    {
        nbChild++;

        DoubleProperty endX;
        DoubleProperty endY   = new SimpleDoubleProperty(getCenterY() + 50);
        endX = new SimpleDoubleProperty(levels.get(depth).get() + 30);
        Anchor child = new Anchor(Color.RED, endX, endY);
        Line line = new BoundLine(x, y, endX, endY);

        child.depth = depth + 1;
        child.parent = this;
        child.levels = levels;
        child.position = nbChild;

        levels.set(depth, endX);
        if ((depth + 1) >= levels.size()) {levels.add(endX);}

        //test something
        Anchor _parent = this;
        while (_parent.parent != null){ _parent = _parent.parent;}
        children.add(child);
        child.recenterParent();
        return new Group(child, line);
    }

    /**
     *
     */
    private void recenterParent()
    {
        Anchor _parent = parent;
        if ( _parent == null || _parent.children.size() == 0){return;}

        double val = parent.getCenterX();
        double posX = _parent.children.get(0).getCenterX() + _parent.children.get(_parent.children.size()-1).getCenterX();
        _parent.setCenterX(posX/2);
        val = (parent.getCenterX() - val);
        Anchor __parent = this;
        while (__parent.parent != null){ __parent = __parent.parent;}
        __parent.moveSibling(_parent.depth, val, _parent);
    }

    /**
     *
     * @param _depth
     * @param val
     * @param src
     */
    private void moveSibling(int _depth, Number val, Anchor src)
    {
        for (Anchor anchor : children) {
            if (_depth == anchor.depth && anchor != src) {
                Anchor child = anchor;
                if (src.position < child.position)
                {
                    child.setCenterX(child.getCenterX() + val.doubleValue());
                }
                else
                {
                    child.setCenterX( child.getCenterX() + val.doubleValue());
                }
            } else {
                anchor.moveSibling(_depth, val, src);
            }
        }
    }



    // make a node movable by dragging it around with the mouse.
            private void enableDrag() {
                final Delta dragDelta = new Delta();
                setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent mouseEvent) {
                        // record a delta distance for the drag and drop operation.
                        dragDelta.x = getCenterX() - mouseEvent.getX();
                        dragDelta.y = getCenterY() - mouseEvent.getY();
                        getScene().setCursor(Cursor.MOVE);
                    }
                });
                setOnMouseReleased(new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent mouseEvent) {
                        getScene().setCursor(Cursor.HAND);
                    }
                });
                setOnMouseDragged(new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent mouseEvent) {
                        double newX = mouseEvent.getX() + dragDelta.x;
                        if (newX > 0 && newX < getScene().getWidth()) {
                            setCenterX(newX);
                        }
                        double newY = mouseEvent.getY() + dragDelta.y;
                        if (newY > 0 && newY < getScene().getHeight()) {
                            setCenterY(newY);
                        }
                    }
                });
                setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent mouseEvent) {
                        if (!mouseEvent.isPrimaryButtonDown()) {
                            getScene().setCursor(Cursor.HAND);
                        }
                    }
                });
                setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent mouseEvent) {
                        if (!mouseEvent.isPrimaryButtonDown()) {
                            getScene().setCursor(Cursor.DEFAULT);
                        }
                    }
                });
            }
        private class Delta { double x, y; }
}

class BoundLine extends Line {
    BoundLine(DoubleProperty startX, DoubleProperty startY, DoubleProperty endX, DoubleProperty endY) {
        startXProperty().bind(startX);
        startYProperty().bind(startY);
        endXProperty().bind(endX);
        endYProperty().bind(endY);
        setStrokeWidth(2);
        setStroke(Color.BLACK.deriveColor(0, 1, 1, 0.5));
        setStrokeLineCap(StrokeLineCap.BUTT);
        //getStrokeDashArray().setAll(10.0, 5.0);
        setMouseTransparent(true);
    }
}