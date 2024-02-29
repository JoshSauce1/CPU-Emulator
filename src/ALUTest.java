import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ALUTest {
    @Test
    void testAdd() {
        ALU alu = new ALU();
        alu.op1.set(3); // Example: 3 in binary is 11
        alu.op2.set(5); // Example: 5 in binary is 101
        Word result = alu.add(alu.op1, alu.op2);
        assertEquals(8, result.getUnsigned()); // 3 + 5 = 8
    }

    @Test
    void testAdd2() {
        ALU alu = new ALU();
        alu.op1.set(10);
        alu.op2.set(10);
        Word result = alu.add(alu.op1, alu.op2);
        assertEquals(20, result.getUnsigned());
    }

    @Test
    void testAdd2neg() {
        ALU alu = new ALU();
        alu.op1.set(-5);
        alu.op2.set(10);
        Word result = alu.add(alu.op1, alu.op2);
        assertEquals(5, result.getUnsigned());
    }



    @Test
    void testSubtract() {
        ALU alu = new ALU();
        alu.op1.set(10); // Example: 10
        alu.op2.set(4);  // Example: 4
        Word result = alu.subtract(alu.op1, alu.op2);
        assertEquals(6, result.getUnsigned());
    }

    @Test
    void testSubtractNeg() {
        ALU alu = new ALU();
        alu.op1.set(10); // Example: 10
        alu.op2.set(-5);  // Example: 4
        Word result = alu.subtract(alu.op1, alu.op2);
        assertEquals(15, result.getUnsigned());
    }

    @Test
    void testMultiply() {
        ALU alu = new ALU();
        alu.op1.set(11); // Example: 6
        alu.op2.set(4); // Example: 7
        Word result = alu.multiply(alu.op1, alu.op2);
        assertEquals(44, result.getUnsigned());
    }

    @Test
    void testMultiplyLarge() {
        ALU alu = new ALU();
        alu.op1.set(552); // Example: 6
        alu.op2.set(112); // Example: 7
        Word result = alu.multiply(alu.op1, alu.op2);
        assertEquals(61824, result.getUnsigned());
    }


    @Test
    void testAdd4Zeros() {
        ALU alu = new ALU();
        alu.op1.set(0);
        alu.op2.set(0);
        Word op3 = new Word();
        op3.set(0);
        Word op4 = new Word();
        op4.set(0);
        Word result = alu.add4(alu.op1, alu.op2, op3, op4);
        assertEquals(0, result.getUnsigned(), "Adding zeros should result in zero.");
    }


    @Test
    void testAdd4Mixed() {
        ALU alu = new ALU();
        alu.op1.set(2); // 10 in binary
        alu.op2.set(3); // 11 in binary
        Word op3 = new Word();
        op3.set(4);     // 100 in binary
        Word op4 = new Word();
        op4.set(1);     // 1 in binary
        Word result = alu.add4(alu.op1, alu.op2, op3, op4);
        assertEquals(10, result.getUnsigned());
    }

    @Test
    void testAdd4Large() {
        ALU alu = new ALU();
        alu.op1.set(1024);
        alu.op2.set(2048);
        Word op3 = new Word();
        op3.set(4096);
        Word op4 = new Word();
        op4.set(8192);
        Word result = alu.add4(alu.op1, alu.op2, op3, op4);
        assertEquals(15360, result.getUnsigned());
    }

    @Test
    void testAdd4Ones() {
        ALU alu = new ALU();
        alu.op1.set(1);
        alu.op2.set(1);
        Word op3 = new Word();
        op3.set(1);
        Word op4 = new Word();
        op4.set(1);
        Word result = alu.add4(alu.op1, alu.op2, op3, op4);
        assertEquals(4, result.getUnsigned());
    }








}
