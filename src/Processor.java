public class Processor {

    static final Word PC = new Word(); //Program counter
    static final Word SP = new Word(); //Stack pointer
    static final Word[] registers = new Word[32];
    static final ALU alu = new ALU();
    static boolean halted = false;

    static {
        System.out.println("resetting.. ");
        PC.set(0); // Reset Program Counter
        SP.set(1023); //Sets stack
        halted = false; // Reset halted status
        for (int i = 0; i < registers.length; i++) {
            registers[i] = new Word(); // Initialize all registers to 0
        }
    }

    public static void run() {
        System.out.println("runnin.. ");
        while (!halted) {
            Word instruction = fetch();
            InstructionDetails details = decode(instruction);
            execute(details);
            PC.increment();
            System.out.println("PC: " + PC.getUnsigned());
        }
    }

    private static Word fetch() {
        System.out.println("fetching.. ");
        Word instruction = MainMemory.read(PC);

        // Directly use the Word object to construct the binary string
        StringBuilder binaryString = new StringBuilder();
        for (int i = 0; i < 32; i++) { // Assume Word object stores bits from 0 to 31
            binaryString.append(instruction.getBit(i).getValue() ? "1" : "0");
        }

        //Debugging purposes
        System.out.println("Fetched instruction in binary: " + binaryString.toString());
        return instruction;
    }


    private static InstructionDetails decode(Word instruction) {
        System.out.println("decoding..");
        InstructionDetails details = new InstructionDetails();

        // Assuming the Word class has a method to get its binary string representation
        String binaryString = instruction.toBinaryString();
        System.out.println("Binary string: " + binaryString);

        // Extract the instruction format from the binary string
        String formatBinary = binaryString.substring(30, 32); // Get the first 2 bits for the format
        int format = Integer.parseInt(formatBinary, 2); // Convert binary string to int

        String opInstructionBinary = binaryString.substring(27, 30); // Get the 3 bits for the opcode instruction
        int opInstruction = Integer.parseInt(opInstructionBinary, 2); // Convert binary string to int

        details.format = format;
        details.opInstruction = opInstruction;

        //Debugging purposes
        System.out.println("Format (binary): " + formatBinary);
        System.out.println("Format (int): " + format);

        System.out.println("opInstructionBinary (int): " + opInstructionBinary);
        System.out.println("opInstruction (int): " + opInstruction);

        // Decode the rest of the instruction based on the format
        switch (format) {
            case 0: // NoR
                decode0RInstruction(details, binaryString);
                break;
            case 1: // 1R "01"
                decode1RInstruction(details, binaryString);
                break;
            case 2: // 2R "10"
                decode2RInstruction(details, binaryString);
                break;
            case 3: // 3R "11"
                decode3RInstruction(details, binaryString);
                break;
            default:
                System.out.println("Undefined format");
                break;
        }

        return details;
    }

    private static void execute(InstructionDetails details) {
        System.out.println("executing..");
        System.out.println("details.opInstruction: " + details.opInstruction);

        // Perform operations based on opcode
        if (details.opInstruction == 0) { //Instructions for MATH 000
            switch (details.format) {
                case 0: // NoR
                    System.out.println("0R Halting.");
                    halted = true;
                    break;
                case 1: // 1R "01"
                    System.out.println("1R format execution.");
                    if (details.rd != null) {
                        Word immediateValueWord = new Word();
                        immediateValueWord.set(details.immediate);

                        // Copy the immediate value into the destination register
                        registers[details.rd].copy(immediateValueWord);
                        System.out.println("details rd " + registers[details.rd]);

                        System.out.println("Copied immediate value " + details.immediate + " into register R" + details.rd);
                    }

                    break;
                case 2: // 2R "10"
                    System.out.println("2R format execution.");
                    if (details.rs != null && details.rd != null) {
                        System.out.println("inside if");
                        // Copy values from source register and destination register to ALU operands

                        int op1 = (int) registers[details.rs].getUnsigned();
                        System.out.println("op1 value: " + op1);
                        alu.op1.set(op1);

                        int op2 = (int) registers[details.rd].getUnsigned();
                        System.out.println("op2 value: " + op2);
                        alu.op2.set(op2);
                        System.out.println("rd details: " + registers[details.rd]);

                        System.out.println("functioncode: " + details.functionCode);
                        // Perform the ALU operation
                        alu.doOperation(details.functionCode);

                        System.out.println("Math result: " + alu.result.getUnsigned());
                        int result = (int) alu.result.getUnsigned();
                        // Store the result back into the destination register
                        registers[details.rd].set(result);
                        System.out.println("final rd details: " + registers[details.rd]);
                    }

                    break;
                case 3: // 3R "11"
                    System.out.println("3R format execution.");
                    if (details.rs1 != null && details.rs2 != null && details.rd != null) {
                        // Copy values from source registers to ALU operands

                        int op1 = (int) registers[details.rs1].getUnsigned();
                        System.out.println("op1 value: " + op1);
                        int op2 = (int) registers[details.rs2].getUnsigned();
                        System.out.println("op2 value: " + op2);

                        alu.op1.set(op1);
                        alu.op2.set(op2);


                        System.out.println("functioncode: " + details.functionCode);
                        // Perform the ALU operation
                        alu.doOperation(details.functionCode);

                        System.out.println("Math result: " + alu.result.getUnsigned());
                        int result = (int) alu.result.getUnsigned();

                        // Store the result back into the destination register
                        registers[details.rd].set(result);
                        System.out.println("final rd details: " + registers[details.rd]);

                    }

                    break;
            }
        }

        //Branch Op Instruction

        if(details.opInstruction == 1){ //BRANCH 001
            System.out.println("BRANCH");
            switch(details.format) {
                case 0: //0R
                    System.out.println("format 0R: ");
                    System.out.println("immediate value: " + details.immediate);
                    Processor.PC.set(details.immediate);
                    System.out.println("PC Value: " + PC.getUnsigned());
                    break;
                case 1: //1R
                    System.out.println("format 1R: ");
                    System.out.println("immediate value: " + details.immediate);

                    int valPC = (int) PC.getUnsigned();
                    System.out.println("valPC: " +valPC);

                    Processor.PC.set(details.immediate + valPC);
                    System.out.println("PC Value: " + PC.getUnsigned());
                    break;
                case 2: //2R
                    System.out.println("format 2R: ");

                    //Set to int so its easier to deal with values..
                    int valuePC = (int) PC.getUnsigned();
                    int val1 = (int) registers[details.rs].getUnsigned();
                    System.out.println("val1 value: " + val1);
                    int val2 = (int) registers[details.rd].getUnsigned();
                    System.out.println("val2 value: " + val2);

                    System.out.println("details.functionCode: " + details.functionCode);


                    boolean evaluate = BooleanOp(details.functionCode, val1, val2);

                    if(evaluate == false){ //Skips next line if boolean condition is not met
                        System.out.println("Line skipped: " + PC.getUnsigned());
                        Processor.PC.set(details.immediate + valuePC);
                        System.out.println("PC Value: " + PC.getUnsigned());
                    }
                    break;
                case 3: //3R
                    System.out.println("format 3R: ");

                    int valuePC2 = (int) PC.getUnsigned();

                    int op1 = (int) registers[details.rs1].getUnsigned();
                    System.out.println("val1 value: " + op1);
                    int op2 = (int) registers[details.rs2].getUnsigned();
                    System.out.println("val2 value: " + op2);


                    System.out.println("details.functionCode: " + details.functionCode);


                    evaluate = BooleanOp(details.functionCode, op1, op2);

                    if(evaluate == false){ //Skips next line if boolean condition is not met
                        System.out.println("Line skipped: " + PC.getUnsigned());
                        Processor.PC.set(details.immediate + valuePC2);
                        System.out.println("PC Value: " + PC.getUnsigned());
                    }

                    break;
            }


        }


        //Instructions for call
        if(details.opInstruction == 2) { //CALL 010
            System.out.println("CALL");

            switch(details.format) {
                case 0: //0R format
                    System.out.println("format 0R: ");
                    System.out.println("immediate value: " + details.immediate);
                    push(PC);
                    System.out.println("pushed pc value: " + PC.getUnsigned());
                    Processor.PC.set(details.immediate);
                    System.out.println("PC Value: " + PC.getUnsigned());
                    break;

                case 1: //1R format
                    System.out.println("format 1R: ");
                    System.out.println("immediate value: " + details.immediate);
                    System.out.println("details rd " + registers[details.rd]);

                    int pcVal =   details.immediate + (int) registers[details.rd].getUnsigned();

                    System.out.println("pcVal: " + pcVal);

                    push(PC);
                    System.out.println("pushed pc value: " + PC.getUnsigned());
                    Processor.PC.set(pcVal);
                    System.out.println("PC Value: " + PC.getUnsigned());

                    break;
                case 2:

                    System.out.println("format 2R: ");

                    //Set to int so its easier to deal with values..
                    int valuePC = (int) PC.getUnsigned();
                    System.out.println("Beginning PC value: " + valuePC);
                    int val1 = (int) registers[details.rs].getUnsigned();
                    System.out.println("val1 value: " + val1);
                    int val2 = (int) registers[details.rd].getUnsigned();
                    System.out.println("val2 value: " + val2);

                    System.out.println("details.functionCode: " + details.functionCode);


                    boolean evaluate = BooleanOp(details.functionCode, val1, val2);

                    if(evaluate == false){ //Skips next line if boolean condition is not met
                        System.out.println("Line skipped: " + PC.getUnsigned());
                        push(PC); //Pushes PC to keep track of line
                        Processor.PC.set(details.immediate + valuePC);
                        System.out.println("PC Value: " + PC.getUnsigned());
                    }
                    break;

                case 3:
                    System.out.println("format 3R: ");

                    int valuePC2 = (int) PC.getUnsigned();
                    System.out.println("Beginning PC value: " + valuePC2);

                    int op1 = (int) registers[details.rs1].getUnsigned();
                    System.out.println("val1 value: " + op1);
                    int op2 = (int) registers[details.rs2].getUnsigned();
                    System.out.println("val2 value: " + op2);

                    System.out.println("rd value:" + registers[details.rd].getUnsigned());


                    System.out.println("details.functionCode: " + details.functionCode);


                    evaluate = BooleanOp(details.functionCode, op1, op2);

                    if(evaluate == false){ //Skips next line if boolean condition is not met
                        System.out.println("Line skipped: " + PC.getUnsigned());
                        System.out.println("PC set to: rd = " + (int)registers[details.rd].getUnsigned() + " + immediate =" + details.immediate);
                        Processor.PC.set( (int)registers[details.rd].getUnsigned() + details.immediate);
                        System.out.println("PC Value: " + PC.getUnsigned());
                    }

                    break;
            }

        }


        if(details.opInstruction == 3) {// PUSH 011
            System.out.println("PUSH");
            switch(details.format) {
                case 0:
                    System.out.println("format 0R:");
                    System.out.println("Format not used");
                    break;
                case 1:
                    System.out.println("format 1R:");
                    if (details.rd != null) {
                        // Copy values from source registers to ALU operands

                        int operand1 = (int) registers[details.rd].getUnsigned();
                        System.out.println("operand1 value: " + operand1);
                        int operand2 = (int) details.immediate;
                        System.out.println("operand2 value: " + operand2);

                        alu.op1.set(operand1);
                        alu.op2.set(operand2);


                        System.out.println("functioncode: " + details.functionCode);
                        // Perform the ALU operation
                        alu.doOperation(details.functionCode);

                        System.out.println("Math result: " + alu.result.getUnsigned());
                        int result = (int) alu.result.getUnsigned();

                        // Store the result into the stack

                        Word finalResult = new Word();
                        finalResult.set(result);

                        push(finalResult);

                    }

                    break;

                case 2: //PUSH 01110
                    System.out.println("2R format execution.");
                    if (details.rs != null && details.rd != null) {
                        System.out.println("inside if");
                        // Copy values from source register and destination register to ALU operands

                        int op1 = (int) registers[details.rs].getUnsigned();
                        System.out.println("op1 value: " + op1);
                        alu.op1.set(op1);

                        int op2 = (int) registers[details.rd].getUnsigned();
                        System.out.println("op2 value: " + op2);
                        alu.op2.set(op2); //
                        System.out.println("rd details: " + registers[details.rd].getUnsigned());

                        System.out.println("functioncode: " + details.functionCode);
                        // Perform the ALU operation
                        alu.doOperation(details.functionCode);

                        System.out.println("Math result: " + alu.result.getUnsigned());
                        int result = (int) alu.result.getUnsigned();

                        // Store the result in the stack
                        Word finalResult = new Word();
                        finalResult.set(result);

                        push(finalResult);

                    }

                    break;

                case 3: //PUSH 01111
                    System.out.println("3R format execution.");
                    if(details.rs1 != null && details.rs2 != null && details.rd != null){

                        // Copy values from source registers to ALU operands

                        int op1 = (int) registers[details.rs1].getUnsigned();
                        System.out.println("op1 value: " + op1);
                        int op2 = (int) registers[details.rs2].getUnsigned();
                        System.out.println("op2 value: " + op2);

                        alu.op1.set(op1);
                        alu.op2.set(op2);

                        System.out.println("functioncode: " + details.functionCode);
                        // Perform the ALU operation
                        alu.doOperation(details.functionCode);

                        System.out.println("Math result: " + alu.result.getUnsigned());
                        int result = (int) alu.result.getUnsigned();

                        // Store the result back into the destination register
                        Word finalResult = new Word();
                        finalResult.set(result);

                        push(finalResult);

                    }
            }

        }

        if(details.opInstruction == 4){ //LOAD 100
            System.out.println("LOAD");
            switch(details.format) {
                case 0:
                    System.out.println("0R format execution.");
                    int pcPop = (int) pop().getUnsigned();
                    System.out.println("Beginning PC value: " + PC.getUnsigned());
                    System.out.println("pop value: " + pcPop);
                    PC.set(pcPop);
                    System.out.println("PC after POP: " + PC.getUnsigned());

                    break;
                case 1:
                    System.out.println("1R format execution.");
                    if(details.rd != null){
                        System.out.println("RD register: " + details.rd);
                        System.out.println("RD value: " + registers[details.rd].getUnsigned());
                        System.out.println("Immediate: " + details.immediate);

                        int temp = (int)registers[details.rd].getUnsigned() + details.immediate;
                        System.out.println("RD: " + registers[details.rd].getUnsigned() + " Immediate: " + details.immediate + " temp: " + temp);
                        Word temp2 = new Word();
                        temp2.set(temp);
                        Word value = MainMemory.read(temp2);
                        System.out.println("value: " + value.getUnsigned());

                        registers[details.rd].copy(value);
                        System.out.println("Register RD: " + registers[details.rd].getUnsigned());
                    }
                    break;

                case 2:
                    System.out.println("2R format execution.");
                    if(details.rs != null){
                        System.out.println("RS register: " + details.rs);
                        System.out.println("RS value: " + registers[details.rs].getUnsigned());

                        System.out.println("Immediate: " + details.immediate);

                        int temp = (int)registers[details.rs].getUnsigned() + details.immediate;
                        System.out.println("RS: " + registers[details.rs].getUnsigned() + " Immediate: " + details.immediate + " temp: " + temp);
                        Word temp2 = new Word();
                        temp2.set(temp);
                        Word value = MainMemory.read(temp2);
                        System.out.println("value: " + value.getUnsigned());

                        registers[details.rd].copy(value);
                        System.out.println("Register RD: " + registers[details.rd].getUnsigned());
                    }
                    break;

                case 3:
                    System.out.println("3R format execution.");
                    if(details.rs1 != null && details.rs2 != null){
                        System.out.println("RS1 register: " + details.rs1);
                        System.out.println("RS1 value: " + registers[details.rs1].getUnsigned());

                        System.out.println("RS2 register: " + details.rs2);
                        System.out.println("RS2 value: " + registers[details.rs2].getUnsigned());


                        int temp = (int)registers[details.rs1].getUnsigned() + (int)registers[details.rs2].getUnsigned();
                        System.out.println("RS1: " + registers[details.rs1].getUnsigned() + " RS2: " + registers[details.rs2].getUnsigned()
                                + " temp: " + temp);

                        Word temp2 = new Word();
                        temp2.set(temp);
                        Word value = MainMemory.read(temp2);
                        System.out.println("value: " + value.getUnsigned());

                        registers[details.rd].copy(value);
                        System.out.println("Register RD: " + registers[details.rd].getUnsigned());
                    }
                    break;
            }
        }

        //STORE
        if(details.opInstruction == 5){ //STORE 101
            switch(details.format){
                case 0:
                    System.out.println("format 0R:");
                    System.out.println("Format not used");
                    break;
                case 1:
                    if(details.rd != null) {
                        System.out.println("format 1R");

                        System.out.println("RD register: " + details.rd);
                        System.out.println("RD value: " + registers[details.rd].getUnsigned());
                        System.out.println("Immediate: " + details.immediate);

                        Word address = new Word();
                        Word value = new Word();

                        address.set((int)registers[details.rd].getUnsigned());
                        System.out.println("address: " + address.getUnsigned());
                        value.set(details.immediate);
                        System.out.println("value: " + value.getUnsigned());


                        MainMemory.write(address, value); // Store value in memory

                    }
                    break;
                case 2:
                    System.out.println("Format 2R");
                    if(details.rd != null && details.rs != null) {

                        System.out.println("RD register: " + details.rd);
                        System.out.println("RD value: " + registers[details.rd].getUnsigned());
                        System.out.println("Immediate: " + details.immediate);


                        System.out.println("RS register: " + details.rs);
                        System.out.println("RS value: " + registers[details.rs].getUnsigned());

                        Word address = new Word();
                        Word value = new Word();

                        address.set((int)registers[details.rd].getUnsigned() + details.immediate);
                        System.out.println("address: " + address.getUnsigned());
                        value.set((int)registers[details.rd].getUnsigned());
                        System.out.println("value: " + value.getUnsigned());


                        MainMemory.write(address, value); // Store value in memory

                    }
                    break;
                case 3:
                    System.out.println("Format 3R");
                    if(details.rd != null && details.rs1 != null && details.rs2 != null) {

                        System.out.println("RD register: " + details.rd);
                        System.out.println("RD value: " + registers[details.rd].getUnsigned());
                        System.out.println("Immediate: " + details.immediate);

                        System.out.println("RS1 register: " + details.rs1);
                        System.out.println("RS1 value: " + registers[details.rs1].getUnsigned());

                        System.out.println("RS2 register: " + details.rs2);
                        System.out.println("RS2 value: " + registers[details.rs2].getUnsigned());

                        Word address = new Word();
                        Word value = new Word();

                        address.set((int)registers[details.rd].getUnsigned() + (int)registers[details.rs1].getUnsigned());
                        System.out.println("address: " + address.getUnsigned());
                        value.set((int)registers[details.rs2].getUnsigned());
                        System.out.println("value: " + value.getUnsigned());


                        MainMemory.write(address, value); // Store value in memory
                    }
                    break;
            }
        }

        //POP/INTERRUPT
        if(details.opInstruction == 6){ // POP/INTERRUPT 110
            System.out.println("POP / INTERUPPT");
            switch(details.format){
                case 0:
                    System.out.println("format 0R:");
                    System.out.println("Format not used");
                    break;
                case 1:
                    if(details.rd != null) {
                        System.out.println("format 1R");

                        System.out.println("Starting RD register: " + details.rd);
                        System.out.println("Starting RD value: " + registers[details.rd].getUnsigned());

                        Word temp = new Word();
                        temp = pop();

                        System.out.println("temp value: " + temp.getUnsigned());


                        registers[details.rd].copy(temp);
                        System.out.println("Register RD: " + registers[details.rd].getUnsigned());
                    }
                    break;
                case 2:
                    if(details.rd != null && details.rs != null) {
                        System.out.println("format 2R");
                        System.out.println("PEEK");

                        System.out.println("Starting RD register: " + details.rd);
                        System.out.println("Starting RD value: " + registers[details.rd].getUnsigned());
                        System.out.println("Immediate value: " + details.immediate);

                        Word peekAddress = new Word();
                        peekAddress.set((int)SP.getUnsigned() - ((int) registers[details.rd].getUnsigned() + details.immediate));
                        Word value = MainMemory.read(peekAddress);

                        registers[details.rd].copy(value);
                        System.out.println("Register RD: " + registers[details.rd].getUnsigned());
                    }
                    break;

                case 3:
                    System.out.println("format 3R");
                    if(details.rd != null && details.rs1 != null && details.rs2 != null) {
                        System.out.println("PEEK");

                        System.out.println("Starting RD register: " + details.rd);
                        System.out.println("Starting RD value: " + registers[details.rd].getUnsigned());
                        System.out.println("Immediate value: " + details.immediate);

                        Word peekAddress = new Word();
                        peekAddress.set((int)SP.getUnsigned() - ((int)registers[details.rs1].getUnsigned() + (int)registers[details.rs2].getUnsigned()));
                        Word value = MainMemory.read(peekAddress);

                        registers[details.rd].copy(value);
                        System.out.println("Register RD: " + registers[details.rd].getUnsigned());
                    }
                    break;
            }
        }

        else{
            System.out.println("Opcode was not recognized");
        }

    }

    //Method to evaluate BooleanOp codes
    private static boolean BooleanOp(int bop, int val1, int val2){
        System.out.println("BooleanOp..");
        System.out.println("bop: " + bop + " val1: " +val1 + " val2" + val2);
        switch(bop) {
            case 0: //0000 Equals
               if (val1 == val2){
                   System.out.println("Expression was true");
                   return true;
               }
               else{
                   System.out.println("Expression was false");
                   return false;
               }
            case 1: // 0001 Not Equal
                if (val1 != val2){
                    System.out.println("Expression was true");
                    return true;
                }
                else{
                    System.out.println("Expression was false");
                    return false;
                }
            case 2: // 0010 Less than
                if (val1 < val2){
                    System.out.println("Expression was true");
                    return true;
                }
                else{
                    System.out.println("Expression was false");
                    return false;
                }
            case 3: // 0011 Greater than equal to
                if (val1 >= val2){
                    System.out.println("Expression was true");
                    return true;
                }
                else{
                    System.out.println("Expression was false");
                    return false;
                }
            case 4: //0100 Greater than
                if (val1 > val2){
                    System.out.println("Expression was true");
                    return true;
                }
                else{
                    System.out.println("Expression was false");
                    return false;
                }
            case 5: // 0101 less than equal to
                if (val1 <= val2){
                    System.out.println("Expression was true");
                    return true;
                }
                else{
                    System.out.println("Expression was false");
                    return false;
                }
        }
        System.out.println("Boolean code was not recognized..");
        return false;
    }


    // Decode methods for each format (0R, 1R, 2R, 3R)
    private static void decode0RInstruction(InstructionDetails details, String binaryString){
        System.out.println("decode0RInstruction..");
        //For 0R format: | immediate (27 bits) | opcode (5 bits) |
        String immediateBinary = binaryString.substring(0, 27);
        details.immediate = Integer.parseInt(immediateBinary, 2);
        System.out.println("Immediate (binary): " + immediateBinary);
    }

    private static void decode1RInstruction(InstructionDetails details, String binaryString) {
        System.out.println("decode1RInstruction..");
        // For 1R format: | immediate (18 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
        String rdBinary = binaryString.substring(22, 27);
        String functionCodeBinary = binaryString.substring(18, 22);
        String immediateBinary = binaryString.substring(0, 18);


        //Debugging purposes
        details.rd = Integer.parseInt(rdBinary, 2);
        details.functionCode = Integer.parseInt(functionCodeBinary, 2);
        details.immediate = Integer.parseInt(immediateBinary, 2);

        System.out.println("RD (binary): " + rdBinary);
        System.out.println("Function Code (binary): " + functionCodeBinary);
        System.out.println("Immediate (binary): " + immediateBinary);


    }

    private static void decode2RInstruction(InstructionDetails details, String binaryString) {
        System.out.println("decode2RInstruction..");
        // For 2R format: | immediate (13 bits) | Rs (5 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
        String immediateBinary = binaryString.substring(0, 13);
        String rsBinary = binaryString.substring(13, 18);
        String functionCodeBinary = binaryString.substring(18, 22);
        String rdBinary = binaryString.substring(22, 27);


        //Debugging purposes
        details.rs = Integer.parseInt(rsBinary, 2);
        details.rd = Integer.parseInt(rdBinary, 2);
        details.functionCode = Integer.parseInt(functionCodeBinary, 2);
        details.immediate = Integer.parseInt(immediateBinary, 2);

        System.out.println("RS (binary): " + rsBinary);
        System.out.println("RD (binary): " + rdBinary);
        System.out.println("Function Code (binary): " + functionCodeBinary);
        System.out.println("Immediate (binary): " + immediateBinary);
    }


    private static void decode3RInstruction(InstructionDetails details, String binaryString) {
        System.out.println("decode3RInstruction.. ");
        // For 3R format: | immediate (8 bits) | Rs1 (5 bits) | Rs2 (5 bits) | Function (4 bits) | RD (5 bits) | Opcode (5 bits)
        // Decode 3R format instruction

        String immediateBinary = binaryString.substring(0, 8);
        String rs1Binary = binaryString.substring(8, 13);
        String rs2Binary = binaryString.substring(13, 18);
        String functionCodeBinary = binaryString.substring(18, 22);
        String rdBinary = binaryString.substring(22, 27);


        details.rs1 = Integer.parseInt(rs1Binary, 2);
        details.rs2 = Integer.parseInt(rs2Binary, 2);
        details.rd = Integer.parseInt(rdBinary, 2);
        details.functionCode = Integer.parseInt(functionCodeBinary, 2);
        details.immediate = Integer.parseInt(immediateBinary, 2);

        System.out.println("RS1 (binary): " + rs1Binary);
        System.out.println("RS2 (binary): " + rs2Binary);
        System.out.println("RD (binary): " + rdBinary);
        System.out.println("Function Code (binary): " + functionCodeBinary);
        System.out.println("Immediate (binary): " + immediateBinary);
    }


    //Stack methods
    private static void push(Word value) {
        System.out.println("SP: " +SP.getUnsigned());
        SP.decrement(); // Move SP to the next free slot
        System.out.println("SP after decrement: " +SP.getUnsigned());
        System.out.println("PC value written to memory: " +value.getUnsigned() + " at address " + SP.getUnsigned());
        MainMemory.write(SP, value); // Store value in the stack


    }

    private static Word pop() {
        System.out.println("SP: " +SP.getUnsigned());
        Word value = MainMemory.read(SP); // Read value from the stack
        SP.increment(); // Move SP back to the previous position
        System.out.println("SP after increment: " +SP.getUnsigned());
        return value;
    }

    public static class InstructionDetails {
        Integer rs1, rs2, rd, rs; // Using Integer to allow null
        int functionCode;
        int format;
        int opInstruction;
        int immediate;
    }
}
