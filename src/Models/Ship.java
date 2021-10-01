package Models;

import java.io.Serializable;

/**
 *
 * @author huert
 */
public class Ship implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private int x, y, length;
    private boolean vertical;

    public Ship() {
    }

    public Ship(String name, int x, int y, int length, boolean vertical) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.length = length;
        this.vertical = vertical;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Ship{" + "name=" + name + ", x=" + x + ", y=" + y + ", length="
                + length + ", vertical=" + vertical + '}';
    }
}
