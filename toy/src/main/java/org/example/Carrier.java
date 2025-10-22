package org.example;
import java.util.ArrayList;

/***
 * wanneer mogen roteren -> roteren mag pas vanaf we uit de rijen rijden
 * dus kijken naar laagst mogelijke beginblok van de storagesection
 * in toy data onderste punt van de storage section = 40
 * dit is ook in Constants.loweststorageY
 */


public class Carrier {
    public int id, craneID, x, y, width, height;
    Direction direction;
    public Container container;


    public Carrier(int id, int craneID, int x, int y) {
        this.id = id;
        this.craneID = craneID;
        this.x = x;
        this.y = y;
        this.width = 4;
        this.height = 8;
        this.direction = Direction.DOWN;
    }

    public void pickupContainerFromStorage(int containerid) {
        if (this.container == null) {
            for (Container c : Data.containersInField) {
                if (c.id == containerid) {
                    this.container = c;
                }
            }
            Data.containersInField.remove(this.container);
        } else System.out.println("you already have a container");
    }

    public void pickupContainerFromDispatch(DispatchSection dispatchSection) {
        if (this.container == null) {
            this.container = dispatchSection.getContainer();
        } else {
            System.out.println("you already have a container");
        }
    }

    public void setOffContainerAtDispatch(DispatchSection dispatchSection) {
        dispatchSection.setContainer(this.container);
    }

    public boolean checkforEqualDirection(Direction direction) {
        return this.direction == direction ||
                this.direction == Direction.UP && direction == Direction.DOWN ||
                this.direction == Direction.DOWN && direction == Direction.UP ||
                this.direction == Direction.LEFT && direction == Direction.RIGHT ||
                this.direction == Direction.RIGHT && direction == Direction.LEFT;
    }

    public void rotate(Direction direction, int time) {
        if (checkforEqualDirection(direction)) {
            System.out.println("No need to turn , equal direction");
        }
        if (Constants.rules.canRotate(this.x, this.y, this.direction)) {
            if (this.direction == Direction.UP || this.direction == Direction.DOWN) {

                this.x -= 2;
                this.y += 2;
            } else {
                this.x += 2;
                this.y -= 2;
            }
            this.direction = direction;
            Data.logs.add(new RotateLog(time, direction));
        }

    }
    private void driveSideWays(int time, int newX) {
        if (this.direction == Direction.UP || this.direction == Direction.DOWN) {
            System.out.println("you cannot drive sideways , direction = up/down");
            return;
        }

        // Map-bounds (laatste geldige x is mapWidth-1)
        if (newX < 0 || newX >= Data.mapWidth) {
            System.out.println("nieuwe X is buiten de map");
            return;
        }

        if (newX == this.x) {
            System.out.println("you already are at the final x dest");
            return;
        }

        int worldDx = newX - this.x;

        // Relatieve delta t.o.v. facing: positief = vooruit
        int dx = (this.direction == Direction.LEFT) ? -worldDx : worldDx;

        // Update positie en log
        this.x = newX;
        Data.logs.add(new MoveLog(time, dx));
    }
    private void driveVertical(int time, int newY) {
        // Kan niet verticaal rijden als richting horizontaal is
        if (this.direction == Direction.LEFT || this.direction == Direction.RIGHT) {
            System.out.println("you cannot drive vertically , direction = sideways");
            return;
        }

        // Controleer mapgrenzen (laatste geldige y is mapHeight-1)
        if (newY < 0 || newY >= Data.mapHeight) {
            System.out.println("nieuwe Y is buiten de map");
            return;
        }

        if (newY == this.y) {
            System.out.println("you already are at the final y dest");
            return;
        }

        // Absolute delta in wereldcoördinaten
        int worldDy = newY - this.y;

        // Relatieve delta t.o.v. facing: positief = vooruit
        int dy = (this.direction == Direction.UP) ? -worldDy : worldDy;

        // Update positie en log
        this.y = newY;
        Data.logs.add(new MoveLog(time, dy));
    }




    public void dropOffInStorage(Storage storage) {
        if (this.container == null) {
            System.out.println("you dont have a container");
            return;
        }
        if (storage == null) {
            System.out.println("you dont have a storage");
            return;
        }
        if (Data.containersInField.get(storage.id * 2) == null) {
            Data.containersInField.set(storage.id * 2, this.container);

        } else if (Data.containersInField.get(storage.id * 2 + 1) == null) {
            Data.containersInField.set(storage.id * 2 + 1, this.container);

        } else {System.out.println("no available storage"); return;}
        this.container = null;

    }

}


