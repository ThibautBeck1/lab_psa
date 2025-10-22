package org.example;

public class DispatchSection {
    private int id, x, y;
    private Container container;
    public DispatchSection(int id, int x , int y){
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public void setContainer(Container c){
        this.container = c;
    }
    public Container getContainer(){
        Container res = this.container;
        this.container = null;
        return res;
    }
    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
