package org.uclouvain.visualsearchtree.examples;

public interface DFSListener {
    default  void solution(int id, int pId) {};
    default void fail(int id, int pId) {};
    default void branch(int id, int pId, int nChilds) {};
}