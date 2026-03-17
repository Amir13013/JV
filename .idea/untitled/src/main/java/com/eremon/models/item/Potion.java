package com.eremon.models.item;

import com.eremon.models.entity.Character;

public class Potion extends Consumable implements Usable {

    public Potion(String name, String description, int healAmount, int manaAmount) {
        super(0, name, description, healAmount, manaAmount);
    }

    @Override
    public void use(Character target) {
        // Exemple d'effet : soigne le perso cible
        target.heal(getHealAmount());
        target.setMana(target.getMana() + getManaAmount());
        System.out.println(target.getName() + " utilise la potion : " + getName() +
                " (+ " + getHealAmount() + " PV, + " + getManaAmount() + " Mana)");
    }
}
