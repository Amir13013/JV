package com.eremon.models.player;

import com.eremon.models.entity.Player;
import com.eremon.Skill;
import com.eremon.models.entity.Effect;
import com.eremon.models.item.HacheCendre;

public class PorteCendre extends Player {
    public PorteCendre() {
        super("Porte-Cendre", 120, 25, 12, 8, 50, 1, 0);

        this.equipWeapon(new HacheCendre());

        addSkill(new Skill("Frappe Cendrée", "Coup de base", 0, 22, 0));
        addSkill(new Skill("Cendres enragées", "Boost d'attaque", 8, 20, 0,
                new Effect("Rage des Cendres", "buff", 10, 2)));
        addSkill(new Skill("Brûlure mémorielle", "Inflige brulure", 12, 18, 0,
                new Effect("Brulure", "debuff", 5, 3)));
        addSkill(new Skill("Frappe brute", "Coup puissant", 0, 30, 0));
    }
}