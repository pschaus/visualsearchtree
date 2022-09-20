package org.uclouvain.visualsearchtree.tree;

public interface TreeListener {
    /**
     *
     * @param id
     * @param pId
     * @param type
     * @param nodeAction
     * @param info
     */
    default void onNodeCreated(int id, int pId, Tree.NodeType type, NodeAction nodeAction, String info){ };

    /**
     * will be fired at the end of search
     */
    default void onSearchEnd(){};
}
