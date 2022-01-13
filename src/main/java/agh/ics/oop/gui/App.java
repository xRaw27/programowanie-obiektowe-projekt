package agh.ics.oop.gui;

import javafx.application.Application;
import javafx.stage.Stage;


public class App extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Stage mainStage = new MenuStage();
        mainStage.show();

    }

}
