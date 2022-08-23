package org.uclouvain.visualsearchtree.server;

import org.uclouvain.visualsearchtree.tree.Tree;
import java.util.EventListener;

public interface ProfilingDataListener extends EventListener {
    /**
     * <b>Note: </b>Detect when new data start coming toward the server buffer
     * @param name
     */
    void newProfilingDetected(String name);

    /**
     * <b>Node: </b>Detect when we have suffisant data to draw a new node
     * @param node
     */
    void newProfilingNodeReady(Tree.Node<String> node);
}
