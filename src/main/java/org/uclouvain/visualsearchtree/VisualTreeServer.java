package org.uclouvain.visualsearchtree;


// A Java program for a Serverside

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VisualTreeServer {

    // initialize socket and input stream
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;

    // are there enough bytes to read something
    private boolean canReadMore = true;
    private List<Byte> buffer = new ArrayList<>();
    private boolean sizeRead = false;
    private int bytesRead = 0;
    private int msgSize = 0;
    private boolean DEBUG = true;

    // constructor with port
    public VisualTreeServer(int port) {
        // starts server and waits for a connection
        try {
            server = new ServerSocket(port);
            System.out.println("Server started");
            System.out.println("Waiting for a client ...");
            socket = server.accept();
            System.out.println("Client accepted");

            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            while (!server.isClosed() || canReadMore) {
                if (socket.getInputStream().available() > 0)
                    canReadMore = true;

                Decoder.addToBuffer(buffer, socket.getInputStream().readAllBytes());
                if(DEBUG) {
                    //System.out.println(buffer);
                }

                // read the size of the next field if haven't already
                if (!sizeRead) {
                    if (buffer.size() < 4) {
                        /// can't read, need to wait for more bytes
                        canReadMore = false;
                        continue;
                    }

                    /// enough bytes to read size
                    byte[] msgSizeBytes = new byte[4];
                    Decoder.readBuffer(msgSizeBytes, buffer, 4);
                    msgSize = Decoder.byteArrayToInt(msgSizeBytes, "LITTLE_ENDIAN");
                    bytesRead += 4;
                    sizeRead = true;
                } else {
                    if (buffer.size() < msgSize) {
                        /// can't read, need to wait for more bytes
                        canReadMore = false;
                        continue;
                    }

                    Decoder.DecodedMessage msgBody = Decoder.deserialize(buffer, msgSize);
                    if(DEBUG) {
                        System.out.println(msgBody.toString());
                        System.out.println("-----");
                    }
                    // TODO: WRITE HANDLE MSG FUNCTION TO EMIT INCOMING DATA
                    //handleMessage(msg);

                    if(msgBody.msgType == Message.MsgType.DONE.getNumber()) {
                        server.close();
                    }

                    bytesRead = 0;
                    sizeRead = false;
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

