package org.example;

final public class LoadLog extends Log{
    LoadLog(int timestamp) {
        super(timestamp);
    }
    @Override
    public void printout() {
        super.printout();
        System.out.println("load");
    }
    @Override
    public String toString(){
        return super.toString() + "load\n";
    }
}