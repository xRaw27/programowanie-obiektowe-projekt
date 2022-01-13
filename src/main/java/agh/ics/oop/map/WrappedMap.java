package agh.ics.oop.map;

import agh.ics.oop.Vector2d;

public class WrappedMap extends AbstractMap{


    public WrappedMap(int mapWidth, int mapHeight, int jungleWidth, int jungleHeight) {
        super(mapWidth, mapHeight, jungleWidth, jungleHeight);
    }


    @Override
    public Vector2d getNewPosition(Vector2d oldPosition, Vector2d provisionalPosition) {
        Vector2d position = provisionalPosition;

        if (provisionalPosition.hasGreaterX(this.topRightMapCorner)) {
            position = position.upperLeft(this.bottomLeftMapCorner);
        }
        else if (provisionalPosition.hasSmallerX(this.bottomLeftMapCorner)) {
            position = position.lowerRight(this.topRightMapCorner);
        }

        if (provisionalPosition.hasGreaterY(this.topRightMapCorner)) {
            position = position.lowerRight(this.bottomLeftMapCorner);
        }
        else if (provisionalPosition.hasSmallerY(this.bottomLeftMapCorner)) {
            position = position.upperLeft(this.topRightMapCorner);
        }

        return position;
    }
}
