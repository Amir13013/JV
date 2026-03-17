package com.eremon.models.item;

public class Weapon extends Item {
    private int damage;
    private int attackSpeed;
    private int range;

    public Weapon(int id, String name, String description, int damage, int attackSpeed, int range) {
        super(id, name, description, false);
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.range = range;
    }

    public int getDamage() { return damage; }
    public int getAttackSpeed() { return attackSpeed; }
    public int getRange() { return range; }

    @Override
    public void use() {
        System.out.println(name + " inflige " + damage + " dégâts !");
    }

    @Override
    public String toString() {
        return super.toString() + " [DMG: " + damage + ", SPD: " + attackSpeed + ", RNG: " + range + "]";
    }
}
