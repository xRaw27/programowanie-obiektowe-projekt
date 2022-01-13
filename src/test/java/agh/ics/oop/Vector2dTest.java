package agh.ics.oop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class Vector2dTest {
    @Test
    public void equalsTest() {
        assertEquals(new Vector2d(3, 4), new Vector2d(3, 4));
        assertEquals(new Vector2d(4123, -5674), new Vector2d(4123, -5674));
        assertEquals(new Vector2d(0, 0), new Vector2d(0, 0));

        assertNotEquals(new Vector2d(3, 3), new Vector2d(3, 4));
        assertNotEquals(new Vector2d(3, 3), new Vector2d(-3, 3));
        assertNotEquals(new Vector2d(4123, -5674), new Vector2d(-5674, 0));
        assertNotEquals(new Vector2d(11, -1), new Vector2d(-1, 11));
    }

    @Test
    public void toStringTest() {
        assertEquals("(3,-123)", new Vector2d(3, -123).toString());
        assertEquals("(-9,0)", new Vector2d(-9, 0).toString());
        assertEquals("(635354,-133323)", new Vector2d(635354, -133323).toString());
        assertEquals("(0,0)", new Vector2d(-0, -0).toString());
    }

    @Test
    public void precedesTest() {
        assertTrue(new Vector2d(3, -123).precedes(new Vector2d(3, -123)));
        assertTrue(new Vector2d(-19, -123).precedes(new Vector2d(0, 0)));
        assertTrue(new Vector2d(86, 763332).precedes(new Vector2d(86, 763333)));
        assertFalse(new Vector2d(0, 0).precedes(new Vector2d(-19, -123)));
        assertFalse(new Vector2d(654, -723).precedes(new Vector2d(4321, -724)));
        assertFalse(new Vector2d(-43, 32).precedes(new Vector2d(-144, 649)));
    }

    @Test
    public void followsTest() {
        assertTrue(new Vector2d(-943, 443).follows(new Vector2d(-943, 443)));
        assertTrue(new Vector2d(422, 0).follows(new Vector2d(0, 0)));
        assertTrue(new Vector2d(8656535, 6232355).follows(new Vector2d(-14778274, -868326)));
        assertFalse(new Vector2d(465, -8153).follows(new Vector2d(12, -8152)));
        assertFalse(new Vector2d(1, 1).follows(new Vector2d(2, 2)));
    }

    @Test
    public void upperRightTest() {
        assertEquals(new Vector2d(0, 0), new Vector2d(0, 0).upperRight(new Vector2d(0, 0)));
        assertEquals(new Vector2d(123, -321), new Vector2d(0, -321).upperRight(new Vector2d(123, -4000)));
        assertEquals(new Vector2d(53333, 17411), new Vector2d(22340, -4441231).upperRight(new Vector2d(53333, 17411)));
        assertEquals(new Vector2d(-123, 5333), new Vector2d(-853, 5333).upperRight(new Vector2d(-123, 5333)));
    }

    @Test
    public void lowerLeftTest() {
        assertEquals(new Vector2d(0, 0), new Vector2d(0, 0).lowerLeft(new Vector2d(0, 0)));
        assertEquals(new Vector2d(0, -4000), new Vector2d(0, -321).lowerLeft(new Vector2d(123, -4000)));
        assertEquals(new Vector2d(22340, -4441231), new Vector2d(22340, -4441231).lowerLeft(new Vector2d(53333, 17411)));
        assertEquals(new Vector2d(-853, 5333), new Vector2d(-853, 5333).lowerLeft(new Vector2d(-123, 5333)));
    }

    @Test
    public void addTest() {
        assertEquals(new Vector2d(0, 0), new Vector2d(0, 0).add(new Vector2d(0, 0)));
        assertEquals(new Vector2d(8600, -16), new Vector2d(5359, 21).add(new Vector2d(3241, -37)));
        assertEquals(new Vector2d(-328, -74), new Vector2d(616, 0).add(new Vector2d(-944, -74)));
        assertEquals(new Vector2d(0, 0), new Vector2d(-477, 62).add(new Vector2d(477, -62)));
    }

    @Test
    public void subtractTest() {
        assertEquals(new Vector2d(0, 0), new Vector2d(0, 0).subtract(new Vector2d(0, 0)));
        assertEquals(new Vector2d(5359, 21), new Vector2d(8600, -16).subtract(new Vector2d(3241, -37)));
        assertEquals(new Vector2d(616, 0), new Vector2d(-328, -74).subtract(new Vector2d(-944, -74)));
        assertEquals(new Vector2d(0, 0), new Vector2d(477, 62).subtract(new Vector2d(477, 62)));
    }

    @Test
    public void oppositeTest() {
        assertEquals(new Vector2d(0, 0), new Vector2d(0, 0).opposite());
        assertEquals(new Vector2d(0, 32420), new Vector2d(0, -32420).opposite());
        assertEquals(new Vector2d(4949, 0), new Vector2d(-4949, 0).opposite());
        assertEquals(new Vector2d(238472, -3737), new Vector2d(-238472, 3737).opposite());
    }
}
