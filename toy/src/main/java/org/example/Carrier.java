package org.example;

import java.util.ArrayList;
import java.util.List;

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
    public List<Log> logs ;

    public Carrier(int id, int craneID, int x, int y) {
        this.id = id;
        this.craneID = craneID;
        this.x = x;
        this.y = y;
        this.width = 4;
        this.height = 8;
        this.direction = Direction.down;
        logs = new ArrayList<>();
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
                this.direction == Direction.up && direction == Direction.down ||
                this.direction == Direction.down && direction == Direction.up ||
                this.direction == Direction.left && direction == Direction.right ||
                this.direction == Direction.right && direction == Direction.left;
    }

    public void rotate(Direction direction, int time) {
        if (checkforEqualDirection(direction)) {
            System.out.println("No need to turn , equal direction");
        }
        if (Constants.rules.canRotate(this.x, this.y, this.direction)) {
            if (this.direction == Direction.up || this.direction == Direction.down) {

                this.x -= 2;
                this.y += 2;
            } else {
                this.x += 2;
                this.y -= 2;
            }
            this.direction = direction;
            this.logs.add(new RotateLog(time, direction));
            new RotateLog(time, direction).printout();
        }

    }
    private int driveSideWays(int time, int newX) {
        if (this.direction == Direction.up || this.direction == Direction.down) {
            System.out.println("you cannot drive sideways , direction = up/down");
            return -1;
        }

        // Map-bounds (laatste geldige x is mapWidth-1)
        if (newX < 0 || newX >= Data.mapWidth) {
            System.out.println("nieuwe X is buiten de map");
            return -1;
        }

        if (newX == this.x) {
            System.out.println("you already are at the final x dest");
            return -1;
        }

        int worldDx = newX - this.x;

        // Relatieve delta t.o.v. facing: positief = vooruit
        int dx = (this.direction == Direction.left) ? -worldDx : worldDx;

        // Update positie en log
        this.x = newX;
        this.logs.add(new MoveLog(time, dx));
        new MoveLog(time, dx).printout();

        return time + Math.abs(dx);
    }
    public int driveTO(int time, int newX, int newY, boolean destVertical) {
        if (this.x == newX && this.y == newY) {
            return time;
        }

        if (destVertical) {
            // Going to storage: horizontal → vertical rotation
            // Rotation does: x += 2, y -= 2
            time = driveSideWays(time, newX - 2);
            rotate(Direction.up, time);
            time++;
            time = driveVertical(time, newY);
        } else {
            // Going to dispatch: vertical → horizontal rotation
            // Rotation does: x -= 2, y += 2
            time = driveVertical(time, newY - 2);
            rotate(Direction.right, time);
            time++;
            time = driveSideWays(time, newX);
        }
        return time;
    }
    private int driveVertical(int time, int newY) {
        // Kan niet verticaal rijden als richting horizontaal is
        if (this.direction == Direction.left || this.direction == Direction.right) {
            System.out.println("you cannot drive vertically , direction = sideways");
            return -1;
        }

        // Controleer mapgrenzen (laatste geldige y is mapHeight-1)
        if (newY < 0 || newY >= Data.mapHeight) {
            System.out.println("nieuwe Y is buiten de map");
            return -1;
        }

        if (newY == this.y) {
            System.out.println("you already are at the final y dest");
            return -1;
        }

        // Absolute delta in wereldcoördinaten
        int worldDy = newY - this.y;

        // Relatieve delta t.o.v. facing: positief = vooruit
        int dy = (this.direction == Direction.up) ? worldDy : -worldDy;

        // Update positie en log
        this.y = newY;
        this.logs.add(new MoveLog(time, dy));
        new MoveLog(time, dy).printout();
        return time + Math.abs(dy);
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


