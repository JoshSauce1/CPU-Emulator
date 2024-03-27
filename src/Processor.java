public class Processor {

    static final Word PC = new Word();
    static final Word[] registers = new Word[32];
    static final ALU alu = new ALU();
    static boolean halted = false;

    static {
        System.out.println("resetting.. ");
        PC.set(0); // Reset Program Counter
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
        details.format = format;

        //Debugging purposes
        System.out.println("Format (binary): " + formatBinary);
        System.out.println("Format (int): " + format);

        // Decode the rest of the instruction based on the format
        switch (format) {
            case 0: // NoR

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
        // Perform operations based on opcode
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

                    System.out.println("Copied immediate value " + details.immediate + " into register R" + details.rd);
                }

                break;
            case 2: // 2R "10"
                System.out.println("2R format execution.");
                if (details.rs != null && details.rd != null) {
                    System.out.println("inside if");
                    // Copy values from source register and destination register to ALU operands
                    alu.op1.copy(registers[details.rs]);
                    System.out.println(registers[details.rs]);
                    alu.op2.copy(registers[details.rd]); //
                    System.out.println(registers[details.rd]);
                    // Convert function code to Bit[] for the ALU operation
                    Bit[] functionBits = new Bit[4];
                    for (int i = 0; i < 4; i++) {
                        functionBits[i] = new Bit((details.functionCode & (1 << i)) != 0);
                    }

                    // Perform the ALU operation
                    alu.doOperation(functionBits);

                    // Store the result back into the destination register
                    registers[details.rd].copy(alu.result);
                    System.out.println(registers[details.rd]);
                }

                break;
            case 3: // 3R "11"
                System.out.println("3R format execution.");
                if (details.rs1 != null && details.rs2 != null && details.rd != null) {
                    // Copy values from source registers to ALU operands
                    alu.op1.copy(registers[details.rs1]);
                    alu.op2.copy(registers[details.rs2]);

                    // Convert function code to Bit[] for the ALU operation
                    Bit[] functionBits = new Bit[4];
                    for (int i = 0; i < 4; i++) {
                        functionBits[i] = new Bit((details.functionCode & (1 << i)) != 0);
                    }

                    // Perform the ALU operation
                    alu.doOperation(functionBits);

                    // Store the result back into the destination register
                    registers[details.rd].copy(alu.result);

                }

                break;
        }
    }

    // Decode methods for each format (1R, 2R, 3R)
    private static void decode1RInstruction(InstructionDetails details, String binaryString) {
        System.out.println("decode1RInstruction..");
        // For 1R format: | format (2 bits) | immediate (16 bits) | function code (4 bits) | RD (5 bits) | opcode (5 bits) |
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
        String rsBinary = binaryString.substring(13, 18);
        String rdBinary = binaryString.substring(22, 27);
        String functionCodeBinary = binaryString.substring(18, 22);
        String immediateBinary = binaryString.substring(0, 13);


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
        String rs1Binary = binaryString.substring(8, 13);
        String rs2Binary = binaryString.substring(13, 18);
        String rdBinary = binaryString.substring(22, 27);
        String functionCodeBinary = binaryString.substring(18, 22);
        String immediateBinary = binaryString.substring(0, 8);

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

    public static class InstructionDetails {
        int opcode;
        Integer rs1, rs2, rd, rs; // Using Integer to allow null
        int functionCode;
        int format;
        int immediate;
    }
}
