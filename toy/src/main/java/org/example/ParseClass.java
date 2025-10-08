package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ParseClass  {
    public static void Parse(BufferedReader br) throws IOException {
        String line;
        String section = "";
        int mapWidth = 0, mapHeight = 0;

        List<Crane> cranes = new ArrayList<>();
        List<Storage> storage = new ArrayList<>();
        List<Carrier> carriers = new ArrayList<>();
        List<ContainerSection> containers = new ArrayList<>();
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
                            // Parse discharge sections
                            ArrayList<DispatchSection> dispatchSections = new ArrayList<>();
                            for (int i = 6; i < 6 + numbers.get(5) * 3; i += 3) {
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
                            cranes.add(new Crane(numbers.get(0), numbers.get(1), numbers.get(2), numbers.get(3), numbers.get(4), numbers.get(5), dispatchSections));
                        }
                    }
                    case "storage" -> {
                        Scanner scanner = new Scanner(line.split("%")[0]);

                        ArrayList<Integer> numbers = new ArrayList<>();

                        // Read all integers from the line
                        while (scanner.hasNextInt()) {
                            numbers.add(scanner.nextInt());
                        }

                        // Storage data line: id, bottom_left_coord (x, y)
                        if (numbers.size() >= 3) {
                            int id = numbers.get(0);
                            int x = numbers.get(1);
                            int y = numbers.get(2);
                            storage.add(new Storage(id, x, y));
                        }
                    }
                    case "carrier" -> {
                        Scanner scanner = new Scanner(line.split("%")[0]);
                        ArrayList<Integer> numbers = new ArrayList<>();

                        // Read all integers from the line
                        while (scanner.hasNextInt()) {
                            numbers.add(scanner.nextInt());
                        }

                        // Carrier data line: id, crane_assignment, bottom_left_coord (x, y)
                        if (numbers.size() >= 4) {
                            int id = numbers.get(0);
                            int craneID = numbers.get(1);
                            int x = numbers.get(2);
                            int y = numbers.get(3);
                            carriers.add(new Carrier(id, craneID, x, y));
                        }
                    }
                    case "container" -> {
                        Scanner scanner = new Scanner(line.split("%")[0]);
                        ArrayList<Integer> numbers = new ArrayList<>();
                        while (scanner.hasNextInt()) {
                            numbers.add(scanner.nextInt());
                        }
                        if (numbers.size() >= 2) {
                            int id = numbers.get(0);
                            int storageID = numbers.get(1);
                            containers.add(new ContainerSection(id, storageID));
                        }}
                    case "demand" -> demand.add(line);
                    default -> {
                        // Ignore lines that belong to an unknown section
                    }
                }
            }
        }
    }
}

