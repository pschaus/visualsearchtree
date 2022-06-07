package org.uclouvain.visualsearchtree.server;

import org.uclouvain.visualsearchtree.tree.Tree;
import java.util.List;
import javax.swing.event.EventListenerList;

public class ProfilingData {
    private List<String> profilingNameList;
    private List<Tree.Node<String>> profilingNodesList;

    // single object for listener
    private final EventListenerList listeners = new EventListenerList();

    public ProfilingData(List<String> profilingNameList, List<Tree.Node<String>> profilingNodesList) {
        this.profilingNameList = profilingNameList;
        this.profilingNodesList = profilingNodesList;
    }

    // getters
    public List<String> getProfilingNameList() {
        return profilingNameList;
    }
    public List<Tree.Node<String>> getProfilingNodesList() {
        return profilingNodesList;
    }

    // setters
    public void addToProfilingNameList(String newProfilingName) {
        profilingNameList.add(newProfilingName);
        fireProfilingNameListChanged(newProfilingName);
    }
    public void addToProfilingNodesList(Tree.Node<String> newProfilingNode) {
        profilingNodesList.add(newProfilingNode);
        fireProfilingNodesListChanged(newProfilingNode);
    }

    // profilingNameList listener handler
    public void addProfilingDataListener(ProfilingDataListener listener) {
        listeners.add(ProfilingDataListener.class, listener);
    }
    public void removeProfilingDataListener(ProfilingDataListener listener) {
        listeners.remove(ProfilingDataListener.class, listener);
    }
    public ProfilingDataListener[] getProfilingDataListeners() {
        return listeners.getListeners(ProfilingDataListener.class);
    }

    // stack of fire
    protected void fireProfilingNameListChanged(String newProfilingName) {
        for(ProfilingDataListener listener : getProfilingDataListeners()) {
            listener.newProfilingDetected(newProfilingName);
        }
    }
    protected void fireProfilingNodesListChanged(Tree.Node<String> newProfilingNode) {
        for(ProfilingDataListener listener : getProfilingDataListeners()) {
            listener.newProfilingNodeReady(newProfilingNode);
        }
    }
}
