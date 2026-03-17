package com.eremon.models.player;

import com.eremon.models.entity.Player;
import com.eremon.Skill;
import com.eremon.models.item.EpeeGrimoire;

public class GardienEchos extends Player {
    public GardienEchos() {
        super("Gardien des Echos", 110, 12, 10, 8, 60, 1, 0);

        this.equipWeapon(new EpeeGrimoire());

        addSkill(new Skill("Coup d'esprit", "Frappe solide", 0, 18, 0));
        addSkill(new Skill("Morsure memoire", "Absorbe HP", 10, 28, 3));
        addSkill(new Skill("Lame spectrale", "Attaque equilibree", 8, 24, 2));
        addSkill(new Skill("Frappe remanente", "Traverse defense", 12, 26, 3));
    }
}
