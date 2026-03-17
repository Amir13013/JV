package com.eremon.models.item;

public class Key extends Item {
    private String doorName;

    public Key(int id, String name, String doorName) {
        super(id, name, "Clé pour : " + doorName, false);
        this.doorName = doorName;
    }

    public String getDoorName() { return doorName; }

    @Override
    public void use() {
        System.out.println(" Clé " + name + " utilisée pour ouvrir : " + doorName);
    }
}
