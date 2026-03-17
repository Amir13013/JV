package com.eremon.views;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;

/**
 * 🎨 GESTIONNAIRE DES ASSETS UI - DÉCOUPAGE CORRECT DES SPRITES
 */
public class UIAssetLoader {

    private static UIAssetLoader instance;

    // Spritesheets chargés
    private Image buttonsSheet;
    private Image iconsSheet;
    private Image numbersSheet;
    private Image numbersLevelsSheet;
    private Image levelsSheet;
    private Image characterPanelSheet;
    private Image mainMenuBg;
    private Image settingsBg;
    private Image inventoryBg;
    private Image equipmentBg;
    private Image craftBg;
    private Image shopBg;

    // Tailles des sprites
    private static final int BUTTON_WIDTH = 16;
    private static final int BUTTON_HEIGHT = 16;
    private static final int ICON_SIZE = 16;
    private static final int NUMBER_SIZE = 8;

    private UIAssetLoader() {
        loadAllAssets();
    }

    public static UIAssetLoader getInstance() {
        if (instance == null) {
            instance = new UIAssetLoader();
        }
        return instance;
    }

    private void loadAllAssets() {
        System.out.println(" Chargement des assets UI...");

        try {
            buttonsSheet = new Image(getClass().getResourceAsStream("/sprites/ui/Buttons.png"));
            System.out.println("  Buttons.png");
        } catch (Exception e) {
            System.err.println("  Buttons.png manquant");
        }

        try {
            iconsSheet = new Image(getClass().getResourceAsStream("/sprites/ui/Icons.png"));
            System.out.println("  Icons.png");
        } catch (Exception e) {
            System.err.println(" Icons.png manquant");
        }

        try {
            numbersSheet = new Image(getClass().getResourceAsStream("/sprites/ui/Numbers.png"));
            System.out.println("  Numbers.png");
        } catch (Exception e) {
            System.err.println("  Numbers.png manquant");
        }

        try {
            numbersLevelsSheet = new Image(getClass().getResourceAsStream("/sprites/ui/Numbers_levels.png"));
            System.out.println("  Numbers_levels.png");
        } catch (Exception e) {
            System.err.println("   Numbers_levels.png manquant");
        }

        try {
            levelsSheet = new Image(getClass().getResourceAsStream("/sprites/ui/Levels.png"));
            System.out.println(" Levels.png");
        } catch (Exception e) {
            System.err.println("  Levels.png manquant");
        }

        try {
            characterPanelSheet = new Image(getClass().getResourceAsStream("/sprites/ui/character_panel.png"));
            System.out.println("   character_panel.png");
        } catch (Exception e) {
            System.err.println("   character_panel.png manquant");
        }

        try {
            mainMenuBg = new Image(getClass().getResourceAsStream("/sprites/ui/Main_menu.png"));
            System.out.println("   Main_menu.png");
        } catch (Exception e) {
            System.err.println("  Main_menu.png manquant");
        }

        try {
            settingsBg = new Image(getClass().getResourceAsStream("/sprites/ui/Settings.png"));
            System.out.println("  Settings.png");
        } catch (Exception e) {
            System.err.println(" Settings.png manquant");
        }

        try {
            inventoryBg = new Image(getClass().getResourceAsStream("/sprites/ui/Inventory.png"));
            System.out.println("   Inventory.png");
        } catch (Exception e) {
            System.err.println("   Inventory.png manquant");
        }

        try {
            equipmentBg = new Image(getClass().getResourceAsStream("/sprites/ui/Equipment.png"));
            System.out.println("   Equipment.png");
        } catch (Exception e) {
            System.err.println("   Equipment.png manquant");
        }

        try {
            craftBg = new Image(getClass().getResourceAsStream("/sprites/ui/Craft.png"));
            System.out.println("  Craft.png");
        } catch (Exception e) {
            System.err.println("   Craft.png manquant");
        }

        try {
            shopBg = new Image(getClass().getResourceAsStream("/sprites/ui/Shop.png"));
            System.out.println("   Shop.png");
        } catch (Exception e) {
            System.err.println("   Shop.png manquant");
        }

        System.out.println(" Assets UI chargés !\n");
    }

    // ==================== BOUTONS (DÉCOUPAGE DEPUIS BUTTONS.PNG) ====================

    /**
     * Récupère UN bouton spécifique depuis la grille Buttons.png
     * Les boutons sont organisés en colonnes de 4 rangées chacune
     */
    public ImageView getButton(ButtonType type, double targetWidth, double targetHeight) {
        if (buttonsSheet == null) return new ImageView();

        Rectangle2D region = getButtonRegion(type);
        ImageView button = new ImageView(buttonsSheet);
        button.setViewport(region);
        button.setFitWidth(targetWidth);
        button.setFitHeight(targetHeight);
        button.setSmooth(false);
        button.setPreserveRatio(false);

        return button;
    }

    private Rectangle2D getButtonRegion(ButtonType type) {
        // Analyse de Buttons.png : 4 colonnes de variations, chaque bouton fait 16x16
        int col = 0, row = 0;

        switch (type) {
            // Colonne 1 (x=0) - Carrés verts simples
            case GREEN_SQUARE_1: col = 0; row = 0; break;
            case GREEN_SQUARE_2: col = 0; row = 1; break;
            case GREEN_SQUARE_3: col = 0; row = 2; break;
            case GREEN_SQUARE_4: col = 0; row = 3; break;

            // Colonne 2 (x=16) - Rectangles moyens
            case GREEN_RECT_1: col = 1; row = 0; break;
            case GREEN_RECT_2: col = 1; row = 1; break;
            case GREEN_RECT_3: col = 1; row = 2; break;
            case GREEN_RECT_4: col = 1; row = 3; break;

            // Colonne 3 (x=32) - Grands rectangles
            case GREEN_LARGE_1: col = 2; row = 0; break;
            case GREEN_LARGE_2: col = 2; row = 1; break;
            case GREEN_LARGE_3: col = 2; row = 2; break;
            case GREEN_LARGE_4: col = 2; row = 3; break;

            // Ligne spéciale "RESTART" (y=64-80)
            case RESTART: return new Rectangle2D(128, 64, 32, 16);

            // Textes sur boutons (colonne spéciale)
            case RESUME: return new Rectangle2D(0, 160, 48, 16);
            case RESTART_TEXT: return new Rectangle2D(48, 160, 48, 16);
            case SETTINGS: return new Rectangle2D(96, 160, 48, 16);
            case INVENTORY: return new Rectangle2D(0, 176, 48, 16);
            case EQUIPMENT: return new Rectangle2D(48, 176, 48, 16);
            case SHOP: return new Rectangle2D(96, 176, 48, 16);
            case CRAFT: return new Rectangle2D(0, 192, 48, 16);
            case QUIT: return new Rectangle2D(48, 192, 48, 16);

            default: col = 2; row = 1; // Défaut : gros bouton vert
        }

        return new Rectangle2D(col * BUTTON_WIDTH, row * BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    public enum ButtonType {
        GREEN_SQUARE_1, GREEN_SQUARE_2, GREEN_SQUARE_3, GREEN_SQUARE_4,
        GREEN_RECT_1, GREEN_RECT_2, GREEN_RECT_3, GREEN_RECT_4,
        GREEN_LARGE_1, GREEN_LARGE_2, GREEN_LARGE_3, GREEN_LARGE_4,
        RESTART, RESUME, RESTART_TEXT, SETTINGS, INVENTORY, EQUIPMENT, SHOP, CRAFT, QUIT
    }

    // ==================== ICÔNES (GRILLE 8x8) ====================

    /**
     * Récupère une icône depuis Icons.png (grille 8x8, icônes 16x16)
     */
    public ImageView getIcon(int index, double size) {
        if (iconsSheet == null) return new ImageView();

        int cols = 8;
        int row = index / cols;
        int col = index % cols;

        ImageView icon = new ImageView(iconsSheet);
        icon.setViewport(new Rectangle2D(col * ICON_SIZE, row * ICON_SIZE, ICON_SIZE, ICON_SIZE));
        icon.setFitWidth(size);
        icon.setFitHeight(size);
        icon.setSmooth(false);

        return icon;
    }

    // ==================== CHIFFRES ====================

    /**
     * Récupère un chiffre depuis Numbers.png (grille 10x10, chiffres 8x8)
     */
    public ImageView getNumber(int digit, double width, double height) {
        if (numbersSheet == null || digit < 0 || digit > 9) return new ImageView();

        // Les chiffres 0-9 sont sur la première ligne
        ImageView numView = new ImageView(numbersSheet);
        numView.setViewport(new Rectangle2D(digit * NUMBER_SIZE, 0, NUMBER_SIZE, NUMBER_SIZE));
        numView.setFitWidth(width);
        numView.setFitHeight(height);
        numView.setSmooth(false);

        return numView;
    }

    // ==================== BARRES HP/MANA ====================

    /**
     * Barre de vie COMPLÈTE depuis character_panel.png
     * Position : à gauche, barre rouge/verte
     */
    public ImageView getHealthBarFull(double width, double height) {
        if (characterPanelSheet == null) return new ImageView();

        ImageView bar = new ImageView(characterPanelSheet);
        // Barre HP pleine (64x16 pixels, colonne de gauche, ligne 2)
        bar.setViewport(new Rectangle2D(0, 16, 64, 16));
        bar.setFitWidth(width);
        bar.setFitHeight(height);
        bar.setSmooth(false);

        return bar;
    }

    /**
     * Barre de vie VIDE (fond gris)
     */
    public ImageView getHealthBarEmpty(double width, double height) {
        if (characterPanelSheet == null) return new ImageView();

        ImageView bar = new ImageView(characterPanelSheet);
        bar.setViewport(new Rectangle2D(0, 0, 64, 16));
        bar.setFitWidth(width);
        bar.setFitHeight(height);
        bar.setSmooth(false);

        return bar;
    }

    /**
     * Barre de mana COMPLÈTE
     */
    public ImageView getManaBarFull(double width, double height) {
        if (characterPanelSheet == null) return new ImageView();

        ImageView bar = new ImageView(characterPanelSheet);
        // Barre Mana bleue/cyan (ligne du bas)
        bar.setViewport(new Rectangle2D(0, 32, 64, 16));
        bar.setFitWidth(width);
        bar.setFitHeight(height);
        bar.setSmooth(false);

        return bar;
    }

    /**
     * Barre de mana VIDE
     */
    public ImageView getManaBarEmpty(double width, double height) {
        if (characterPanelSheet == null) return new ImageView();

        ImageView bar = new ImageView(characterPanelSheet);
        bar.setViewport(new Rectangle2D(0, 48, 64, 16));
        bar.setFitWidth(width);
        bar.setFitHeight(height);
        bar.setSmooth(false);

        return bar;
    }

    /**
     * Portrait du personnage (cercle avec portrait)
     */
    public ImageView getCharacterPortrait(double size) {
        if (characterPanelSheet == null) return new ImageView();

        ImageView portrait = new ImageView(characterPanelSheet);
        // Portrait dans le cercle (colonne de droite)
        portrait.setViewport(new Rectangle2D(64, 0, 32, 32));
        portrait.setFitWidth(size);
        portrait.setFitHeight(size);
        portrait.setSmooth(false);

        return portrait;
    }

    // ==================== NIVEAUX ====================

    /**
     * Indicateur de niveau depuis Levels.png
     */
    public ImageView getLevelIndicator(int level, double size) {
        if (levelsSheet == null) return new ImageView();

        // Levels.png : grille 3x3 de cercles (32x32 chacun)
        int cols = 3;
        int levelSize = 32;

        int index = Math.min(level - 1, 8);
        int row = index / cols;
        int col = index % cols;

        ImageView levelView = new ImageView(levelsSheet);
        levelView.setViewport(new Rectangle2D(col * levelSize, row * levelSize, levelSize, levelSize));
        levelView.setFitWidth(size);
        levelView.setFitHeight(size);
        levelView.setSmooth(false);

        return levelView;
    }

    // ==================== FENÊTRES COMPLÈTES ====================

    public Image getMainMenuBackground() { return mainMenuBg; }
    public Image getSettingsBackground() { return settingsBg; }
    public Image getInventoryBackground() { return inventoryBg; }
    public Image getEquipmentBackground() { return equipmentBg; }
    public Image getCraftBackground() { return craftBg; }
    public Image getShopBackground() { return shopBg; }
}