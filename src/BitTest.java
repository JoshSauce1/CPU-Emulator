import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BitTest {

    @Test
    public void testSet() {
        Bit bit = new Bit();
        bit.set(true);
        assertEquals(true, bit.getValue());

        bit.set(false);
        assertEquals(false, bit.getValue());
    }

    @Test
    public void testToggle() {
        Bit bit = new Bit();
        bit.toggle();
        assertEquals(true, bit.getValue());

        bit.toggle();
        assertEquals(false, bit.getValue());
    }

    @Test
    public void testAnd() {
        Bit bit1 = new Bit(true);
        Bit bit2 = new Bit(true);
        Bit result = bit1.and(bit2);
        assertEquals(true, result.getValue());

        bit2.set(false);
        result = bit1.and(bit2);
        assertEquals(false, result.getValue());
    }

    @Test
    public void testOr() {
        Bit bit1 = new Bit(true);
        Bit bit2 = new Bit(true);
        Bit result = bit1.or(bit2);
        assertEquals(true, result.getValue());

        bit1.set(false);
        result = bit1.or(bit2);
        assertEquals(true, result.getValue());

        bit2.set(false);
        result = bit1.or(bit2);
        assertEquals(false, result.getValue());
    }

    @Test
    public void testXor() {
        Bit bit1 = new Bit(true);
        Bit bit2 = new Bit(false);
        Bit result = bit1.xor(bit2);
        assertEquals(true, result.getValue());

        bit2.set(true);
        result = bit1.xor(bit2);
        assertEquals(false, result.getValue());
    }

    @Test
    public void testNot() {
        Bit bit = new Bit(true);
        Bit result = bit.not();
        assertEquals(false, result.getValue());

        bit.set(false);
        result = bit.not();
        assertEquals(true, result.getValue());
    }
}
