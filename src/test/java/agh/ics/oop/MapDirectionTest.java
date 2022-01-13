package agh.ics.oop;

import agh.ics.oop.animal.MapDirection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapDirectionTest {
    @Test
    public void rotateTest() {
        assertEquals(MapDirection.NORTH, MapDirection.WEST.rotate(2));
        assertEquals(MapDirection.NORTHEAST, MapDirection.SOUTH.rotate(5));
        assertEquals(MapDirection.EAST, MapDirection.SOUTH.rotate(6));
        assertEquals(MapDirection.SOUTHEAST, MapDirection.SOUTHEAST.rotate(0));
        assertEquals(MapDirection.SOUTH, MapDirection.SOUTHEAST.rotate(1));
        assertEquals(MapDirection.SOUTHWEST, MapDirection.WEST.rotate(7));
        assertEquals(MapDirection.WEST, MapDirection.WEST.rotate(8));
        assertEquals(MapDirection.NORTHWEST, MapDirection.SOUTH.rotate(3));
    }

    @Test
    public void toUnitVectorTest() {
        assertEquals(new Vector2d(-1, 0), MapDirection.WEST.toUnitVector());
        assertEquals(new Vector2d(0, -1), MapDirection.SOUTH.toUnitVector());
        assertEquals(new Vector2d(1, -1), MapDirection.SOUTHEAST.toUnitVector());
        assertEquals(new Vector2d(-1, 1), MapDirection.NORTHWEST.toUnitVector());
    }

}
