package org.example;

final public class LoadLog extends Log{
    LoadLog(int timestamp) {
        super(timestamp);
    }
    @Override
    public void printout() {
        System.out.println("load");
    }
}