package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.application.InventoryToJson;
import bgu.spl.mics.application.JavaToJson;
import bgu.spl.mics.application.publishers.TimeService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * That's where Q holds his gadget (e.g. an explosive pen was used in GoldenEye, a geiger counter in Dr. No, etc).
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory {
    private List<String> gadgets;
    int Tick;

    private static class SingeltonHolder {
        private static Inventory instance = new Inventory();
    }

    private Inventory() {
        gadgets = new LinkedList<>();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static Inventory getInstance() {
        return SingeltonHolder.instance;
    }


    /**
     * Initializes the inventory. This method adds all the items given to the gadget
     * inventory.
     * <p>
     *
     * @param inventory Data structure containing all data necessary for initialization
     *                  of the inventory.
     */
    public void load(String[] inventory) {
        for (int i = 0; i < inventory.length; i++) {
            gadgets.add(inventory[i]);
        }
    }

    /**
     * acquires a gadget and returns 'true' if it exists.
     * <p>
     *
     * @param gadget Name of the gadget to check if available
     * @return ‘false’ if the gadget is missing, and ‘true’ otherwise
     */
    public synchronized boolean getItem(String gadget) { //check synchronized

        if (gadgets.contains(gadget)) {
            gadgets.remove(gadget);
            return true;
        }
        return false;
    }


    public int getTick() {
        return this.Tick;
    }

    /**
     * <p>
     * Prints to a file name @filename a serialized object List<Gadget> which is a
     * List of all the reports in the diary.
     * This method is called by the main method in order to generate the output.
     */
    public void printToFile(String filename) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter writer = new FileWriter(filename);
            gson.toJson(gadgets, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


