package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;


public class Main {

    static void main() throws IOException {
        // Parse the input file

        try (InputStream is = Main.class.getResourceAsStream("/toy2.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            ParseClass.Parse(br);
        }
        Constants.rules = new Rules();
        ///  smart saving method for the containers = see parse class for explination
        System.out.println("------------------------");
        System.out.println("print out all the containers");
        System.out.println("------------------------");

        for (Container container : Data.containersInField) {
            System.out.println(container);
        }

        System.out.println("lowest storage y " +  Constants.lowestStorageY);


        System.out.println("Map: " + Data.mapWidth + "x" + Data.mapHeight);
        System.out.println("Cranes: " + Data.cranes.size());
        System.out.println("Carriers: " + Data.carriers.size());
        System.out.println("storage size " +   Data.storage.size());
        System.out.println("Containerplaatsen " + Data.containersInField.size());



        System.out.println("------------------");



        // de carriers toevoegen aan hun crane
        for (Crane c: Data.cranes) {
            for (Carrier ca : Data.carriers) {
                if (ca != null && ca.craneID == c.craneSectionID)
                    c.availableCarriers.add(ca);
            }
         }


        // demands and solution
        for (Storage s: Data.storage) {
            System.out.println("storage " + s);
        }
        int time = 0;
        boolean firstLaod = true;
        for (Demand demand : Data.demands) {
            System.out.println("\nDemand for Crane " + demand.getCraneId());
            Crane crane = Data.cranes.get(demand.getCraneId());
            System.out.println(crane);

            for (Ship ship : demand.getShips()) {
                System.out.println("  Ship " + ship.getId() + " operations:");
                Operation previousOp = null;

                for (Operation op : ship.getOperations()) {
                    System.out.println("    " + op);

                    if (op instanceof UnloadOperation) {
                        Storage storage = Data.storage.get(((UnloadOperation) op).getStorageId());

                        // take carrier out of list of available carriers for the crane
                        Carrier carrier = crane.availableCarriers.removeFirst();

                        DispatchSection d = crane.getDispatchSections(op.getDischargeId());

                        // drive to crane
                        time = carrier.driveTO(time, d.getX() - 2, d.getY() - 1, false);

                        // pickup
                        carrier.pickupContainerFromDispatch(d);
                        carrier.logs.add(new LoadLog(time));
                        time++;

                        // drive to storage
                        time = carrier.driveTO(time, storage.x -1, storage.y - 2, true);

                        // drop off
                        carrier.dropOffInStorage(storage);
                        carrier.logs.add(new UnLoadLog(time));
                        time++;

                        // go in a list of available carriers
                        crane.availableCarriers.add(carrier);

                    } else if (op instanceof LoadOperation) {
                    LoadOperation loadOp = (LoadOperation) op;

                    // Find the container
                    Container container = null;
                    for (Container c : Data.containersInField) {
                        if (c != null && c.id == loadOp.getContainerId()) {
                            container = c;
                            System.out.println("Container found in field: " + c);
                            break;
                        }
                    }

                    if (container == null) {
                        System.out.println("Container not found!");
                        continue;
                    }

                    // Get storage location BEFORE removing from field
                    int targetX = container.getX();
                    int targetY = container.getY();
                    Storage storage = container.storage;

                    // take carrier out of list of available carriers
                    Carrier carrier = crane.availableCarriers.removeFirst();
                    DispatchSection d = crane.getDispatchSections(op.getDischargeId());

                    // Check if we need U-shape movement
                    boolean needsUShape = false;
                    if (previousOp instanceof UnloadOperation) {
                        Storage prevStorage = Data.storage.get(((UnloadOperation) previousOp).getStorageId());
                        // Check if storage is in different column
                        if (prevStorage.x != storage.x) {
                            needsUShape = true;
                        }
                    }

                    if (needsUShape) {
                        // U-shape: down -> sideways -> up
                        time = carrier.driveVertical(time, Constants.lowestStorageY);
                        carrier.rotate(Direction.right, time);
                        time++;
                        time = carrier.driveSideWays(time, targetX + 1);
                        carrier.rotate(Direction.up, time);
                        time++;
                        time = carrier.driveVertical(time, targetY - 2);
                    } else {
                        // Same column or first load: use driveTO
                        time = carrier.driveTO(time, targetX -1, targetY - 2, true);
                    }

                    // pickup from storage
                    carrier.pickupContainerFromStorage(loadOp.getContainerId());
                    carrier.logs.add(new LoadLog(time));
                    time++;

                    // drive to crane (dispatch section)
                    time = carrier.driveTO(time, d.getX() - 2, d.getY() - 1, false);

                    // drop off at dispatch
                    carrier.setOffContainerAtDispatch(d);

                    carrier.logs.add(new UnLoadLog(time));
                    time++;

                    // go in a list of available carriers
                    crane.availableCarriers.add(carrier);
                }

                    previousOp = op;


                }


            }


        }
        Carrier c = Data.carriers.getFirst();
        c.driveTO(time, c.x -9, c.y, false);
        System.out.println("----------LOGSSS------------");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {

            for (Carrier carrier : Data.carriers) {
                for (Log log : carrier.logs) {
                    writer.write(log.toString());  // schrijf de log
                }
            }

            System.out.println("Logs succesvol weggeschreven naar output.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


