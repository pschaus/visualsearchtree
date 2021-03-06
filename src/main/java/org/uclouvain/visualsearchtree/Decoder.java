package org.uclouvain.visualsearchtree;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Decoder {
    private static final int NODE = 0;
    private static final int DONE = 1;
    private static final int START = 2;
    private static final int RESTART = 3;

    public static class DecodedMessage{
        public String msgTypeName;
        public int msgType;
        public int nodeId;
        public int nodePid;
        public int nodeAlt;
        public int nodeChildren;
        public int nodeStatus;
        public String nodeLabel;
        public String nodeNoGood;
        public String nodeInfo;

        public String toString() {
            if(msgType == 0)
                return "\n\n["+msgTypeName+" { nodeId: "+nodeId+", nodePid: "+nodePid+", nodeAlt: "+nodeAlt+", nodeChildren: "+nodeChildren+", nodeStatus: "+nodeStatus+", nodeLabel:"+nodeLabel+", nodeNoGood: "+nodeNoGood+", nodeInfo:"+nodeInfo+"  }]";
            else if (msgType == 2)
                return "\n\n["+msgTypeName+" { nodeInfo:"+ nodeInfo +" }]";
            else
                return "\n\n["+msgTypeName+" {  }]";
        }
    }

    private static DecodedMessage formatData;

    public static void addToBuffer(List<Byte> buffer, byte[] incomingBytes) {
        for (byte byteData: incomingBytes) {
            buffer.add(byteData);
        }
    }
    public static DecodedMessage deserialize(List<Byte> buffer, int msgSize) {
        formatData = new DecodedMessage();
        byte[] msgBody = new byte[msgSize];

        readBuffer(msgBody, buffer, msgSize);

        // msg type
        switch ((int) msgBody[0] & 0xFF){
            case NODE:
                formatData.msgTypeName = "NODE";
                formatData.msgType = NODE;
                formatData.nodeId = byteArrayToInt(readBytes(msgBody, 1, 4), "BIG_ENDIAN");
                formatData.nodePid = byteArrayToInt(readBytes(msgBody, 13, 16), "BIG_ENDIAN");
                formatData.nodeAlt = byteArrayToInt(readBytes(msgBody, 25, 28), "BIG_ENDIAN");
                formatData.nodeChildren = byteArrayToInt(readBytes(msgBody, 29, 32), "BIG_ENDIAN");
                formatData.nodeStatus = (int) msgBody[33] & 0xFF;
                //decode optional data
                if(msgBody.length > 34) {
                    int i = 34;
                    do{
                        int opt_type = (int) msgBody[i] & 0xFF;
                        int opt_size = byteArrayToInt(readBytes(msgBody, i+1, i+5), "BIG_ENDIAN");
                        String opt_msg = new String(readBytes(msgBody, i+5, i+6+opt_size-2), StandardCharsets.US_ASCII);
                        i = i+6+opt_size-1;
                        if(opt_type == 0) {
                            formatData.nodeLabel = opt_msg.trim();
                        }
                        else if(opt_type == 1) {
                            formatData.nodeNoGood = opt_msg.trim();
                        }
                        else{
                            formatData.nodeInfo = opt_msg.trim();
                        }
                    }while(msgBody.length > i);
                }
                break;
            case DONE:
                formatData.msgTypeName = "DONE";
                formatData.msgType = DONE;
                break;
            case START:
                formatData.msgTypeName = "START";
                formatData.msgType = START;
                if(msgBody.length > 1) {
                    int opt_type = (int) msgBody[1] & 0xFF;
                    int opt_size = byteArrayToInt(readBytes(msgBody, 2, 5), "BIG_ENDIAN");
                    String opt_msg = new String(readBytes(msgBody, 6, 6+opt_size-1), StandardCharsets.UTF_8);
                    formatData.nodeInfo = opt_msg.trim();
                }
                break;
            case RESTART:
                formatData.msgTypeName = "RESTART";
                formatData.msgType = RESTART;
                break;
            default:
                System.out.println("ERROR: Invalid Message type");
        }
        return formatData;
    };
    public static boolean readBuffer(byte[] b, List<Byte> buffer, int len) {
        if(len<=0 && buffer.size() == 0)
            return false;
        for (int i = 0; i < len; i++) {
            b[i] = buffer.remove(0);
        }
        return true;
    }
    private static byte[] readBytes(byte[] bytes, int off, int len) {
        byte[] b = new byte[len];
        int j = 0;
        if(len-off<0)
            return new byte[0];
        else {
            for(int i = off; i <= len; i++){
                b[j] = bytes[i];
                j++;
            }
            return b;
        }
    }
    public static int byteArrayToInt(byte[] bytes, String endian) {
        final ByteBuffer bb = ByteBuffer.wrap(bytes);
        if(endian == "BIG_ENDIAN")
            bb.order(ByteOrder.BIG_ENDIAN);
        else
            bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getInt();
    }
}
