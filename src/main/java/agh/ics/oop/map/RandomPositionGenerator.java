package agh.ics.oop.map;

import agh.ics.oop.Rectangle;
import agh.ics.oop.Vector2d;

import java.util.List;
import java.util.Random;

public class RandomPositionGenerator {

    private final IMap map;

    public RandomPositionGenerator(IMap map) {
        this.map = map;
    }

    private Vector2d nthUnoccupiedPositionInRectangles(List<Rectangle> rectangles, int n) {
        int counter = 0;

        for (Rectangle rectangle : rectangles) {
            for (int x = rectangle.bottomLeftCorner.x; x <= rectangle.topRightCorner.x; x++) {
                for (int y = rectangle.bottomLeftCorner.y; y <= rectangle.topRightCorner.y; y++) {
                    Vector2d position = new Vector2d(x, y);

                    if (!this.map.isOccupied(position)) {
                        if (n == counter) {
                            return position;
                        }

                        counter += 1;
                    }
                }
            }
        }

        return null;
    }

    private Vector2d randomPositionInRectangles(List<Rectangle> rectangles, int[] areas, int sumOfAreas) {
        int randNumber = (new Random()).nextInt(sumOfAreas);

        int i = 0;
        while (randNumber >= areas[i]) {
            randNumber -= areas[i];
            i += 1;
        }

        Rectangle rectangle = rectangles.get(i);
        return rectangle.bottomLeftCorner.add(new Vector2d(randNumber % rectangle.getWidth(), randNumber / rectangle.getWidth()));
    }

    public Vector2d randomUnoccupiedPositionInRectangles(List<Rectangle> rectangles, int sumOfAreas, int numberOfOccupiedCells) {
        int[] areas = rectangles.stream().map(Rectangle::getArea).mapToInt(x -> x).toArray();

        if (numberOfOccupiedCells < IMap.BOUNDARY_OCCUPIED_RATIO * sumOfAreas) {
            Vector2d randPosition = randomPositionInRectangles(rectangles, areas, sumOfAreas);
            while (this.map.isOccupied(randPosition)) {
                randPosition = randomPositionInRectangles(rectangles, areas, sumOfAreas);
            }

            return randPosition;
        }
        else if (numberOfOccupiedCells < sumOfAreas) {
            int randPositionNumber = (new Random()).nextInt(sumOfAreas - numberOfOccupiedCells);
            return nthUnoccupiedPositionInRectangles(rectangles, randPositionNumber);
        }

        return null;
    }
}
