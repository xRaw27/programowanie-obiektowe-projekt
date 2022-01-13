package agh.ics.oop.engine;

import agh.ics.oop.Grass;
import agh.ics.oop.SimulationStatistics;
import agh.ics.oop.Vector2d;
import agh.ics.oop.animal.Animal;
import agh.ics.oop.gui.SimulationStage;
import agh.ics.oop.map.IMap;
import javafx.application.Platform;

import java.util.*;

public class SimulationEngine implements IEngine, Runnable {

    private final SimulationStage gui;
    private final IMap map;
    private final SimulationStatistics statistics;
    private final int startEnergy;
    private final int moveEnergy;
    private final int plantEnergy;
    private final int grassSpawnEachDay;
    private final int refreshTime;
    private final boolean isMagic;

    private int currentEpoch = 0;
    private int numberOfSpawnedAnimals = 0;
    private int magicRuleCounter = 0;

    private final List<Animal> animals = new ArrayList<>();
    private final Set<Vector2d> positionsContainingAnimals = new HashSet<>();

    private boolean running = false;

    public SimulationEngine(SimulationStage gui, IMap map, SimulationStatistics statistics, int startEnergy, int moveEnergy, int plantEnergy, int initialNumberOfAnimals, int grassSpawnEachDay, int refreshTime, boolean isMagic) {
        this.gui = gui;
        this.map = map;
        this.statistics = statistics;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.grassSpawnEachDay = grassSpawnEachDay;
        this.refreshTime = refreshTime;
        this.isMagic = isMagic;

        this.randomPlace(initialNumberOfAnimals);
        this.statistics.setInitialValues(this.numberOfSpawnedAnimals, this.numberOfSpawnedAnimals * this.startEnergy);
    }

    private void randomPlace(int numberOfAnimalsToAdd) {
        while (numberOfAnimalsToAdd > 0) {

            Vector2d randomPosition = this.map.randomUnoccupiedMapPosition();
            if (randomPosition != null) {
                Animal newAnimal = new Animal(this.numberOfSpawnedAnimals + 1, this.map, randomPosition, this.startEnergy, this.currentEpoch);

                this.map.place(newAnimal);
                this.animals.add(newAnimal);
                this.numberOfSpawnedAnimals += 1;
            }
            else {
                System.out.println("Map is full. No new animal was added.");
            }
            numberOfAnimalsToAdd -= 1;
        }
    }

    private void animalsMoveOrDie() {
        this.positionsContainingAnimals.clear();

        for (Iterator<Animal> iterator = this.animals.iterator(); iterator.hasNext(); ) {
            Animal animal = iterator.next();

            if (animal.getEnergy() < this.moveEnergy) {
                animal.die();
                iterator.remove();
                this.map.remove(animal);
                this.statistics.updateSumOfEpochsLived(this.currentEpoch - animal.epochOfBirth);
            }
            else {
                animal.move(this.moveEnergy);
                this.positionsContainingAnimals.add(animal.getPosition());
            }
        }
    }

    private void animalsEatAndReproduce() {
        for (Vector2d position : this.positionsContainingAnimals) {
            this.map.feedAnimalsAtPosition(position);

            Animal newAnimal = this.map.reproduceAnimalsAtPosition(position, this.numberOfSpawnedAnimals + 1, this.startEnergy / 2, this.currentEpoch);
            if (newAnimal != null) {
                this.map.place(newAnimal);
                this.animals.add(newAnimal);
                this.numberOfSpawnedAnimals += 1;

                if (newAnimal.isDescendantOfObservedAnimal()) {
                    this.statistics.updateObservedAnimalDescendentsCount();
                }
            }
        }
    }

    private void addNewPlants() {
        for (int i = 0; i < this.grassSpawnEachDay; i++) {
            Vector2d junglePosition = this.map.randomUnoccupiedJunglePosition();
            Vector2d steppePosition = this.map.randomUnoccupiedSteppePosition();

            if (junglePosition != null) {
                this.map.addNewPlant(junglePosition, new Grass(this.plantEnergy));
            }
            if (steppePosition != null) {
                this.map.addNewPlant(steppePosition, new Grass(this.plantEnergy));
            }
        }
    }

    private void simulationMagicRule() {
        if (this.animals.size() == 5 && this.magicRuleCounter < 3) {
            this.magicRuleCounter += 1;

            for (Animal animal : new ArrayList<>(this.animals)) {
                Vector2d randomPosition = this.map.randomUnoccupiedMapPosition();
                if (randomPosition != null) {
                    Animal newAnimal = new Animal(this.numberOfSpawnedAnimals + 1, this.map, randomPosition, this.startEnergy, this.currentEpoch, animal);

                    this.map.place(newAnimal);
                    this.animals.add(newAnimal);
                    this.numberOfSpawnedAnimals += 1;
                }
            }
            Platform.runLater(this.gui::magicRule);
        }
    }

    private void updateStatistics() {
        int totalChildrenCount = 0;
        int sumOfEnergy = 0;

        for (Animal animal : this.animals) {
            totalChildrenCount += animal.getChildrenCount();
            sumOfEnergy += animal.getEnergy();
            this.statistics.updateGenotypes(animal.genotype);
        }

        int numberOfLiveAnimals = this.animals.size();
        this.statistics.setEndOfEpochStatistics(this.map.getCurrentNumberOfPlants(), numberOfLiveAnimals,
                this.numberOfSpawnedAnimals - numberOfLiveAnimals, totalChildrenCount, sumOfEnergy);
        this.statistics.endOfEpochUpdateData();
    }

    private void prepareToObserveNewAnimal() {
        if (this.statistics.isNewAnimalToObserve()) {
            for (Animal animal : this.animals) {
                animal.setAsNotDescendant();
            }
            this.statistics.animalsDescendantStatusCleared();
        }
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public void stop() {
        this.running = false;
    }

    @Override
    public void run() {
        this.running = true;

        this.prepareToObserveNewAnimal();

        Platform.runLater(this.gui::drawMap);
        Platform.runLater(this.gui::updateStatistics);

        while (this.running && this.animals.size() > 0) {
            try {
                Thread.sleep(this.refreshTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.currentEpoch += 1;
            this.statistics.newEpoch(this.currentEpoch);

            this.animalsMoveOrDie();
            this.animalsEatAndReproduce();
            this.addNewPlants();
            if (this.isMagic) {
                this.simulationMagicRule();
            }
            this.updateStatistics();

            Platform.runLater(this.gui::drawMap);
            Platform.runLater(this.gui::updateStatistics);
        }

        Platform.runLater(this.gui::simulationStopped);
    }

    @Override
    public List<Animal> getCopyOfAnimalsList() {
        return new ArrayList<>(this.animals);
    }
}
