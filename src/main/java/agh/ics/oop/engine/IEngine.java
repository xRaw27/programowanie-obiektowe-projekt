package agh.ics.oop.engine;

import agh.ics.oop.animal.Animal;

import java.util.List;

public interface IEngine {

    void run();

    void stop();

    boolean isRunning();

    List<Animal> getCopyOfAnimalsList();

}