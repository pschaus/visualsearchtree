package org.uclouvain.visualsearchtree.bridge;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.TimeUnit;

public class Connector {
    // ATTRIBUTE
    private Socket clientSocket;
    private DataOutputStream out;
    private Message msg;
    private boolean DEBUG;

    //CONSTRUCTOR
    public Connector() {
        System.out.println("\n---------------------------------------");
        System.out.println("MiniCP-CPProfiler Connector initialized");
        this.DEBUG = true;
    }

    /**
     * <b>Note: </b> Enum of NodeStatus
     * <ul>
     *     <li>SOLVED</li>
     *     <li>FAILED</li>
     *     <li>BRANCH</li>
     *     <li>SKIPPED</li>
     * </ul>
     */
    // METHODS
    public enum NodeStatus {
        SOLVED(0),
        FAILED(1),
        BRANCH(2),
        SKIPPED(6);
        private final int id;
        private NodeStatus(int id) { this.id = id; }
        public int getNumber() { return id; }
    }

    /**
     * <b>Note: </b>Help to connect to a specific port. Then we can send data through
     * this canal to the profiler
     * @param port
     * @throws IOException
     */
    public void connect(int port) throws IOException {
        this.clientSocket = new Socket("localhost", port);
        this.out = new DataOutputStream(clientSocket.getOutputStream());
        this.msg = new Message(this);
        System.out.println("Connected to 'localhost:'" + port + "\n");
    }


    /**
     * <b>Note: </b>THis method can be used to end connect to current session.
     * It helps to free resources used to join server.
     * @throws IOException
     * @throws InterruptedException
     */
    public void disconnect() throws IOException, InterruptedException {
        msg.setType(Message.MsgType.DONE);
        sendThroughSocket(msg.toBytes());
        //testMsg(msg.toBytes());
        msg.clear();
        // 01 -> CLOSE STREAM
        out.close();
        // 02 -> CLOSE SOCKET
        clientSocket.close();
        System.out.println("\nMiniCP-CPProfiler Connector closed");
        System.out.println("---------------------------------------\n");
    }

    /**
     * <b>Note: </b>This message is send the first time we send data to our server.
     * The rid should be equal to -1
     * @param rid
     * @throws IOException
     * @throws InterruptedException
     */
    public void start(int rid) throws IOException, InterruptedException {
        start("", rid);
    }

    /**
     * <b>Note: </b>This message is send the first time we send data to our server.
     * The rid should be equal to -1
     * <br><br>
     * <p>
     *     Here we can define a name to our profiling so this name will be display
     *     in our server profiling list tab
     * </p>
     * @param rid
     * @throws IOException
     * @throws InterruptedException
     */
    public void start(String file_name, int rid) throws IOException, InterruptedException {
        msg = msg.setType(Message.MsgType.START).setLabel(file_name).setRestartId(rid);
        sendThroughSocket(msg.toBytes());
        msg.clear();
    }

    public void restart(int rid) throws IOException, InterruptedException {
        restart("", rid);
    }

    public void restart(String file_name, int rid) throws IOException, InterruptedException {
        msg = msg.setType(Message.MsgType.RESTART).setLabel(file_name).setRestartId(rid);
        sendThroughSocket(msg.toBytes());
        msg.clear();
    }

    /**
     * <b>Note: </b>This function  help to create a new node
     * @param sid
     * @param pid
     * @param alt
     * @param kids
     * @param status
     * @return {@link org.uclouvain.visualsearchtree.bridge.Message Node}
     */
    private Message createNewNode(int sid, int pid, int alt, int kids, NodeStatus status) {
        return msg.setType(Message.MsgType.NODE)
                .setNodeId(sid)
                .setNodePid(pid)
                .setNodeAlt(alt)
                .setNodeChildren(kids)
                .setNoteStatus(status.getNumber());
    }

    /**
     * <b>Note: </b>This function  help to create a new node
     * @param sid
     * @param pid
     * @param alt
     * @param kids
     * @param status
     * @return {@link org.uclouvain.visualsearchtree.bridge.Message Node}
     */
    public Message createNode(int sid, int pid, int alt, int kids, NodeStatus status) {
        return this.createNewNode(sid, pid, alt, kids, status);
    }

    /**
     * <b>Note: </b>This function create a new node and send it
     * through socket to our server
     * @param sid
     * @param pid
     * @param alt
     * @param kids
     * @param status
     * @throws IOException
     * @throws InterruptedException
     */
    public void sendNode(int sid, int pid, int alt, int kids, NodeStatus status) throws IOException, InterruptedException {
        Message msg = createNewNode(sid, pid, alt, kids, status);
        sendThroughSocket(msg.toBytes());
        msg.clear();
    }

    /**
     * <b>Note: </b>THis function send new node to our server
     * though socket when this node is passed to it as argument
     * @param msg
     * @throws IOException
     * @throws InterruptedException
     */
    public void sendNode(Message msg) throws IOException, InterruptedException {
        sendThroughSocket(msg.toBytes());
        msg.clear();
    }

    /**
     * <b>Note: </b>This function is used to send data through socket.
     * @param msg
     * @throws IOException
     * @throws InterruptedException
     */
    private synchronized void sendThroughSocket(byte[] msg) throws IOException, InterruptedException {
        int msg_size = msg.length;
        byte[] size_buffer = new byte[4];
        ByteBuffer.wrap(size_buffer).order(ByteOrder.LITTLE_ENDIAN).putInt(msg_size);

        if(DEBUG) {
            System.out.print("SENT: ");
            System.out.print(bytesToString(size_buffer));
            System.out.println(bytesToString(msg));
        }

        // 01 -> SEND MSG SIZE
        out.write(size_buffer);
        // 02 -> SEND MSG NOW
        out.write(msg);
        out.flush();
        TimeUnit.SECONDS.sleep(0);
    }

    // ---------- FOR DEBUG -----------
    private int unsignedByteToInt(byte b) {
        return (int) b & 0xFF;
    }

    public String bytesToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(4 * bytes.length);
        sb.append("[");

        for (int i = 0; i < bytes.length; i++) {
            sb.append(this.unsignedByteToInt(bytes[i]));
            if (i + 1 < bytes.length) {
                sb.append(",");
            }
        }

        sb.append("]");
        return sb.toString();
    }
    //--------------------------------------
}