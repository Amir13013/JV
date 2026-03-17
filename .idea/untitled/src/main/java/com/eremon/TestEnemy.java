package com.eremon;

import com.eremon.models.player.PorteCendre;
import com.eremon.models.entity.Vociferes;
import com.eremon.models.entity.Deverses;
import com.eremon.models.entity.Marteleurs;
import com.eremon.systems.combat.CombatSystem;

public class TestEnemy {
    public static void main(String[] args) {
        PorteCendre hero = new PorteCendre();

        System.out.println("=== TEST 1 : Combat contre Vocifere ===\n");
        Vociferes vocifere = new Vociferes();
        CombatSystem.startCombat(hero, vocifere);

        System.out.println("\n\n=== TEST 2 : Combat contre Deverse ===\n");
        hero = new PorteCendre();
        Deverses deverse = new Deverses();
        CombatSystem.startCombat(hero, deverse);

        System.out.println("\n\n=== TEST 3 : Combat contre Marteleur ===\n");
        hero = new PorteCendre();
        Marteleurs marteleur = new Marteleurs();
        CombatSystem.startCombat(hero, marteleur);
    }
}
