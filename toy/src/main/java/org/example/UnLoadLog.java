package org.example;



final public class UnLoadLog extends Log{
    UnLoadLog(int timestamp) {
        super(timestamp);
    }
    @Override
    public void printout() {
        super.printout();
        System.out.println("unload");
    }
    @Override
    public String toString(){
        return super.toString() + "unload\n";
    }
}