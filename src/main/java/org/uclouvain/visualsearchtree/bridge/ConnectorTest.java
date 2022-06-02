package org.uclouvain.visualsearchtree.bridge;

import org.uclouvain.visualsearchtree.bridge.Connector;

import java.io.IOException;

public class ConnectorTest {
    public static void main(String[] args) {
        Connector connector = new Connector();
        try {
            connector.connect(6666);
            connector.start("premier_test",-1);

            connector.sendNode(0,-1,-1,2, Connector.NodeStatus.BRANCH);
            /*connector.createNode(1, 0, 0, 0, Connector.NodeStatus.FAILED).setNodeLabel("test").send();
            connector.createNode(2, 0, 1, 2, Connector.NodeStatus.BRANCH).setNodeLabel("HIJ").setNodeInfo("some info").send();
            connector.createNode(3, 2, 1, 2, Connector.NodeStatus.BRANCH).setNodeLabel("EFG").setNodeInfo("some info").send();
            connector.createNode(5, 3, 1, 0, Connector.NodeStatus.FAILED).setNodeLabel("ABC").setNodeInfo("some info").send();
            connector.createNode(6, 3, 1, 0, Connector.NodeStatus.FAILED).setNodeLabel("Solved node").setNodeInfo("some info").send();
            connector.createNode(4, 2, 1, 2, Connector.NodeStatus.BRANCH).setNodeLabel("XYZ").setNodeInfo("some info").send();
            connector.createNode(7, 4, 1, 0, Connector.NodeStatus.FAILED).setNodeLabel("XYZ").setNodeInfo("some info").send();
            connector.createNode(8, 4, 1, 2, Connector.NodeStatus.BRANCH).setNodeLabel("XYZ").setNodeInfo("some info").send();
            connector.createNode(9, 8, 1, 2, Connector.NodeStatus.FAILED).setNodeLabel("XYZ").setNodeInfo("some info").send();
            connector.createNode(10, 8, 1, 2, Connector.NodeStatus.SOLVED).setNodeLabel("XYZ").setNodeInfo("some info").send();
*/
            connector.disconnect();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
