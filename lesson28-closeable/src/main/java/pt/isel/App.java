package pt.isel;

import static java.lang.System.gc;
import static java.lang.System.out;

public class App {

    static final int SIZE = 100_000;
    public static void main(String[] args) {
        garbageAndClean();
        out.println("----- Returning from garbageAndClean()");
        System.gc();
        allocatedMem();


    }
    public static void garbageAndClean() {
        allocatedMem();
        System.gc();
        allocatedMem();
        System.gc();
        allocatedMem();
        out.println("------ Making garbage");
        // KEEPING the resulting array in a local variable
        // that is a Root Reference avoid GC to clean that memory
        Object[] objects = makeGarbage(SIZE);
        allocatedMem();
        System.gc();
        allocatedMem();
        System.gc();
        allocatedMem();
    }

    private static void allocatedMem() {
        Runtime runtime = Runtime.getRuntime();
        long allocated = runtime.totalMemory() - runtime.freeMemory();
        out.println(allocated);
    }

    public static Object[] makeGarbage(int size) {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) {
            arr[i] = new Object();
        }
        return arr;
    }
}
