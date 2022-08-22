package org.uclouvain.visualsearchtree.server;

import org.uclouvain.visualsearchtree.tree.Tree;
import java.util.List;
import javax.swing.event.EventListenerList;

public class ProfilingData {
    private List<String> profilingNameList;
    private List<Tree.Node<String>> profilingNodesList;
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

    /**
     * <b>Note: </b>Add profiling name into server name list
     * @param newProfilingName
     */
    // setters
    public void addToProfilingNameList(String newProfilingName) {
        profilingNameList.add(newProfilingName);
        fireProfilingNameListChanged(newProfilingName);
    }

    /**
     * <b>Note: </b>Add new node to server Nodes List in order to allow tree drawing
     * @param newProfilingNode
     */
    public void addToProfilingNodesList(Tree.Node<String> newProfilingNode) {
        profilingNodesList.add(newProfilingNode);
        fireProfilingNodesListChanged(newProfilingNode);
    }

    /**
     * <b>Note: </b>Add new node silently to server
     * @param newProfilingNode
     */
    public void addSilentlyToProfilingNodesList(Tree.Node<String> newProfilingNode) {
        profilingNodesList.add(newProfilingNode);
    }

    /**
     * <b>Note: </b>Add listener to server
     * @param listener
     */
    // profilingNameList listener handler
    public void addProfilingDataListener(ProfilingDataListener listener) {
        listeners.add(ProfilingDataListener.class, listener);
    }


    /**
     * <b>Note: </b>Remove listener from server
     * @param listener
     */
    public void removeProfilingDataListener(ProfilingDataListener listener) {
        listeners.remove(ProfilingDataListener.class, listener);
    }

    /**
     * Get all Listener bound to server
     * @return
     */
    public ProfilingDataListener[] getProfilingDataListeners() {
        return listeners.getListeners(ProfilingDataListener.class);
    }

    /**
     * <b>Note: </b>Alert when profiling start
     * @param newProfilingName
     */
    // stack of fire
    protected void fireProfilingNameListChanged(String newProfilingName) {
        for(ProfilingDataListener listener : getProfilingDataListeners()) {
            listener.newProfilingDetected(newProfilingName);
        }
    }

    /**
     * <b>Note: </b>Alert When profiling is ready
     * @param newProfilingNode
     */
    protected void fireProfilingNodesListChanged(Tree.Node<String> newProfilingNode) {
        for(ProfilingDataListener listener : getProfilingDataListeners()) {
            listener.newProfilingNodeReady(newProfilingNode);
        }
    }
}
