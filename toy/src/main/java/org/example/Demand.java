package org.example;

import java.util.ArrayList;
import java.util.List;

public class Demand {
    private int craneId;
    private List<Ship> ships;

    public Demand(int craneId) {
        this.craneId = craneId;
        this.ships = new ArrayList<>();
    }

    public int getCraneId() {
        return craneId;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void addShip(Ship ship) {
        this.ships.add(ship);
    }

    public int getTotalOperations() {
        return ships.stream()
                .mapToInt(Ship::getOperationCount)
                .sum();
    }

    @Override
    public String toString() {
        return "Demand{crane=" + craneId + ", ships=" + ships.size() +
                ", totalOps=" + getTotalOperations() + "}";
    }
}