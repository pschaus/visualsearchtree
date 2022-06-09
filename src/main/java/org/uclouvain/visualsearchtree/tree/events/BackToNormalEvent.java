package org.uclouvain.visualsearchtree.tree.events;

import javafx.event.EventType;

public class BackToNormalEvent extends CustomEvent {
    public static final EventType<CustomEvent> BACK_TO_NORMAL = new EventType(CUSTOM_EVENT_TYPE, "Backtonormal");

    public BackToNormalEvent() {
        super(BACK_TO_NORMAL);
    }

    @Override
    public void invokeHandler(BackToNormalEventHandler handler) {
        handler.unClick();
    }
}
