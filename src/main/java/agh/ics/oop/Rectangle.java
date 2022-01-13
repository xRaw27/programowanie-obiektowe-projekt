package agh.ics.oop;

public class Rectangle {
    public final Vector2d bottomLeftCorner;
    public final Vector2d topRightCorner;

    public Rectangle(Vector2d bottomLeftCorner, Vector2d topRightCorner) {
        this.bottomLeftCorner = bottomLeftCorner;
        this.topRightCorner = topRightCorner;
    }

    public Vector2d getDiagonalVector() {
        return this.topRightCorner.subtract(this.bottomLeftCorner).add(new Vector2d(1, 1));
    }

    public int getWidth() {
        return this.topRightCorner.x - this.bottomLeftCorner.x + 1;
    }

    public int getHeight() {
        return this.topRightCorner.y - this.bottomLeftCorner.y + 1;
    }

    public int getArea() {
        if (this.bottomLeftCorner.precedes(this.topRightCorner)) {
            return this.getWidth() * this.getHeight();
        } else {
            return 0;
        }
    }

    public boolean inRectangle(Vector2d position) {
        return position.precedes(this.topRightCorner) && position.follows(this.bottomLeftCorner);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
        Rectangle that = (Rectangle) other;

        return this.bottomLeftCorner.equals(that.bottomLeftCorner) && this.topRightCorner.equals(that.topRightCorner);
    }
}
