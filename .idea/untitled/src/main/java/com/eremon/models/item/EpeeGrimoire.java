package com.eremon.models.item;

public class EpeeGrimoire extends Weapon {
    public EpeeGrimoire() {
        super(103, "Épée-Grimoire", "Épée courte + grimoire - Gardien des Échos", 12, 4, 3);
    }

    @Override
    public void use() {
        System.out.println(" ÉPÉE-GRIMOIRE utilisée ! ATK +12, Équilibré, Morsure de mémoire");
    }
}
