package com.eremon.models.item;

public class PotionEsprit extends Consumable {
    public PotionEsprit() {
        super(201, "Potion d'Esprit", "Restaure 40 Mana - Souffle des échos", 0, 40);
    }

    @Override
    public void use() {
        System.out.println(" Potion d'Esprit utilisée ! +40 Mana");
    }
}
