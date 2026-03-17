package com.eremon.models.player;

import com.eremon.models.entity.Player;
import com.eremon.Skill;
import com.eremon.models.item.DagueSouvenir;

public class LameSouvenir extends Player {
    public LameSouvenir() {
        super(
                "Lame-Souvenir", // nom
                85,              // health
                18,              // attack
                8,               // defense
                12,              // speed
                40,              // mana
                1,               // level
                0                // experience
        );

        this.equipWeapon(new DagueSouvenir());
        this.addSkill(new Skill("Entaille rapide", "Attaque rapide.", 0, 20, 0));
        this.addSkill(new Skill("Coup furtif", "Attaque sournoise.", 0, 25, 0));
        this.addSkill(new Skill("Vol de mémoire", "Attaque spéciale.", 0, 22, 0));
        this.addSkill(new Skill("Frappe éclatante", "Coup violent.", 0, 28, 0));
    }
}
