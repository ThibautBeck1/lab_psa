package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;


public class Main {

    static void main() throws IOException {
        // Parse the input file
        Data data;
        try (InputStream is = Main.class.getResourceAsStream("/toy2.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            ParseClass.Parse(br);
        }
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

        // de carriers toevoegen aan hun crane
        for (Crane c: Data.cranes) {
            for (Carrier ca : Data.carriers) {
                if (ca != null && ca.craneID == c.craneSectionID)
                    c.availableCarriers.add(ca);
            }
         }
        int t = 0;
        // demands and solution
        for (Storage s: Data.storage) {
            System.out.println("storage " + s);
        }
        for (Demand demand : Data.demands) {
            System.out.println("\nDemand for Crane " + demand.getCraneId());
            Crane crane = Data.cranes.get(demand.getCraneId());
            System.out.println(crane);

            for (Ship ship : demand.getShips()) {
                System.out.println("  Ship " + ship.getId() + " operations:");
                for (Operation op : ship.getOperations()) {
                    System.out.println("    " + op);
                    if (op instanceof UnloadOperation){

                        Storage storage = Data.storage.get(((UnloadOperation) op).getStorageId());

                        // take carrier out of list of availlable carriers for the crane
                        Carrier carrier = crane.availableCarriers.removeFirst();
                        DispatchSection d = crane.getDispatchSections(op.getDischargeId());

                        t = carrier.driveTo(d.getX() ,d.getY() ,t, false);
                        // drive to crane
                        // pickup
                        carrier.pickupContainer(new Container(((UnloadOperation) op).getContainerId()));
                        // drive to storage
                        t = carrier.driveTo(storage.x +2,storage.y -2 ,t , true);

                        // drop off
                        carrier.dropOff(storage);

                        // for later : make it go away to a safe location

                        // go in a list of availlable carriers
                        // this list makes sure that you could always find the 'idle' carreir
                        crane.availableCarriers.add(carrier);
                    } else if (op instanceof LoadOperation) {
                        // take carrier out of list of availlable carriers for the crane
                        // drive to storage
                        // pickup
                        // drive to crane
                        // drop off
                        // go in a list of availlable carriers

                    }
                }
            }
        }



    }
}


