package org.uclouvain.visualsearchtree.examples;

public interface DFSListener {
    /**
     * Called when a node is a solution
     * @param id nodeId
     * @param pId nodePId
     */
    default  void solution(int id, int pId) {};

    /**
     * Called when a node is a fail
     * @param id nodeId
     * @param pId nodePId
     */
    default void fail(int id, int pId) {};

    /**
     * Called when a node is a branch
     * @param id nodeId
     * @param pId nodePId
     * @param nChilds number of children of the node
     */
    default void branch(int id, int pId, int nChilds) {};
}