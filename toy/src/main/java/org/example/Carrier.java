package org.example;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayDeque;
import java.util.Deque;

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
    public List<Log> logs;

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
                if (c != null && c.id == containerid) {  // ⚠️ Voeg c != null toe
                    this.container = c;
                    break;  // Stop de loop als je de container gevonden hebt
                }
            }
            if (this.container != null) {
                Data.containersInField.remove(this.container);
            } else {
                System.out.println("Container " + containerid + " not found in field");
            }
        } else {
            System.out.println("you already have a container");
        }
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
        this.container = null;
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

        } else {
            System.out.println("no available storage");
            return;
        }
        this.container = null;

    }

    // in de class Carrier (extra velden)
    private final Deque<PathEnvelope> planned = new ArrayDeque<>();

    // aggregatie van moves (1 log per segment)
    private Direction pendingDir = null;
    private int pendingCount = 0;
    private int pendingStartTime = 0;

    // helper om een extern berekend pad te plannen
    public void plan(Deque<PathEnvelope> q) {
        planned.addAll(q);
    }

    // flush de geaccumuleerde move als 1 MoveLog
    public void flushPendingMoves(int time) {
        if (pendingCount != 0) {
            logs.add(new MoveLog(pendingStartTime, pendingCount)); // keep sign
            pendingCount = 0;
            pendingDir = null;
        }
    }

    public boolean tick(int time) {
        if (planned.isEmpty()) return false;

        PathEnvelope step = planned.removeFirst();

        // delta of this single step
        int dx = step.getOnderHoek().x - this.x;
        int dy = step.getOnderHoek().y - this.y;

        // rotation frame? (direction changes without a single-grid move)
        boolean directionChanged = (this.direction != step.getDirection());
        boolean notSingleMove = (Math.abs(dx) + Math.abs(dy) != 1);
        if (directionChanged && notSingleMove) {
            flushPendingMoves(time);
            this.x = step.getOnderHoek().x;
            this.y = step.getOnderHoek().y;
            this.direction = step.getDirection();
            logs.add(new RotateLog(time, this.direction));
            return true;
        }

        // signed step in the RIGHT/DOWN frame:
        // RIGHT axis:  dx>0 => +1 (right), dx<0 => -1 (left)
        // DOWN axis:   y smaller is "down", so dy<0 => +1 (down), dy>0 => -1 (up)
        int signedStep;
        if (step.getDirection() == Direction.right) {
            signedStep = (dx > 0) ? +1 : -1;
        } else { // Direction.down
            signedStep = (dy < 0) ? +1 : -1;
        }

        // aggregate by logical axis; sign encodes left/up vs right/down
        if (pendingDir == null) {
            pendingDir = step.getDirection();
            pendingStartTime = time;
            pendingCount = signedStep;       // ⬅ use signed value, not 1
        } else if (pendingDir == step.getDirection()) {
            pendingCount += signedStep;      // ⬅ add signed step
        } else {
            flushPendingMoves(time);
            pendingDir = step.getDirection();
            pendingStartTime = time;
            pendingCount = signedStep;       // ⬅ use signed value
        }

        // advance pose
        this.x = step.getOnderHoek().x;
        this.y = step.getOnderHoek().y;
        this.direction = step.getDirection();
        return true;
    }
}