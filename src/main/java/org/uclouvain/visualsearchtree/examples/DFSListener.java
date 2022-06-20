package org.uclouvain.visualsearchtree.examples;

public interface DFSListener {
    default  void solution(int pId, int id) {};
    default void fail(int pId, int id) {};
    default void branch(int pId, int id,int nChilds) {};
}