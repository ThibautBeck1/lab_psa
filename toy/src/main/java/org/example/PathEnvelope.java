package org.example;

import java.awt.*;

/**
 * Eén stap in het pad van de carrier.
 * Houdt enkel de linkeronderhoek en de richting bij.
 * Assenstelsel: x neemt toe naar rechts, y neemt af naar beneden.
 */
public class PathEnvelope {
    private final Point onderHoek;      // linkeronderhoek (x, y)
    private final Direction direction;  // huidige richting

    public PathEnvelope(int x, int y, Direction direction) {
        this.onderHoek = new Point(x, y);
        this.direction = direction;
    }

    public Point getOnderHoek() { return onderHoek; }
    public Direction getDirection() { return direction; }

    public int getX() { return onderHoek.x; }
    public int getY() { return onderHoek.y; }

    @Override
    public String toString() {
        return "PathEnvelope{onderhoek=(" + onderHoek.x + "," + onderHoek.y +
                "), direction=" + direction + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PathEnvelope that)) return false;
        return onderHoek.equals(that.onderHoek) &&
                direction == that.direction;
    }

    @Override
    public int hashCode() {
        int result = onderHoek.hashCode();
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        return result;
    }
}
