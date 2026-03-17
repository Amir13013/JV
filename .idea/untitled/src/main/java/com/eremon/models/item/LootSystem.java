package com.eremon.models.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LootSystem {
    private static final Random random = new Random();

    public static List<Item> getEnemyLoot(String enemyType) {
        List<Item> loot = new ArrayList<>();

        switch (enemyType.toLowerCase()) {
            case "vocifere":
                if (random.nextDouble() < 0.5) loot.add(ItemFactory.createDagger());
                if (random.nextDouble() < 0.3) loot.add(ItemFactory.createHealthPotion());
                break;

            case "deverse":
                if (random.nextDouble() < 0.7) loot.add(ItemFactory.createGreatSword());
                if (random.nextDouble() < 0.4) loot.add(ItemFactory.createHealthPotion());
                break;

            case "marteleur":
                if (random.nextDouble() < 0.6) loot.add(ItemFactory.createBow());
                if (random.nextDouble() < 0.5) loot.add(ItemFactory.createManaPotion());
                break;

            default:
                if (random.nextDouble() < 0.2) loot.add(ItemFactory.createHealthPotion());
        }

        return loot;
    }

    public static void dropLoot(Inventory inventory, String enemyType) {
        List<Item> drops = getEnemyLoot(enemyType);
        for (Item item : drops) {
            if (!inventory.addItem(item)) {
                System.out.println(" Inventaire plein ! " + item.getName() + " est tombé au sol.");
            }
        }
    }
}
