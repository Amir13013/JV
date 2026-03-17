package com.eremon;

import com.eremon.models.entity.Character;
import com.eremon.models.entity.Effect;
import com.eremon.models.entity.Summon;

public class Skill {
    private String name, description;
    private int manaCost, damage, cooldown;
    private Effect effect;
    private boolean isSummonSkill;

    public Skill(String name, String description, int manaCost, int damage, int cooldown) {
        this(name, description, manaCost, damage, cooldown, null, false);
    }

    public Skill(String name, String description, int manaCost, int damage, int cooldown, Effect effect) {
        this(name, description, manaCost, damage, cooldown, effect, false);
    }

    public Skill(String name, String description, int manaCost, int damage, int cooldown, Effect effect, boolean isSummonSkill) {
        this.name = name;
        this.description = description;
        this.manaCost = manaCost;
        this.damage = damage;
        this.cooldown = cooldown;
        this.effect = effect;
        this.isSummonSkill = isSummonSkill;
    }

    public void use(Character attacker, Character defender) {
        if (attacker.getMana() < manaCost) {
            System.out.println(attacker.getName() + " n'a pas assez de mana !");
            return;
        }

        attacker.setMana(attacker.getMana() - manaCost);
        System.out.println(attacker.getName() + " utilise " + name + " !");

        if (isSummonSkill) {
            Summon echoSpectral = new Summon(
                    "Echo Spectral",
                    30,
                    12,
                    5,
                    8,
                    0,
                    2,
                    attacker
            );
            echoSpectral.addSkill(new Skill("Frappe Spectrale", "Attaque simple", 0, 12, 0));
            attacker.summon(echoSpectral);
            return;
        }

        if (damage > 0) {
            int realDmg = (int)(damage * (0.9 + Math.random() * 0.2));

            if (Math.random() < 0.15) {
                realDmg = (int)(realDmg * 1.5);
                System.out.println("   COUP CRITIQUE !");
            }

            int actualDmg = Math.max(0, realDmg - defender.getDefense());
            defender.takeDamage(realDmg);

            System.out.println("   " + defender.getName() + " subit " + actualDmg + " degats (HP: " + defender.getHealth() + "/" + defender.getMaxHealth() + ")");
        }

        if (effect != null) {
            if (effect.getType().equals("buff")) {
                attacker.applyEffect(effect);
                System.out.println("   " + attacker.getName() + " gagne " + effect.getName() + " !");
            } else {
                defender.applyEffect(effect);
                System.out.println("   " + defender.getName() + " subit " + effect.getName() + " !");
            }
        }
    }

    public String getName() { return name; }
    public int getManaCost() { return manaCost; }
    public int getDamage() { return damage; }
}
