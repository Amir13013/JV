package com.eremon.systems.combat;

import com.eremon.models.entity.Character;
import com.eremon.models.entity.Player;
import com.eremon.models.entity.Summon;
import com.eremon.models.item.LootSystem;
import com.eremon.Skill;

public class CombatSystem {
    public static void startCombat(Character first, Character second) {
        int turn = 1;
        int maxTurns = 10;

        System.out.println("===========================================");
        System.out.println("  FRAGMENT D'EREMON - COMBAT EPIQUE   ");
        System.out.println("===========================================\n");

        System.out.println(first.getName() + " VS " + second.getName());
        System.out.println(first.getName() + " - HP: " + first.getHealth() + "/" + first.getMaxHealth() + " | Mana: " + first.getMana());
        System.out.println(second.getName() + " - HP: " + second.getHealth() + "/" + second.getMaxHealth() + " | Mana: " + second.getMana());
        System.out.println("\n--- DEBUT DU COMBAT ---\n");

        while (first.isAlive() && second.isAlive() && turn <= maxTurns) {
            System.out.println("========== TOUR " + turn + " ==========");

            Skill skill1 = first.getSkills().get((int)(Math.random() * first.getSkills().size()));
            skill1.use(first, second);

            Summon summon1 = first.getActiveSummon();
            if (summon1 != null && !summon1.isExpired()) {
                summon1.getSkills().get(0).use(summon1, second);
            }

            first.tickEffects();
            second.tickEffects();
            first.tickSummon();

            if (!second.isAlive()) break;

            System.out.println();

            Skill skill2 = second.getSkills().get((int)(Math.random() * second.getSkills().size()));
            skill2.use(second, first);

            Summon summon2 = second.getActiveSummon();
            if (summon2 != null && !summon2.isExpired()) {
                summon2.getSkills().get(0).use(summon2, first);
            }

            first.tickEffects();
            second.tickEffects();
            second.tickSummon();

            System.out.println();
            turn++;
        }

        System.out.println("\n===========================================");
        System.out.println("         FIN DU COMBAT");
        System.out.println("===========================================");
        System.out.println(first.getName() + " - HP: " + first.getHealth() + "/" + first.getMaxHealth());
        System.out.println(second.getName() + " - HP: " + second.getHealth() + "/" + second.getMaxHealth());

        Character winner = null;
        Character loser = null;

        if (first.isAlive() && !second.isAlive()) {
            System.out.println("\n🏆 " + first.getName() + " est vainqueur !");
            winner = first;
            loser = second;
        } else if (second.isAlive() && !first.isAlive()) {
            System.out.println("\n💀 " + second.getName() + " est vainqueur !");
            winner = second;
            loser = first;
        } else if (turn > maxTurns) {
            System.out.println("⏱️ Limite de tours atteinte !");
        } else {
            System.out.println("💥 Double KO !");
        }

        if (winner != null && winner instanceof Player) {
            Player player = (Player) winner;
            System.out.println("💰 BUTIN DE " + loser.getName() + " :");
            LootSystem.dropLoot(player.getInventory(), loser.getName());
        }
    }
}
