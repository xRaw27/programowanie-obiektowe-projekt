package agh.ics.oop.animal;

import agh.ics.oop.*;
import agh.ics.oop.map.IMap;

import java.util.ArrayList;
import java.util.List;

public class Animal implements IMapElement, Comparable<Animal> {
    public final int id;
    public final int epochOfBirth;
    public final Genotype genotype;

    private final IMap map;
    private final List<IPositionChangedObserver> positionChangedObservers = new ArrayList<>();
    private final List<IAnimalDiedObserver> animalDiedObservers = new ArrayList<>();
    private final int startEnergy;

    private Vector2d position;
    private MapDirection direction;
    private int energy;
    private int childrenCount = 0;
    private boolean descendantOfObservedAnimal = false;

    public Animal(int id, IMap map, Vector2d initialPosition, int startEnergy, int epochOfBirth) {
        this.id = id;
        this.map = map;
        this.genotype = new Genotype();
        this.position = initialPosition;
        this.direction = MapDirection.randomDirection();
        this.energy = startEnergy;
        this.startEnergy = startEnergy;
        this.epochOfBirth = epochOfBirth;
    }

    public Animal(int id, IMap map, Animal animal1, Animal animal2, int epochOfBirth) {
        this.id = id;
        this.map = map;
        this.genotype = new Genotype(animal1.genotype, animal2.genotype, Math.round((float) animal1.energy * 32 / ( animal1.energy + animal2.energy)));
        this.position = animal1.position;
        this.direction = MapDirection.randomDirection();
        this.energy = animal1.energy / 4 + animal2.energy / 4;
        this.startEnergy = this.energy;
        this.epochOfBirth = epochOfBirth;
        this.descendantOfObservedAnimal = animal1.descendantOfObservedAnimal || animal2.descendantOfObservedAnimal;
    }

    public Animal(int id, IMap map, Vector2d initialPosition, int startEnergy, int epochOfBirth, Animal animal) {
        this.id = id;
        this.map = map;
        this.genotype = new Genotype(animal.genotype);
        this.position = initialPosition;
        this.direction = MapDirection.randomDirection();
        this.energy = startEnergy;
        this.startEnergy = startEnergy;
        this.epochOfBirth = epochOfBirth;
    }

    public void eat(Grass grass, int dividedInto) {
        this.energy += grass.getPlantEnergy() / dividedInto;
    }

    public void copulate() {
        this.energy -= this.energy / 4;
        this.childrenCount += 1;
    }

    public void die() {
        this.animalDiedObservers.forEach(IAnimalDiedObserver::observedAnimalDied);
    }

    public void move(int moveEnergy) {
        int moveDirection = this.genotype.randomGene();
        this.energy -= moveEnergy;

        switch (moveDirection) {
            case 0 -> {
                Vector2d provisionalNewPosition = this.position.add(this.direction.toUnitVector());
                this.updatePosition(provisionalNewPosition);
            }
            case 4 -> {
                Vector2d provisionalNewPosition = this.position.subtract(this.direction.toUnitVector());
                this.updatePosition(provisionalNewPosition);
            }
            default -> this.direction = this.direction.rotate(moveDirection);
        }
    }

    private void updatePosition(Vector2d provisionalNewPosition) {
        Vector2d oldPosition = this.position;
        Vector2d newPosition = this.map.getNewPosition(this.position, provisionalNewPosition);

        this.position = newPosition;
        this.positionChangedObservers.forEach(observer -> observer.positionChanged(this, oldPosition, newPosition));
    }

    public void addPositionChangedObserver(IPositionChangedObserver observer) {
        this.positionChangedObservers.add(observer);
    }

    public void addAnimalDiedObserver(IAnimalDiedObserver observer) {
        this.animalDiedObservers.add(observer);
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public int getEnergy() {
        return this.energy;
    }

    public int getChildrenCount() {
        return this.childrenCount;
    }

    public void setAsDescendant() {
        this.descendantOfObservedAnimal = true;
    }

    public void setAsNotDescendant() {
        this.descendantOfObservedAnimal = false;
    }

    public boolean isDescendantOfObservedAnimal() {
        return this.descendantOfObservedAnimal;
    }

    @Override
    public String getImageSrc() {
        return direction.getImageSrc();
    }

    @Override
    public double getProgressBarStatus() {
        return (double) this.energy / this.startEnergy / 2;
    }

    @Override
    public boolean isAnimal() {
        return true;
    }

    @Override
    public int compareTo(Animal other) {
        int energyCompare = Integer.compare(this.energy, other.energy);

        if (energyCompare == 0) {
            return Integer.compare(this.id, other.id);
        }
        else {
            return energyCompare;
        }
    }

    @Override
    public String toString() {
        return "id = " + this.id + ", position = " + this.position + ", direction = " + this.direction + ", energy = " + this.energy + ", genotype = " + this.genotype;
    }
}
