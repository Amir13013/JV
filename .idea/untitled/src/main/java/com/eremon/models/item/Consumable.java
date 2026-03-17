package com.eremon.models.item;

import com.eremon.models.entity.Character;

public class Consumable extends Item {
    private int healAmount;
    private int manaAmount;

    public Consumable(int id, String name, String description, int healAmount, int manaAmount) {
        super(id, name, description, true);
        this.healAmount = healAmount;
        this.manaAmount = manaAmount;
    }

    public int getHealAmount() { return healAmount; }
    public int getManaAmount() { return manaAmount; }

    @Override
    public void use() {
        if (healAmount > 0) System.out.println(name + " restaure " + healAmount + " PV !");
        if (manaAmount > 0) System.out.println(name + " restaure " + manaAmount + " Mana !");
    }

    public void apply(Character target) {
        if (healAmount > 0) {
            target.heal(healAmount);
            System.out.println("Le Fragment " + target.getName() + " utilise " + name + " → +" + healAmount + " PV");
        }
        if (manaAmount > 0) {
            int newMana = target.getMana() + manaAmount;
            target.setMana(newMana);
            System.out.println("Le Fragment " + target.getName() + " utilise " + name + " → +" + manaAmount + " Mana");
        }
    }

    @Override
    public String toString() {
        return super.toString() + " [HEAL: " + healAmount + ", MANA: " + manaAmount + "]";
    }
}
