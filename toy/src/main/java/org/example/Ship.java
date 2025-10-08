package org.example;

import java.util.ArrayList;
import java.util.List;

// Ship class
public class Ship {
    private int id;
    private List<Operation> operations;

    public Ship(int id) {
        this.id = id;
        this.operations = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void addOperation(Operation operation) {
        this.operations.add(operation);
    }

    public int getOperationCount() {
        return operations.size();
    }

    @Override
    public String toString() {
        return "Ship{id=" + id + ", operations=" + operations.size() + "}";
    }
}