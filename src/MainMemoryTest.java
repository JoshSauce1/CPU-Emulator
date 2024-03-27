import org.junit.*;
import static org.junit.Assert.*;

public class MainMemoryTest {

    private MainMemory memory;
    private String[] data;

    @Before
    public void setUp() {
        // Init MainMemory with sample data before each test
        data = new String[]{"00000000000000000000000000000001", "00000000000000000000000000000010"};
        MainMemory.load(data);
    }


    @Test
    public void testWrite() {
        Word address = new Word();
        address.set(1); // Writing to address 1
        Word newValue = new Word();
        newValue.set(3); // Example new value
        memory.write(address, newValue);

        Word actual = memory.read(address);
        assertEquals(newValue.toString(), actual.toString());
    }

    @Test
    public void testIncrement() {
        // Init a Word to test increment func
        Word testWord = new Word();
        testWord.set(0); // Set to value 0
        testWord.increment(); // Incrementing test word to 1
        Word expected = new Word();
        expected.set(1); //expected value should be 1

        assertEquals(expected.toString(), testWord.toString());
    }

    @Test
    public void testRead() {
        // Test reading from memory loaded in setUp
        Word address = new Word();
        address.set(1); // Address of the second element in data

        // Expected should match the second binary string
        Word expected = new Word();
        for (int i = 0; i < 32; i++) {
            expected.setBit(i, new Bit(data[1].charAt(i) == '1'));
        }

        Word actual = MainMemory.read(address);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testWriteAndVerifyMultipleValues() {
        String[] valuesToWrite = {
                "00000000000000000000000000000011",
                "00000000000000000000000000000100"
        };

        for (int address = 2; address <= 3; address++) {
            Word value = fromBinaryString(valuesToWrite[address - 2]);
            Word addressWord = new Word();
            addressWord.set(address);
            MainMemory.write(addressWord, value);
        }

        for (int address = 2; address <= 3; address++) {
            Word expected = fromBinaryString(valuesToWrite[address - 2]);
            Word addressWord = new Word();
            addressWord.set(address);
            Word actual = MainMemory.read(addressWord);
            assertEquals(expected.toString(), actual.toString());
        }
    }


    @Test
    public void testOverwriteValue() {
        Word address = new Word();
        address.set(1);
        Word newValue = fromBinaryString("00000000000000000000000000001010");
        MainMemory.write(address, newValue);

        Word actual = MainMemory.read(address);
        assertEquals(newValue.toString(), actual.toString());
    }


    @Test
    public void testReadOutOfRange() {
        // Test reading from an address out of range
        Word address = new Word();
        address.set(1024); // Out of range address
        Word actualValue = MainMemory.read(address);
        assertEquals(null, actualValue);
    }

    private Word fromBinaryString(String binaryString) {
        Word word = new Word();
        for (int i = 0; i < binaryString.length(); i++) {
            word.setBit(i, new Bit(binaryString.charAt(i) == '1'));
        }
        return word;
    }

}
