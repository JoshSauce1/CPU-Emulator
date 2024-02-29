import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WordTest2 {

    @Test
    public void testRightShiftNoOp() {
        Word word = new Word();
        word.setBit(15, new Bit(true)); // Adjusted to use Bit object
        Word shifted = word.rightShift(0);
        assertTrue(shifted.getBit(15).getValue(), "Bit at index 15 should remain true after shifting by 0.");
    }

    @Test
    public void testRightShiftByOne() {
        Word word = new Word();
        word.setBit(1, new Bit(true)); // Adjusted to use Bit object
        Word shifted = word.rightShift(1);
        assertTrue(shifted.getBit(0).getValue(), "Bit at index 0 should be true after shifting by 1.");
        assertFalse(shifted.getBit(1).getValue(), "Bit at index 1 should be false after shifting by 1.");
    }

    @Test
    public void testRightShiftByTwo() {
        Word word = new Word();
        word.setBit(2, new Bit(true)); // Adjusted to use Bit object
        Word shifted = word.rightShift(2);
        assertTrue(shifted.getBit(0).getValue(), "Bit at index 0 should be true after shifting by 2.");
        assertFalse(shifted.getBit(2).getValue(), "Bit at index 2 should be false after shifting by 2.");
    }

    @Test
    public void testRightShiftByWordSize() {
        Word word = new Word();
        word.setBit(15, new Bit(true)); // Adjusted to use Bit object
        Word shifted = word.rightShift(32);
        for (int i = 0; i < 32; i++) {
            assertFalse(shifted.getBit(i).getValue(), "All bits should be false after right shifting by word size.");
        }
    }

    @Test
    public void testRightShiftGreaterThanWordSize() {
        Word word = new Word();
        word.setBit(15, new Bit(true)); // Adjusted to use Bit object
        Word shifted = word.rightShift(33);
        for (int i = 0; i < 32; i++) {
            assertFalse(shifted.getBit(i).getValue(), "All bits should be false after right shifting by more than word size.");
        }
    }

    @Test
    public void testLeftShift() {
        Word word = new Word();

        // Setting every odd index bit to true
        for (int i = 1; i < 32; i += 2) {
            word.setBit(i, new Bit(true));
        }

        Word result = word.leftShift(1);

        // After left shifting, every original odd index bit should be at even index
        assertTrue(result.getBit(0).getValue(), "Expected bit[0] to be true after left shift");
        for (int i = 1; i < 32; i++) {
            // Now, we expect every even index (which was odd before the shift) to be true
            // and every odd index to be false.
            if (i % 2 == 0) {
                assertTrue(result.getBit(i).getValue(), "Expected bit[" + i + "] to be true after left shift");
            } else {
                assertFalse(result.getBit(i).getValue(), "Expected bit[" + i + "] to be false after left shift");
            }
        }
        // Specifically asserting bit 31 to be false as it's the new bit coming in from the shift
        assertFalse(result.getBit(31).getValue(), "Expected bit[31] to be false after left shift");
    }


    @Test
    void testLeftShiftByZero() {
        Word word = new Word();
        word.set(1); // Assuming set(int value) correctly initializes the word
        Word shifted = word.leftShift(0);
        assertEquals(word.getUnsigned(), shifted.getUnsigned(), "Shifting by 0 should not change the word.");
    }

    @Test
    void testLeftShiftByOne() {
        Word word = new Word();
        word.set(1); // Setting LSB to 1, representing the number 1
        Word shifted = word.leftShift(1);
        assertEquals(2, shifted.getUnsigned(), "Shifting by 1 should double the value.");
    }

    @Test
    void testLeftShiftByWordSize() {
        Word word = new Word();
        word.set(1); // Set initially to 1
        Word shifted = word.leftShift(32);
        assertEquals(0, shifted.getUnsigned(), "Shifting by the word size should result in 0.");
    }

    @Test
    void testLeftShiftMoreThanWordSize() {
        Word word = new Word();
        word.set(1); // Set initially to 1
        Word shifted = word.leftShift(33);
        assertEquals(0, shifted.getUnsigned(), "Shifting more than the word size should result in 0.");
    }



    @Test
    public void testShiftBeyondWordLength() {
        Word word = new Word();
        word.setBit(0, new Bit(true)); // Set the leftmost bit to true

        Word rightShifted = word.rightShift(32);
        Word leftShifted = word.leftShift(32);

        // Verify all bits are false after shifting by the word's size
        for (int i = 0; i < 32; i++) {
            assertFalse(rightShifted.getBit(i).getValue(), "Expected all bits to be false after right shifting by 32");
            assertFalse(leftShifted.getBit(i).getValue(), "Expected all bits to be false after left shifting by 32");
        }
    }

    @Test
    public void testGetUnsignedWithZero() {
        Word word = new Word(); // Default to all bits false
        assertEquals(0L, word.getUnsigned(), "Unsigned value of all 0 bits should be 0.");
    }

    @Test
    public void testGetUnsignedWithMaxValue() {
        Word word = new Word();
        for (int i = 0; i < 32; i++) {
            word.setBit(i, new Bit(true)); // Set all bits to 1
        }
        assertEquals(0xFFFFFFFFL, word.getUnsigned(), "Unsigned value of all 1 bits should be 4294967295.");
    }

    @Test
    public void testGetSignedWithZero() {
        Word word = new Word(); // Default to all bits false
        assertEquals(0, word.getSigned(), "Signed value of all 0 bits should be 0.");
    }

    @Test
    public void testGetSignedWithNegativeOne() {
        Word word = new Word();
        for (int i = 0; i < 32; i++) {
            word.setBit(i, new Bit(true)); // Set all bits to 1
        }
        assertEquals(-1, word.getSigned(), "Signed value of all 1 bits should be -1.");
    }

    @Test
    public void testGetSignedWithMaxInt() {
        Word word = new Word();
        for (int i = 0; i < 31; i++) { // Set all bits to 1 except the sign bit
            word.setBit(i, new Bit(true));
        }
        assertEquals(Integer.MAX_VALUE, word.getSigned(), "Signed value should be Integer.MAX_VALUE.");
    }

    @Test
    public void testGetSignedWithMinInt() {
        Word word = new Word();
        word.setBit(31, new Bit(true)); // Set only the sign bit
        assertEquals(Integer.MIN_VALUE, word.getSigned(), "Signed value should be Integer.MIN_VALUE.");
    }



    // Test Increment





}
