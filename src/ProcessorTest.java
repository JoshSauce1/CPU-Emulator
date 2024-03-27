import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProcessorTest {


    @Test
    void testGenerate25InR3() {
        // Assuming binary strings are correctly encoded following the instruction formats
        String[] program = {

                //(Rd) + (Function) + (Immediate) + (Format) + (Opcode)
                "00001" + "1110"  + "000000000000000101" + "01" + "000",
                  // MATH ADD R2, R1, R3
                 "00000000000000000000000000000000"         // HALT
//                 Placeholder for example. Actual binary strings need to follow the SIA32 encoding.
        };

        MainMemory.load(program);
        Processor.run();

        assertEquals(5, Processor.registers[1].getUnsigned(), "R3 should contain 25 after execution");

    }

    // Additional tests for other operations and instruction formats
}
