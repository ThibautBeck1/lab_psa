package org.example;

import java.util.ArrayList;
import java.util.List;

public class ParsedData {
    public int mapWidth;
    public int mapHeight;
    public List<Crane> cranes;
    public List<Storage> storage;
    public List<Carrier> carriers;
    public List<Container> containers;
    public List<Demand> demands;
    public int totalNewContainers;

    public ParsedData() {
        this.cranes = new ArrayList<>();
        this.storage = new ArrayList<>();
        this.carriers = new ArrayList<>();
        this.containers = new ArrayList<>();
        this.demands = new ArrayList<>();
    }

    @Override
    public String toString() {
        return String.format(
                "Map: %dx%d, Cranes: %d, Storage: %d, Carriers: %d, " +
                        "Containers: %d, Demands: %d, New Containers: %d",
                mapWidth, mapHeight, cranes.size(), storage.size(),
                carriers.size(), containers.size(), demands.size(),
                totalNewContainers
        );
    }
}
