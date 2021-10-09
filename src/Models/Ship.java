package Models;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author huert
 */
public class Ship implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private int x, y, length, life;
    private boolean vertical;

    public static boolean isValidPosition(List<Ship> ships, Ship newShip) {
        if (ships.isEmpty()) {
            return true;
        }
        Ship ship;
        int start, end, fixed, newStart, newFixed, newEnd;
        newEnd = newShip.getLength() - 1;
        if (newShip.isVertical()) {
            newStart = newShip.getY();
            newFixed = newShip.getX();
        } else {
            newStart = newShip.getX();
            newFixed = newShip.getY();
        }
        newEnd += newStart;
        for (int i = 0; i < ships.size(); i++) {
            ship = ships.get(i);
            end = ship.getLength() - 1;
            if (ship.isVertical()) {
                start = ship.getY();
                fixed = ship.getX();
                end += start;
                if (newShip.isVertical()) {
                    if (((start <= newStart && newStart <= end)
                            || (start <= newEnd && newEnd <= end))
                            && fixed == newFixed) {
                        return false;
                    }
                } else {
                    if ((start <= newFixed && newFixed <= end)
                            && (newStart <= fixed && fixed <= newEnd)) {
                        return false;
                    }
                }
            } else {
                start = ship.getX();
                fixed = ship.getY();
                end += start;
                if (newShip.isVertical()) {
                    if ((start <= newFixed && newFixed <= end)
                            && (newStart <= fixed && fixed <= newEnd)) {
                        return false;
                    }
                } else {
                    if (((start <= newStart && newStart <= end)
                            || (start <= newEnd && newEnd <= end))
                            && fixed == newFixed) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    public static boolean isDamaged(Ship ship, int x, int y){
        int range = ship.getLength() - 1;
        if(ship.isVertical()){
            range += ship.getY();
            if(ship.getX() == x && ship.getY() <= y && y >= range){
                ship.setLife(ship.getLife() - 1);
                return true;
            } else {
                return false;
            }
        } else {
            range += ship.getX();
            if(ship.getY() == y && ship.getX() <= x && x >= range){
                ship.setLife(ship.getLife() - 1);
                return true;
            } else {
                return false;
            }
        }
    }

    public Ship() {
    }

    public Ship(String name, int x, int y, int length, boolean vertical) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.length = length;
        this.life = length;
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

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public boolean isVertical() {
        return vertical;
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    @Override
    public String toString() {
        return "Ship{" + "name=" + name + ", x=" + x + ", y=" + y + ", length=" + length + ", life=" + life + ", vertical=" + vertical + '}';
    }

}
