package com.eremon.models.entity;

import com.eremon.Skill;
import com.eremon.models.entity.Effect;
import java.util.ArrayList;
import java.util.List;

public class Enemy extends Character {
    private String behavior;
    private List<String> lootTable;
    private int memoryFragmentsReward;


    public Enemy(String name, int health, int attack, int defense, int speed, int mana, String behavior, int memoryFragments) {
        super(name, health, attack, defense, speed, mana);
        this.behavior = behavior;
        this.lootTable = new ArrayList<>();
        this.memoryFragmentsReward = memoryFragments;
        initializeEnemySkills();
    }


    public Enemy(String name, int health, int attack, int defense, int speed, int mana) {
        this(name, health, attack, defense, speed, mana, "aggressive", 10);
    }


    private void initializeEnemySkills() {
        // Compétence basique
        addSkill(new Skill(
                "Attaque Basique",
                "Une attaque simple",
                0,
                getAttack(),
                0
        ));

        // Compétence spéciale selon le type d'ennemi
        if (getName().contains("Mouth")) {
            addSkill(new Skill(
                    "Morsure Vicieuse",
                    "Une morsure qui inflige des dégâts de saignement",
                    5,
                    getAttack() + 5,
                    1,
                    new Effect("Saignement", "debuff", 3, 3)
            ));
        } else if (getName().contains("Eye")) {
            addSkill(new Skill(
                    "Rayon Psychique",
                    "Un rayon mental qui réduit la défense",
                    8,
                    getAttack() + 3,
                    2,
                    new Effect("Vulnérabilité", "debuff", -5, 2)
            ));
        } else if (getName().contains("Amalgam")) {
            addSkill(new Skill(
                    "Frappe Mutante",
                    "Une attaque chaotique",
                    6,
                    getAttack() + 8,
                    1
            ));
        } else if (getName().contains("Tentacule")) {
            addSkill(new Skill(
                    "Étreinte Étouffante",
                    "Immobilise et blesse",
                    7,
                    getAttack() + 4,
                    2,
                    new Effect("Paralysie", "debuff", -3, 2)
            ));
        } else {
            // Compétence par défaut
            addSkill(new Skill(
                    "Frappe Sombre",
                    "Une attaque des ténèbres",
                    5,
                    getAttack() + 5,
                    1
            ));
        }
    }

    // 🎁 GÉNÉRER LE LOOT
    public void generateLoot() {
        // Loot basique
        if (Math.random() < 0.3) {
            addLoot("Potion de Soin");
        }
        if (Math.random() < 0.15) {
            addLoot("Potion de Mana");
        }
        if (Math.random() < 0.1) {
            addLoot("Fragment de Mémoire");
        }

        // Loot rare selon le type
        if (getName().contains("Amalgam") && Math.random() < 0.05) {
            addLoot("Essence Corrompue");
        }
        if (getName().contains("Eye") && Math.random() < 0.05) {
            addLoot("Cristal Psychique");
        }
    }

    public void addLoot(String item) {
        lootTable.add(item);
    }

    public List<String> getLootTable() {
        return lootTable;
    }

    public String getBehavior() {
        return behavior;
    }

    public int getMemoryFragmentsReward() {
        return memoryFragmentsReward;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    @Override
    public String toString() {
        return getName() + " [HP: " + getHealth() + "/" + getMaxHealth() + " | ATK: " + getAttack() + " | DEF: " + getDefense() + "]";
    }
}