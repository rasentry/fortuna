package com.grunka.random.fortuna;

import org.junit.Before;
import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class RandomDataBufferTest {

    private RandomDataBuffer randomDataBuffer;
    private Function<Integer, byte[]> dataSupplier;

    @Before
    public void setUp() {
        randomDataBuffer = new RandomDataBuffer();
        dataSupplier = (i) -> new byte[]{(byte) 0xde, (byte) 0xad, (byte) 0xbe, (byte) 0xef, (byte) 0xfa, (byte) 0xce, (byte) 0xfe, (byte) 0xed};
    }

    @Test
    public void testGettingBits() {
        assertEquals("deadbeef", Integer.toHexString(randomDataBuffer.next(32, dataSupplier)));
        assertEquals("f", Integer.toHexString(randomDataBuffer.next(4, dataSupplier)));
        assertEquals("ac", Integer.toHexString(randomDataBuffer.next(8, dataSupplier)));
        assertEquals("efee", Integer.toHexString(randomDataBuffer.next(16, dataSupplier)));
        assertEquals("ddeadbee", Integer.toHexString(randomDataBuffer.next(32, dataSupplier)));
        assertEquals("f", Integer.toHexString(randomDataBuffer.next(4, dataSupplier)));

        for (int i = 0; i < 32; i++) {
            assertEquals("" + Integer.toBinaryString(0xfacefeed).charAt(i), Integer.toBinaryString(randomDataBuffer.next(1, dataSupplier)));
        }
        for (int i = 0; i < 32; i++) {
            assertEquals("" + Integer.toBinaryString(0xdeadbeef).charAt(i), Integer.toBinaryString(randomDataBuffer.next(1, dataSupplier)));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void shouldFailIfNoDataIsProvided() {
        randomDataBuffer.next(1, (i) -> new byte[0]);
    }
}
