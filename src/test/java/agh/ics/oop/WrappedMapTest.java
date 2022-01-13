package agh.ics.oop;

import agh.ics.oop.animal.Animal;
import agh.ics.oop.map.WrappedMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class WrappedMapTest {

    @Test
    public void getNewPositionTest() {
        WrappedMap map1 = new WrappedMap(4, 2, 3, 1);
        WrappedMap map2 = new WrappedMap(19, 10, 4, 8);

        assertEquals(new Vector2d(0, 0), map1.getNewPosition(new Vector2d(3, 1), new Vector2d(4, 2)));
        assertEquals(new Vector2d(0, 1), map1.getNewPosition(new Vector2d(0, 0), new Vector2d(0, -1)));
        assertEquals(new Vector2d(2, 1), map1.getNewPosition(new Vector2d(1, 0), new Vector2d(2, -1)));
        assertEquals(new Vector2d(3, 0), map1.getNewPosition(new Vector2d(0, 1), new Vector2d(-1, 4)));
        assertEquals(new Vector2d(1, 1), map1.getNewPosition(new Vector2d(0, 1), new Vector2d(1, 1)));
        assertEquals(new Vector2d(3, 1), map1.getNewPosition(new Vector2d(2, 0), new Vector2d(3, 1)));

        assertEquals(new Vector2d(9, 0), map2.getNewPosition(new Vector2d(9, 9), new Vector2d(9, 10)));
        assertEquals(new Vector2d(18, 7), map2.getNewPosition(new Vector2d(0, 6), new Vector2d(-1, 7)));
        assertEquals(new Vector2d(12, 9), map2.getNewPosition(new Vector2d(13, 0), new Vector2d(12, -1)));
        assertEquals(new Vector2d(0, 9), map2.getNewPosition(new Vector2d(18, 0), new Vector2d(19, -1)));
        assertEquals(new Vector2d(6, 8), map2.getNewPosition(new Vector2d(6, 7), new Vector2d(6, 8)));
        assertEquals(new Vector2d(13, 4), map2.getNewPosition(new Vector2d(14, 5), new Vector2d(13, 4)));
    }

    @Test
    public void getMapRectangleTest() {
        WrappedMap map1 = new WrappedMap(4, 2, 3, 1);
        WrappedMap map2 = new WrappedMap(19, 10, 4, 8);

        assertEquals(new Rectangle(new Vector2d(0,0), new Vector2d(3, 1)), map1.getMapRectangle());
        assertEquals(new Rectangle(new Vector2d(0,0), new Vector2d(18, 9)), map2.getMapRectangle());
    }

    @Test
    public void getJungleRectangleTest() {
        WrappedMap map1 = new WrappedMap(4, 2, 3, 1);
        WrappedMap map2 = new WrappedMap(19, 10, 4, 8);

        assertEquals(new Rectangle(new Vector2d(0,0), new Vector2d(2, 0)), map1.getJungleRectangle());
        assertEquals(new Rectangle(new Vector2d(7,1), new Vector2d(10, 8)), map2.getJungleRectangle());
    }

    @Test
    public void borderedMapIntegrationTest() {
        WrappedMap map1 = new WrappedMap(19, 10, 4, 8);

        Animal animal1 = new Animal(1, map1, new Vector2d(0, 8), 33, 0);
        Animal animal2 = new Animal(2, map1, new Vector2d(15, 3), 30, 0);

        map1.place(animal1);
        map1.place(animal2);

        Grass plant1 = new Grass(13);
        Grass plant2 = new Grass(65);

        map1.addNewPlant(new Vector2d(2, 2), plant1);
        map1.addNewPlant(new Vector2d(5, 5), plant2);

        assertEquals(animal1, map1.objectAt(new Vector2d(0, 8)));
        assertEquals(animal2, map1.objectAt(new Vector2d(15, 3)));
        assertEquals(plant1, map1.objectAt(new Vector2d(2, 2)));
        assertEquals(plant2, map1.objectAt(new Vector2d(5, 5)));
        assertNull(map1.objectAt(new Vector2d(8, 8)));
        assertNull(map1.objectAt(new Vector2d(8, 3)));
        assertNull(map1.objectAt(new Vector2d(15, 2)));
        assertEquals(2, map1.getCurrentNumberOfPlants());

        map1.positionChanged(animal1, new Vector2d(0, 8), new Vector2d(2, 2));
        map1.feedAnimalsAtPosition(new Vector2d(2, 2));

        assertEquals(animal1, map1.objectAt(new Vector2d(2, 2)));
        assertEquals(46, animal1.getEnergy());
        assertEquals(1, map1.getCurrentNumberOfPlants());
    }

}
