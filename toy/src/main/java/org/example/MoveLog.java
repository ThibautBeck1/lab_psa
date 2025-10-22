package org.example;
final public class MoveLog extends Log{
    int units;
    public MoveLog(int timestamp ,int  units ) {
        super(timestamp);
        this.units =  units ;
    }

    @Override
    public void printout() {
        super.printout();
        System.out.println("move " + this.units);
    }
}