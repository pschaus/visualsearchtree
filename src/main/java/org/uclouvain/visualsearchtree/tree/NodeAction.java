package org.uclouvain.visualsearchtree.tree;

@FunctionalInterface
public interface NodeAction {
    void nodeAction(TreeVisual.NodeInfoData info);
}