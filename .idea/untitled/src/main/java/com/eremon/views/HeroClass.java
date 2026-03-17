
package com.eremon.views;

import com.eremon.models.entity.Player;
import com.eremon.models.player.PorteCendre;
import com.eremon.models.player.GardienEchos;
import com.eremon.models.player.LameSouvenir;
import com.eremon.models.player.EclatEsprit;
import javafx.scene.image.Image;

public class HeroClass {

    public enum ClassType {
        MAGE(0, "Porte-Cendre", "Guerrier des cendres aux frappes devastatrices",
                "/sprites/overworld/MAGE_SPRITE/MAGE_03.png",
                "/sprites/combat/MAGE_COMBAT_SPRITE_80x112_03.png",
                PorteCendre.class),

        WARRIOR(1, "Gardien des Echos", "Combattant equilibre maitrisant l'epee et la magie",
                "/sprites/overworld/WARRIOR_SPRITE/WARRIOR_01.png",
                "/sprites/combat/WARRIOR_COMBAT_SPRITE/WARRIOR_COMBAT_SPRITE_80x112_01.png",
                GardienEchos.class),

        ASSASSIN(2, "Lame-Souvenir", "Ombre furtive aux coups rapides et mortels",
                "/sprites/overworld/ASSASSIN_SPRITE/ASSASSIN_01.png",
                "/sprites/combat/ASSASSIN_COMBAT_SPRITE/ASSASSIN_COMBAT_SPRITE_80x112_01.png",
                LameSouvenir.class),

        ARCHER(3, "Eclat Esprit", "Maitre spirituel invoquant les echos des morts",
                "/sprites/overworld/ARCHER_SPRITE/ARCHER_01.png",
                "/sprites/combat/ARCHER_COMBAT_SPRITE/ARCHER_COMBAT_SPRITE_80x112_02.png",
                EclatEsprit.class);

        private final int index;
        private final String name;
        private final String description;
        private final String walkSpritePath;
        private final String combatSpritePath;
        private final Class<? extends Player> playerClass;

        ClassType(int index, String name, String description,
                  String walkSpritePath, String combatSpritePath,
                  Class<? extends Player> playerClass) {
            this.index = index;
            this.name = name;
            this.description = description;
            this.walkSpritePath = walkSpritePath;
            this.combatSpritePath = combatSpritePath;
            this.playerClass = playerClass;
        }

        public int getIndex()               { return index; }
        public String getName()             { return name; }
        public String getDescription()      { return description; }
        public String getWalkSpritePath()   { return walkSpritePath; }
        public String getCombatSpritePath() { return combatSpritePath; }
        public Class<? extends Player> getPlayerClass() { return playerClass; }

        public static ClassType fromIndex(int index) {
            for (ClassType type : values()) {
                if (type.index == index) return type;
            }
            return MAGE;
        }

        public static ClassType fromName(String name) {
            for (ClassType type : values()) {
                if (type.name.equalsIgnoreCase(name)) return type;
            }
            return MAGE;
        }
    }

    private ClassType classType;
    private Image walkSprite;
    private Image combatSprite;

    public HeroClass(ClassType classType) {
        this.classType = classType;
        loadSprites();
    }

    public HeroClass(int classIndex) {
        this.classType = ClassType.fromIndex(classIndex);
        loadSprites();
    }

    public HeroClass(String className) {
        this.classType = ClassType.fromName(className);
        loadSprites();
    }

    private void loadSprites() {
        try {
            walkSprite = new Image(getClass().getResourceAsStream(classType.getWalkSpritePath()));
        } catch (Exception e) {
            System.err.println("Sprite d'exploration non trouvé: " + classType.getWalkSpritePath());
            try {
                walkSprite = new Image(getClass().getResourceAsStream("/image/main-character.png"));
            } catch (Exception ex) {
                System.err.println("Sprite par défaut non trouvé !");
            }
        }
        try {
            combatSprite = new Image(getClass().getResourceAsStream(classType.getCombatSpritePath()));
        } catch (Exception e) {
            System.err.println("Sprite de combat non trouvé: " + classType.getCombatSpritePath());
            combatSprite = walkSprite;
        }
    }

    public Player createPlayer() {
        try {
            return classType.getPlayerClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            System.err.println("Erreur création personnage: " + e.getMessage());
            return new PorteCendre();
        }
    }

    /** Alias pour compatibilité */
    public Player createPlayerInstance() {
        return createPlayer();
    }

    public int getIndex()               { return classType.getIndex(); }
    public ClassType getClassType()     { return classType; }
    public String getClassName()        { return classType.getName(); }
    public String getDescription()      { return classType.getDescription(); }
    public Image getWalkSprite()        { return walkSprite; }
    public Image getCombatSprite()      { return combatSprite; }
    public String getWalkSpritePath()   { return classType.getWalkSpritePath(); }
    public String getCombatSpritePath() { return classType.getCombatSpritePath(); }
}