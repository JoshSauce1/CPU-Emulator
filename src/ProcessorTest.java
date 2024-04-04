import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProcessorTest {


    @BeforeEach
    void resetRegisters() {
        // Reset registers before each test
        Processor.PC.set(0);
        Processor.SP.set(1023);
        Processor.halted = false;
        for (int i = 0; i < Processor.registers.length; i++) {
            Processor.registers[i].set(0);
        }

    }


    //Branch
    @Test
    void test00100op() {
        String[] program = {

                //For 0R format: | immediate (27 bits) | opcode (5 bits) |

                "000000000000000000000000011" + "00100"
        };

        MainMemory.load(program);
        Processor.run();

        assertEquals(5, Processor.PC.getUnsigned(), "PC should be 5 after setting it to 3 then runs two times after halting");


    }

    @Test
    void test00101op() {
        String[] program = {

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000011" + "0000" + "00000" + "00101",
        };

        MainMemory.load(program);
        Processor.run();

        assertEquals(5, Processor.PC.getUnsigned(), "PC should be 5 after adding 3 to PC which is 0 then runs two times ");


    }

    @Test
    void test00110op() {

        String[] program = {

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000010" + "0000" + "00010" + "00001", //puts 2 in register 2

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000011" + "0000" + "00011" + "00001", //puts 3 in register 2

                // For 2R format: | immediate (13 bits) | Rs (5 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "0000000000001" + "00010" + "0000" + "00011" + "00110", //Should skip next line if RS does not equals RD

                //Random for testing..
                "00000000000000000000000000000101"
        };

        MainMemory.load(program);
        Processor.run();

        assertEquals(5, Processor.PC.getUnsigned(), "PC should be 5 after skipping line 4 then halting ");
    }


    @Test
    void test00111op() {

        String[] program = {

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000010" + "0000" + "00010" + "00001", //puts 2 in register 2

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000011" + "0000" + "00011" + "00001", //puts 3 in register 3

                // For 3R format: | immediate (8 bits) | Rs1 (5 bits) | Rs2 (5 bits) | Function (4 bits) | RD (5 bits) | Opcode (5 bits)
                "00000010" + "00010" + "00011" + "0000" + "00011" + "00111", // Skips next 2 lines if rs1 and rs2 arent equal

                "00000000000000000000000000001001"
        };

        MainMemory.load(program);
        Processor.run();

        assertEquals(6, Processor.PC.getUnsigned(), "PC should be 6 after skipping line 4 and 5 then halting ");
    }


    //Call functions

    @Test
    void test01000op() {

        String[] program = {

                //For 0R format: | immediate (27 bits) | opcode (5 bits) |

                "000000000000000000000000011" + "01000" // puts pc at 3 then pushes old pc value on the stack
        };

        resetRegisters();
        MainMemory.load(program);
        Processor.run();

        assertEquals(1022, Processor.SP.getUnsigned()); //SP should be 1022 after writing old pc to it and decrementing


    }


    @Test
    void test01001op() { //Call Dest only

        String[] program = {

                // For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000011" + "0000" + "00001" + "01001"
        };

        resetRegisters();
        MainMemory.load(program);
        Processor.run();

        assertEquals(1022, Processor.SP.getUnsigned()); //SP should be 1022 after writing old pc to it and decrementing


    }


    @Test
    void test01010op() { //Call 2R

        String[] program = {

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000010" + "0000" + "00010" + "00001", //puts 2 in register 2

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000011" + "0000" + "00011" + "00001", //puts 3 in register 3

                // For 2R format: | immediate (13 bits) | Rs (5 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "0000000000011" + "00010" + "0000" + "00011" + "01010", //Should skip next line if RS does not equals RD

                //Random for testing..
                "00000000000000000000000000000101"
        };

        MainMemory.load(program);
        Processor.run();

        assertEquals(7, Processor.PC.getUnsigned(), "PC should be 7 after adding immediate to pc then halting ");


    }



    @Test
    void test01011op() { //Call 3R

        String[] program = {

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000010" + "0000" + "00010" + "00001", //puts 2 in register 2

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000011" + "0000" + "00011" + "00001", //puts 3 in register 3

                // For 3R format: | immediate (8 bits) | Rs1 (5 bits) | Rs2 (5 bits) | Function (4 bits) | RD (5 bits) | Opcode (5 bits)
                "00000010" + "00010" + "00011" + "0000" + "00011" + "01011", // Skips next 2 lines if rs1 and rs2 arent equal

                "00000000000000000000000000001001", "00000000000000000000000000000000"
        };

        MainMemory.load(program);
        Processor.run();

        assertEquals(7, Processor.PC.getUnsigned(), "PC should be 7 after adding rd 3 and immediate 2 to pc ");

    }


    //PUSH Tests


    @Test
    void test01100op() { //Push 0R

        String[] program = {

                //For 0R format: | immediate (27 bits) | opcode (5 bits) |

                "000000000000000000000000011" + "01100",
                "000000000000000000000000000" + "00000"
        };

        resetRegisters();
        MainMemory.load(program);
        Processor.run();

        //Should output that "this is not a valid format" in the console

    }


    @Test
    void test01101op() { //Push 1R

        String[] program = {

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000010" + "0000" + "00010" + "00001", //puts 2 in register 2

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000011" + "0111" + "00010" + "01101", // RD MOP Immediate then stores result in stack

                "000000000000000000000000000" + "00000"
        };

        resetRegisters();
        MainMemory.load(program);
        Processor.run();

        assertEquals(3, Processor.PC.getUnsigned());

    }

    @Test
    void test01110op() { //PUSH 2R
        // Assuming binary strings are correctly encoded following the instruction formats
        String[] program = {

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000011" + "0000" + "00010" + "00001", //puts 3 in register 2

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000011" + "0000" + "00011" + "00001", //puts 3 in register 3

                //For 2R format: | immediate (13 bits) | Rs (5 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "0000000000001" + "00010" + "0111" + "00011" + "01110",

                "00000000000000000000000000000000"         // HALT

        };

        MainMemory.load(program);
        Processor.run();

    }


    @Test
    void test01111op() {
        // Assuming binary strings are correctly encoded following the instruction formats
        String[] program = {

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000011" + "0000" + "00010" + "00001", //puts 2 in register 2

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000011" + "0000" + "00011" + "00001", //puts 3 in register 3

                // For 3R format: | immediate (8 bits) | Rs1 (5 bits) | Rs2 (5 bits) | Function (4 bits) | RD (5 bits) | Opcode (5 bits)
                "00000000" + "00010" + "00011" + "0111" + "00011" + "01111", //adds reg 2 and 3 together and puts result in reg 3


        };

        MainMemory.load(program);
        Processor.run();

        //Should store value 6 in Stack


    }


    // LOAD tests



    @Test
    void test10000op() { //Load 0R

        String[] program = {

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000010" + "0000" + "00010" + "00001", //puts 2 in register 2

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000011" + "0111" + "00010" + "01101", // RD MOP Immediate then stores result in stack

                //For 0R format: | immediate (27 bits) | opcode (5 bits) |
                "000000000000000000000000000" + "10000" // Sets the PC to POP

        };

        resetRegisters();
        MainMemory.load(program);
        Processor.run();

        assertEquals(8, Processor.PC.getUnsigned()); //Should be 8 after setting PC to 6 then running two times after

    }


    @Test
    void test10001op() { //Load 1R

        MainMemory memory = null;

        Word address = new Word();
        address.set(4); // Writing to address 4
        Word newValue = new Word();
        newValue.set(6); // Example new value
        memory.write(address, newValue);

        String[] program = {

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000010" + "0000" + "00100" + "00001", //puts 2 in register 4

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000010" + "0000" + "00100" + "10001", //Adds 2 to 2 val in register 4 in memory array then sets value to rd

                "000000000000000000" + "0000" + "00000" + "00000"
        };

        resetRegisters();
        MainMemory.load(program);
        Processor.run();

        Word actual = memory.read(address);
        Assert.assertEquals(Processor.registers[4].getUnsigned(), actual.getUnsigned()); //Register 4 should contain value 6
    }



    @Test
    void test10010op() { //Load 2R

        MainMemory memory = null;

        Word address = new Word();
        address.set(4); // Writing to address 4
        Word newValue = new Word();
        newValue.set(6); // Example new value
        memory.write(address, newValue);

        String[] program = {

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000010" + "0000" + "00100" + "00001", //puts 2 in register 4

                // For 2R format: | immediate (13 bits) | Rs (5 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "0000000000010" + "00100" + "0000" + "00100" + "10010", //Adds 2 to 2 val in register 4 in memory array then sets value to rd

                "000000000000000000" + "0000" + "00000" + "00000"
        };

        resetRegisters();
        MainMemory.load(program);
        Processor.run();

        Word actual = memory.read(address);
        Assert.assertEquals(Processor.registers[4].getUnsigned(), actual.getUnsigned()); //Register 4 should contain value 6 after reading from mem
    }

    @Test
    void test10011op() { //Load 3R

        MainMemory memory = null;

        Word address = new Word();
        address.set(4); // Writing to address 4
        Word newValue = new Word();
        newValue.set(6); // Example new value
        memory.write(address, newValue);

        String[] program = {

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000010" + "0000" + "00100" + "00001", //puts 2 in register 4

                // For 2R format: | immediate (13 bits) | Rs (5 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "0000000000010" + "00100" + "0000" + "00100" + "10010", //Adds 2 to 2 val in register 4 in memory array then sets value to rd

                "000000000000000000" + "0000" + "00000" + "00000"
        };

        resetRegisters();
        MainMemory.load(program);
        Processor.run();

        Word actual = memory.read(address);
        Assert.assertEquals(Processor.registers[4].getUnsigned(), actual.getUnsigned()); //Register 4 should contain value 6 after reading from mem
    }



    //Store tests

    @Test
    void test10101op() { //Store 1R

        MainMemory memory = null;

        Word address = new Word();
        address.set(4); // Writing to address 4
        Word newValue = new Word();
        newValue.set(6); // Example new value
        memory.write(address, newValue);

        String[] program = {

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000100" + "0000" + "00100" + "00001", //puts 4 in register 4

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000111" + "0000" + "00100" + "10101", //7 in address 4

                "00000000000000000000000000000000" //Halt
        };

        resetRegisters();
        MainMemory.load(program);
        Processor.run();

        Word actual = memory.read(address);
        Assert.assertEquals(7, actual.getUnsigned()); //Value should be 7, not 6 after storing immediate into memory
    }


    @Test
    void test10110op() { //Store 2R

        MainMemory memory = null;

        Word address = new Word();
        address.set(4); // Writing to address 4
        Word newValue = new Word();
        newValue.set(6); // Example new value
        memory.write(address, newValue);

        String[] program = {

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000010" + "0000" + "00100" + "00001", //puts 2 in register 4

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000011" + "0000" + "00011" + "00001", //puts 3 in register 3

                // For 2R format: | immediate (13 bits) | Rs (5 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "0000000000010" + "00011" + "0000" + "00100" + "10110", //stores rs value adds rd 2 and immediate 2

                "00000000000000000000000000000000" //Halt
        };

        resetRegisters();
        MainMemory.load(program);
        Processor.run();

        Word actual = memory.read(address);
        Assert.assertEquals(2, actual.getUnsigned()); //Value should be 2, not 6 after storing rs value into memory
    }


    @Test
    void test10111op() { //Store 3R

        MainMemory memory = null;

        Word address = new Word();
        address.set(5); // Writing to address 4
        Word newValue = new Word();
        newValue.set(6); // Example new value
        memory.write(address, newValue);

        String[] program = {

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000010" + "0000" + "00100" + "00001", //puts 2 in register 4

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000011" + "0000" + "00011" + "00001", //puts 3 in register 3

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000010" + "0000" + "00101" + "00001", //puts 2 in register 5

                // For 3R format: | immediate (8 bits) | Rs1 (5 bits) | Rs2 (5 bits) | Function (4 bits) | RD (5 bits) | Opcode (5 bits) |
                "00000000" + "00100" +"00101" + "0000" + "00011" + "10111", //stores rs value adds rd 2 and immediate 2

                "00000000000000000000000000000000" //Halt
        };

        resetRegisters();
        MainMemory.load(program);
        Processor.run();

        Word actual = memory.read(address);
        Assert.assertEquals(2, actual.getUnsigned()); //Value should be 2, not 6 after storing rs2 value into memory
    }


    @Test
    void test11001op() {


        String[] program = {

                "00000000000100000000000100000001",

                // For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000001" + "0000" + "00001" + "01001",

                "00000000000100000000000100000001", //Random for testing

                //For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
                "000000000000000010" + "0000" + "00100" + "11001", //pops pc in register 4

                "00000000000000000000000000000000" //Halt
        };

        resetRegisters();
        MainMemory.load(program);
        Processor.run();

        Assert.assertEquals(5, Processor.PC.getUnsigned()); //Value should be 5
    }





    }


