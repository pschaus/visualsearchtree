package org.uclouvain.visualsearchtree.tree;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TestTreeDraw extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override public void start(final Stage stage) throws Exception {
        DoubleProperty startX = new SimpleDoubleProperty(100);
        DoubleProperty startY = new SimpleDoubleProperty(100);

        Anchor start   = new Anchor(startX, startY);

        stage.setTitle("Line Manipulation Sample");
        Group child1g = start.addChild();
        Group child2g = start.addChild();
        Group child3g = start.addChild();

        Anchor chid1 = (Anchor) child1g.getChildren().get(0);
        Anchor chid2 = (Anchor) child2g.getChildren().get(0);
        Anchor chid3 = (Anchor) child3g.getChildren().get(0);

        Group child1_1g = chid1.addChild();
        Group child1_2g = chid1.addChild();
        Group child1_3g = chid1.addChild();

        Group child2_2g = chid2.addChild();
        Group child2_3g = chid2.addChild();
        Group child2_1g = chid2.addChild();

        Group child3_2g = chid3.addChild();
        Group child3_3g = chid3.addChild();
        Group child3_1g = chid3.addChild();

        Group child4g = start.addChild();
        Anchor chid4 = (Anchor) child3g.getChildren().get(0);

        Anchor chid5 = (Anchor) child4g.getChildren().get(0);

        stage.setScene(new Scene(new Group(start, child1g, child2g, child3g, child4g, child1_1g, child1_2g, child1_3g, child2_1g, child2_2g, child2_3g, child3_1g, child3_2g, child3_3g, chid4.addChild(), chid4.addChild(), chid4.addChild(), chid5.addChild(), chid5.addChild(), chid5.addChild()), 400, 400, Color.ALICEBLUE));
        stage.show();
    }

//    class BoundLine extends Line {
//        BoundLine(DoubleProperty startX, DoubleProperty startY, DoubleProperty endX, DoubleProperty endY) {
//            startXProperty().bind(startX);
//            startYProperty().bind(startY);
//            endXProperty().bind(endX);
//            endYProperty().bind(endY);
//            setStrokeWidth(2);
//            setStroke(Color.GRAY.deriveColor(0, 1, 1, 0.5));
//            setStrokeLineCap(StrokeLineCap.BUTT);
//            //getStrokeDashArray().setAll(10.0, 5.0);
//            setMouseTransparent(true);
//        }
//    }
//
//        // a draggable anchor displayed around a point.
//        class Anchor extends Circle {
//            DoubleProperty x, y;
//            int nbChild;
//            int depth;
//            double last_pos;
//            Anchor parent = null;
//            List<Anchor> children = new ArrayList<>();
//            List<DoubleProperty> levels = null;
//            int position = 0;
//
//            Anchor(Color color, DoubleProperty x, DoubleProperty y) {
//                super(x.get(), y.get(), 10);
//                this.x = x;
//                this.y = y;
//                nbChild = 0;
//                depth = 0;
//                setFill(color.deriveColor(1, 1, 1, 0.5));
//                setStroke(color);
//                setStrokeWidth(2);
//                setStrokeType(StrokeType.OUTSIDE);
//                levels = new ArrayList<>();
//                levels.add(this.x);
//                x.bind(centerXProperty());
//                y.bind(centerYProperty());
//                enableDrag();
//            }
//
//            // make a node movable by dragging it around with the mouse.
//            private void enableDrag() {
//                final Delta dragDelta = new Delta();
//                setOnMousePressed(new EventHandler<MouseEvent>() {
//                    @Override public void handle(MouseEvent mouseEvent) {
//                        // record a delta distance for the drag and drop operation.
//                        dragDelta.x = getCenterX() - mouseEvent.getX();
//                        dragDelta.y = getCenterY() - mouseEvent.getY();
//                        getScene().setCursor(Cursor.MOVE);
//                    }
//                });
//                setOnMouseReleased(new EventHandler<MouseEvent>() {
//                    @Override public void handle(MouseEvent mouseEvent) {
//                        getScene().setCursor(Cursor.HAND);
//                    }
//                });
//                setOnMouseDragged(new EventHandler<MouseEvent>() {
//                    @Override public void handle(MouseEvent mouseEvent) {
//                        double newX = mouseEvent.getX() + dragDelta.x;
//                        if (newX > 0 && newX < getScene().getWidth()) {
//                            setCenterX(newX);
//                        }
//                        double newY = mouseEvent.getY() + dragDelta.y;
//                        if (newY > 0 && newY < getScene().getHeight()) {
//                            setCenterY(newY);
//                        }
//                    }
//                });
//                setOnMouseEntered(new EventHandler<MouseEvent>() {
//                    @Override public void handle(MouseEvent mouseEvent) {
//                        if (!mouseEvent.isPrimaryButtonDown()) {
//                            getScene().setCursor(Cursor.HAND);
//                        }
//                    }
//                });
//                setOnMouseExited(new EventHandler<MouseEvent>() {
//                    @Override public void handle(MouseEvent mouseEvent) {
//                        if (!mouseEvent.isPrimaryButtonDown()) {
//                            getScene().setCursor(Cursor.DEFAULT);
//                        }
//                    }
//                });
//            }
//
//            public Group addChild()
//            {
//                nbChild++;
//                DoubleProperty endX   = new SimpleDoubleProperty(levels.get(depth).get() + 100);
//                DoubleProperty endY   = new SimpleDoubleProperty(getCenterY() + 50);
//                Anchor child = new Anchor(Color.RED, endX, endY);
//                Line line = new BoundLine(x, y, endX, endY);
//
//                child.depth = depth + 1;
//                child.parent = this;
//                child.levels = levels;
//                child.position = nbChild;
//                //
//                endX.addListener(((observableValue, number, t1) -> {
//                    child.recenterParent();
//                }));
//                levels.set(depth, endX);
//                if ( depth + 1 >= levels.size() )
//                    levels.add(endX);
//                children.add(child);
//                child.recenterParent();
//                return new Group(child, line);
//            }
//
//            private void recenterParent()
//            {
//                Anchor _parent = parent;
//                if ( _parent == null || _parent.children.size() == 0)
//                    return;
//
//                double val = parent.getCenterX();
//                double posX = _parent.children.get(0).getCenterX() + _parent.children.get(_parent.children.size()-1).getCenterX();
//                _parent.setCenterX(posX/2);
//                val = parent.getCenterX() - val;
//                while (_parent.parent != null){ _parent = _parent.parent;}
//                _parent.moveSibling(parent.depth, val, parent);
//            }
//
//            private void moveSibling(int _depth, Number val, Anchor src)
//            {
//                for (int i = 0; i < children.size(); i++) {
//                    if (_depth == children.get(i).depth && children.get(i) != src) {
//                        Anchor child = children.get(i);
//                        if (src.position < child.position)
//                            child.setCenterX(child.getCenterX() + val.doubleValue());
//                    }
//                    else
//                    {
//                        children.get(i).moveSibling(_depth, val, src);
//                    }
//                }
//            }
//            // records relative x and y co-ordinates.
//            private class Delta { double x, y; }
//        }
}