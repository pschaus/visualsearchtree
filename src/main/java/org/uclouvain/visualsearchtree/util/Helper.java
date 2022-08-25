package org.uclouvain.visualsearchtree.util;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

public class Helper {

    /**
     * Determine the v_value and h_value to set the corresponding properties for the ScrollPane in order to center its content
     * @param sp the StackPane which is the ScrollPane content
     * @param scrollPane the ScrollPane for which the content will be positioned to the center
     * @return List of doubles
     */
    public static List<Double> centerScrollPaneBar(StackPane sp, ScrollPane scrollPane){
        List<Double> values = new ArrayList<>();
        double height, width, y, x, v, h, v_value, h_value ;

        height = scrollPane.getContent().getBoundsInLocal().getHeight();
        width = scrollPane.getContent().getBoundsInLocal().getWidth();
        y = (sp.getBoundsInParent().getMaxY() + sp.getBoundsInParent().getMinY()) / 2.0;
        x = (sp.getBoundsInParent().getMaxX() + sp.getBoundsInParent().getMinX()) / 2.0;
        v = scrollPane.getViewportBounds().getHeight();
        h = scrollPane.getViewportBounds().getWidth();

        v_value = scrollPane.getVmax() * ((y - 0.5 * v) / (height - v));
        h_value = scrollPane.getHmax() * ((x - 0.5 * h) / (width - h));
        values.add(v_value);
        values.add(h_value);
        return values ;
    }
}
