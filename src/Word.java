public class Word {
    private Bit[] bits;

    // Constructors
    public Word() {
        this.bits = new Bit[32];
        for (int i = 0; i < 32; i++) {
            this.bits[i] = new Bit();
        }
    }

    // Methods

    // Get a new Bit that has the same value as bit i
    public Bit getBit(int i) {
        return new Bit(this.bits[i].getValue());
    }

    // set bit i's value
    public void setBit(int i, Bit value) {
        this.bits[i].set(value.getValue());
    }

    // and two words, returning a new Word
    public Word and(Word other) {
        Word result = new Word();
        for (int i = 0; i < 32; i++) {
            result.bits[i] = this.bits[i].and(other.bits[i]);
        }
        return result;
    }

    // or two words, returning a new Word
    public Word or(Word other) {
        Word result = new Word();
        for (int i = 0; i < 32; i++) {
            result.bits[i] = this.bits[i].or(other.bits[i]);
        }
        return result;
    }

    // xor two words, returning a new Word
    public Word xor(Word other) {
        Word result = new Word();
        for (int i = 0; i < 32; i++) {
            result.bits[i] = this.bits[i].xor(other.bits[i]);
        }
        return result;
    }

    // negate this word, creating a new Word
    public Word not() {
        Word result = new Word();
        for (int i = 0; i < 32; i++) {
            result.bits[i] = this.bits[i].not();
        }
        return result;
    }

    // right shift this word by amount bits, creating a new Word
    public Word rightShift(int amount) {
        Word result = new Word();
        for (int i = 0; i < 32; i++) {
            if (i < 32 - amount) {
                // Use the getValue() method to get the boolean value of the bit
                result.bits[i].set(this.bits[i + amount].getValue());
            } else {
                // Explicitly set the remaining bits to false
                result.bits[i].clear(); // Use the clear() method to set the bit to false
            }
        }
        return result;
    }


    // Left shift this word by amount bits, creating a new Word
    public Word leftShift(int amount) {
        Word result = new Word();
        for (int i = 0; i < 32; i++) {
            if (i + amount < 32) {
                result.bits[i + amount].set(this.bits[i].getValue());
            }
        }
        // No need to explicitly set the leftmost bits to false as the new Word is initialized with all bits as false
        return result;
    }




    // returns a comma separated string t’s and f’s
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            sb.append(this.bits[i].toString());
            if (i < 31) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    // returns the value of this word as a long
    public long getUnsigned() {
        long unsignedValue = 0;
        for (int i = 0; i < 32; i++) {
            unsignedValue += bits[i].getValue() ? (1L << i) : 0; // Update the unsigned value based on bit's value
        }
        return unsignedValue;
    }

    // Method to convert the Word object to a signed int
    public int getSigned() {
        int signedValue = 0;
        for (int i = 0; i < 32; i++) {
            if (bits[i].getValue()) {
                if (i < 31) {
                    signedValue |= (1 << i); // Set bit i
                } else {
                    // Directly handle the sign bit
                    signedValue |= Integer.MIN_VALUE;
                }
            }
        }
        return signedValue;
    }


    // copies the values of the bits from another Word into this one
    public void copy(Word other) {
        for (int i = 0; i < 32; i++) {
            this.bits[i].set(other.bits[i].getValue());
        }
    }

    // set the value of the bits of this Word (used for tests)
    public void set(int value) {
        for (int i = 0; i < 32; i++) {
            int mask = 1 << i;
            if ((value & mask) != 0) {
                this.bits[i].set(true);
            } else {
                this.bits[i].set(false);
            }
        }
    }
    public Word getWord() {
        // Create a new Word object to store the binary string
        Word word = new Word();

        // Extract the bits from the Word object
        for (int i = 0; i < 32; i++) {
            // Set the bit value in the Word object based on the current bit in the bits array
            word.setBit(i, new Bit(this.bits[i].getValue()));
        }

        return word;
    }

    public void increment() {
        Bit carry = new Bit(true); // Start with carry of 1
        for (int i = 0; i < 32; i++) {
            Bit sum = bits[i].xor(carry); // Sum of bit and carry
            carry = bits[i].and(carry); // carry is the AND of bit and prev carry
            bits[i] = sum; // Update bit with the sum

            if (!carry.getValue()) {
                break; // When the carry is 0, we are done
            }
        }
    }



}
