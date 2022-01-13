package agh.ics.oop.gui;

import agh.ics.oop.engine.EngineType;
import agh.ics.oop.map.MapType;

public class SimulationProperties {

    final int mapWidth;
    final int mapHeight;
    final int jungleWidth;
    final int jungleHeight;
    final MapType mapType;

    final int startEnergy;
    final int moveEnergy;
    final int plantEnergy;

    final int initialNumberOfAnimals;
    final int grassSpawnEachDay;
    final int refreshTime;
    final EngineType engineType;

    public SimulationProperties(MenuStage menu) {

        this.mapWidth = Integer.parseInt(menu.mapWidthTextField.getText());
        this.mapHeight = Integer.parseInt(menu.mapHeightTextField.getText());
        if (this.mapWidth < 2) {
            throw new IllegalArgumentException("The map width given is too small");
        }
        if (this.mapHeight < 2 ) {
            throw new IllegalArgumentException("The map height given is too small");
        }

        float ratio = Float.parseFloat(menu.jungleRatioTextField.getText());
        this.jungleWidth = (int) Math.round(this.mapWidth * Math.sqrt(ratio));
        this.jungleHeight = (int) Math.round(this.mapHeight * Math.sqrt(ratio));
        if (this.jungleWidth <= 0 || this.jungleHeight <= 0) {
            throw new IllegalArgumentException("The jungle ratio given is too small to create the map");
        }
        if (this.jungleWidth >= this.mapWidth || this.jungleHeight >= this.mapHeight ) {
            throw new IllegalArgumentException("The jungle ratio given is too big to create the map");
        }

        this.startEnergy = Integer.parseInt(menu.startEnergyTextField.getText());
        this.moveEnergy = Integer.parseInt(menu.moveEnergyTextField.getText());
        this.plantEnergy = Integer.parseInt(menu.plantEnergyTextField.getText());
        if (this.startEnergy <= 0 || this.moveEnergy <= 0 || this.plantEnergy <= 0) {
            throw new IllegalArgumentException("All energy properties must be greater than 0");
        }

        this.initialNumberOfAnimals = Integer.parseInt(menu.initialNumberOfAnimalsTextField.getText());
        if (this.initialNumberOfAnimals <= 0) {
            throw new IllegalArgumentException("The initial number of animals must be greater than 0");
        }

        this.grassSpawnEachDay = Integer.parseInt(menu.grassSpawnEachDayTextField.getText());
        if (this.grassSpawnEachDay <= 0) {
            throw new IllegalArgumentException("The number of grass spawn each day must be greater than 0");
        }

        this.refreshTime = Integer.parseInt(menu.refreshTimeTextField.getText());
        if (this.refreshTime <= 5) {
            throw new IllegalArgumentException("Refresh time given is too low");
        }

        this.mapType = (MapType) menu.mapTypeToggleGroup.getSelectedToggle().getUserData();
        this.engineType = (EngineType) menu.evolutionTypeToggleGroup.getSelectedToggle().getUserData();
    }

}
