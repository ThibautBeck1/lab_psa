package org.example;

public class Carrier {
    public int id, craneID, x, y, width, height, orientation;
    public Carrier(int id, int craneID, int x, int y){
        this.id = id;
        this.craneID = craneID;
        this.x = x;
        this.y = y;
        width = 4;
        height = 8;
        orientation = 0;
    }
    public void rotate(){
        width = 8;
        height = 4;
        orientation = 1;
    }
}
