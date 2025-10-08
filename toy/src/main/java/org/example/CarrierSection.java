package org.example;

public class CarrierSection {
    public int id, craneID, x, y, width, height, orientation;
    public CarrierSection(int id, int craneID, int x, int y){
        this.id = id;
        this.craneID = id;
        this.x = x;
        this.y = x;
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
