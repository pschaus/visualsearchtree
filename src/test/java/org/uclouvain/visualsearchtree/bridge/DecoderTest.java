package org.uclouvain.visualsearchtree.bridge;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

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

    @ParameterizedTest
    @CsvSource({
            "1, 2, 6, 7",
            "0, 3, 5, 8",
    })
    void addToBuffer(int val1, int val2, int val3, int val4) {
        byte[] new_data = new byte[] {(byte) val1, (byte) val2, (byte) val3, (byte) val4};
        int bufferSizeBefore = buffer.size();
        Decoder.addToBuffer(buffer, new_data);
        assertTrue(buffer.size() == (new_data.length+bufferSizeBefore));
    }

    @ParameterizedTest
    @MethodSource("dataProvider")
    void readBuffer_tryRetrieveBytes(int val1, int val2, int val3, int val4, int size_to_retrieve, Integer[] data_expected) {
        // add data
        byte[] new_data = new byte[] {(byte) val1, (byte) val2, (byte) val3, (byte) val4};
        Decoder.addToBuffer(buffer, new_data);

        // test tasks
        byte[] expectedToBeRetrieve = new byte[data_expected.length];
        byte[] dataToBeRetrieve = new byte[size_to_retrieve];
        for (int i = 0; i < data_expected.length; i++) {
            expectedToBeRetrieve[i] = (byte)data_expected[i].intValue();
        }
        boolean status = Decoder.readBuffer(dataToBeRetrieve, buffer, size_to_retrieve);

        assertTrue(status);
        assertArrayEquals(expectedToBeRetrieve, dataToBeRetrieve);
        assertEquals(6 - size_to_retrieve, buffer.size());
    }

    static Stream<Arguments> dataProvider() {
        return Stream.of(
                arguments(3, 4, 5, 6, 3, new Integer[]{0,1,3}),
                arguments(4, 8, 5, 9, 4, new Integer[]{0,1,4,8})
        );
    }
    @ParameterizedTest
    @CsvSource({
            "12",
            "24",
            "8",
    })
    void readBuffer_tryRetrieveMoreBytesThanItContain(int size) {
        byte[] dataToBeRetrieve = new byte[size];
        Exception exception = assertThrows(
                IndexOutOfBoundsException.class, () ->
                        Decoder.readBuffer(dataToBeRetrieve, buffer, size)
        );
        assertEquals("Index 0 out of bounds for length 0", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "6", "7", "1",
    })
    void readBuffer_tryRetrieveNegativeSizeOfBytes(int value) {
        byte[] dataToBeRetrieve = new byte[value];
        boolean status = Decoder.readBuffer(dataToBeRetrieve, buffer, -1);
        assertFalse(status);
    }

    @Test
//    @ParameterizedTest
//    @CsvSource({
//            "1, 2, 6, 7",
//            "0, 3, 5, 8",
//    })
    void byteArrayToInt() {
        byte[] byteData = new byte[] {(byte) 0, (byte) 1, (byte) 2, (byte) 3};
        assertTrue(Decoder.byteArrayToInt(byteData, "BIG_ENDIAN") == 66051);
        assertTrue(Decoder.byteArrayToInt(byteData, "LITTLE_ENDIAN") != 66051);
    }
}