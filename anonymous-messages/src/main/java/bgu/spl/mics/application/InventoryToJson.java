package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Inventory;

import java.util.List;

public class InventoryToJson{
    List<String> inventoryThatLeft;
    public InventoryToJson(List<String> gadgets){
        inventoryThatLeft= gadgets;
    }
}
