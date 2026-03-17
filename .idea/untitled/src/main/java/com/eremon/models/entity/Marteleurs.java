package com.eremon.models.entity;

import com.eremon.Skill;
import com.eremon.models.entity.Effect;

public class Marteleurs extends Enemy {
    public Marteleurs() {
        super("Marteleur", 80, 15, 10, 3, 0, "Lent - Attaque lourde", 25);

        addSkill(new Skill("Choc Devastateur", "Frappe lourde", 0, 25, 0));
        addSkill(new Skill("Onde de Choc", "Attaque qui etourdit", 0, 20, 0,
                new Effect("Etourdi", "debuff", -5, 2)));

        addLoot("Fragment Lourd");
        addLoot("Essence de Force");
        addLoot("Plaque de Corruption");
    }
}
