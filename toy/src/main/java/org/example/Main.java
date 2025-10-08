package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        try (InputStream is = Main.class.getResourceAsStream("/toy.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String line;
            String section = "";
            int mapWidth = 0, mapHeight = 0;

            List<String> cranes = new ArrayList<>();
            List<String> storage = new ArrayList<>();
            List<String> carriers = new ArrayList<>();
            List<String> containers = new ArrayList<>();
            List<String> demand = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("%")) continue;

                if (line.contains("size of the map")) {
                    String[] parts = line.split(" ");
                    mapWidth = Integer.parseInt(parts[0]);
                    mapHeight = Integer.parseInt(parts[1]);
                    section = "";
                } else if (line.startsWith("crane section")) {
                    section = "crane";
                } else if (line.startsWith("storage section")) {
                    section = "storage";
                } else if (line.startsWith("carrier section")) {
                    section = "carrier";
                } else if (line.startsWith("container section")) {
                    section = "container";
                } else if (line.startsWith("demand section")) {
                    section = "demand";
                } else {
                    switch (section) {
                        case "crane" -> cranes.add(line);
                        case "storage" -> storage.add(line);
                        case "carrier" -> carriers.add(line);
                        case "container" -> containers.add(line);
                        case "demand" -> demand.add(line);
                    }
                }
            }

            // ✅ Print all parsed sections clearly
            System.out.println("=== MAP ===");
            System.out.println("Width: " + mapWidth + ", Height: " + mapHeight);

            System.out.println("\n=== CRANES ===");
            cranes.forEach(System.out::println);

            System.out.println("\n=== STORAGE ===");
            storage.forEach(System.out::println);

            System.out.println("\n=== CARRIERS ===");
            carriers.forEach(System.out::println);

            System.out.println("\n=== CONTAINERS ===");
            containers.forEach(System.out::println);

            System.out.println("\n=== DEMAND ===");
            demand.forEach(System.out::println);
        }
    }
}
