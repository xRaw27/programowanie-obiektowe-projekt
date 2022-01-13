package agh.ics.oop.map;

import agh.ics.oop.Vector2d;

public class BorderedMap extends AbstractMap{

    public BorderedMap(int mapWidth, int mapHeight, int jungleWidth, int jungleHeight) {
        super(mapWidth, mapHeight, jungleWidth, jungleHeight);
    }

    @Override
    public Vector2d getNewPosition(Vector2d oldPosition, Vector2d provisionalPosition) {

        if (provisionalPosition.precedes(this.topRightMapCorner) && provisionalPosition.follows(this.bottomLeftMapCorner)) {
            return provisionalPosition;
        }
        else {
            return oldPosition;
        }

    }
}
