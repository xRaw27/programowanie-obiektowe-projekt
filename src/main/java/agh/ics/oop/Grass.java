package agh.ics.oop;

public class Grass implements IMapElement{

    private final int plantEnergy;

    public Grass(int plantEnergy) {
        this.plantEnergy = plantEnergy;
    }

    public int getPlantEnergy() {
        return this.plantEnergy;
    }

    @Override
    public String getImageSrc() {
        return "src/main/resources/grass.png";
    }

    @Override
    public double getProgressBarStatus() {
        return -1;
    }

    @Override
    public boolean isAnimal() {
        return false;
    }
}

