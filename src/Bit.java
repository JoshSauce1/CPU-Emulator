public class Bit {
    private boolean value;

    //Constructors
    public Bit() {
        this.value = false;
    }

    public Bit(boolean value) {
        this.value = value;
    }

    //Methods

    // sets the bit to true
    public void set(boolean value) {
        this.value = value;
    }

    // changes the value from true to false or false to true
    public void toggle() {
        if (this.value == true){
            this.value =false;
        }
        else{
            this.value = true;
        }
    }

    // sets the value of the bit
    public void set() {
        this.value = true;
    }

    // sets the bit to false
    public void clear() {
        this.value = false;
    }

    // returns the current value
    public boolean getValue() {
        return this.value;
    }

    // performs and on two bits and returns a new bit set to the result
    public Bit and(Bit other) {
        if (this.value == true) {
            //If both values are true
            if(other.value ==true) {
                return new Bit(true);
            }
            //If this.value is true and other.value is false
            else{
                return new Bit(false);
            }
        }
        else{
            //If this.value is false and other.value is false
            return new Bit(false);
        }
    }

    // performs or on two bits and returns a new bit set to the result
    public Bit or(Bit other) {
        if (this.value ==true) {
            return new Bit(true);
        }
        else {
            if(other.value == true) {
                return new Bit(true);
            }
            else{
                return new Bit(false);
            }
        }
    }

    // performs xor on two bits and returns a new bit set to the result
    public Bit xor(Bit other) {
        if (this.value == true) {
            if(other.value ==false) {
                return new Bit(true);
            }
            else{
                return new Bit(false);
            }
        }
        //If this.value is false..
        else {
            if(other.value == true) {
                return new Bit(true);
            }
            else{
                return new Bit(false);
            }
        }
    }

    // performs not on the existing bit, returning the result as a new bit
    public Bit not() {
        if (this.value == true) {
            return new Bit(false);
        }
        else {
            return new Bit(true);
        }
    }

    // returns “t” or “f”
    @Override
    public String toString() {
        if(this.value ==true){
            return "t";
        }
        else{
            return "f";
        }
    }
}
