import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProcessorTest2 {



    @BeforeEach
    void resetRegisters() {
        // Reset registers before each test
        Processor.PC.set(0);
        Processor.halted = false;
        for (int i = 0; i < Processor.registers.length; i++) {
            Processor.registers[i].set(0);
        }
    }

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

    @Test
    void testGenerate9InR3() {
        // Assuming binary strings are correctly encoded following the instruction formats
        String[] program = {

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000011" + "0000" + "00010" + "00001", //puts 1 in register 2

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000011" + "0000" + "00011" + "00001", //puts 3 in register 3

                //For 2R format: | immediate (13 bits) | Rs (5 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "0000000000001" + "00010" + "0111" + "00011" + "00010",

                "00000000000000000000000000000000"         // HALT
//                 Placeholder for example. Actual binary strings need to follow the SIA32 encoding.
        };

        MainMemory.load(program);
        Processor.run();

        assertEquals(9, Processor.registers[3].getUnsigned(), "R3 should contain 25 after execution");


    }

    @Test
    void test3R() {
        // Assuming binary strings are correctly encoded following the instruction formats
        String[] program = {

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000010" + "0000" + "00010" + "00001", //puts 1 in register 2

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000011" + "0000" + "00011" + "00001", //puts 3 in register 3

                // For 3R format: | immediate (8 bits) | Rs1 (5 bits) | Rs2 (5 bits) | Function (4 bits) | RD (5 bits) | Opcode (5 bits)
                "00000000" + "00010" + "00011" + "0111" + "00011" + "00011", //adds reg 2 and 3 together and puts result in reg 3


        };

        MainMemory.load(program);
        Processor.run();

        assertEquals(6, Processor.registers[3].getUnsigned(), "R3 should contain 25 after execution");


    }
}
