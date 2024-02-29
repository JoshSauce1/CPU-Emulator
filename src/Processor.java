public class Processor {
    private static Word PC = new Word();
    private static Word SP = new Word();

    // Static init for PC and SP
    static {
        PC.set(0); // Start of program counter
        SP.set(1024); // Start of stack pointer
    }

    public static void run() {
        Bit halted = new Bit(false);
        while (!halted.getValue()) {
            Word instruction = fetch();

            // Placeholder for  operations
            decode(instruction);
            execute(instruction);
            store(instruction);

            // Increment PC
            PC.increment();
        }
    }

    private static Word fetch() {
        // Fetch instruction from memory using PC as the address
        return MainMemory.read(PC);
    }


    // Empty methods
    private static void decode(Word instruction) {
    }

    private static void execute(Word instruction) {
    }

    private static void store(Word instruction) {
    }
}
