package Models;

/**
 *
 * @author huert
 */
public class Ship {
    private int x, y, length;
    private boolean vertical;

    public Ship() {
    }

    public Ship(int x, int y, int length, boolean vertical) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.vertical = vertical;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean getVertical() {
        return vertical;
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }
}
