package org.uclouvain.visualsearchtree;

import java.io.IOException;

public class ConnectorTest {
    public static void main(String[] args) {
        Connector connector = new Connector();
        try {
            connector.connect(6666);
            connector.start("premier_test",-1);

            connector.sendNode(0,-1,-1,2, Connector.NodeStatus.BRANCH);
            connector.createNode(1, 0, 0, 0, Connector.NodeStatus.FAILED).setNodeLabel("test").setNodeRestartId(-1).send();
            connector.createNode(2, 0, 1, 0, Connector.NodeStatus.SOLVED).setNodeLabel("Solved node").setNodeInfo("some info").send();

            connector.disconnect();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
