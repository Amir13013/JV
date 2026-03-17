package com.eremon.factories;

public class ItemFactory {


    public static Weapon createSword() {
        return new Weapon(1, "Épée", "Une épée classique", 15, 2, 1);
    }

    public static Weapon createGreatSword() {
        return new Weapon(2, "Grande Épée", "Une épée massive et puissante", 25, 1, 1);
    }

    public static Weapon createBow() {
        return new Weapon(3, "Arc", "Arc classique", 12, 3, 5);
    }

    public static Weapon createDagger() {
        return new Weapon(4, "Dague", "Une petite dague rapide", 8, 4, 1);
    }


    public static Consumable createHealthPotion() {
        return new Consumable(10, "Potion de Vie", "Restaure 50 PV", 50, 0);
    }

    public static Consumable createManaPotion() {
        return new Consumable(11, "Potion de Mana", "Restaure 30 Mana", 0, 30);
    }

    public static Consumable createFullRestore() {
        return new Consumable(12, "Élixir Complet", "Restaure tout", 100, 100);
    }

    public static Consumable createStaminaPotion() {
        return new Consumable(13, "Potion d'Endurance", "Restaure 40 PV et 20 Mana", 40, 20);
    }
}
