package agh.ics.oop.gui;

import agh.ics.oop.engine.EngineType;
import agh.ics.oop.map.MapType;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MenuStage extends Stage {

    TextField mapWidthTextField = new TextField("15");
    TextField mapHeightTextField = new TextField("15");
    TextField jungleRatioTextField = new TextField("0.1");
    ToggleGroup mapTypeToggleGroup = new ToggleGroup();

    TextField startEnergyTextField = new TextField("40");
    TextField moveEnergyTextField = new TextField("1");
    TextField plantEnergyTextField = new TextField("12");

    TextField initialNumberOfAnimalsTextField = new TextField("40");
    TextField grassSpawnEachDayTextField = new TextField("1");
    TextField refreshTimeTextField = new TextField("40");
    ToggleGroup evolutionTypeToggleGroup = new ToggleGroup();

    private final Button startButton = new Button("Start simulation");

    public MenuStage() {
        GridPane menuGrid = initializeMenuGrid();

        this.startButton.setOnAction(actionEvent -> {
            try {
                SimulationStage simulationStage = new SimulationStage(new SimulationProperties(this));
                simulationStage.show();
            } catch (NumberFormatException e) {
                System.out.println("Wrong property format. " + e.getMessage());
                this.exceptionMessagePopup("Wrong property format. " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                this.exceptionMessagePopup(e.getMessage());
            }
        });

        this.setScene(new Scene(menuGrid, 400, 550));
        this.setTitle("Simulation menu");
        this.startButton.requestFocus();
    }

    private void exceptionMessagePopup(String message) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(this);

        Label messageLabel = new Label(message);
        messageLabel.setAlignment(Pos.CENTER);

        popupStage.setScene(new Scene(messageLabel, 380, 60));
        popupStage.show();
    }

    private GridPane initializeMenuGrid() {
        GridPane grid = new GridPane();

        Label mapSettingsLabel = new Label("Map settings");
        grid.add(mapSettingsLabel, 0, 0, 2, 1);
        GridPane.setHalignment(mapSettingsLabel, HPos.CENTER);

        Label energySettingsLabel = new Label("Energy settings");
        grid.add(energySettingsLabel, 0, 6, 2, 1);
        GridPane.setHalignment(energySettingsLabel, HPos.CENTER);

        Label otherSettingsLabel = new Label("Other settings");
        grid.add(otherSettingsLabel, 0, 10, 2, 1);
        GridPane.setHalignment(otherSettingsLabel, HPos.CENTER);

        grid.add(new Label("Map width: "), 0, 1, 1, 1);
        grid.add(new Label("Map height: "), 0, 2, 1, 1);
        grid.add(new Label("Jungle ratio: "), 0, 3, 1, 1);
        grid.add(new Label("Start energy: "), 0, 7, 1, 1);
        grid.add(new Label("Move energy: "), 0, 8, 1, 1);
        grid.add(new Label("Plant energy: "), 0, 9, 1, 1);
        grid.add(new Label("Initial number of animals: "), 0, 11, 1, 1);
        grid.add(new Label("Grass spawn each day: "), 0, 12, 1, 1);
        grid.add(new Label("Refresh time (ms): "), 0, 13, 1, 1);

        grid.add(this.mapWidthTextField, 1, 1, 1, 1);
        grid.add(this.mapHeightTextField, 1, 2, 1, 1);
        grid.add(this.jungleRatioTextField, 1, 3, 1, 1);
        grid.add(this.startEnergyTextField, 1, 7, 1, 1);
        grid.add(this.moveEnergyTextField, 1, 8, 1, 1);
        grid.add(this.plantEnergyTextField, 1, 9, 1, 1);
        grid.add(this.initialNumberOfAnimalsTextField, 1, 11, 1, 1);
        grid.add(this.grassSpawnEachDayTextField, 1, 12, 1, 1);
        grid.add(this.refreshTimeTextField, 1, 13, 1, 1);

        RadioButton radioButton1 = new RadioButton("Bordered map");
        RadioButton radioButton2 = new RadioButton("Wrapped map");
        RadioButton radioButton3 = new RadioButton("Normal Evolution");
        RadioButton radioButton4 = new RadioButton("Magic Evolution");
        grid.add(radioButton1, 0, 4, 1, 1);
        grid.add(radioButton2, 1, 4, 1, 1);
        grid.add(radioButton3, 0, 14, 1, 1);
        grid.add(radioButton4, 1, 14, 1, 1);

        radioButton1.setToggleGroup(this.mapTypeToggleGroup);
        radioButton2.setToggleGroup(this.mapTypeToggleGroup);
        radioButton3.setToggleGroup(this.evolutionTypeToggleGroup);
        radioButton4.setToggleGroup(this.evolutionTypeToggleGroup);
        radioButton1.setUserData(MapType.BORDERED);
        radioButton2.setUserData(MapType.WRAPPED);
        radioButton3.setUserData(EngineType.NORMAL);
        radioButton4.setUserData(EngineType.MAGIC);

        radioButton1.setSelected(true);
        radioButton3.setSelected(true);

        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        grid.add(this.startButton, 0, 15, 2, 1);
        GridPane.setHalignment(this.startButton, HPos.CENTER);
        GridPane.setMargin(this.startButton, new Insets(30, 0, 0, 0));

        return grid;
    }

}
