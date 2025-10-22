package org.example;

import java.util.ArrayList;
import java.util.List;

public final class Data {


    public static int mapWidth;
    public static int mapHeight;

    public static List<Crane> cranes = new ArrayList<>();
    public static List<Storage> storage = new ArrayList<>();
    public static List<Carrier> carriers = new ArrayList<>();
    public static List<Container> containersInField = new ArrayList<>();
    public static List<Demand> demands = new ArrayList<>();
    public static int totalNewContainers;
    public static List<Log> logs = new ArrayList<>();
    /** Handig als je opnieuw wilt parsen. */
    public static void reset() {
        mapWidth = 0; mapHeight = 0; totalNewContainers = 0;
        cranes.clear(); storage.clear(); carriers.clear();
        containersInField.clear(); demands.clear();
    }

    public static String summary() {
        return String.format(
                "Map: %dx%d, Cranes: %d, Storage: %d, Carriers: %d, " +
                        "Containers: %d, Demands: %d, New Containers: %d",
                mapWidth, mapHeight,
                cranes.size(), storage.size(), carriers.size(),
                containersInField.size(), demands.size(), totalNewContainers
        );
    }
}
