package org.uclouvain.visualsearchtree.bridge;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DecoderTest {
    // data
    static List<Byte> buffer;
    static Decoder decoder;


    // init
    @BeforeEach
    public void init() {
        buffer = new ArrayList<>();
        buffer.add((byte) 0);
        buffer.add((byte) 1);
    }

    @Test
    void addToBuffer() {
        byte[] new_data = new byte[] {(byte) 2, (byte) 3, (byte) 4, (byte) 5};
        Decoder.addToBuffer(buffer, new_data);
        assertTrue(buffer.size() > new_data.length);
    }

    @Test
    void readBuffer_tryRetrieveBytes() {
        // add data
        byte[] new_data = new byte[] {(byte) 2, (byte) 3, (byte) 4, (byte) 5};
        Decoder.addToBuffer(buffer, new_data);

        // test tasks
        byte[] dataToBeRetrieve = new byte[5];
        byte[] expectedToBeRetrieve = new byte[] {(byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4};
        boolean status = Decoder.readBuffer(dataToBeRetrieve, buffer, 5);

        assertTrue(status);
        assertArrayEquals(dataToBeRetrieve, expectedToBeRetrieve);
        assertTrue(buffer.size() == 1);
    }

    @Test
    void readBuffer_tryRetrieveMoreBytesThanItContain() {
        byte[] dataToBeRetrieve = new byte[10];
        Exception exception = assertThrows(
                IndexOutOfBoundsException.class, () ->
                Decoder.readBuffer(dataToBeRetrieve, buffer, 10)
        );
        assertEquals("Index 0 out of bounds for length 0", exception.getMessage());
    }

    @Test
    void readBuffer_tryRetrieveNegativeSizeOfBytes() {
        byte[] dataToBeRetrieve = new byte[1];
        boolean status = Decoder.readBuffer(dataToBeRetrieve, buffer, -1);
        assertTrue(!status);
    }

    @Test
    void byteArrayToInt() {
        byte[] byteData = new byte[] {(byte) 0, (byte) 1, (byte) 2, (byte) 3};
        assertTrue(Decoder.byteArrayToInt(byteData, "BIG_ENDIAN") == 66051);
        assertTrue(Decoder.byteArrayToInt(byteData, "LITTLE_ENDIAN") != 66051);
    }
}