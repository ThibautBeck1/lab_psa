package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main {
    static void main() throws IOException {
        // Parse the input file
        ParsedData data;
        try (InputStream is = Main.class.getResourceAsStream("/toy.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            data = ParseClass.Parse(br);
        }

        // Print parsed data for verification
        System.out.println("Parsed successfully!");
        System.out.println("Map: " + data.mapWidth + "x" + data.mapHeight);
        System.out.println("Cranes: " + data.cranes.size());
        System.out.println("Carriers: " + data.carriers.size());
        System.out.println("Containers: " + data.containers.size());

        // Display demands
        for (Demand demand : data.demands) {
            System.out.println("\nDemand for Crane " + demand.getCraneId());
            for (Ship ship : demand.getShips()) {
                System.out.println("  Ship " + ship.getId() + " operations:");
                for (Operation op : ship.getOperations()) {
                    System.out.println("    " + op);
                }
            }
        }
    }
}
