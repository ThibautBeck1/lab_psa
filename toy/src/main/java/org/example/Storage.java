// src/main/java/org/example/Storage.java
package org.example;

public class Storage {
    public int id, x, y;


    public Storage(int id, int x, int y) { // Constructor takes a Point
        this.id = id;
        this.x = x;
        this.y = y;
     // hier niet de breedte van de storage opslaan voor mem eff
    }



    // Optional: toString for easy printing
    @Override
    public String toString() {
        return "Storage{" +
                "id=" + id +
                ", bottomLeft=" + x + ',' + y  +
                ", dimensions=" + 2+ "x" + 4 +
                '}';
    }
}