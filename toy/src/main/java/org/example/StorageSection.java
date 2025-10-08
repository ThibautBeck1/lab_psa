// src/main/java/org/example/Storage.java
package org.example;

public class StorageSection {
    private int id, x, y;
    private static final int WIDTH = 2;
    private static final int HEIGHT = 4;

    public StorageSection(int id, int x, int y) { // Constructor takes a Point
        this.id = id;
        this.x = x;
        this.y = y;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getX() { // Returns a Point object
        return x;
    }
    public int getY() { // Returns a Point object
        return y;
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    // Optional: toString for easy printing
    @Override
    public String toString() {
        return "Storage{" +
                "id=" + id +
                ", bottomLeft=" + x + ',' + y  +
                ", dimensions=" + WIDTH + "x" + HEIGHT +
                '}';
    }
}