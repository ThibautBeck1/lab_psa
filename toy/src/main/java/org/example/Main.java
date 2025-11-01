package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Deque;


public class Main {

    static void main() throws IOException {
        // Parse the input file

        try (InputStream is = Main.class.getResourceAsStream("/toy.txt");
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
                if (ca != null && ca.craneID == c.craneSectionID) {

                    c.availableCarriers.add(ca);
                }
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

                        // carrier nemen
                        Carrier carrier = crane.availableCarriers.removeFirst();
                        DispatchSection d = crane.getDispatchSections(op.getDischargeId());

                        // ===== 1) naar crane (dispatch) – precompute + ticks =====
                        {
                            int tx = d.getX() - 2;
                            int ty = d.getY() - 1;
                            System.out.println("Precompute path to dispatch (" + tx + "," + ty + ") from carrier "
                                    + carrier.id + " at (" + carrier.x + "," + carrier.y + ")");
                            Deque<PathEnvelope> q = CalculatePath.calculatePathEnvelopes(carrier, tx, ty, false);
                            carrier.plan(q);
                            while (carrier.tick(time)) {
                                time++;
                            }
                            carrier.flushPendingMoves(time); // sluit move-segment af vóór pickup
                        }

                        // ===== pickup =====
                        carrier.pickupContainerFromDispatch(d);
                        carrier.logs.add(new LoadLog(time));
                        time++;

                        // ===== 2) naar storage – precompute + ticks =====
                        {
                            int tx = storage.x - 1;
                            int ty = storage.y - 2;
                            Deque<PathEnvelope> q = CalculatePath.calculatePathEnvelopes(carrier, tx, ty, true);
                            carrier.plan(q);
                            while (carrier.tick(time)) {
                                time++;
                            }
                            carrier.flushPendingMoves(time); // sluit move-segment af vóór drop
                        }

                        // ===== drop off =====
                        carrier.dropOffInStorage(storage);
                        carrier.logs.add(new UnLoadLog(time));
                        time++;

                        // terug in pool
                        crane.availableCarriers.add(carrier);

                    } else if (op instanceof LoadOperation) {
                        LoadOperation loadOp = (LoadOperation) op;

                        // container zoeken
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

                        int targetX = container.getX();
                        int targetY = container.getY();
                        Storage storage = container.storage;

                        // carrier nemen
                        Carrier carrier = crane.availableCarriers.removeFirst();
                        DispatchSection d = crane.getDispatchSections(op.getDischargeId());

                        // Check if we need U-shape movement
                        boolean needsUShape = false;

                        // ===== Same column or first load: één pad =====
                        Deque<PathEnvelope> q = CalculatePath.calculatePathEnvelopes(carrier, targetX - 1, targetY - 2, true);
                        carrier.plan(q);
                        while (carrier.tick(time)) {
                            time++;
                        }
                        carrier.flushPendingMoves(time);


                        // pickup from storage
                        carrier.pickupContainerFromStorage(loadOp.getContainerId());
                        carrier.logs.add(new LoadLog(time));
                        time++;

                        // terug naar crane (dispatch)

                        int tx = d.getX() - 2;
                        int ty = d.getY() - 1;
                        q = CalculatePath.calculatePathEnvelopes(carrier, tx, ty, false);
                        carrier.plan(q);
                        while (carrier.tick(time)) {
                            time++;
                        }
                        carrier.flushPendingMoves(time);


                        // drop at dispatch
                        carrier.setOffContainerAtDispatch(d);
                        carrier.logs.add(new UnLoadLog(time));
                        time++;

                        // terug in pool
                        crane.availableCarriers.add(carrier);
                    }
                }
            }
        }

        Carrier c = Data.carriers.getFirst();
        Deque<PathEnvelope> q = CalculatePath.calculatePathEnvelopes(c, c.x -9, c.y, false);
        c.plan(q);
        while (c.tick(time)) { time++; }
        c.flushPendingMoves(time);


        System.out.println("----------LOGSSS------------");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {

            for (Carrier carrier : Data.carriers) {
                writer.write("carrier " + carrier.id + "\n");
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


