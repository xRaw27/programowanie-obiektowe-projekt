package agh.ics.oop;

import agh.ics.oop.animal.Animal;
import agh.ics.oop.animal.MapDirection;
import agh.ics.oop.map.IMap;
import agh.ics.oop.map.WrappedMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnimalTest {

    @Test
    public void animalIntegrationTest() {

        IMap map1 = new WrappedMap(19, 10, 4, 8);

        Animal animal1 = new Animal(1, map1, new Vector2d(1, 1), 33, 0);
        Animal animal2 = new Animal(2, map1, new Vector2d(1, 1), 30, 0);

        Animal animal3 = new Animal(3, map1, animal1, animal2, 0);
        animal1.copulate();
        animal2.copulate();

        Animal animal4 = new Animal(4, map1, animal2, animal3, 0);
        animal2.copulate();
        animal3.copulate();

        assertEquals(25, animal1.getEnergy());
        assertEquals(18, animal2.getEnergy());
        assertEquals(12, animal3.getEnergy());
        assertEquals(8, animal4.getEnergy());

        assertEquals(new Vector2d(1, 1), animal3.getPosition());
        assertEquals(new Vector2d(1, 1), animal4.getPosition());

        animal1.eat(new Grass(10), 1);
        animal2.eat(new Grass(421), 5);

        assertEquals(35, animal1.getEnergy());
        assertEquals(102, animal2.getEnergy());

        assertEquals(1, animal1.getChildrenCount());
        assertEquals(2, animal2.getChildrenCount());
        assertEquals(1, animal3.getChildrenCount());
        assertEquals(0, animal4.getChildrenCount());

        assertTrue(animal4.isAnimal());
    }
}
