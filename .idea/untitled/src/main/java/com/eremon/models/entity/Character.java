package com.eremon.models.entity;

import java.util.ArrayList;
import java.util.List;
import com.eremon.Skill;
import com.eremon.models.item.Weapon;
import com.eremon.models.item.Inventory;

public class Character {
    private String name;
    private int health, maxHealth, attack, defense, speed, mana;
    private int baseAttack, baseDefense;
    private List<Skill> skills = new ArrayList<>();
    private List<Effect> activeEffects = new ArrayList<>();
    private Summon activeSummon = null;
    private Weapon equippedWeapon;
    private Inventory inventory;
    private double x = 0;
    private double y = 0;

    public Character(String name, int health, int attack, int defense, int speed, int mana) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.attack = attack;
        this.defense = defense;
        this.baseAttack = attack;
        this.baseDefense = defense;
        this.speed = speed;
        this.mana = mana;
        this.equippedWeapon = null;
        this.inventory = new Inventory();
    }

    public void equipWeapon(Weapon weapon) {
        this.equippedWeapon = weapon;
        System.out.println(name + " equipe " + weapon.getName() + " (+" + weapon.getDamage() + " ATK)");
    }

    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }

    public int getTotalAttack() {
        int totalAtk = attack;
        if (equippedWeapon != null) {
            totalAtk += equippedWeapon.getDamage();
        }
        return totalAtk;
    }

    public int getAttack() {
        return getTotalAttack();
    }

    public void summon(Summon s) {
        if (activeSummon != null && !activeSummon.isExpired()) {
            System.out.println("   " + name + " a deja une invocation active !");
            return;
        }
        activeSummon = s;
        System.out.println("   " + name + " invoque " + s.getName() + " !");
    }

    public Summon getActiveSummon() {
        return activeSummon;
    }

    public void tickSummon() {
        if (activeSummon != null) {
            activeSummon.tickLifetime();
            if (activeSummon.isExpired()) {
                System.out.println("   " + activeSummon.getName() + " disparait !");
                activeSummon = null;
            }
        }
    }

    public void applyEffect(Effect effect) {
        activeEffects.add(new Effect(effect.getName(), effect.getType(), effect.getValue(), effect.getDuration()));
        updateStats();
    }

    public void tickEffects() {
        for (int i = activeEffects.size() - 1; i >= 0; i--) {
            Effect e = activeEffects.get(i);

            if (e.getName().contains("Brulure")) {
                takeDamage(e.getValue());
                System.out.println("   " + name + " subit " + e.getValue() + " degats de brulure !");
            }

            e.tick();
            if (e.isExpired()) {
                activeEffects.remove(i);
                System.out.println("   " + e.getName() + " sur " + name + " se dissipe !");
                updateStats();
            }
        }
    }

    private void updateStats() {
        attack = baseAttack;
        defense = baseDefense;

        for (Effect e : activeEffects) {
            if (e.getName().contains("Rage")) attack += e.getValue();
            if (e.getName().contains("Armure")) defense += e.getValue();
        }
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    protected void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getHp() {
        return health;
    }

    public int getMaxHp() {
        return maxHealth;
    }

    public int getMana() {
        return mana;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpeed() {
        return speed;
    }

    public void setMana(int m) {
        this.mana = m;
    }

    public void takeDamage(int dmg) {
        health -= dmg;
        if (health < 0) health = 0;
    }

    public void heal(int amount) {
        health += amount;
        if (health > maxHealth) health = maxHealth;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void move(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void moveBy(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    protected int getBaseAttack() {
        return baseAttack;
    }

    protected void setBaseAttack(int attack) {
        this.baseAttack = attack;
    }

    protected int getBaseDefense() {
        return baseDefense;
    }

    protected void setBaseDefense(int defense) {
        this.baseDefense = defense;
    }
    
    protected void setMaxHp(int maxHp) {
        this.maxHealth = maxHp;
    }

    protected void setAttack(int attack) {
        this.baseAttack = attack;
        this.attack = attack;
    }

    protected void setDefense(int defense) {
        this.baseDefense = defense;
        this.defense = defense;
    }

    public int getMaxMana() {
        return 100; // ou ajoute un champ maxMana si besoin
    }
}