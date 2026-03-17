package com.eremon.models.entity;

import com.eremon.Skill;

public class Deverses extends Enemy {
    public Deverses() {
        super("Deverse", 40, 10, 2, 8, 0, "Patrouille - Chasse le joueur", 15);

        addSkill(new Skill("Ruee Instable", "Charge rapide", 0, 15, 0));
        addSkill(new Skill("Laceration Frenetique", "Attaques multiples", 0, 10, 0));

        addLoot("Cristal Instable");
        addLoot("Essence de Mouvement");
    }
}
