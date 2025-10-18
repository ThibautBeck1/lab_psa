package org.example;
import java.util.ArrayList;
public class Carrier {
    public int id, craneID, x, y, width, height, orientation;
    public ArrayList<Log> logs = new ArrayList<>();
    public Carrier(int id, int craneID, int x, int y){
        this.id = id;
        this.craneID = craneID;
        this.x = x;
        this.y = y;
        width = 4;
        height = 8;
        orientation = 0;
    }
    public void rotate(){
        width = 8;
        height = 4;
        orientation = 1;
    }
}

enum Act {
    MOVE , LOAD , UNLOAD ,FACE
}
enum Direction {
    UP, DOWN, LEFT, RIGHT
}
class Log {
    public int timestamp;
    public Log(int time ) {
        this.timestamp = time;
    }
}
final class moveLog extends Log{
    int units;
        public moveLog(int timestamp ,int  units ) {
        super(timestamp);
        this.units =  units ;

    }
}
final class RotateLog extends Log{
    int units;
    Direction direction;
    public RotateLog(int timestamp ,int  units , Direction direction) {
        super(timestamp);
        this.units =  units;
        this.direction = direction;
    }
}
