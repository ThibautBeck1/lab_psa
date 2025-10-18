package org.example;
import java.util.ArrayList;

/***
 * Direction
 * direction up = 1, down = 3       -> oneven : omhoog/omlaag
 * direction Right = 2 , Left = 4   -> even   : rechts/links
 * beginnen met direction 1 -> Omhoog
 *
 *
 * wanneer mogen roteren -> roteren mag pas vanaf we uit de rijen rijden
 *
 */
public class Carrier {
    public int id, craneID, x, y, width, height , direction;


    public ArrayList<Log> logs = new ArrayList<>();
    public Carrier(int id, int craneID, int x, int y){
        this.id = id;
        this.craneID = craneID;
        this.x = x;
        this.y = y;
        this.width = 4;
        this.height = 8;
        this.direction = 1;
    }
    public void rotate(int dir , int time) {
        if (this.direction == dir) System.out.println(" je kan geen nul graden roteren");
        else if (this.direction %2 == dir%2) System.out.println("Dit kan niet , 180 graden omdraaien carrier kan niet");
        else if (this.direction %2 == 0) { // rechts, links -> boven/beneden
            width = 4;
            height = 8;
            this.direction = dir;
            // doordat we verdraaien veranderd ons onderste punt ook
            this.x += 2;
            this.y -= 2;
            logs.add(new RotateLog(time , dir));

        } else { // boven, beneden -> rechts links
            width = 8;
            height = 4;
            this.direction = dir;
            // doordat we verdraaien veranderd ons onderste punt ook
            this.x -= 2;
            this.y += 2;
            logs.add(new RotateLog(time , dir));
        }

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
    int direction;
    public RotateLog(int timestamp , int direction) {
        super(timestamp);
        this.direction = direction;
    }
}
