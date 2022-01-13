package agh.ics.oop;

import agh.ics.oop.animal.Animal;
import agh.ics.oop.animal.Genotype;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimulationStatistics implements IAnimalDiedObserver {

    private int numberOfLiveAnimals;
    private int numberOfDeadAnimals = 0;
    private int currentEpoch = 0;
    private int currentNumberOfPlants = 0;

    private int sumOfLiveAnimalsEnergy;
    private int sumOfEpochsLived = 0;
    private int totalChildrenCountOfLiveAnimals = 0;
    private final HashMap<Genotype, Integer> genotypes = new HashMap<>();

    private Animal observedAnimal;
    private int observedAnimalDescendentsCount = 0;
    private int observedAnimalInitialChildrenCount = 0;
    private int observedAnimalEpochOfDeath = 0;
    private boolean newAnimalToObserve = false;

    List<int[]> simulationData = new ArrayList<>();
    int[] simulationDataSums = new int[] {0, 0, 0, 0, 0, 0};

    public void setInitialValues(int initialNumberOfLiveAnimals, int initialSumOfLiveAnimalsEnergy) {
        this.numberOfLiveAnimals = initialNumberOfLiveAnimals;
        this.sumOfLiveAnimalsEnergy = initialSumOfLiveAnimalsEnergy;
    }

    public void newEpoch(int epoch) {
        this.currentEpoch = epoch;
        this.genotypes.clear();
    }

    public void updateSumOfEpochsLived(int epochsLived) {
        this.sumOfEpochsLived += epochsLived;
    }

    public void updateGenotypes(Genotype genotype) {
        int count = this.genotypes.getOrDefault(genotype, 0);
        this.genotypes.put(genotype, count + 1);
    }

    public void setEndOfEpochStatistics(int currentNumberOfPlants, int numberOfLiveAnimals, int numberOfDeadAnimals, int totalChildrenCount, int sumOfEnergy) {
        this.currentNumberOfPlants = currentNumberOfPlants;
        this.numberOfLiveAnimals = numberOfLiveAnimals;
        this.numberOfDeadAnimals = numberOfDeadAnimals;
        this.totalChildrenCountOfLiveAnimals = totalChildrenCount;
        this.sumOfLiveAnimalsEnergy = sumOfEnergy;
    }

    public void observeAnimal(Animal animal) {
        this.newAnimalToObserve = true;
        this.observedAnimalDescendentsCount = 0;
        this.observedAnimalInitialChildrenCount = animal.getChildrenCount();
        this.observedAnimalEpochOfDeath = 0;
        this.observedAnimal = animal;
        this.observedAnimal.addAnimalDiedObserver(this);
    }

    public boolean isNewAnimalToObserve() {
        if (this.newAnimalToObserve) {
            this.newAnimalToObserve = false;
            return true;
        }
        return false;
    }

    public void animalsDescendantStatusCleared() {
        this.observedAnimal.setAsDescendant();
    }

    public void updateObservedAnimalDescendentsCount() {
        this.observedAnimalDescendentsCount += 1;
    }

    @Override
    public void observedAnimalDied() {
        this.observedAnimalEpochOfDeath = this.currentEpoch;
    }

    public int getCurrentEpoch() {
        return this.currentEpoch;
    }

    public int getNumberOfLiveAnimals() {
        return this.numberOfLiveAnimals;
    }

    public int getCurrentNumberOfPlants() {
        return this.currentNumberOfPlants;
    }

    public double getLiveAnimalsAverageEnergy() {
        return (this.numberOfLiveAnimals == 0) ? 0 : (double) this.sumOfLiveAnimalsEnergy / this.numberOfLiveAnimals;
    }

    public double getAverageChildrenCountOfLiveAnimals() {
        return (this.numberOfLiveAnimals == 0) ? 0 : (double) this.totalChildrenCountOfLiveAnimals / this.numberOfLiveAnimals;
    }

    public double getAverageNumberOfEpochsLived() {
        return (this.numberOfDeadAnimals == 0) ? 0 : (double) this.sumOfEpochsLived / this.numberOfDeadAnimals;
    }

    public String getDominantGenotype() {
        return (this.genotypes.size() == 0) ? "" : Collections.max(this.genotypes.entrySet(), Map.Entry.comparingByValue()).getKey().toString();
    }

    public String getAnimalsWithDominantGenotype(List<Animal> animals) {
        if (this.genotypes.size() == 0) {
            return "No animal is alive";
        }
        else {
            Genotype dominantGenotype = Collections.max(this.genotypes.entrySet(), Map.Entry.comparingByValue()).getKey();
            StringBuilder stringBuilder = new StringBuilder("Animals with dominant genotype:");
            for (Animal animal : animals) {
                if (animal.genotype.equals(dominantGenotype)) {
                    stringBuilder.append("\n").append(animal);
                }
            }
            return stringBuilder.toString();
        }
    }

    public boolean isAnyAnimalObserved() {
        return this.observedAnimal != null;
    }

    public int getObservedAnimalChildrenCount() {
        return (this.observedAnimal == null) ? 0 : this.observedAnimal.getChildrenCount() - this.observedAnimalInitialChildrenCount;
    }

    public int getObservedAnimalDescendentsCount() {
        return this.observedAnimalDescendentsCount;
    }

    public int getObservedAnimalEpochOfDeath() {
        return this.observedAnimalEpochOfDeath;
    }

    public void endOfEpochUpdateData() {
        this.simulationData.add(new int[] {this.currentEpoch, this.numberOfLiveAnimals, this.currentNumberOfPlants,
                (int)this.getLiveAnimalsAverageEnergy(), (int)this.getAverageChildrenCountOfLiveAnimals(), (int)this.getAverageNumberOfEpochsLived()});

        for (int i = 0; i < 6; i++) {
            this.simulationDataSums[i] += this.simulationData.get(this.simulationData.size() - 1)[i];
        }
    }

    private String convertToCSV(int[] data) {
        return Arrays.stream(data)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(","));
    }

    private String simulationDataSummary() {
        return Arrays.stream(this.simulationDataSums)
                .map(x -> x / this.simulationData.size())
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(","));
    }

    public void saveSimulationData(File csvOutputFile) {
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            pw.println("Epoch, Number of animals, Number of plants, Average energy, Average children count, Average epochs lived");
            this.simulationData.stream().map(this::convertToCSV).forEach(pw::println);
            pw.println("Average values:");
            pw.println(this.simulationDataSummary());
            pw.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveObservedAnimalData(File csvOutputFile) {
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            pw.println("Children count, Descendents count, Epoch of death");
            pw.println(this.getObservedAnimalChildrenCount() + "," + this.getObservedAnimalDescendentsCount() + "," + this.getObservedAnimalEpochOfDeath());
            pw.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
