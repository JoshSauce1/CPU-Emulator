import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProcessorTest2 {


    @Test
    void testSequenceOfOperations() {

        String[] program = {
                "000000000000000010"+"0000"+"00001"+"00001",
                "000000000000000011"+"0000"+"00010"+"00001",
                "00000000000000000000000000000000"

        };
        MainMemory.load(program);
        Processor.run();
        assertEquals(2, Processor.registers[1].getUnsigned(), "R1 should contain immediate value 2");
        assertEquals(3, Processor.registers[2].getUnsigned(), "R2 should contain immediate value 3");
        assertTrue(Processor.halted, "Processor should be halted after executing HALT instruction.");
    }
}
