package Models;

/**
 *
 * @author huert
 */
public class Ship {
    private int x, y, length;
    private int orintation;

    public Ship() {
    }

    public Ship(int x, int y, int length, int orintation) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.orintation = orintation;
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

    public int getOrintation() {
        return orintation;
    }

    public void setOrintation(int orintation) {
        this.orintation = orintation;
    }
}
