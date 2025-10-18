// src/main/java/org/example/Storage.java
package org.example;

public class Storage {
    public int id, x, y;
    public int width;
    public int height;

    public Storage(int id, int x, int y) { // Constructor takes a Point
        this.id = id;
        this.x = x;
        this.y = y;
        width = 2;
        height = 4;
    }

    // Getters


    // Optional: toString for easy printing
    @Override
    public String toString() {
        return "Storage{" +
                "id=" + id +
                ", bottomLeft=" + x + ',' + y  +
                ", dimensions=" + width + "x" + height +
                '}';
    }
}