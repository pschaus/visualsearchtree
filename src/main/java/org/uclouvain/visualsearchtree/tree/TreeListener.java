package org.uclouvain.visualsearchtree.tree;

public interface TreeListener {
    /**
     *
     * @param id nodeId
     * @param pId nodePid
     * @param type nodeType
     * @param nodeAction nodeAction
     * @param info nodeInfo
     */
    default void onNodeCreated(int id, int pId, Tree.NodeType type, NodeAction nodeAction, String info){ }

    /**
     * will be fired at the end of search
     */
    default void onSearchEnd(){}
}
