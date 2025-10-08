package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) throws IOException {
        try (InputStream is = Main.class.getResourceAsStream("/toy.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String line;
            String section = "";
            int mapWidth = 0, mapHeight = 0;

            List<Crane> cranes = new ArrayList<>();
            List<String> storage = new ArrayList<>();
            List<String> carriers = new ArrayList<>();
            List<String> containers = new ArrayList<>();
            List<String> demand = new ArrayList<>();
            int numberOfCranes = 0;
            boolean readingCraneData = false;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("%")) {
                    continue;
                }

                // Header lines that set the current section
                if (line.contains("size of the map")) {
                    String[] parts = line.split("\\s+");
                    mapWidth = Integer.parseInt(parts[0]);
                    mapHeight = Integer.parseInt(parts[1]);
                    section = "";
                } else if (line.startsWith("crane section")) {
                    section = "crane";
                    readingCraneData = false; // Reset for crane section
                } else if (line.startsWith("storage section")) {
                    section = "storage";
                } else if (line.startsWith("carrier section")) {
                    section = "carrier";
                } else if (line.startsWith("container section")) {
                    section = "container";
                } else if (line.startsWith("demand section")) {
                    section = "demand";
                } else {
                    // Data lines
                    switch (section) {
                        case "crane" -> {
                            Scanner scanner = new Scanner(line.split("%")[0]);
                            ArrayList<Integer> numbers = new ArrayList<>();

                            // Read all integers from the line
                            while (scanner.hasNextInt()) {
                                numbers.add(scanner.nextInt());
                            }

                            if (!readingCraneData) {
                                // First line in crane section: number of cranes
                                numberOfCranes = numbers.get(0);
                                readingCraneData = true;
                            } else {
                                // Crane data line: id, bottom_left_coord, top_right_coord, #discharge, [discharge_id, bottom_left_coord]
                                int id = numbers.get(0);
                                int x1 = numbers.get(1);
                                int y1 = numbers.get(2);
                                int x2 = numbers.get(3);
                                int y2 = numbers.get(4);
                                int numDischarges = numbers.get(5);

                                // Parse discharge sections
                                ArrayList<DispatchSection> dispatchSections = new ArrayList<>();
                                for (int i = 6; i < 6 + numDischarges * 3; i += 3) {
                                    if (i + 2 < numbers.size()) {
                                        dispatchSections.add(
                                                new DispatchSection(
                                                        numbers.get(i),
                                                        numbers.get(i + 1),
                                                        numbers.get(i + 2)
                                                )
                                        );
                                    }
                                }

                                // Add crane to the list
                                cranes.add(new Crane(id, x1, y1, x2, y2, numDischarges, dispatchSections));
                            }
                        }
                        case "storage" -> storage.add(line);
                        case "carrier" -> carriers.add(line);
                        case "container" -> containers.add(line);
                        case "demand" -> demand.add(line);
                        default -> {
                            // Ignore lines that belong to an unknown section
                        }
                    }
                }
            }

            // Print everything nicely
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
