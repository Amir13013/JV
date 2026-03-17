package com.eremon.models.item;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Item> items;
    private final int maxSize = 20;

    public Inventory() {
        this.items = new ArrayList<>();
    }

    public boolean addItem(Item item) {
        if (items.size() < maxSize) {
            items.add(item);
            System.out.println(" " + item.getName() + " ajouté à l'inventaire.");
            return true;
        } else {
            System.out.println("Inventaire plein !");
            return false;
        }
    }

    public void removeItem(Item item) {
        if (items.remove(item)) {
            System.out.println("" + item.getName() + " retiré de l'inventaire.");
        }
    }

    public Item getItem(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return null;
    }

    public Item getItemByName(String name) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    public List<Item> getAllItems() {
        return new ArrayList<>(items);
    }

    public int getSize() {
        return items.size();
    }

    public int getMaxSize() {
        return maxSize;
    }

    public boolean isFull() {
        return items.size() >= maxSize;
    }

    public void display() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  INVENTAIRE (" + items.size() + "/" + maxSize + ")                     ║");
        System.out.println("╚════════════════════════════════════════╝");
        if (items.isEmpty()) {
            System.out.println("  (vide)");
        } else {
            for (int i = 0; i < items.size(); i++) {
                System.out.println((i + 1) + ". " + items.get(i).toString());
            }
        }
        System.out.println();
    }

    public void displayInventory() {
        display();
    }
}