package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class ParseClass  {
    public static ParsedData Parse(BufferedReader br) throws IOException {
        String line;
        String section = "";
        int mapWidth = 0, mapHeight = 0;

        List<Crane> cranes = new ArrayList<>();
        List<Storage> storage = new ArrayList<>();
        List<Carrier> carriers = new ArrayList<>();
        List<Container> containers = new ArrayList<>();
        List<Demand> demands = new ArrayList<>();

        int numberOfCranes = 0;
        boolean readingCraneData = false;

        // Demand section state variables
        int totalNewContainers = 0;
        Demand currentDemand = null;
        Ship currentShip = null;
        int expectedOperations = 0;
        int operationsRead = 0;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("%")) {
                continue;
            }

            // Header lines
            if (line.contains("size of the map")) {
                String[] parts = line.split("\\s+");
                mapWidth = Integer.parseInt(parts[0]);
                mapHeight = Integer.parseInt(parts[1]);
                section = "";
            } else if (line.startsWith("crane section")) {
                section = "crane";
                readingCraneData = false;
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
                        while (scanner.hasNextInt()) {
                            numbers.add(scanner.nextInt());
                        }

                        if (!readingCraneData) {
                            numberOfCranes = numbers.get(0);
                            readingCraneData = true;
                        } else {
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
                            cranes.add(new Crane(
                                    numbers.get(0), numbers.get(1), numbers.get(2),
                                    numbers.get(3), numbers.get(4), numbers.get(5),
                                    dispatchSections
                            ));
                        }
                        scanner.close();
                    }

                    case "storage" -> {
                        Scanner scanner = new Scanner(line.split("%")[0]);
                        ArrayList<Integer> numbers = new ArrayList<>();
                        while (scanner.hasNextInt()) {
                            numbers.add(scanner.nextInt());
                        }
                        if (numbers.size() >= 3) {
                            storage.add(new Storage(
                                    numbers.get(0), numbers.get(1), numbers.get(2)
                            ));
                        }
                        scanner.close();
                    }

                    case "carrier" -> {
                        Scanner scanner = new Scanner(line.split("%")[0]);
                        ArrayList<Integer> numbers = new ArrayList<>();
                        while (scanner.hasNextInt()) {
                            numbers.add(scanner.nextInt());
                        }
                        if (numbers.size() >= 4) {
                            carriers.add(new Carrier(
                                    numbers.get(0), numbers.get(1),
                                    numbers.get(2), numbers.get(3)
                            ));
                        }
                        scanner.close();
                    }

                    case "container" -> {
                        Scanner scanner = new Scanner(line.split("%")[0]);
                        ArrayList<Integer> numbers = new ArrayList<>();
                        while (scanner.hasNextInt()) {
                            numbers.add(scanner.nextInt());
                        }
                        if (numbers.size() >= 2) {
                            containers.add(new Container(
                                    numbers.get(0), numbers.get(1)
                            ));
                        }
                        scanner.close();
                    }

                    case "demand" -> {
                        if (line.startsWith("demand crane")) {
                            // Parse "demand crane 0"
                            String[] parts = line.split("\\s+");
                            int craneId = Integer.parseInt(parts[2]);
                            currentDemand = new Demand(craneId);
                            demands.add(currentDemand);
                        } else if (line.startsWith("ship")) {
                            // Parse "ship 0"
                            String[] parts = line.split("\\s+");
                            int shipId = Integer.parseInt(parts[1]);
                            currentShip = new Ship(shipId);
                            if (currentDemand != null) {
                                currentDemand.addShip(currentShip);
                            }
                            operationsRead = 0;
                        } else if (line.startsWith("unload")) {
                            // Parse "unload 0 3 0"
                            String[] parts = line.split("\\s+");
                            int dischargeId = Integer.parseInt(parts[1]);
                            int containerId = Integer.parseInt(parts[2]);
                            int storageId = Integer.parseInt(parts[3]);
                            UnloadOperation op = new UnloadOperation(
                                    dischargeId, containerId, storageId
                            );
                            if (currentShip != null) {
                                currentShip.addOperation(op);
                                operationsRead++;
                            }
                        } else if (line.startsWith("load")) {
                            // Parse "load 0 2"
                            String[] parts = line.split("\\s+");
                            int dischargeId = Integer.parseInt(parts[1]);
                            int containerId = Integer.parseInt(parts[2]);
                            LoadOperation op = new LoadOperation(
                                    dischargeId, containerId
                            );
                            if (currentShip != null) {
                                currentShip.addOperation(op);
                                operationsRead++;
                            }
                        } else {
                            // Parse number of containers or operations
                            Scanner scanner = new Scanner(line.split("%")[0]);
                            if (scanner.hasNextInt()) {
                                int value = scanner.nextInt();
                                if (totalNewContainers == 0 && currentDemand == null) {
                                    totalNewContainers = value;
                                } else if (currentShip != null && expectedOperations == 0) {
                                    expectedOperations = value;
                                } else {
                                    // Ship count line: "1 0" means 1 ship with id 0
                                    ArrayList<Integer> shipIds = new ArrayList<>();
                                    shipIds.add(value);
                                    while (scanner.hasNextInt()) {
                                        shipIds.add(scanner.nextInt());
                                    }
                                }
                            }
                            scanner.close();
                        }
                    }
                }
            }
        }

        // Create and return parsed data
        ParsedData data = new ParsedData();
        data.mapWidth = mapWidth;
        data.mapHeight = mapHeight;
        data.cranes = cranes;
        data.storage = storage;
        data.carriers = carriers;
        data.containers = containers;
        data.demands = demands;
        data.totalNewContainers = totalNewContainers;

        return data;
    }

}

