package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main {

    static void main() throws IOException {
        // Parse the input file
        Data data;
        try (InputStream is = Main.class.getResourceAsStream("/toy.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            ParseClass.Parse(br);
        }

        // Print parsed data for verification
        System.out.println("Parsed successfully!");
        System.out.println("Map: " + Data.mapWidth + "x" + Data.mapHeight);
        System.out.println("Cranes: " + Data.cranes.size());
        System.out.println("Carriers: " + Data.carriers.size());
        System.out.println("Containers: " + Data.containers.size());

        // Display demands
        for (Demand demand : Data.demands) {
            System.out.println("\nDemand for Crane " + demand.getCraneId());
            for (Ship ship : demand.getShips()) {
                System.out.println("  Ship " + ship.getId() + " operations:");
                for (Operation op : ship.getOperations()) {
                    System.out.println("    " + op);
                }
            }
        }
        ///  smart saving method for the containers = see parse class for explination
        System.out.println("------------------------");
        System.out.println("print out all the containers");
        System.out.println("------------------------");

       for (Container container : Data.containers) {
           System.out.println(container);
       }

       System.out.println("lowest storage y " +  Constants.lowestStorageY);



    }
}


