package com.eremon.models.item;

public class PotionVitale extends Consumable {
    public PotionVitale() {
        super(200, "Potion Vitale", "Restaure 50 PV - Essence de mémoire brûlée", 50, 0);
    }

    @Override
    public void use() {
        System.out.println("Potion Vitale utilisée ! +50 PV");
    }
}
