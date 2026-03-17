package com.eremon.controllers;

import com.eremon.models.entity.Character;
import com.eremon.models.entity.Player;
import com.eremon.Skill;
import java.util.Scanner;

public class CombatController {

    private Character player;
    private Character enemy;
    private boolean combatActive;
    private int currentTurn;
    private Scanner scanner = new Scanner(System.in);

    public CombatController() {
        this.combatActive = false;
        this.currentTurn = 0;
    }

    public void startCombat(Character player, Character enemy) {
        this.player = player;
        this.enemy = enemy;
        this.combatActive = true;
        this.currentTurn = 1;

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║             COMBAT TOUR PAR TOUR      ║");
        System.out.println("╚════════════════════════════════════════╝");

        boucleCombat();
    }

    private void boucleCombat() {
        while (combatActive) {
            System.out.println("\n========== TOUR " + currentTurn + " ==========");
            afficheStats();

            tourJoueur();
            if (!combatActive || enemy.isDead()) { conclureCombat(); break; }

            tourEnnemi();
            if (!combatActive || player.isDead()) { conclureCombat(); break; }

            currentTurn++;
        }
    }

    private void afficheStats() {
        System.out.println(player.getName() + " - HP: " + player.getHealth() + "/" + player.getMaxHealth() + ", Mana: " + player.getMana());
        System.out.println(enemy.getName() + " - HP: " + enemy.getHealth() + "/" + enemy.getMaxHealth());
    }

    private void tourJoueur() {
        System.out.println("\nQue veux-tu faire ?");
        for (int i = 0; i < player.getSkills().size(); i++) {
            Skill skill = player.getSkills().get(i);
            System.out.println((i+1) + ". " + skill.getName() + " (Mana : " + skill.getManaCost() + ")");
        }
        int offset = player.getSkills().size();
        System.out.println((offset+1) + ". Inventaire");
        System.out.println((offset+2) + ". Fuir");

        int choix = 0;
        while (choix < 1 || choix > offset+2) {
            System.out.print("Ton choix : ");
            try {
                choix = Integer.parseInt(scanner.nextLine());
            } catch (Exception ignored) {}
        }

        if (choix <= offset) {
            Skill skill = player.getSkills().get(choix-1);
            skill.use(player, enemy);
        } else if (choix == offset+1) {
            System.out.println(">>> Inventaire (fonction à implémenter)");
        } else {
            System.out.println(">>> Tu fuis le combat !");
            combatActive = false;
        }
    }

    private void tourEnnemi() {
        if (enemy.getSkills().isEmpty()) {
            System.out.println(enemy.getName() + " ne fait rien.");
            return;
        }
        Skill skill = enemy.getSkills().get(0);
        System.out.println(enemy.getName() + " utilise " + skill.getName() + " !");
        skill.use(enemy, player);
    }

    private void conclureCombat() {
        combatActive = false;
        if (player.isDead()) {
            System.out.println(player.getName() + " a été vaincu !");
        } else if (enemy.isDead()) {
            System.out.println(player.getName() + " est victorieux !");
            if (player instanceof Player) {
                int xpGain = (enemy.getMaxHealth() + enemy.getAttack() + enemy.getDefense()) / 2;
                ((Player) player).gainExperience(xpGain);
                System.out.println("Tu gagnes " + xpGain + " XP !");
            }
        } else {
            System.out.println("Combat interrompu ou fuite !");
        }
    }
}
