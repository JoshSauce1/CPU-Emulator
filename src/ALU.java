public class ALU {

    Word op1 = new Word();
    Word op2 = new Word();
    Word result = new Word();

    public void doOperation(Bit[] operation){
        int operationCode = 0;
        for (int i = 0; i < 4; i++) {
            if (operation[i].getValue()) { // If bit is true
                operationCode = (operationCode << 1) | 1;
            }
        }

        // Determine the operation based on the operation code
        if (operationCode == 0b1000) {
            result.copy(op1.and(op2));
        }
        else if (operationCode == 0b1001) { // or
            result.copy(op1.or(op2));
        }
        else if (operationCode == 0b1010) { // xor
            result.copy(op1.xor(op2));
        }
        else if (operationCode == 0b1011) { // not
            result.copy(op1.not());
        }
        // Determine the operation based on the operation code
        else if (operationCode == 0b1100) { // left shift
            // Extract the shift amount from op2 (ignore all but the lowest 5 bits)
            int shiftAmount = (int) (op2.getUnsigned() & 0b11111);

            // Perform left shift operation on op1
            result.copy(op1.leftShift(shiftAmount));
        }
        else if (operationCode == 0b1101) { // right shift
            // Extract the shift amount from op2 (ignore all but the lowest 5 bits)
            int shiftAmount = (int) (op2.getUnsigned() & 0b11111);

            // Perform right shift operation on op1
            result.copy(op1.rightShift(shiftAmount));
        }

        else if (operationCode == 0b1110) { // add
            System.out.println("Addition");
            result.copy(add(op1, op2));
        }
        else if (operationCode == 0b1111) { // subtract
            System.out.println("Subtraction");
            result.copy(subtract(op1, op2));
        }
        else if (operationCode == 0b0111) { // multiply
            result.copy(multiply(op1, op2));
        }
        else {
            // Invalid operation code
            System.out.println("Could not recognize operation code");
        }

    }

    //public for testing purposes
    //This method adds two words of 32 bits each
    public Word add(Word a, Word b) {
        Word results = new Word(); // Initialize the results Word object
        Bit carry = new Bit(false);
        for (int i = 0; i < 32; i++) {
            Bit bitA = a.getBit(i);
            Bit bitB = b.getBit(i);

            // Calculate sum of bits and carry: sum = bitA XOR bitB XOR carry
            Bit sum = bitA.xor(bitB).xor(carry);

            // Set the sum bit in the results Word
            results.setBit(i, sum);

            // Calculate carry for the next iteration
            Bit carryOut = bitA.and(bitB).or(bitA.xor(bitB).and(carry));
            carry = carryOut;
        }
        return results; // Return the results Word object
        // Overflow is ignored
    }

    public Word add4(Word a, Word b, Word c, Word d) {
        Word result = new Word();
        Bit carry = new Bit(false); // Initial carry is false (0).

        for (int i = 0; i < 32; i++) {
            Bit bitA = a.getBit(i);
            Bit bitB = b.getBit(i);
            Bit bitC = c.getBit(i);
            Bit bitD = d.getBit(i);

            // Calculate the sum for the current bit position and the carry.
            Bit sum = bitA.xor(bitB).xor(bitC).xor(bitD).xor(carry);

            // Calculate intermediate carries.
            Bit carryAB = bitA.and(bitB);
            Bit carryAC = bitA.and(bitC);
            Bit carryAD = bitA.and(bitD);
            Bit carryBC = bitB.and(bitC);
            Bit carryBD = bitB.and(bitD);
            Bit carryCD = bitC.and(bitD);
            Bit carryAll = bitA.and(bitB).and(bitC).and(bitD);

            // Determine the final carry by combining all intermediate carries.
            Bit finalCarry = carryAB.or(carryAC).or(carryAD)
                    .or(carryBC).or(carryBD).or(carryCD)
                    .or(carry.and(bitA.xor(bitB).xor(bitC).xor(bitD)));

            // Handle the double carry scenario.
            if (carryAll.getValue()) {
                // If there was a carry from the previous bit, adjust for double carry.
                if (carry.getValue()) {
                    // This is a "double carry" scenario. Propagate an additional carry to the next bit.
                    finalCarry = new Bit(true); // Ensure the carry is set for propagation.
                }
                // Adjust the sum if all bits and initial carry are 1, indicating a carry forward.
                sum = sum.xor(carry);
            }

            // Set the calculated sum bit in the result.
            result.setBit(i, sum);

            // Update carry for the next iteration.
            carry = finalCarry;
        }

        // Since we're ignoring overflow, no need to handle bits beyond 32.
        return result; // Return the computed result.
    }




    //public for testing purposes
    //This method subtracts 32 bit words
    public Word subtract(Word a, Word b) {
        // Invert b to get its two's complement. The not() method should invert all bits in the Word.
        Word invertedB = b.not();

        // Create a Word representing 1 for adding to the inverted b to complete two's complement
        Word one = new Word();
        one.setBit(0, new Bit(true)); // Set the least significant bit to 1

        // Add one to the inverted operand to complete the two's complement
        Word complementB = add(invertedB, one);

        // Perform addition of a and the two's complement of b
        return add(a, complementB);
    }



    //public for testing purposes
    //multiplys 32 bit words
    public Word multiply(Word a, Word b) {
        Word result = new Word(); // Init result to 0
        for (int i = 0; i < 32; i++) {
            if (b.getBit(i).getValue()) {
                Word shiftedA = a.leftShift(i); // Shift 'a' left by 'i' bits for each set bit in 'b'
                result = add(result, shiftedA); // Accumulate the partial products
            }
        }
        return result; // Return the final product
    }


















}

