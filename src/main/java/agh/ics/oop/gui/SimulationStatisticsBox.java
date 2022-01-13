package agh.ics.oop.gui;

import agh.ics.oop.SimulationStatistics;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class SimulationStatisticsBox extends VBox {

    private final SimulationStatistics statistics;

    private XYChart.Series<Number, Number> numberOfAnimalsSeries;
    private XYChart.Series<Number, Number> numberOfPlantsSeries;
    private XYChart.Series<Number, Number> liveAnimalsAverageEnergySeries;
    private XYChart.Series<Number, Number> averageChildrenCountOfLiveAnimalsSeries;
    private XYChart.Series<Number, Number> averageNumberOfEpochsLivedSeries;
    private final Label dominantGenotypeLabel = new Label();

    private final Label observedAnimalChildrenCountLabel = new Label();
    private final Label observedAnimalDescendentsCountLabel = new Label();
    private final Label observedAnimalEpochOfDeathLabel = new Label();

    public SimulationStatisticsBox(SimulationStatistics statistics) {
        this.statistics = statistics;

        GridPane grid = new GridPane();
        Label genotypeLabel = new Label("Dominant genotype: ");
        Label observedAnimalLabel = new Label("Observed animal statistics: ");
        LineChart<Number, Number> numberOfMapElementsChart = this.createNumberOfMapElementsChart();

        grid.add(new Label("Number of elements on map"), 0, 0, 3, 1);
        grid.add(new Label("Live animals average energy"), 0, 2, 1, 1);
        grid.add(new Label("Average children count of live animals"), 1, 2, 1, 1);
        grid.add(new Label("Average number of epochs lived"), 2, 2, 1, 1);
        grid.add(genotypeLabel, 0, 4, 1, 1);
        grid.add(numberOfMapElementsChart, 0, 1, 3, 1);
        grid.add(this.createLiveAnimalsAverageEnergyChart(), 0, 3, 1, 1);
        grid.add(this.createAverageChildrenCountOfLiveAnimalsChart(), 1, 3, 1, 1);
        grid.add(this.createAverageNumberOfEpochsLivedChart(), 2, 3, 1, 1);
        grid.add(this.dominantGenotypeLabel, 1, 4, 2, 1);

        grid.add(observedAnimalLabel, 0, 5, 3, 1);
        grid.add(this.observedAnimalChildrenCountLabel, 0, 6, 1, 1);
        grid.add(this.observedAnimalDescendentsCountLabel, 1, 6, 1, 1);
        grid.add(this.observedAnimalEpochOfDeathLabel, 2, 6, 1, 1);

        grid.getChildren().forEach(element -> GridPane.setHalignment(element, HPos.CENTER));
        GridPane.setHalignment(genotypeLabel, HPos.RIGHT);
        GridPane.setHalignment(this.dominantGenotypeLabel, HPos.LEFT);
        GridPane.setHalignment(this.observedAnimalChildrenCountLabel, HPos.RIGHT);
        GridPane.setHalignment(this.observedAnimalEpochOfDeathLabel, HPos.LEFT);
        GridPane.setMargin(numberOfMapElementsChart, new Insets(0, 0, 25, 0));
        GridPane.setMargin(observedAnimalLabel, new Insets(25, 0, 0, 0));

        this.getChildren().add(grid);
    }

    public void updateStatistics() {
        int currentEpoch = this.statistics.getCurrentEpoch();

        this.numberOfAnimalsSeries.getData().add(new XYChart.Data<>(currentEpoch, this.statistics.getNumberOfLiveAnimals()));
        this.numberOfPlantsSeries.getData().add(new XYChart.Data<>(currentEpoch, this.statistics.getCurrentNumberOfPlants()));
        this.liveAnimalsAverageEnergySeries.getData().add(new XYChart.Data<>(currentEpoch, this.statistics.getLiveAnimalsAverageEnergy()));
        this.averageChildrenCountOfLiveAnimalsSeries.getData().add(new XYChart.Data<>(currentEpoch, this.statistics.getAverageChildrenCountOfLiveAnimals()));
        this.averageNumberOfEpochsLivedSeries.getData().add(new XYChart.Data<>(currentEpoch, this.statistics.getAverageNumberOfEpochsLived()));
        this.dominantGenotypeLabel.setText(this.statistics.getDominantGenotype());

        if (this.statistics.isAnyAnimalObserved()) {
            this.observedAnimalChildrenCountLabel.setText("Children count: " + this.statistics.getObservedAnimalChildrenCount());
            this.observedAnimalDescendentsCountLabel.setText("Descendents count: " + this.statistics.getObservedAnimalDescendentsCount());

            int epochOfDeath = this.statistics.getObservedAnimalEpochOfDeath();
            this.observedAnimalEpochOfDeathLabel.setText((epochOfDeath == 0) ? "Animal is alive" : "Epoch of death: " + epochOfDeath);
        }
    }

    private LineChart<Number, Number> createNumberOfMapElementsChart() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Epoch");
        yAxis.setLabel("Number of elements on map");

        LineChart<Number, Number> numberOfMapElementsChart = new LineChart<>(xAxis, yAxis);
        numberOfMapElementsChart.setPrefHeight(350);
        numberOfMapElementsChart.setCreateSymbols(false);

        this.numberOfAnimalsSeries = new XYChart.Series<>();
        this.numberOfAnimalsSeries.setName("Number of animals");
        this.numberOfPlantsSeries = new XYChart.Series<>();
        this.numberOfPlantsSeries.setName("Number of plants");
        numberOfMapElementsChart.getData().add(this.numberOfAnimalsSeries);
        numberOfMapElementsChart.getData().add(this.numberOfPlantsSeries);

        return numberOfMapElementsChart;
    }

    private LineChart<Number, Number> createLiveAnimalsAverageEnergyChart() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Epoch");
        yAxis.setLabel("Energy");

        LineChart<Number, Number> liveAnimalsAverageEnergyChart = new LineChart<>(xAxis, yAxis);
        liveAnimalsAverageEnergyChart.setPrefHeight(200);
        liveAnimalsAverageEnergyChart.setCreateSymbols(false);

        this.liveAnimalsAverageEnergySeries = new XYChart.Series<>();
        liveAnimalsAverageEnergyChart.getData().add(this.liveAnimalsAverageEnergySeries);
        liveAnimalsAverageEnergyChart.setLegendVisible(false);

        return liveAnimalsAverageEnergyChart;
    }

    private LineChart<Number, Number> createAverageChildrenCountOfLiveAnimalsChart() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Epoch");
        yAxis.setLabel("Children count");

        LineChart<Number, Number> averageChildrenCountOfLiveAnimalsChart = new LineChart<>(xAxis, yAxis);
        averageChildrenCountOfLiveAnimalsChart.setPrefHeight(200);
        averageChildrenCountOfLiveAnimalsChart.setCreateSymbols(false);

        this.averageChildrenCountOfLiveAnimalsSeries = new XYChart.Series<>();
        averageChildrenCountOfLiveAnimalsChart.getData().add(this.averageChildrenCountOfLiveAnimalsSeries);
        averageChildrenCountOfLiveAnimalsChart.setLegendVisible(false);

        return averageChildrenCountOfLiveAnimalsChart;
    }

    private LineChart<Number, Number> createAverageNumberOfEpochsLivedChart() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Epoch");
        yAxis.setLabel("Epochs lived");

        LineChart<Number, Number> averageNumberOfEpochsLivedChart = new LineChart<>(xAxis, yAxis);
        averageNumberOfEpochsLivedChart.setPrefHeight(200);
        averageNumberOfEpochsLivedChart.setCreateSymbols(false);

        this.averageNumberOfEpochsLivedSeries = new XYChart.Series<>();
        averageNumberOfEpochsLivedChart.getData().add(this.averageNumberOfEpochsLivedSeries);
        averageNumberOfEpochsLivedChart.setLegendVisible(false);

        return averageNumberOfEpochsLivedChart;
    }

}
