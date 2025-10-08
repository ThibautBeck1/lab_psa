package org.example;

public class DispatchSection {
    private int id;
    private Point bL;
    public DispatchSection(int id, int x , int y){
        this.id = id;
        this.bL = new Point(x, y);
        System.out.printf("new dispatchsection ID: %d, x: %d, y: %d\n", id, x, y);
    }
}
