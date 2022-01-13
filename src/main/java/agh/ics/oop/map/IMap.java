package agh.ics.oop.map;

import agh.ics.oop.Grass;
import agh.ics.oop.Rectangle;
import agh.ics.oop.animal.Animal;
import agh.ics.oop.Vector2d;

public interface IMap {

    public static final double BOUNDARY_OCCUPIED_RATIO = 0.9;

    Vector2d randomUnoccupiedJunglePosition();

    Vector2d randomUnoccupiedSteppePosition();

    Vector2d randomUnoccupiedMapPosition();

    Vector2d getNewPosition(Vector2d oldPosition, Vector2d provisionalNewPosition);

    Rectangle getMapRectangle();

    Rectangle getJungleRectangle();

    void addNewPlant(Vector2d position, Grass newPlant);

    int getCurrentNumberOfPlants();

    void place(Animal animal);

    void remove(Animal animal);

    void feedAnimalsAtPosition(Vector2d position);

    Animal reproduceAnimalsAtPosition(Vector2d position, int newAnimalId, int requiredEnergy, int currentEpoch);

    boolean isOccupied(Vector2d position);

    Object objectAt(Vector2d position);
}