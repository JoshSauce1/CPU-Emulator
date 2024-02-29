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
}
