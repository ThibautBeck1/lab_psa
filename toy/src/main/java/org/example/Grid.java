package org.example;

import java.util.BitSet;
import java.util.Arrays;

public class Grid {
    // Permanent verboden cellen (cranes, storage, muren, …)


    static int HORIZON;
    static int T0;
    static BitSet[][] occ = null;
    static BitSet[] staticBlocked = new BitSet[Data.mapWidth];
    /* ---------- STATIC (tijd-onafhankelijk) ---------- */
    public static void initstatic() {
        // Init alle kolommen
        for (int x = 0; x < Data.mapWidth; x++) {
            staticBlocked[x] = new BitSet(Data.mapHeight); }
    // ---- Cranes blokkeren (jouw oorspronkelijke logica) ----
        for (Crane c : Data.cranes) {
            for (int i = c.bottomLeftX; i <= c.bottomLeftX + c.width; i++) {
                staticBlocked[i].set(c.bottomLeftY); staticBlocked[i].set(c.topRightY);
            } }
        // ---- Storagezones blokkeren (volledige rechthoek) ----
        for (Storage s : Data.storage) {
            for (int w = 0; w < Constants.storageWidth; w++) {
                staticBlocked[s.x + w].set(s.y, s.y + Constants.storageHeight);
            } }

    }


    /* ---------- DYNAMIC (tijd = 3e dimensie via ringbuffer) ---------- */
    public static void placeContainers(int t) {
        int s = slot(t);
        for (Container container: Data.containersInField){
            if (container != null){
            int w = Constants.storageWidth;
            int h = Constants.storageHeight;
            for (int dx = 0; dx < w; dx++) {

                occ[s][container.getX() + dx].set(container.getY(), container.getY() +h);
            }
        }
        }
    }

        public static void placeCrane(int t) {
        int s = slot(t);

        for (Crane c : Data.cranes) {
            int w =  c.topRightX - c.bottomLeftX;
            int H = c.topRightY - c.bottomLeftY;
            for (int dx = 0; dx < w; dx++) {
                occ[s][c.bottomLeftX + dx].set(c.topRightY);
                occ[s][c.bottomLeftX + dx].set(c.topRightY + H);
            }
        }
    }
    public static void initRingBuffer(int horizon, int startT) {
        HORIZON = horizon;
        T0 = startT;
        occ = new BitSet[HORIZON][Data.mapWidth];
        for (int t = 0; t < HORIZON; t++) {
            for (int x = 0; x < Data.mapWidth; x++) {
                occ[t][x] = new BitSet(Data.mapHeight);
            }
        }
    }

    static int slot(int t) { return Math.floorMod(t, HORIZON); }

    /**
     * Direction → footprint
     * up/down (1,3)  → verticaal: 4 breed × 8 hoog
     * right/left (2,4) → horizontaal: 8 breed × 4 hoog
     */
    static int wOfDir(int dir) {
        return (dir % 2 == 1) ? 3 : 8;
    }
    static int hOfDir(int dir) {
        return (dir % 2 == 1) ? 8 : 3;
    }
    public static boolean testForCarrier(int t, int x, int y, int dir) {
        final int w = wOfDir(dir), h = hOfDir(dir);
        // bounds for full footprint (even though we only block edges)
        if (x < 0 || y < 0 || x + w > Data.mapWidth || y + h > Data.mapHeight) return false;

        int s = slot(t);

        if (dir % 2 == 1) {
            // VERTICAL (4×8): check ONLY left & right columns in [y .. y+h)
                      // <- last column index
            if (occ[s][x].get(y, y + h).cardinality() > 0) return false;
            if (occ[s][x+w].get(y, y + h).cardinality() > 0) return false;
        } else {
            // HORIZONTAL (8×4): check ONLY bottom & top rows across [x .. x+w)
                       // <- last row index
            for (int dx = 0; dx < w; dx++) {
                if (occ[s][x + dx].get(y)) return false;
                if (occ[s][x + dx].get(y+3)) return false;
            }
        }
        return true;
    }

    /** reserveer footprint (zonder check) */
    public static void occupyCarrier(int t, int x, int y, int dir) {

        final int w = wOfDir(dir), h = hOfDir(dir);
        int s = slot(t);
        if (dir % 2 == 1 ) {
            occ[s][x].set(y, y + h);
            occ[s][x + w].set(y, y + h);
        } else {
            for (int dx = 0; dx < w; dx++){
            occ[s][x + dx].set(y);
            occ[s][x + dx].set(y+3);
        }}
    }

    /** probeer te plaatsen, return false als niet past */
    public static boolean tryOccupyRectAt(int t, int x, int y, int dir) {
        if (!testForCarrier(t, x, y, dir)) return false;
        occupyCarrier(t, x, y, dir);
        return true;
    }

    /** volledige slice t dynamisch leegmaken */
    public static void clearSlice(int t) {
        int s = slot(t);
        for (int x = 0; x < Data.mapWidth; x++) {
            occ[s][x].clear();
        }
        //placeCrane(t);
        //placeContainers(t);
    }

    /** slice tFrom → tTo kopiëren (enkel dyn occ) */
    public static void copySlice(int tFrom, int tTo) {
        int a = slot(tFrom), b = slot(tTo);
        for (int x = 0; x < Data.mapWidth; x++) {
            occ[b][x].clear();
            occ[b][x].or(occ[a][x]);
        }
    }

    /** Debug/visualisatie van één slice (alleen bezettingsraster) */

    /** Meerdere slices printen */
    public static void printSlices(int tStart, int count) {
        for (int k = 0; k < count; k++) {
            printCombinedSlice(tStart + k);
        }
    }

    /* ---------- Demo ---------- */
    public static void demoInitRingAndSeed() {
        int hor = 100;
        int now= 0;
        initstatic();
        initRingBuffer(hor, now);
        clearSlice(now);

        // Carrier richting voldoet aan gevraagd van Carrier.java
        int dir = 4; // 1 = up
        Carrier carrier = Data.carriers.getFirst();

        if (!tryOccupyRectAt(now, carrier.x, carrier.y, carrier.direction)) {
            System.out.printf("⚠️  rect past niet op t=%d op (%d,%d) dir=%d%n", now, carrier.x, carrier.y, carrier.direction);
        }
        System.out.println("this is basicly with the carrier");
        printCombinedSlice(now);

        clearSlice(now +1);

        printCombinedSlice(now +1+ hor);



    }
    // --- VERVANG printSlice(...) door dit: ---
    /** Debug/visualisatie van één slice: COMBINED (static + dynamic) */
    public static void printCombinedSlice(int t) {
        int s = slot(t);
        System.out.println("\n--- Map at t=" + t + " (slot " + s + ") ---");
        for (int y = Data.mapHeight - 1; y >= 0; y--) {
            StringBuilder row = new StringBuilder(Data.mapWidth + 8);
            for (int x = 0; x < Data.mapWidth; x++) {
                boolean isStatic = staticBlocked[x].get(y);
                boolean isDyn    = occ[s][x].get(y);
                char ch = '.';
                if (isStatic) ch = '#';       // statisch heeft voorrang in weergave
                if (isDyn) ch = 'X';     // dynamisch (alleen als niet statisch)
                row.append(ch);
            }
            System.out.println(row);
        }
        System.out.println("--- end map ---\n");
    }

// --- HANDIGE HELPERS: ---
    /** Start de volgende tijdslice: maak t+1 leeg zodat je alle carriers voor die tick opnieuw kan inplannen. */
    public static void beginNextSlice(int t) {
        clearSlice(t + 1);
    }

    /** Alias: probeer te plaatsen op tijd t (checkt static + dyn). */
    public static boolean tryPlaceAt(int t, int x, int y, int dir) {
        return tryOccupyRectAt(t, x, y, dir);
    }

    /**
     * Plan één stap vooruit van (oldX,oldY,oldDir) naar (newX,newY,newDir) op t+1.
     * Returned true wanneer de nieuwe footprint op t+1 vrij is en gereserveerd werd.
     * Opmerking: we bewaren geen oude footprint/trace in t+1; je plant elke tick alle carriers opnieuw in t+1.
     */
    public static boolean planStep(int t, int oldX, int oldY, int oldDir, int newX, int newY, int newDir) {
        int tNext = t + 1;
        // Zorg dat t+1 eerst leeg is (of gebruik beginNextSlice(t) één keer vóór je alle carriers plaatst).
        // Hier géén clearSlice(tNext) om niet per carrier elkaars werk te wissen.
        return tryPlaceAt(tNext, newX, newY, newDir);
    }

}