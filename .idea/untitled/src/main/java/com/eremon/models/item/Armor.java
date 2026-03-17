package com.eremon.models.item;

public class Armor extends Item {
    private int defenseBonus;

    public Armor(int id, String name, String description, int defenseBonus) {
        super(id, name, description, true);
        this.defenseBonus = defenseBonus;
    }

    public int getDefenseBonus() { return defenseBonus; }

    @Override
    public void use() {
        System.out.println("Arme " + name + " équipée ! Défense +  " + defenseBonus);
    }
}
