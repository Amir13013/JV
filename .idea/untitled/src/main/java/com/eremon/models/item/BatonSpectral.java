package com.eremon.models.item;

public class BatonSpectral extends Weapon {
    public BatonSpectral() {
        super(102, "Bâton Spectral", "Bâton des échos - Éclat-Esprit", 8, 3, 8);
    }

    @Override
    public void use() {
        System.out.println(" BÂTON SPECTRAL utilisé ! ATK +8, Projectiles lents, Invocation d'échos");
    }
}
