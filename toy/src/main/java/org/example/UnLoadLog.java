package org.example;



final public class UnLoadLog extends Log{
    UnLoadLog(int timestamp) {
        super(timestamp);
    }
    @Override
    public void printout() {
        System.out.println("unload");
    }
}