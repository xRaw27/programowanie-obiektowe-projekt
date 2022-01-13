package agh.ics.oop;

import agh.ics.oop.Vector2d;
import agh.ics.oop.animal.Animal;

public interface IPositionChangedObserver {

    void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition);

}
