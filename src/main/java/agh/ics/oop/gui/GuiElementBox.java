package agh.ics.oop.gui;

import agh.ics.oop.*;
import agh.ics.oop.animal.Animal;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

public class GuiElementBox extends VBox {

    private final IMapElement mapElement;

    public GuiElementBox(IMapElement mapElement, Map<String, Image> loadedImages) {
        String imageSrc = mapElement.getImageSrc();

        if (loadedImages.get(imageSrc) == null) {
            try {
                loadedImages.put(imageSrc, new Image(new FileInputStream(imageSrc)));
                System.out.println("Loaded new image: " + imageSrc);
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
                System.exit(0);
            }
        }

        ImageView imageView = new ImageView(loadedImages.get(mapElement.getImageSrc()));
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        this.getChildren().add(imageView);

        if (mapElement.getProgressBarStatus() >= 0) {
            ProgressBar progressBar = new ProgressBar(mapElement.getProgressBarStatus());
            this.getChildren().add(progressBar);
        }

        this.setAlignment(Pos.CENTER);
        this.setPrefWidth(50);
        this.mapElement = mapElement;
    }

    public boolean containsAnimal() {
        return this.mapElement.isAnimal();
    }

    public Animal getAnimal() {
        return this.mapElement.isAnimal() ? (Animal) this.mapElement : null;
    }
}
