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
            connector.createNode(1, 0, 0, 0, Connector.NodeStatus.FAILED).setNodeLabel("n=0").setNodeInfo("{\"cost\": 10, \"param1\": 1, \"other\": \"Some info on node 1\"}").send();
            connector.createNode(2, 0, 1, 2, Connector.NodeStatus.BRANCH).setNodeLabel("n=1").setNodeInfo("{\"cost\": 9, \"param1\": 2, \"other\": \"Some info on node 2\"}").send();
            connector.createNode(3, 2, 1, 2, Connector.NodeStatus.BRANCH).setNodeLabel("n=2").setNodeInfo("{\"cost\": 7, \"param1\": 3, \"other\": \"Some info on node 3\"}").send();
            connector.createNode(5, 3, 1, 0, Connector.NodeStatus.FAILED).setNodeLabel("n=3").setNodeInfo("{\"cost\": 6, \"param1\": 4, \"other\": \"Some info on node 4\"}").send();
            connector.createNode(6, 3, 1, 0, Connector.NodeStatus.FAILED).setNodeLabel("n=4").setNodeInfo("{\"cost\": 5, \"param1\": 5, \"other\": \"Some info on node 5\"}").send();
            connector.createNode(4, 2, 1, 2, Connector.NodeStatus.BRANCH).setNodeLabel("n=5").setNodeInfo("{\"cost\": 3, \"param1\": 6, \"other\": \"Some info on node 6\"}").send();
            connector.createNode(7, 4, 1, 0, Connector.NodeStatus.FAILED).setNodeLabel("n=6").setNodeInfo("{\"cost\": 3, \"param1\": 7, \"other\": \"Some info on node 7\"}").send();
            connector.createNode(8, 4, 1, 2, Connector.NodeStatus.BRANCH).setNodeLabel("n=7").setNodeInfo("{\"cost\": 2, \"param1\": 8, \"other\": \"Some info on node 8\"}").send();
            connector.createNode(9, 8, 1, 2, Connector.NodeStatus.FAILED).setNodeLabel("n=8").setNodeInfo("{\"cost\": 2, \"param1\": 9, \"other\": \"Some info on node 9\"}").send();
            connector.createNode(10, 8, 1, 2, Connector.NodeStatus.SOLVED).setNodeLabel("s=9").setNodeInfo("{\"cost\": 1, \"param1\": 10, \"other\": \"Some info on node 10\"}").send();

            connector.disconnect();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
