package com.eremon.views;

import com.eremon.models.player.PorteCendre;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Rectangle2D;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.util.Duration;

public class HUDView extends Pane {

    private com.eremon.models.entity.Player player;

    // Assets UI
    private Image characterPanelSheet;
    private Image iconsSheet;
    private Image levelsSheet;

    // Barres de vie/mana (sprites)
    private ImageView hpBarView;
    private ImageView manaBarView;
    private Rectangle hpClip;
    private Rectangle manaClip;

    // Textes
    private Text hpText;
    private Text manaText;
    private Text levelText;
    private Text positionText;
    private Text stateText;

    // Régions dans character_panel.png (à ajuster selon votre fichier)
    private static final Rectangle2D HP_BAR_REGION = new Rectangle2D(192, 16, 160, 24);
    private static final Rectangle2D MANA_BAR_REGION = new Rectangle2D(192, 40, 160, 24);

    public HUDView(com.eremon.models.entity.Player player) {
        this.player = player;
        this.setPrefSize(1280, 720);
        this.setPickOnBounds(false); // Permet les clics à travers le HUD

        loadAssets();
        setupTopPanel();
        setupBottomInfo();
    }

    private void loadAssets() {
        try {
            characterPanelSheet = new Image(getClass().getResourceAsStream("/ui/character_panel.png"));
            System.out.println("✅ character_panel.png chargé pour HUD");
        } catch (Exception e) {
            System.err.println("⚠️ character_panel.png non trouvé pour HUD");
        }

        try {
            iconsSheet = new Image(getClass().getResourceAsStream("/ui/Icons.png"));
            System.out.println("✅ Icons.png chargé");
        } catch (Exception e) {
            System.err.println("⚠️ Icons.png non trouvé");
        }

        try {
            levelsSheet = new Image(getClass().getResourceAsStream("/ui/Levels.png"));
            System.out.println("✅ Levels.png chargé");
        } catch (Exception e) {
            System.err.println("⚠️ Levels.png non trouvé");
        }
    }

    private void setupTopPanel() {
        // Panneau semi-transparent en haut
        Rectangle panelBg = new Rectangle(0, 0, 1280, 100);
        panelBg.setFill(Color.rgb(0, 0, 0, 0.6));
        this.getChildren().add(panelBg);

        // Bordure décorative
        Rectangle border = new Rectangle(0, 98, 1280, 2);
        border.setFill(Color.web("#8B4513"));
        this.getChildren().add(border);

        // Nom du joueur
        Text playerName = new Text(player.getName());
        playerName.setLayoutX(20);
        playerName.setLayoutY(30);
        playerName.setFill(Color.GOLD);
        playerName.setFont(Font.font("Serif", 24));
        playerName.setStyle("-fx-font-weight: bold; -fx-effect: dropshadow(gaussian, black, 4, 0.8, 2, 2);");
        this.getChildren().add(playerName);

        // === BARRE DE VIE ===
        double hpX = 20;
        double hpY = 50;

        if (characterPanelSheet != null) {
            hpBarView = new ImageView(characterPanelSheet);
            hpBarView.setViewport(HP_BAR_REGION);
            hpBarView.setLayoutX(hpX);
            hpBarView.setLayoutY(hpY);
            hpBarView.setSmooth(false);

            hpClip = new Rectangle(HP_BAR_REGION.getWidth(), HP_BAR_REGION.getHeight());
            hpBarView.setClip(hpClip);

            this.getChildren().add(hpBarView);
        } else {
            // Fallback: barre classique
            Rectangle hpBg = new Rectangle(hpX, hpY, 200, 24);
            hpBg.setFill(Color.rgb(40, 10, 10));
            hpBg.setStroke(Color.RED);
            hpBg.setStrokeWidth(2);
            hpBg.setArcWidth(8);
            hpBg.setArcHeight(8);
            this.getChildren().add(hpBg);
        }

        hpText = new Text(player.getHp() + " / " + player.getMaxHp());
        hpText.setLayoutX(hpX + 55);
        hpText.setLayoutY(hpY + 18);
        hpText.setFill(Color.WHITE);
        hpText.setFont(Font.font("Arial", 16));
        hpText.setStyle("-fx-font-weight: bold; -fx-effect: dropshadow(gaussian, black, 3, 1, 1, 1);");
        this.getChildren().add(hpText);

        // === BARRE DE MANA ===
        double manaX = 250;
        double manaY = 50;

        if (characterPanelSheet != null) {
            manaBarView = new ImageView(characterPanelSheet);
            manaBarView.setViewport(MANA_BAR_REGION);
            manaBarView.setLayoutX(manaX);
            manaBarView.setLayoutY(manaY);
            manaBarView.setSmooth(false);

            double manaPercent = (double) player.getMana() / player.getMaxMana();
            manaClip = new Rectangle(
                    MANA_BAR_REGION.getWidth() * manaPercent,
                    MANA_BAR_REGION.getHeight()
            );
            manaBarView.setClip(manaClip);

            this.getChildren().add(manaBarView);
        } else {
            Rectangle manaBg = new Rectangle(manaX, manaY, 200, 24);
            manaBg.setFill(Color.rgb(10, 10, 40));
            manaBg.setStroke(Color.BLUE);
            manaBg.setStrokeWidth(2);
            manaBg.setArcWidth(8);
            manaBg.setArcHeight(8);
            this.getChildren().add(manaBg);
        }

        manaText = new Text(player.getMana() + " / " + player.getMaxMana());
        manaText.setLayoutX(manaX + 55);
        manaText.setLayoutY(manaY + 18);
        manaText.setFill(Color.LIGHTBLUE);
        manaText.setFont(Font.font("Arial", 16));
        manaText.setStyle("-fx-font-weight: bold; -fx-effect: dropshadow(gaussian, black, 3, 1, 1, 1);");
        this.getChildren().add(manaText);

        // === NIVEAU ===
        levelText = new Text("Niveau " + player.getLevel());
        levelText.setLayoutX(500);
        levelText.setLayoutY(65);
        levelText.setFill(Color.web("#FFD700"));
        levelText.setFont(Font.font("Serif", 20));
        levelText.setStyle("-fx-font-weight: bold; -fx-effect: dropshadow(gaussian, black, 3, 0.8, 2, 2);");
        this.getChildren().add(levelText);
    }

    private void setupBottomInfo() {
        // Mini panneau en bas à droite
        Rectangle miniPanel = new Rectangle(1030, 630, 240, 80);
        miniPanel.setFill(Color.rgb(0, 0, 0, 0.6));
        miniPanel.setStroke(Color.web("#8B4513"));
        miniPanel.setStrokeWidth(2);
        miniPanel.setArcWidth(10);
        miniPanel.setArcHeight(10);
        this.getChildren().add(miniPanel);

        // Position
        positionText = new Text("Position: (0, 0)");
        positionText.setLayoutX(1045);
        positionText.setLayoutY(655);
        positionText.setFill(Color.web("#CCCCCC"));
        positionText.setFont(Font.font("Arial", 14));
        this.getChildren().add(positionText);

        // État
        stateText = new Text("État: EXPLORATION");
        stateText.setLayoutX(1045);
        stateText.setLayoutY(675);
        stateText.setFill(Color.LIGHTGREEN);
        stateText.setFont(Font.font("Arial", 14));
        this.getChildren().add(stateText);

        // Instructions
        Text instructions = new Text("ESC: Menu | I: Inventaire");
        instructions.setLayoutX(1045);
        instructions.setLayoutY(695);
        instructions.setFill(Color.web("#888888"));
        instructions.setFont(Font.font("Arial", 12));
        this.getChildren().add(instructions);
    }

    public void update(String gameState) {
        // Mettre à jour les barres de vie et mana
        updateHealthBar();
        updateManaBar();

        // Mettre à jour les textes
        hpText.setText(Math.max(0, player.getHp()) + " / " + player.getMaxHp());
        manaText.setText(Math.max(0, player.getMana()) + " / " + player.getMaxMana());
        levelText.setText("Niveau " + player.getLevel());
        positionText.setText(String.format("Position: (%d, %d)", (int)player.getX(), (int)player.getY()));

        // Mettre à jour l'état
        stateText.setText("État: " + gameState);
        if (gameState.equals("COMBAT")) {
            stateText.setFill(Color.RED);
        } else {
            stateText.setFill(Color.LIGHTGREEN);
        }
    }

    private void updateHealthBar() {
        if (hpBarView != null && hpClip != null) {
            double hpPercent = Math.max(0, (double) player.getHp() / player.getMaxHp());
            double targetWidth = HP_BAR_REGION.getWidth() * hpPercent;

            Timeline anim = new Timeline(
                    new KeyFrame(Duration.millis(300),
                            new KeyValue(hpClip.widthProperty(), targetWidth)
                    )
            );
            anim.play();
        }
    }

    private void updateManaBar() {
        if (manaBarView != null && manaClip != null) {
            double manaPercent = Math.max(0, (double) player.getMana() / player.getMaxMana());
            double targetWidth = MANA_BAR_REGION.getWidth() * manaPercent;

            Timeline anim = new Timeline(
                    new KeyFrame(Duration.millis(300),
                            new KeyValue(manaClip.widthProperty(), targetWidth)
                    )
            );
            anim.play();
        }
    }
}