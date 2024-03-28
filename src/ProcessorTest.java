import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProcessorTest {


//    @Test
//    void testGenerate25InR3() {
//        // Assuming binary strings are correctly encoded following the instruction formats
//        String[] program = {
//
//                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
//                "000000000000000001" + "0000" + "00001" + "00001",
//                  // MATH ADD R2, R1, R3
//                 "00000000000000000000000000000000"         // HALT
////                 Placeholder for example. Actual binary strings need to follow the SIA32 encoding.
//        };
//
//        MainMemory.load(program);
//        Processor.run();
//
//        assertEquals(1, Processor.registers[1].getUnsigned(), "R3 should contain 25 after execution");
//
//    }


//    @Test
//    void testGenerate9InR3() {
//        // Assuming binary strings are correctly encoded following the instruction formats
//        String[] program = {
//
//                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
//                "000000000000000011" + "0000" + "00010" + "00001", //puts 1 in register 2
//
//                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
//                "000000000000000011" + "0000" + "00011" + "00001", //puts 3 in register 3
//
//                //For 2R format: | immediate (13 bits) | Rs (5 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
//                "0000000000001" + "00010" + "0111" + "00011" + "00010",
//
//                "00000000000000000000000000000000"         // HALT
////                 Placeholder for example. Actual binary strings need to follow the SIA32 encoding.
//        };
//
//        MainMemory.load(program);
//        Processor.run();
//
//        assertEquals(9, Processor.registers[3].getUnsigned(), "R3 should contain 25 after execution");
//
//
//    }

    @Test
    void testGenerate9InR3() {
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
