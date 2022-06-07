package org.uclouvain.visualsearchtree.server;

import org.uclouvain.visualsearchtree.tree.Tree;
import java.util.EventListener;

public interface ProfilingDataListener extends EventListener {
    void newProfilingDetected(String name);
    void newProfilingNodeReady(Tree.Node<String> node);
}
