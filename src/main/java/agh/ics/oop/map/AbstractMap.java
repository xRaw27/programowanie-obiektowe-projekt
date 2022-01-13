package agh.ics.oop.map;

import agh.ics.oop.IPositionChangedObserver;
import agh.ics.oop.Rectangle;
import agh.ics.oop.animal.Animal;
import agh.ics.oop.Grass;
import agh.ics.oop.Vector2d;

import java.util.*;

abstract class AbstractMap implements IMap, IPositionChangedObserver {

    protected final Vector2d bottomLeftMapCorner;
    protected final Vector2d topRightMapCorner;
    private final Vector2d bottomLeftJungleCorner;
    private final Vector2d topRightJungleCorner;

    private final int mapArea;
    private final int jungleArea;
    private final int steppeArea;
    private final List<Rectangle> mapRectangles;
    private final List<Rectangle> jungleRectangles;
    private final List<Rectangle> steppeRectangles;

    private int numberOfOccupiedJungleCells = 0;
    private int numberOfOccupiedSteppeCells = 0;

    private final RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(this);
    private final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    private final Map<Vector2d, Grass> plants = new HashMap<>();

    public AbstractMap(int mapWidth, int mapHeight, int jungleWidth, int jungleHeight) {
        this.bottomLeftMapCorner = new Vector2d(0, 0);
        this.topRightMapCorner = new Vector2d(mapWidth - 1, mapHeight - 1);

        this.bottomLeftJungleCorner = new Vector2d((mapWidth - jungleWidth) / 2, (mapHeight - jungleHeight) / 2);
        this.topRightJungleCorner = this.bottomLeftJungleCorner.add(new Vector2d(jungleWidth - 1, jungleHeight - 1));

        Rectangle mapRectangle = new Rectangle(this.bottomLeftMapCorner, this.topRightMapCorner);
        Rectangle jungleRectangle = new Rectangle(this.bottomLeftJungleCorner, this.topRightJungleCorner);

        this.mapArea = mapRectangle.getArea();
        this.jungleArea = jungleRectangle.getArea();
        this.steppeArea = mapRectangle.getArea() - jungleRectangle.getArea();

        this.mapRectangles =  new ArrayList<>(List.of(mapRectangle));
        this.jungleRectangles =  new ArrayList<>(List.of(jungleRectangle));
        this.steppeRectangles = new ArrayList<>();
        this.addSteppeRectangles();
    }

    private void addSteppeRectangles() {
        if (this.bottomLeftMapCorner.y != this.bottomLeftJungleCorner.y) {
            this.steppeRectangles.add(new Rectangle(this.bottomLeftMapCorner, new Vector2d(this.topRightMapCorner.x, this.bottomLeftJungleCorner.y - 1)));
        }
        if (this.bottomLeftMapCorner.x != this.bottomLeftJungleCorner.x) {
            this.steppeRectangles.add(new Rectangle(new Vector2d(this.bottomLeftMapCorner.x, this.bottomLeftJungleCorner.y), new Vector2d(this.bottomLeftJungleCorner.x - 1, this.topRightJungleCorner.y)));
        }
        this.steppeRectangles.add(new Rectangle(new Vector2d(this.topRightJungleCorner.x + 1, this.bottomLeftJungleCorner.y), new Vector2d(this.topRightMapCorner.x, this.topRightJungleCorner.y)));
        this.steppeRectangles.add(new Rectangle(new Vector2d(this.bottomLeftMapCorner.x, this.topRightJungleCorner.y + 1), this.topRightMapCorner));
    }

    private boolean isInJungle(Vector2d position) {
        return position.precedes(this.topRightJungleCorner) && position.follows(this.bottomLeftJungleCorner);
    }

    private void updateNumberOfOccupiedCells(Vector2d position, int change) {
        if (this.isInJungle(position)) {
            this.numberOfOccupiedJungleCells += change;
        } else {
            this.numberOfOccupiedSteppeCells += change;
        }
    }

    @Override
    public Vector2d randomUnoccupiedJunglePosition() {
        return this.randomPositionGenerator.randomUnoccupiedPositionInRectangles(this.jungleRectangles, this.jungleArea, this.numberOfOccupiedJungleCells);
    }

    @Override
    public Vector2d randomUnoccupiedSteppePosition() {
        return this.randomPositionGenerator.randomUnoccupiedPositionInRectangles(this.steppeRectangles, this.steppeArea, this.numberOfOccupiedSteppeCells);
    }

    @Override
    public Vector2d randomUnoccupiedMapPosition() {
        return this.randomPositionGenerator.randomUnoccupiedPositionInRectangles(this.mapRectangles, this.mapArea, this.numberOfOccupiedJungleCells + this.numberOfOccupiedSteppeCells);
    }

    @Override
    public Rectangle getMapRectangle() {
        return new Rectangle(this.bottomLeftMapCorner, this.topRightMapCorner);
    }

    @Override
    public Rectangle getJungleRectangle() {
        return new Rectangle(this.bottomLeftJungleCorner, this.topRightJungleCorner);
    }

    @Override
    public void addNewPlant(Vector2d position, Grass newPlant) {
        this.plants.put(position, newPlant);
        this.updateNumberOfOccupiedCells(position, 1);
    }

    @Override
    public int getCurrentNumberOfPlants() {
        return this.plants.size();
    }

    @Override
    public void place(Animal animal) {
        Vector2d position = animal.getPosition();

        this.animals.putIfAbsent(position, new ArrayList<>());
        List<Animal> animalsAtPosition = this.animals.get(position);
        animalsAtPosition.add(animal);

        if (animalsAtPosition.size() == 1) {
            this.updateNumberOfOccupiedCells(position, 1);
        }

        animal.addPositionChangedObserver(this);
    }

    @Override
    public void remove(Animal animal) {
        Vector2d position = animal.getPosition();

        List<Animal> animalsAtPosition = this.animals.get(position);
        animalsAtPosition.remove(animal);

        if (animalsAtPosition.size() == 0) {
            this.animals.remove(position);
            this.updateNumberOfOccupiedCells(position, -1);
        }
    }

    @Override
    public void feedAnimalsAtPosition(Vector2d position) {
        Grass grass = this.plants.get(position);

        if (grass != null) {
            int maxEnergyAtPosition = Collections.max(this.animals.get(position)).getEnergy();

            List<Animal> animalsToFeed = this.animals.get(position)
                    .stream()
                    .filter(animal -> animal.getEnergy() == maxEnergyAtPosition)
                    .toList();

            for (Animal animal : animalsToFeed) {
                animal.eat(grass, animalsToFeed.size());
            }

            this.plants.remove(position);
        }
    }

    @Override
    public Animal reproduceAnimalsAtPosition(Vector2d position, int newAnimalId, int requiredEnergy, int currentEpoch) {
        List<Animal> animalsAtPosition = this.animals.get(position);

        if (animalsAtPosition.size() >= 2) {
            Animal animal1 = Collections.max(animalsAtPosition);
            animalsAtPosition.remove(animal1);

            Animal animal2 = Collections.max(animalsAtPosition);
            animalsAtPosition.add(animal1);

            if (animal2.getEnergy() >= requiredEnergy) {
                Animal newAnimal = new Animal(newAnimalId, this, animal1, animal2, currentEpoch);
                animal1.copulate();
                animal2.copulate();
                return newAnimal;
            }
        }
        return null;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return (this.animals.get(position) != null || this.plants.get(position) != null);
    }

    @Override
    public Object objectAt(Vector2d position) {
        if (this.animals.get(position) != null) {
            return Collections.max(this.animals.get(position));
        }

        return this.plants.get(position);
    }

    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {
        List<Animal> animalsAtOldPosition = this.animals.get(oldPosition);
        animalsAtOldPosition.remove(animal);

        if (animalsAtOldPosition.size() == 0) {
            this.animals.remove(oldPosition);
            this.updateNumberOfOccupiedCells(oldPosition, -1);
        }

        this.animals.putIfAbsent(newPosition, new ArrayList<>());
        List<Animal> animalsAtNewPosition = this.animals.get(newPosition);
        animalsAtNewPosition.add(animal);

        if (animalsAtNewPosition.size() == 1 && this.plants.get(newPosition) == null) {
            this.updateNumberOfOccupiedCells(newPosition, 1);
        }
    }

    @Override
    public String toString() {
        return "Occupied jungle cells: " + this.numberOfOccupiedJungleCells + "\nOccupied steppe cells: " + this.numberOfOccupiedSteppeCells + "\nAnimals: " + this.animals + "\nPlants: " + this.plants;
    }
}
