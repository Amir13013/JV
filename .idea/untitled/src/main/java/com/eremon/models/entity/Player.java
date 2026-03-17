package com.eremon.models.entity;

import com.eremon.models.item.Item;
import com.eremon.models.item.Consumable;

public class Player extends Character {

    private int level;
    private int experience;
    private int experienceToNextLevel;

    public Player(String name, int health, int attack, int defense, int speed, int mana, int level, int experience) {
        super(name, health, attack, defense, speed, mana);
        this.level = level;
        this.experience = experience;
        this.experienceToNextLevel = level * 100;
    }

    public int getLevel() { return level; }
    public int getExperience() { return experience; }
    public int getExperienceToNextLevel() { return experienceToNextLevel; }

    public void gainExperience(int amount) {
        this.experience += amount;
        System.out.println("+" + amount + " XP (Total: " + experience + "/" + experienceToNextLevel + ")");
        while (experience >= experienceToNextLevel) {
            levelUp();
        }
    }

    private void levelUp() {
        this.experience -= experienceToNextLevel;
        this.level++;
        this.experienceToNextLevel = (int)(experienceToNextLevel * 1.5);
        int hpBonus = 10, manaBonus = 5, atkBonus = 2, defBonus = 1;
        setMaxHp(getMaxHp() + hpBonus);
        heal(hpBonus);
        setMana(getMana() + manaBonus);
        setAttack(getAttack() + atkBonus);
        setDefense(getDefense() + defBonus);
        System.out.println(" LEVEL UP ! Niveau " + level);
        System.out.println("+10 HP | +5 Mana | +2 ATK | +1 DEF");
    }

    public void gainXP(int amount) { gainExperience(amount); }

    public boolean pickUp(Item item) { return getInventory().addItem(item); }
    public void dropItem(Item item) { getInventory().removeItem(item); }
    public Item getItem(int index) { return getInventory().getItem(index); }
    public Item getItemByName(String name) { return getInventory().getItemByName(name); }
    public void showInventory() { getInventory().displayInventory(); }

    public boolean usePotion(String potionName, Character target) {
        Item item = getInventory().getItemByName(potionName);
        if (item == null) {
            System.out.println(" Potion non trouvée !");
            return false;
        }
        if (!(item instanceof Consumable)) {
            System.out.println("Cet item n'est pas une potion !");
            return false;
        }
        Consumable potion = (Consumable) item;
        potion.apply(target);
        getInventory().removeItem(item);
        System.out.println(" Potion utilisée !");
        return true;
    }

    public void displayInfo() {
        System.out.println("========================================");
        System.out.println("Nom : " + getName() + " | HP : " + getHp() + "/" + getMaxHp() + " | Mana : " + getMana());
        System.out.println("Level : " + level + " | XP : " + experience + "/" + experienceToNextLevel);
        System.out.println("Attaque : " + getAttack() + " | Défense : " + getDefense() + " | Vitesse : " + getSpeed());
        System.out.println("========================================");
    }

    public void displayFullInfo() {
        displayInfo();
        showInventory();
    }


    private double x, y;

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public double getX() { return x; }
    public double getY() { return y; }
}
