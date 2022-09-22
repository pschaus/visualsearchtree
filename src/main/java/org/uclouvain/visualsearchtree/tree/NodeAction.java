package org.uclouvain.visualsearchtree.tree;

@FunctionalInterface
public interface NodeAction {
    /**
     * Will be called once the node has been double clicked or right clicked
     */
    void nodeAction();
}