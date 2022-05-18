package org.uclouvain.visualsearchtree;


// A Java program for a Serverside

import java.net.*;
import java.io.*;

public class VisualTreeServer {

    //initialize socket and input stream
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;

    // constructor with port
    public VisualTreeServer(int port) {
        // starts server and waits for a connection
        try {
            server = new ServerSocket(port);
            System.out.println("Server started");
            System.out.println("Waiting for a client ...");
            socket = server.accept();
            System.out.println("Client accepted");
            // takes input from the client socket

            //InputStream stream = socket.getInputStream();
            //byte[] data = new byte[100];
            //int count = stream.read(data);


            in = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));
            String line = "";
            // reads message from client until "Over" is sent
            while (!line.equals("Over")) {
                try {
                    line = in.readUTF();
                    System.out.println(line);


                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            System.out.println("Closing connection");
            // close connection
            socket.close();
            in.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String args[]) {
        VisualTreeServer server = new VisualTreeServer(6666);
    }
}

