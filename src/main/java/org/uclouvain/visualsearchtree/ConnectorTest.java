package org.uclouvain.visualsearchtree;

import java.io.IOException;

public class ConnectorTest {
    public static void main(String[] args) {
        Connector connector = new Connector();
        try {
            connector.connect(6666);
            connector.sendNode(-1,2,1,1, Connector.NodeStatus.BRANCH);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
