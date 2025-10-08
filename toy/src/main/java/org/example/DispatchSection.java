package org.example;

public class DispatchSection {
    private int id, x, y;
    public DispatchSection(int id, int x , int y){
        this.id = id;
        this.x = x;
        this.y = y;
        System.out.printf("new dispatchsection ID: %d, x: %d, y: %d\n", id, x, y);
    }
}
