package agh.ics.oop.gui;

import agh.ics.oop.IMapElement;
import agh.ics.oop.Rectangle;
import agh.ics.oop.SimulationStatistics;
import agh.ics.oop.Vector2d;
import agh.ics.oop.animal.Animal;
import agh.ics.oop.engine.*;
import agh.ics.oop.map.*;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.*;

public class SimulationStage extends Stage {

    private IMap map;
    private IEngine engine;
    private final SimulationStatistics statistics;

    private final SimulationStatisticsBox simulationStatisticsBox;
    private final GridPane grid;
    private final Button showSelectedAnimalGenotypeButton;
    private final Button observeSelectedAnimalButton;
    private final Button showAnimalsWithDominantGenotypeButton;
    private final Button saveSimulationDataButton;
    private final Button saveObservedAnimalDataButton;

    private final Map<Vector2d, FlowPane> gridCells = new HashMap<>();
    private final Map<String, Image> loadedImages = new HashMap<>();

    private Animal selectedAnimal = null;

    public SimulationStage(SimulationProperties properties) {
        switch (properties.mapType) {
            case BORDERED -> this.map = new BorderedMap(properties.mapWidth, properties.mapHeight, properties.jungleWidth, properties.jungleHeight);
            case WRAPPED -> this.map = new WrappedMap(properties.mapWidth, properties.mapHeight, properties.jungleWidth, properties.jungleHeight);
        }

        this.statistics = new SimulationStatistics();

        switch (properties.engineType) {
            case NORMAL -> this.engine = new SimulationEngine(this, this.map, this.statistics, properties.startEnergy, properties.moveEnergy,
                    properties.plantEnergy, properties.initialNumberOfAnimals, properties.grassSpawnEachDay, properties.refreshTime, false);

            case MAGIC -> this.engine = new SimulationEngine(this, this.map, this.statistics, properties.startEnergy, properties.moveEnergy,
                    properties.plantEnergy, properties.initialNumberOfAnimals, properties.grassSpawnEachDay, properties.refreshTime, true);
        }

        this.simulationStatisticsBox = new SimulationStatisticsBox(this.statistics);

        this.showSelectedAnimalGenotypeButton = new Button("Show selected animal genotype");
        this.observeSelectedAnimalButton = new Button("Observe selected animal");
        this.showAnimalsWithDominantGenotypeButton = new Button("Show animals with dominant genotype");
        this.saveSimulationDataButton = new Button("Save simulation data");
        this.saveObservedAnimalDataButton = new Button("Save observed animal data");
        this.initializeStatisticsButtons();

        this.grid = new GridPane();
        this.initializeGridPane();

        HBox mainHBox = new HBox(createLeftPanel(), createScrollPane(properties));
        Scene scene = new Scene(mainHBox);
        scene.getStylesheets().add("style.css");

        this.setScene(scene);
        this.setTitle("Simulation  -  Map: " + properties.mapType + "   Engine: " + properties.engineType + "   Map size: " + properties.mapWidth + "x" + properties.mapHeight);
        this.setOnCloseRequest(event -> this.engine.stop());
    }

    public void updateStatistics() {
        this.simulationStatisticsBox.updateStatistics();
    }

    public void drawMap() {
        Rectangle mapRectangle = this.map.getMapRectangle();

        for (int i = mapRectangle.bottomLeftCorner.x; i <= mapRectangle.topRightCorner.x; i++) {
            for (int j = mapRectangle.bottomLeftCorner.y; j <= mapRectangle.topRightCorner.y; j++) {
                Vector2d position = new Vector2d(i, j);

                this.gridCells.get(position).getChildren().clear();

                if (this.map.isOccupied(position)) {
                    GuiElementBox guiElementBox = new GuiElementBox((IMapElement) this.map.objectAt(position), this.loadedImages);
                    this.gridCells.get(position).getChildren().add(guiElementBox);
                }
            }
        }
    }

    public void simulationStopped() {
        this.enableStatisticsButtons();

        Rectangle mapRectangle = this.map.getMapRectangle();
        for (int i = mapRectangle.bottomLeftCorner.x; i <= mapRectangle.topRightCorner.x; i++) {
            for (int j = mapRectangle.bottomLeftCorner.y; j <= mapRectangle.topRightCorner.y; j++) {
                Vector2d position = new Vector2d(i, j);

                if (this.cellContainsAnimal(position)) {
                    GuiElementBox guiElementBox = ((GuiElementBox) this.gridCells.get(position).getChildren().get(0));

                    guiElementBox.setOnMouseClicked(event -> {
                        this.selectedAnimal = guiElementBox.getAnimal();
                    });
                }
            }
        }
    }

    public void magicRule() {
        this.messagePopup("Magic rule!!!", "M A G I C");
    }

    private boolean cellContainsAnimal(Vector2d position) {
        return this.gridCells.get(position).getChildren().size() == 1 && ((GuiElementBox) this.gridCells.get(position).getChildren().get(0)).containsAnimal();
    }

    private void messagePopup(String message, String title) {
        Stage popupStage = new Stage();
        Label messageLabel = new Label(message);
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setTextAlignment(TextAlignment.CENTER);
        messageLabel.setPadding(new Insets(20, 20, 20, 20));

        popupStage.setScene(new Scene(messageLabel));
        popupStage.setTitle(title);
        popupStage.show();
    }

    private VBox createLeftPanel() {
        VBox leftPanel = new VBox();
        leftPanel.setPrefWidth(700);
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.setSpacing(15);
        leftPanel.getChildren().add(this.createStartStopButton());
        leftPanel.getChildren().add(this.simulationStatisticsBox);

        GridPane statisticsButtonsGrid = new GridPane();
        statisticsButtonsGrid.add(this.showSelectedAnimalGenotypeButton, 0, 0, 1, 1);
        statisticsButtonsGrid.add(this.observeSelectedAnimalButton, 1, 0, 1, 1);
        statisticsButtonsGrid.add(this.showAnimalsWithDominantGenotypeButton, 2, 0, 1, 1);
        leftPanel.getChildren().add(statisticsButtonsGrid);
        leftPanel.getChildren().add(this.saveSimulationDataButton);
        leftPanel.getChildren().add(this.saveObservedAnimalDataButton);
        leftPanel.setPadding(new Insets(20, 0, 20, 0));

        ColumnConstraints cc = new ColumnConstraints(233);
        cc.setHalignment(HPos.CENTER);
        statisticsButtonsGrid.getColumnConstraints().addAll(cc, cc, cc);

        return leftPanel;
    }

    private ScrollPane createScrollPane(SimulationProperties properties) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(Math.min(1200, properties.mapWidth * 58 + 2), Math.min(900, properties.mapWidth * 58 + 2));
        scrollPane.setContent(this.grid);

        return scrollPane;
    }

    private Button createStartStopButton() {
        Button startStopButton = new Button("Start/Stop simulation");

        startStopButton.setOnAction(event -> {
            if (this.engine.isRunning()) {
                this.engine.stop();
            } else {
                this.disableStatisticsButtons();
                this.selectedAnimal = null;

                Thread engineThread = new Thread((Runnable) this.engine);
                engineThread.start();
            }
        });

        return startStopButton;
    }

    private void initializeStatisticsButtons() {
        this.disableStatisticsButtons();

        this.showSelectedAnimalGenotypeButton.setOnAction(event -> {
            if (this.selectedAnimal != null) {
                this.messagePopup(this.selectedAnimal.genotype.toString(), "Selected animal genotype");
            }
        });

        this.showAnimalsWithDominantGenotypeButton.setOnAction(event -> {
            String animalsWithDominantGenotypeString = this.statistics.getAnimalsWithDominantGenotype(this.engine.getCopyOfAnimalsList());
            this.messagePopup(animalsWithDominantGenotypeString, "List of animals with dominant genotype");
        });

        this.observeSelectedAnimalButton.setOnAction(event -> {
            if (this.selectedAnimal != null) {
                this.statistics.observeAnimal(this.selectedAnimal);
                this.messagePopup("Selected animal will be observed:\n" + this.selectedAnimal.toString(), "Observe");
            }
        });

        this.saveSimulationDataButton.setOnAction(event -> {
            this.statistics.saveSimulationData(this.getFile());
        });

        this.saveObservedAnimalDataButton.setOnAction(event -> {
            this.statistics.saveObservedAnimalData(this.getFile());
        });

    }

    private File getFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("simulation_statistics.csv");
        fileChooser.setTitle("Save simulation statistics");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        return fileChooser.showSaveDialog(new Stage());
    }

    private void disableStatisticsButtons() {
        this.showSelectedAnimalGenotypeButton.setDisable(true);
        this.observeSelectedAnimalButton.setDisable(true);
        this.showAnimalsWithDominantGenotypeButton.setDisable(true);
        this.saveSimulationDataButton.setDisable(true);
        this.saveObservedAnimalDataButton.setDisable(true);
    }

    private void enableStatisticsButtons() {
        this.showSelectedAnimalGenotypeButton.setDisable(false);
        this.observeSelectedAnimalButton.setDisable(false);
        this.showAnimalsWithDominantGenotypeButton.setDisable(false);
        this.saveSimulationDataButton.setDisable(false);
        this.saveObservedAnimalDataButton.setDisable(false);
    }

    private void initializeGridPane() {
        Rectangle mapRectangle = this.map.getMapRectangle();
        Rectangle jungleRectangle = this.map.getJungleRectangle();

        ColumnConstraints cc = new ColumnConstraints(58);
        cc.setHalignment(HPos.CENTER);
        RowConstraints rc = new RowConstraints(58);
        rc.setValignment(VPos.CENTER);

        for (int i = mapRectangle.bottomLeftCorner.x; i <= mapRectangle.topRightCorner.x; i++) {
            this.grid.getColumnConstraints().add(cc);
        }

        for (int i = mapRectangle.bottomLeftCorner.y; i <= mapRectangle.topRightCorner.y; i++) {
            this.grid.getRowConstraints().add(rc);
        }

        for (int i = mapRectangle.bottomLeftCorner.x; i <= mapRectangle.topRightCorner.x; i++) {
            for (int j = mapRectangle.bottomLeftCorner.y; j <= mapRectangle.topRightCorner.y; j++) {
                Vector2d position = new Vector2d(i, j);

                FlowPane cell = new FlowPane();
                cell.setAlignment(Pos.CENTER);

                if (jungleRectangle.inRectangle(position)) {
                    cell.getStyleClass().add("jungle-cell");
                } else {
                    cell.getStyleClass().add("steppe-cell");
                }

                this.gridCells.put(position, cell);
                this.grid.add(cell, i - mapRectangle.bottomLeftCorner.x, mapRectangle.topRightCorner.y - j, 1, 1);
            }
        }
    }
}
