package org.example;


abstract public class Log {
    public int timestamp;
    public Log(int time ) {
        this.timestamp = time;
    }
    public void printout(){
        System.out.print(this.timestamp +" ");
    }
    public String toString(){
        return this.timestamp + " ";
    }
}
