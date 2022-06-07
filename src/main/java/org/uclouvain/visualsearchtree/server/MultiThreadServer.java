package org.uclouvain.visualsearchtree.server;

import java.net.*;
import java.io.*;

public class MultiThreadServer {
    public MultiThreadServer(int port, ProfilingData profilingData) {
        try{
            ServerSocket server = new ServerSocket(port);
            int counter = 0;
            System.out.println("Profiler Server Started ....");
            while(true) {
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

//    public static void main(String args[]) {
//        MultiThreadServer server = new MultiThreadServer(6666);
//    }
}
