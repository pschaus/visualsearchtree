package org.uclouvain.visualsearchtree.tree;

public interface DrawListener {
    /**
     * Will be called at the end of draw
     */
    default void onFinish(){};
    default Anchor onUINodeCreated(int id, int pId, Tree.NodeType type, NodeAction nodeAction, String info){return new Anchor();};

}
