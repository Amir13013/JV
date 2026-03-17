package com.eremon.systems.progression;

import com.eremon.models.entity.Player;

public class ProgressionSystem {

    public static void handleLevelUp(Player player, int newLevel) {
        System.out.println("Gestion du passage au niveau " + newLevel);
    }

    public static int calculateXPForLevel(int level) {
        return level * 100;
    }

    public static void grantRewards(Player player, String rewardType) {
        System.out.println("Recompense accordee : " + rewardType);
    }
}
