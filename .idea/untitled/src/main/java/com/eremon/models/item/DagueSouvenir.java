package com.eremon.models.item;

public class DagueSouvenir extends Weapon {
    public DagueSouvenir() {
        super(101, "Dague-Souvenir", "Dague furtive voleuse de mémoire - Lame-Souvenir", 12, 5, 1);
    }

    @Override
    public void use() {
        System.out.println(" DAGUE-SOUVENIR utilisée ! ATK +12, Vol de mémoire, Vitesse ++");
    }
}
