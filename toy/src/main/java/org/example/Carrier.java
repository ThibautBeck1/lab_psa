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
 * dus kijken naar laagst mogelijke beginblok van de storagesection
 * in toy data onderste punt van de storage section = 40
 * dit is ook in Constants.loweststorageY
 */
public class Carrier {
    public int id, craneID, x, y, width, height , direction;
    public Container container;

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
    public void pickupContainer(Container container){
        if (this.container != null)System.out.println(this.id + "already has a container");
        //checken of the carrier can pickup the container
        //upwards downwards position
        else if (direction %2 != 0 && container.getX()  ==this.x +1 &&  container.getY() ==this.y +2) {
            this.container = container;
            System.out.println(this.id + " picked up container " + this.container);
        }
        // horizontal pos , checking if the x of the container is 2 higher and the y is 1 higher
        else if (direction %2 == 0 && container.getX()  ==this.x +2 &&  container.getY() ==this.y +1) {
            this.container = container;
            System.out.println(this.id + " picked up container " + this.container);
        }
    }


    public void rotate(int dir , int time) {
        //TODO update this
        if (this.direction == dir) System.out.println(" je kan geen nul graden roteren");
        else if (this.direction % 2 == dir % 2)
            System.out.println("Dit kan niet , 180 graden omdraaien carrier kan niet");
        else if (this.direction % 2 == 0) { // rechts, links -> boven/beneden
            width = 4;
            height = 8;
            this.direction = dir;
            // doordat we verdraaien veranderd ons onderste punt ook
            this.x += 2;
            this.y -= 2;
            logs.add(new RotateLog(time, dir));

        } else { // boven, beneden -> rechts links
            width = 8;
            height = 4;
            this.direction = dir;
            // doordat we verdraaien veranderd ons onderste punt ook
            this.x -= 2;
            this.y += 2;
            logs.add(new RotateLog(time, dir));
        }
    }
        public int driveTo(int x ,int y ,int t, boolean verticalDestination){

            return t;

        }
        public void dropOffInStorage(Storage storage){
            if (this.container == null){
                System.out.println("je kan geen container afzetten als je er geen hebt");
            }
            else{
                if (Data.containersInField.get(storage.id*2) ==null){
                    Data.containersInField.set(storage.id*2, this.container);
                }else if (Data.containersInField.get(storage.id*2 +1) ==null){
                    Data.containersInField.set(storage.id*2+1, this.container);
                } else System.out.println("je kan geen 3 containers op elkaar plaatsen");
                this.container = null;
            }
        }
        public void dropOffInCrane(Crane crane){
        this.container = null;


        }
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
