package org.uclouvain.visualsearchtree.server;

import java.net.*;

public class MultiThreadServer {
    /**
     * <b>Note: </b> Multi Thead server used to process many profiling data by
     * the same time by launching  {@link org.uclouvain.visualsearchtree.server.ServerClientThread ServerClientThread}
     * @param port
     * @param profilingData
     */
    public MultiThreadServer(int port, ProfilingData profilingData) {
        try {
            ServerSocket server = new ServerSocket(port);
            int counter = 0;
            System.out.println("Profiler Server Started ....");
            while (true) {
                counter++;
                Socket serverClient = server.accept();
                System.out.println(">> " + "Listener No:" + counter + " started!");
                ServerClientThread sct = new ServerClientThread(serverClient, counter, port, profilingData);
                sct.run();
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
