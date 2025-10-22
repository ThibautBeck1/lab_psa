package org.example;

final public class RotateLog extends Log{
    Direction direction;
    public RotateLog(int timestamp , Direction direction) {
        super(timestamp);
        this.direction = direction;
    }
    @Override
    public void printout() {
        super.printout();
        System.out.println("rotate " + this.direction);
    }
}