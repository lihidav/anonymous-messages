/*package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    Inventory inv = new Inventory();
    @BeforeEach
    public void setUp(){
        inv = Inventory.getInstance();
    }

    @Test

    public void getInstanceTest(){
        Inventory inv2 = Inventory.getInstance();
        assertEquals(inv2, inv);
    }

    public void getItemTest(){ // test to load also
        String[] invList = {"Skyhook", "geigerCounter", "X-ray","daggerShoe"};
        assertFalse(inv.getItem("Skyhook")); //not loaded yet
        inv.load(invList);
        assertTrue(inv.getItem("SkyHook"));
        assertTrue(inv.getItem("geigerCounter"));
        assertTrue(inv.getItem("X-ray"));
        assertTrue(inv.getItem("daggerShoe"));
    }
}
*/