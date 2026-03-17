package com.eremon.models.entity;

import com.eremon.Skill;

public class Vociferes extends Enemy {
    public Vociferes() {
        super("Vocifere", 50, 8, 4, 5, 0, "Stationnaire - Crie au contact", 10);

        addSkill(new Skill("Cri Dechire-Ame", "Hurlement de souffrance", 0, 12, 0));
        addSkill(new Skill("Griffure Desesperee", "Attaque paniquee", 0, 8, 0));

        addLoot("Fragment de Souvenir");
        addLoot("Essence de Cri");
    }
}
