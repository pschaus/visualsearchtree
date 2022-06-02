package org.uclouvain.visualsearchtree.server;

import org.uclouvain.visualsearchtree.bridge.Decoder;
import org.uclouvain.visualsearchtree.bridge.Message;
import org.uclouvain.visualsearchtree.tree.Tree;
import org.uclouvain.visualsearchtree.tree.VisualTree;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class ServerClientThread extends VisualTree implements Runnable {
    // initialize socket and input stream
    private Socket socket = null;
    private DataInputStream in = null;
    private int port = 6666;

    // are there enough bytes to read something
    private boolean canReadMore = true;
    private List<Byte> buffer = new ArrayList<>();
    private boolean sizeRead = false;
    private int bytesRead = 0;
    private int msgSize = 0;
    private boolean DEBUG = true;

    // stack of decoded incoming message
    private List<Decoder.DecodedMessage> decodedMessagesList = new ArrayList<>();

    // node we have to send to VisualTree for visualization
    private Tree.Node<String> NodeTree;

    private int clientNo;

    ServerClientThread(Socket inSocket,int counter, int port){
        socket = inSocket;
        clientNo = counter;
        this.port = port;
    }

    public void run(){
        try {
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            while (canReadMore) {
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
                    if(msgBody.msgType == Message.MsgType.NODE.getNumber())
                        decodedMessagesList.add(msgBody);

                    if(DEBUG) {
                        System.out.println(msgBody.toString());
                        System.out.println("-----");
                    }
                    // TODO: WRITE HANDLE MSG FUNCTION TO EMIT INCOMING DATA
                    //handleMessage(msg);

                    if(msgBody.msgType == Message.MsgType.DONE.getNumber()) {
                        //socket.close();
                        canReadMore = false;
                        NodeTree = Decoder.treeBuilder(decodedMessagesList);
                    }

                    bytesRead = 0;
                    sizeRead = false;
                }
            }

            main(new String[] {"dd"});
            //launch(VisualTree.class);

            //VisualTree treeDrawer = new VisualTree(getNodeTree());
            System.out.println("Closing connection");
            // close connection
            socket.close();
            in.close();
        }
        catch (IOException i) {
            System.out.println(i);
        }
        finally {
            System.out.println("Client No:" + clientNo + " exit!! ");
        }
    }

    public Tree.Node<String> getNodeTree() {
        return NodeTree;
    }

    public int getPort() {
        return port;
    }
}