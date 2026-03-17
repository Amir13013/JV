package com.eremon.models.item;

public class HacheCendre extends Weapon {
    public HacheCendre() {
        super(100, "Hache de Cendre", "Énorme hache brûlante - Porte-Cendre", 18, 1, 2);
    }

    @Override
    public void use() {
        System.out.println(" HACHE DE CENDRE utilisée ! ATK +18, Brûlure mémorielle");
    }
}
