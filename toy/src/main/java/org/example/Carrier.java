package org.example;
import java.util.ArrayList;

/***
 * Direction
 * direction up = 1, down = 3       -> oneven : omhoog/omlaag
 * direction Right = 2 , Left = 4   -> even   : rechts/links
 * beginnen met direction 3 -> Omlaag
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
        this.direction = 3;
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
        else if (Grid.tryRotate(time , x, y , direction)){
            System.out.println("Hier roteren kan niet ");
        }
        else if (this.direction % 2 == 0) { // rechts, links -> boven/beneden
            width = 4;
            height = 8;
            this.direction = dir;
            // doordat we verdraaien veranderd ons onderste punt ook
            this.x += 2;
            this.y -= 2;
            Grid.tryOccupyRectAt(time , this.x, this.y,direction );
            logs.add(new RotateLog(time, dir));


        } else { // boven, beneden -> rechts links
            width = 8;
            height = 4;
            this.direction = dir;
            // doordat we verdraaien veranderd ons onderste punt ook
            this.x -= 2;
            this.y += 2;
            Grid.tryOccupyRectAt(time , this.x, this.y,direction );
            logs.add(new RotateLog(time, dir));
        }
    }
    public int driveTo(int desX, int desY, int t, boolean verticalDestination) {
        // Step 1: Move in current direction until aligned for rotation
        while (!canRotateHere(t)) {
            Grid.printCombinedSlice(t);
            if (direction == 1 || direction == 3) { // Up/Down
                int dy = (desY > y) ? 1 : -1;
                if (Grid.testForCarrier(t+1, x, y+dy, direction)) {
                    y += dy;
                    t++;
                    Grid.occupyCarrier(t, x, y, direction);
                    logs.add(new moveLog(t, dy));

                } else {
                    // Blocked, try alternative or break
                    t++;
                }
            } else { // Right/Left
                int dx = (desX > x) ? 1 : -1;
                if (Grid.testForCarrier(t+1, x+dx, y, direction)) {
                    x += dx;
                    t++;
                    Grid.occupyCarrier(t, x, y, direction);
                    logs.add(new moveLog(t, dx));
                } else {
                    // Blocked, try alternative or break
                    t++;
                }
            }
        }


        // Step 2: Rotate to align with destination axis
        int targetDirection = verticalDestination ? 1 : 2; // 1=Up, 2=Right (adjust as needed)
        if (direction != targetDirection && Grid.tryRotate(t, x, y, targetDirection)) {
            rotate(targetDirection, t);
            t++;
        }

        // Step 3: Move along new direction to destination
        while ((verticalDestination && (y != desY) ) || (!verticalDestination && (x != desX) )) {
            if (verticalDestination) {
                int dy = (desY > y) ? 1 : -1;
                if (Grid.tryOccupyRectAt(t, x, y + dy, direction)) {
                    y += dy;
                    logs.add(new moveLog(t, dy));
                    t++;
                } else {
                    break;
                }
            } else {
                int dx = (desX > x) ? 1 : -1;
                if (Grid.tryOccupyRectAt(t, x + dx, y, direction)) {
                    x += dx;
                    logs.add(new moveLog(t, dx));
                    t++;
                } else {
                    break;
                }
            }
        }

        // Step 4: Final rotation if needed
        if ((verticalDestination && direction != 1) || (!verticalDestination && direction != 2)) {
            if (Grid.tryRotate(t, x, y, verticalDestination ? 1 : 2)) {
                rotate(verticalDestination ? 1 : 2, t);
                t++;
            }
        }

        return t;
    }

    // Helper to check if rotation is allowed at current position
    private boolean canRotateHere(int t) {
        // Implement your rule, e.g. y >= Constants.loweststorageY
        return Grid.tryRotate(t, x, y, direction % 2 == 0 ? 1 : 2);
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
