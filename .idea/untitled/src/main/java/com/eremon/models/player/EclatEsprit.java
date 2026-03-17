package com.eremon.models.player;

import com.eremon.models.entity.Player;
import com.eremon.Skill;
import com.eremon.models.item.BatonSpectral;

public class EclatEsprit extends Player {
    public EclatEsprit() {
        super("Eclat Esprit", 100, 15, 10, 10, 60, 1, 0);

        this.equipWeapon(new BatonSpectral());

        addSkill(new Skill("Lame Spirituelle", "Attaque rapide et precise", 0, 24, 0));
        addSkill(new Skill("Projection", "Attaque a distance", 3, 20, 0));
        addSkill(new Skill("Echo des Morts", "Invoque un esprit", 15, 0, 0, null, true));
        addSkill(new Skill("Meditation", "Regenere du mana", 0, 0, 0));
    }
}
