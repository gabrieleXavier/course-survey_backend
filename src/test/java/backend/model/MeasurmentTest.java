package backend.model;


import backend.model.measurment.Choice;
import backend.model.measurment.Value;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MeasurmentTest {
    private Choice e1, e2, e3;
    private Value e4, e5, e6;

    private static Long id1 = new Long(0);
    private static Long id2 = new Long(1);
    private static Long id3 = new Long(2);
    private static Long id4 = new Long(3);

    @Before
    public void setUp() {
        e1 = new Choice(id1);
        e2 = new Choice(id2);
        e3 = new Choice(id1);
        e4 = new Value(id3);
        e5 = new Value(id4);
        e6 = new Value(id3);
    }

    @Test
    public void testEquals() {
        assertEquals(e1.getId(), e1.getId());
        assertEquals(e1.getId(), e3.getId());
        assertEquals(e3.getId(), e3.getId());

        assertEquals(e4.getId(), e4.getId());
        assertEquals(e4.getId(), e6.getId());
        assertEquals(e6.getId(), e6.getId());
    }

    @Test
    public void testHashCode() {
        assertEquals(e1.hashCode(), e1.hashCode());
        assertEquals(e2.hashCode(), e2.hashCode());
        assertEquals(e3.hashCode(), e3.hashCode());

        assertEquals(e4.hashCode(), e4.hashCode());
        assertEquals(e5.hashCode(), e5.hashCode());
        assertEquals(e6.hashCode(), e6.hashCode());
    }

    @Test
    public void testNotEquals() {
        assertNotEquals(e1.getId(), e2.getId());
        assertNotEquals(e3.getId(), e2.getId());

        assertNotEquals(e4.getId(), e5.getId());
        assertNotEquals(e6.getId(), e5.getId());

        assertNotEquals(e1.getId(), null);
        assertNotEquals(e1.getId(), "Ciao");

        assertNotEquals(e4.getId(), null);
        assertNotEquals(e4.getId(), "Ciao");
    }
}
