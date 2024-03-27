public class MainMemory {

    private static Word[] memory = new Word[1024]; //Memory size

    // memory initialization
    static {
        for (int i = 0; i < memory.length; i++) {
            memory[i] = new Word(); // Initialize each Word in memory
        }
    }

    public static Word read(Word address) {
        int addr = (int) address.getUnsigned(); // Casting to int
        if (addr >= 0 && addr < 1024) {
            // getword to create and return a copy of the Word at memory address
            return memory[addr].getWord();
        }
        return null; // handles case appropriately
    }


    public static void write(Word address, Word value){
        int addr = (int) address.getUnsigned(); //Cast to int
        if (addr >= 0 && addr < 1024) {
            memory[addr].copy(value); //Copys value into memory
        }
    }

    public static void load(String[] data){
        for (int i = 0; i < data.length && i < 1024; i++) {
            for (int bit = 0; bit < 32; bit++) { // Use 32 for word size
                boolean bitValue = data[i].charAt(bit) == '1';
                memory[i].setBit(bit, new Bit(bitValue));
            }
            System.out.println("Loaded at address " + i + ": " + memory[i]);
        }
    }



}
