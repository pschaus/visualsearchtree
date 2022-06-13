package org.uclouvain.visualsearchtree.tree.events;

import javafx.event.EventHandler;

public abstract class BackToNormalEventHandler implements EventHandler<BackToNormalEvent> {
    public abstract void unClick();


    @Override
    public void handle(BackToNormalEvent event) {
        event.invokeHandler(this);
    }
}
