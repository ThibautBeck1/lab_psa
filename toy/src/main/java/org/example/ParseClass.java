package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class ParseClass {
    public static void Parse(BufferedReader br) throws IOException {
        String line;
        String section = "";
        int mapWidth = 0, mapHeight = 0;

        List<Crane> cranes = new ArrayList<>();
        List<Storage> storage = new ArrayList<>();
        List<Carrier> carriers = new ArrayList<>();
        List<Container> containers = null; // wordt gezet bij "container section"
        List<Demand> demands = new ArrayList<>();
        Queue<Integer> expectedShipIds = new LinkedList<>();


        int numberOfCranes = 0;
        boolean readingCraneData = false;

        // Demand section state variables
        int totalNewContainers = 0;
        Demand currentDemand = null;
        Ship currentShip = null;
        int expectedOperations = 0;
        int operationsRead = 0;
        String[] parts = br.readLine().split("\\s+");
        mapWidth = Integer.parseInt(parts[0]);
        mapHeight = Integer.parseInt(parts[1]);
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("%")) {
                continue;
            }
            if (line.startsWith("crane section")) {
                section = "crane";
                readingCraneData = false;
            } else if (line.startsWith("storage section")) {
                section = "storage";
            } else if (line.startsWith("carrier section")) {
                section = "carrier";
            } else if (line.startsWith("container section")) {
                section = "container";
                int n = storage.size() * 2;        // two slots per storage place
                containers = new ArrayList<>(n);
                for (int i = 0; i < n; i++) containers.add(null); // <-- important
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
                            int craneId = numbers.get(0);
                            Crane crane = new Crane(
                                    craneId, numbers.get(1), numbers.get(2),
                                    numbers.get(3), numbers.get(4), numbers.get(5),
                                    dispatchSections
                            );
                            cranes.add(crane);
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
                            int storageId = numbers.get(0);
                            Storage storageObj = new Storage(
                                    storageId, numbers.get(1), numbers.get(2)
                            );
                            storage.add(storageObj);
                            if (numbers.get(2) < Constants.lowestStorageY) { Constants.lowestStorageY = numbers.get(2); }
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
                            int carrierId = numbers.get(0);
                            Carrier carrier = new Carrier(
                                    carrierId, numbers.get(1),
                                    numbers.get(2), numbers.get(3)
                            );
                            carriers.add( carrier);
                        }
                        scanner.close();
                    }

                    case "container" -> {
                        /**
                         *
                         * Hoe opslag containers
                         * dus we hebben plaatsen waar 2 containers kunnen staan
                         * array beste plaatsbesparing -> [ plaats 0 :cont 1 , plaats 0 :cont 2,  plaats 1 :cont 1 , plaats 1 :cont 2 , ...]
                         * -> 0,1 voor 0 ;2,3 voor 1; 4.5 voor 2 ; 6,7 voor 3
                         * container plaats *2 is de eerste container plaats *2+1 voor 2 de container (zoals een binaire complete boom
                         *
                         */

                        Scanner scanner = new Scanner(line.split("%")[0]);
                        ArrayList<Integer> numbers = new ArrayList<>();
                        while (scanner.hasNextInt()) {
                            numbers.add(scanner.nextInt());
                        }
                        if (numbers.size() >= 2) {
                            int containerId = numbers.get(0);
                            int locationId = numbers.get(1);
                            Container container = new Container(
                                    containerId, locationId
                            );
                            if (containers.get(locationId*2)  == null)
                                containers.set(locationId*2,container);
                            else containers.set(locationId*2 +1,container);
                        }
                        scanner.close();
                    }

                    case "demand" -> {
                        if (line.startsWith("demand crane")) {
                            parts = line.split("\\s+");
                            int craneId = Integer.parseInt(parts[2]);
                            currentDemand = new Demand(craneId);
                            demands.add( currentDemand);
                            expectedShipIds.clear(); // A new crane demand resets the ship queue
                        } else if (line.startsWith("ship")) {
                           parts = line.split("\\s+");
                            int shipId = Integer.parseInt(parts[1]);

                            // Optional but recommended: Validate this ship was expected
                            if (!expectedShipIds.isEmpty() && expectedShipIds.peek() == shipId) {
                                expectedShipIds.poll(); // Acknowledge and remove from queue
                            }

                            currentShip = new Ship(shipId);
                            if (currentDemand != null) {
                                currentDemand.addShip(currentShip);
                            }
                            operationsRead = 0;
                            expectedOperations = 0;
                        } else if (line.startsWith("unload")) {
                            parts = line.split("\\s+");
                            int dischargeId = Integer.parseInt(parts[1]);
                            int containerId = Integer.parseInt(parts[2]);
                            int storageId = Integer.parseInt(parts[3]);
                            UnloadOperation op = new UnloadOperation(dischargeId, containerId, storageId);
                            if (currentShip != null) {
                                currentShip.addOperation(op);
                                operationsRead++;
                            }
                        } else if (line.startsWith("load")) {
                            parts = line.split("\\s+");
                            int dischargeId = Integer.parseInt(parts[1]);
                            int containerId = Integer.parseInt(parts[2]);
                            LoadOperation op = new LoadOperation(dischargeId, containerId);
                            if (currentShip != null) {
                                currentShip.addOperation(op);
                                operationsRead++;
                            }
                        } else {
                            // This block handles numeric lines: total containers, ship manifests, and operation counts
                            Scanner scanner = new Scanner(line.split("%")[0]);
                            if (scanner.hasNextInt()) {
                                int value = scanner.nextInt();
                                if (totalNewContainers == 0 && demands.isEmpty()) {
                                    // First number in the section is the total new containers
                                    totalNewContainers = value;
                                } else if (currentDemand != null && expectedShipIds.isEmpty()) {
                                    // This is the ship manifest line (e.g., "2 0 1")
                                    // 'value' holds the count (2), and the scanner has the IDs (0, 1)
                                    while (scanner.hasNextInt()) {
                                        expectedShipIds.add(scanner.nextInt());
                                    }
                                } else if (currentShip != null && expectedOperations == 0) {
                                    // This is the number of operations for the current ship
                                    expectedOperations = value;
                                }
                            }
                            scanner.close();
                        }
                    }
                }
            }
        }

        // Create and return parsed data

        Data.mapWidth = mapWidth;
        Data.mapHeight = mapHeight;
        Data.cranes.addAll(cranes);
        Data.storage.addAll(storage);
        Data.carriers.addAll(carriers);
        if (containers != null) Data.containers.addAll(containers);
        Data.demands.addAll(demands);
        Data.totalNewContainers = totalNewContainers;
    }
}
