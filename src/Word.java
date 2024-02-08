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
        //subtracts by i amount to shift
        for (int i = amount; i < 32; i++) {
            result.bits[i] = this.bits[i - amount];
        }
        return result;
    }

    // left shift this word by amount bits, creating a new Word
    public Word leftShift(int amount) {
        Word result = new Word();
        for (int i = 0; i < 32 - amount; i++) {
            //adds by i amount to shift
            result.bits[i] = this.bits[i + amount];
        }
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
            signedValue += bits[i].getValue() ? (1 << i) : 0; //update signed value based on bit's value
        }
        // if MSB is set perform sign extension
        if (bits[31].getValue()) {
            signedValue |= Integer.MIN_VALUE;
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

}
