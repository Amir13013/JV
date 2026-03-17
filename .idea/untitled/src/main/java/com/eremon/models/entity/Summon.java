package com.eremon.models.entity;

import com.eremon.Skill;

public class Summon extends Character {
    private int lifetime;
    private Character owner;

    public Summon(String name, int health, int attack, int defense, int speed, int mana, int lifetime, Character owner) {
        super(name, health, attack, defense, speed, mana);
        this.lifetime = lifetime;
        this.owner = owner;
    }

    public void tickLifetime() {
        lifetime--;
    }

    public boolean isExpired() {
        return lifetime <= 0 || isDead();
    }

    public int getLifetime() {
        return lifetime;
    }

    public Character getOwner() {
        return owner;
    }
}
