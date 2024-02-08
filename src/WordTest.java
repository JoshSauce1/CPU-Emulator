import org.junit.Test;

import static org.junit.Assert.*;

public class WordTest {

    @Test
    public void testGetBit() {
        Word word = new Word();
        Bit bit = word.getBit(0);
        assertEquals(false, bit.getValue());
    }

    @Test
    public void testSetBit() {
        Word word = new Word();
        Bit bit = new Bit(true);
        word.setBit(0, bit);
        assertEquals(true, word.getBit(0).getValue());
    }

    @Test
    public void testAnd() {
        Word word1 = new Word();
        Word word2 = new Word();

        // Sets all bits in word2 to true
        for (int i = 0; i < 32; i++) {
            word2.setBit(i, new Bit(true));
        }

        Word result = word1.and(word2);

        for (int i = 0; i < 32; i++) {
            assertEquals(false, result.getBit(i).getValue());
        }
    }

    @Test
    public void testOr() {
        Word word1 = new Word();
        Word word2 = new Word();

        // Sets all bits in word2 to true
        for (int i = 0; i < 32; i++) {
            word2.setBit(i, new Bit(true));
        }

        Word result = word1.or(word2);

        for (int i = 0; i < 32; i++) {
            assertEquals(true, result.getBit(i).getValue());
        }
    }

    @Test
    public void testXor() {
        Word word1 = new Word();
        Word word2 = new Word();

        // Sets every even-indexed bit in word2 to true
        for (int i = 0; i < 32; i += 2) {
            word2.setBit(i, new Bit(true));
        }

        Word result = word1.xor(word2);

        for (int i = 0; i < 32; i++) {
            assertEquals(i % 2 == 0, result.getBit(i).getValue());
        }
    }

    @Test
    public void testNot() {
        Word word = new Word();

        // Setting all bits to true
        for (int i = 0; i < 32; i++) {
            word.setBit(i, new Bit(true));
        }

        Word result = word.not();

        for (int i = 0; i < 32; i++) {
            assertEquals(false, result.getBit(i).getValue());
        }
    }

    @Test
    public void testRightShift() {
        Word word = new Word();

        // Setting every even index bit to true
        for (int i = 0; i < 32; i += 2) {
            word.setBit(i, new Bit(true));
        }

        Word result = word.rightShift(1);

        // After right shifting, every bit should be shifted right by 1
        for (int i = 0; i < 31; i++) {
            assertEquals(i % 2 == 0, result.getBit(i + 1).getValue());
        }

        // The last bit should be false after shifting
        assertEquals(false, result.getBit(0).getValue());
    }

    @Test
    public void testLeftShiftAllTrue() {
        Word word = new Word();

        // Setting every bit to true
        for (int i = 0; i < 32; i++) {
            word.setBit(i, new Bit(true));
        }

        Word result = word.leftShift(1);

        // After left shifting, every bit should be shifted left by 1
        for (int i = 1; i < 32; i++) {
            assertEquals(true, result.getBit(i - 1).getValue());
        }

        // The first bit should be false after shifting
        assertEquals(false, result.getBit(31).getValue());
    }

    @Test
    public void testToString() {
        Word word = new Word();

        // sets every even index bit to true
        for (int i = 0; i < 32; i += 2) {
            word.setBit(i, new Bit(true));
        }

        String expected = "t,f,t,f,t,f,t,f,t,f,t,f,t,f,t,f,t,f,t,f,t,f,t,f,t,f,t,f,t,f,t,f";
        assertEquals(expected, word.toString());
    }



    @Test
    public void testGetUnsignedAllTrue() {
        Word word = new Word();

        // Setting every bit to true
        for (int i = 0; i < 32; i++) {
            word.setBit(i, new Bit(true));
        }

        // The unsigned value should be 4294967295 (2^32 - 1)
        assertEquals(4294967295L, word.getUnsigned());
    }

    @Test
    public void testGetSignedAllTrue() {
        Word word = new Word();

        // Setting every bit to true
        for (int i = 0; i < 32; i++) {
            word.setBit(i, new Bit(true));
        }

        // The signed value should be -1
        assertEquals(-1, word.getSigned());
    }


    @Test
    public void testCopy() {
        Word word1 = new Word();
        Word word2 = new Word();

        // Sets every even index bit in word2 to true
        for (int i = 0; i < 32; i += 2) {
            word2.setBit(i, new Bit(true));
        }

        word1.copy(word2);

        for (int i = 0; i < 32; i++) {
            assertEquals(i % 2 == 0, word1.getBit(i).getValue());
        }
    }

    @Test
    public void testSetMethodWithZero() {
        Word word = new Word();

        // Set the value to 0
        word.set(0);

        // Verify that every bit is set to false
        for (int i = 0; i < 32; i++) {
            assertFalse(word.getBit(i).getValue());
        }
    }






}
