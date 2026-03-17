package com.eremon.models.entity;

import com.eremon.Skill;
import com.eremon.models.entity.Effect;

public class Marvin extends Enemy {
    public Marvin() {
        super("Marvin", 50, 15, 10, 3, 0, "Lent - Attaque lourde", 25);

        addSkill(new Skill("Marvin Strike", "Frappe lourde", 0, 1000, 0));

    }
}