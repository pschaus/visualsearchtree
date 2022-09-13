package org.uclouvain.visualsearchtree.server;

import org.uclouvain.visualsearchtree.bridge.Connector;

import java.io.IOException;

public class Client {
    public static void main(String[] args)  {
        Connector client = new Connector();
        try {
            client.connect(6650);
            client.start("premier_test",-1);
            client.sendNode(0,-1,-1,2, Connector.NodeStatus.BRANCH);
            client.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
