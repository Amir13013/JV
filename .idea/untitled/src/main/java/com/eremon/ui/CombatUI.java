package com.eremon.ui;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.animation.TranslateTransition;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.util.Duration;
import com.eremon.models.entity.Player;
import com.eremon.models.entity.Enemy;
import com.eremon.Skill;

public class CombatUI extends Pane {
    private Player player;
    private Enemy enemy;
    private Text combatLog;
    private Runnable onCombatEnd;
    private boolean playerTurn = true;

    private ImageView playerSprite;
    private ImageView enemySprite;
    private Image hudSheet;

    private ImageView playerHpBar;
    private ImageView playerManaBar;
    private ImageView enemyHpBar;

    private Rectangle playerHpClip;
    private Rectangle playerManaClip;
    private Rectangle enemyHpClip;

    private Text playerHpText;
    private Text enemyHpText;
    private Text playerManaText;

    private Pane leftPanel;
    private Pane rightPanel;

    private static final Rectangle2D PLAYER_HP_REGION   = new Rectangle2D(192, 16, 160, 24);
    private static final Rectangle2D PLAYER_MANA_REGION = new Rectangle2D(192, 40, 160, 24);
    private static final Rectangle2D ENEMY_HP_REGION    = new Rectangle2D(192, 64, 160, 24);

    public CombatUI(Player player, Enemy enemy, String playerSpritePath, String enemySpritePath, Runnable onCombatEnd) {
        this.player = player;
        this.enemy = enemy;
        this.onCombatEnd = onCombatEnd;

        this.setPrefSize(1280, 720);
        this.setStyle("-fx-background-color: linear-gradient(to bottom, #0a0a0a 0%, #1a0a1a 50%, #0a0a1a 100%);");

        try {
            hudSheet = new Image(getClass().getResourceAsStream("/ui/character_panel.png"));
        } catch (Exception e) {
            System.err.println(" Impossible de charger /ui/character_panel.png pour le HUD");
            hudSheet = null;
        }

        setupBattleground();
        setupSprites(playerSpritePath, enemySpritePath);
        setupPlayerUI();
        setupEnemyUI();
        setupActionPanel();
        setupCombatLog();
    }

    private void setupBattleground() {
        Rectangle ground = new Rectangle(0, 480, 1280, 240);
        ground.setFill(Color.rgb(15, 10, 20, 0.8));
        this.getChildren().add(ground);

        Rectangle line1 = new Rectangle(0, 478, 1280, 2);
        line1.setFill(Color.rgb(150, 50, 50, 0.7));
        Rectangle line2 = new Rectangle(0, 480, 1280, 2);
        line2.setFill(Color.rgb(100, 50, 100, 0.5));
        this.getChildren().addAll(line1, line2);
    }

    private void setupSprites(String playerPath, String enemyPath) {
        try {
            Image playerImg = new Image(getClass().getResourceAsStream(playerPath));
            playerSprite = new ImageView(playerImg);
            playerSprite.setFitWidth(200);
            playerSprite.setFitHeight(280);
            playerSprite.setLayoutX(130);
            playerSprite.setLayoutY(200);
            playerSprite.setPreserveRatio(true);
            this.getChildren().add(playerSprite);
            System.out.println(" Sprite de combat chargé: " + playerPath);
        } catch (Exception e) {
            System.err.println("Sprite combat joueur non trouvé: " + playerPath);
            try {
                Image playerImg = new Image(getClass().getResourceAsStream("/sprites/overworld/MAGE_03.png"));
                playerSprite = new ImageView(playerImg);
                playerSprite.setFitWidth(200);
                playerSprite.setFitHeight(200);
                playerSprite.setLayoutX(130);
                playerSprite.setLayoutY(240);
                this.getChildren().add(playerSprite);
            } catch (Exception ex) {
                Rectangle fallback = new Rectangle(130, 240, 100, 100);
                fallback.setFill(Color.GREEN);
                this.getChildren().add(fallback);
            }
        }


        try {
            String demonSprite;
            if (enemy.getName().contains("Vocifere")) {
                int choice = (int)(Math.random() * 3);
                demonSprite = "/sprites/combat/demon_idle_" + (choice == 0 ? "1" : choice == 1 ? "3" : "5") + ".png";
            } else {
                int choice = (int)(Math.random() * 3);
                demonSprite = "/sprites/combat/demon_idle_" + (choice == 0 ? "2" : choice == 1 ? "4" : "6") + ".png";
            }

            Image enemyImg = new Image(getClass().getResourceAsStream(demonSprite));
            enemySprite = new ImageView(enemyImg);
            enemySprite.setFitWidth(280);
            enemySprite.setFitHeight(280);
            enemySprite.setLayoutX(860);
            enemySprite.setLayoutY(180);
            enemySprite.setPreserveRatio(true);
            this.getChildren().add(enemySprite);
        } catch (Exception e) {
            System.err.println("Sprite ennemi non trouvé");
            Rectangle fallback = new Rectangle(880, 200, 120, 120);
            fallback.setFill(Color.DARKRED);
            this.getChildren().add(fallback);
        }
    }

    private void setupPlayerUI() {
        leftPanel = new Pane();
        leftPanel.setPrefSize(380, 200);
        leftPanel.setLayoutX(15);
        leftPanel.setLayoutY(505);

        try {
            Image panelBg = new Image(getClass().getResourceAsStream("/ui/character_panel.png"));
            ImageView panelView = new ImageView(panelBg);
            panelView.setViewport(new Rectangle2D(0, 128, 190, 96));
            panelView.setFitWidth(380);
            panelView.setFitHeight(200);
            panelView.setOpacity(0.95);
            panelView.setSmooth(false);
            leftPanel.getChildren().add(panelView);
        } catch (Exception e) {
            leftPanel.setStyle(
                    "-fx-background-color: rgba(25, 15, 15, 0.95);" +
                            "-fx-border-color: #8b4513;" +
                            "-fx-border-width: 3;" +
                            "-fx-border-radius: 12;" +
                            "-fx-background-radius: 12;"
            );
        }

        Text playerName = new Text(player.getName());
        playerName.setLayoutX(20);
        playerName.setLayoutY(35);
        playerName.setFill(Color.GOLD);
        playerName.setFont(Font.font("Serif", 26));
        playerName.setStyle("-fx-font-weight: bold; -fx-effect: dropshadow(gaussian, black, 4, 0.8, 2, 2);");

        double hpX = 20;
        double hpY = 60;
        double manaX = 20;
        double manaY = 100;

        if (hudSheet != null) {
            playerHpBar = new ImageView(hudSheet);
            playerHpBar.setViewport(PLAYER_HP_REGION);
            playerHpBar.setLayoutX(hpX);
            playerHpBar.setLayoutY(hpY);
            playerHpBar.setSmooth(false);
            playerHpClip = new Rectangle(PLAYER_HP_REGION.getWidth(), PLAYER_HP_REGION.getHeight());
            playerHpBar.setClip(playerHpClip);

            playerHpText = new Text(player.getHp() + " / " + player.getMaxHp());
            playerHpText.setLayoutX(hpX + PLAYER_HP_REGION.getWidth() / 2 - 35);
            playerHpText.setLayoutY(hpY - 4);
            playerHpText.setFill(Color.WHITE);
            playerHpText.setFont(Font.font("Arial", 18));
            playerHpText.setStyle("-fx-font-weight: bold; -fx-effect: dropshadow(gaussian, black, 3, 1, 1, 1);");

            playerManaBar = new ImageView(hudSheet);
            playerManaBar.setViewport(PLAYER_MANA_REGION);
            playerManaBar.setLayoutX(manaX);
            playerManaBar.setLayoutY(manaY);
            playerManaBar.setSmooth(false);

            double manaPercent = player.getMana() / (double) player.getMaxMana();
            playerManaClip = new Rectangle(
                    PLAYER_MANA_REGION.getWidth() * manaPercent,
                    PLAYER_MANA_REGION.getHeight()
            );
            playerManaBar.setClip(playerManaClip);

            playerManaText = new Text("Mana : " + player.getMana() + " / " + player.getMaxMana());
            playerManaText.setLayoutX(manaX + PLAYER_MANA_REGION.getWidth() / 2 - 60);
            playerManaText.setLayoutY(manaY - 4);
            playerManaText.setFill(Color.LIGHTBLUE);
            playerManaText.setFont(Font.font("Arial", 16));
            playerManaText.setStyle("-fx-font-weight: bold; -fx-effect: dropshadow(gaussian, black, 3, 1, 1, 1);");

            leftPanel.getChildren().addAll(playerHpBar, playerManaBar, playerHpText, playerManaText);
        } else {
            Rectangle hpBg = new Rectangle(hpX, hpY, 340, 24);
            hpBg.setFill(Color.rgb(30, 10, 10));
            hpBg.setStroke(Color.rgb(180, 60, 60));
            hpBg.setArcWidth(10);
            hpBg.setArcHeight(10);

            playerHpBar = new ImageView();
            playerHpClip = new Rectangle();

            playerHpText = new Text(player.getHp() + " / " + player.getMaxHp());
            playerHpText.setLayoutX(hpX + 120);
            playerHpText.setLayoutY(hpY + 18);
            playerHpText.setFill(Color.WHITE);

            Rectangle manaBg = new Rectangle(manaX, manaY, 340, 20);
            manaBg.setFill(Color.rgb(10, 10, 30));
            manaBg.setStroke(Color.rgb(60, 120, 220));
            manaBg.setArcWidth(10);
            manaBg.setArcHeight(10);

            playerManaBar = new ImageView();
            playerManaClip = new Rectangle();

            playerManaText = new Text("Mana : " + player.getMana() + " / " + player.getMaxMana());
            playerManaText.setLayoutX(manaX + 100);
            playerManaText.setLayoutY(manaY + 16);
            playerManaText.setFill(Color.LIGHTBLUE);

            leftPanel.getChildren().addAll(hpBg, manaBg, playerHpText, playerManaText);
        }

        Text stats = new Text("⚔ ATK: " + player.getAttack() + "  🛡 DEF: " + player.getDefense() + "  ⭐ LVL: " + player.getLevel());
        stats.setLayoutX(20);
        stats.setLayoutY(170);
        stats.setFill(Color.rgb(220, 220, 220));
        stats.setFont(Font.font("Arial", 16));
        stats.setStyle("-fx-effect: dropshadow(gaussian, black, 2, 0.8, 1, 1);");

        leftPanel.getChildren().addAll(playerName, stats);
        this.getChildren().add(leftPanel);
    }

    private void setupEnemyUI() {
        Pane enemyPanel = new Pane();
        enemyPanel.setPrefSize(380, 120);
        enemyPanel.setLayoutX(885);
        enemyPanel.setLayoutY(90);
        enemyPanel.setStyle(
                "-fx-background-color: rgba(40, 10, 40, 0.92);" +
                        "-fx-border-color: #8b008b;" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(139, 0, 139, 0.8), 12, 0.7, 0, 0);"
        );

        Text enemyName = new Text(enemy.getName().toUpperCase());
        enemyName.setLayoutX(20);
        enemyName.setLayoutY(30);
        enemyName.setFill(Color.web("#ff1493"));
        enemyName.setFont(Font.font("Serif", 24));
        enemyName.setStyle("-fx-font-weight: bold; -fx-effect: dropshadow(gaussian, black, 4, 0.8, 2, 2);");

        double hpX = 20;
        double hpY = 65;

        if (hudSheet != null) {
            enemyHpBar = new ImageView(hudSheet);
            enemyHpBar.setViewport(ENEMY_HP_REGION);
            enemyHpBar.setLayoutX(hpX);
            enemyHpBar.setLayoutY(hpY);
            enemyHpBar.setSmooth(false);
            enemyHpClip = new Rectangle(ENEMY_HP_REGION.getWidth(), ENEMY_HP_REGION.getHeight());
            enemyHpBar.setClip(enemyHpClip);

            enemyHpText = new Text(enemy.getHp() + " / " + enemy.getMaxHp());
            enemyHpText.setLayoutX(hpX + ENEMY_HP_REGION.getWidth() / 2 - 35);
            enemyHpText.setLayoutY(hpY - 4);
            enemyHpText.setFill(Color.WHITE);
            enemyHpText.setFont(Font.font("Arial", 20));
            enemyHpText.setStyle("-fx-font-weight: bold; -fx-effect: dropshadow(gaussian, black, 3, 1, 1, 1);");

            enemyPanel.getChildren().addAll(enemyHpBar, enemyHpText, enemyName);
        } else {
            Rectangle hpBg = new Rectangle(hpX, hpY, 340, 24);
            hpBg.setFill(Color.rgb(30, 10, 30));
            hpBg.setStroke(Color.rgb(180, 60, 180));
            hpBg.setArcWidth(10);
            hpBg.setArcHeight(10);

            enemyHpBar = new ImageView();
            enemyHpClip = new Rectangle();

            enemyHpText = new Text(enemy.getHp() + " / " + enemy.getMaxHp());
            enemyHpText.setLayoutX(165);
            enemyHpText.setLayoutY(hpY + 18);
            enemyHpText.setFill(Color.WHITE);
            enemyHpText.setFont(Font.font("Arial", 20));

            enemyPanel.getChildren().addAll(hpBg, enemyHpText, enemyName);
        }

        this.getChildren().add(enemyPanel);
    }

    private void setupActionPanel() {
        rightPanel = new Pane();
        rightPanel.setPrefSize(865, 200);
        rightPanel.setLayoutX(400);
        rightPanel.setLayoutY(505);

        try {
            Image actionBg = new Image(getClass().getResourceAsStream("/ui/Action_panel.png"));
            ImageView actionView = new ImageView(actionBg);
            actionView.setFitWidth(865);
            actionView.setFitHeight(200);
            actionView.setOpacity(0.90);
            actionView.setSmooth(false);
            rightPanel.getChildren().add(actionView);
        } catch (Exception e) {
            rightPanel.setStyle(
                    "-fx-background-color: rgba(35, 25, 15, 0.95);" +
                            "-fx-border-color: #d4a574;" +
                            "-fx-border-width: 3;" +
                            "-fx-border-radius: 12;" +
                            "-fx-background-radius: 12;"
            );
        }

        Text title = new Text("  ACTIONS DE COMBAT ");
        title.setLayoutX(280);
        title.setLayoutY(32);
        title.setFill(Color.GOLD);
        title.setFont(Font.font("Serif", 22));
        title.setStyle("-fx-font-weight: bold; -fx-effect: dropshadow(gaussian, black, 4, 0.9, 2, 2);");
        rightPanel.getChildren().add(title);

        GridPane skillGrid = new GridPane();
        skillGrid.setLayoutX(25);
        skillGrid.setLayoutY(50);
        skillGrid.setHgap(18);
        skillGrid.setVgap(18);

        int row = 0, col = 0;
        for (Skill skill : player.getSkills()) {
            Button btn = createSkillButton(skill);
            skillGrid.add(btn, col, row);
            col++;
            if (col > 1) {
                col = 0;
                row++;
            }
        }

        VBox utilityBox = new VBox(18);
        utilityBox.setLayoutX(705);
        utilityBox.setLayoutY(50);

        Button potionBtn = createUtilityButton(" Potion", Color.web("#2d6d2d"));
        potionBtn.setOnAction(e -> usePotion());

        Button fleeBtn = createUtilityButton(" Fuir", Color.web("#6d2d2d"));
        fleeBtn.setOnAction(e -> flee());

        utilityBox.getChildren().addAll(potionBtn, fleeBtn);

        rightPanel.getChildren().addAll(skillGrid, utilityBox);
        this.getChildren().add(rightPanel);
    }

    private Button createSkillButton(final Skill skill) {
        final Button btn = new Button();

        VBox content = new VBox(3);
        content.setAlignment(Pos.CENTER);

        final Text nameText = new Text(skill.getName());
        nameText.setFill(Color.WHITE);
        nameText.setFont(Font.font("Arial", 15));
        nameText.setStyle("-fx-font-weight: bold;");

        final Text infoText = new Text("DMG: " + skill.getDamage() + "  MP: " + skill.getManaCost());
        infoText.setFill(Color.rgb(200, 200, 200));
        infoText.setFont(Font.font("Arial", 12));

        content.getChildren().addAll(nameText, infoText);
        btn.setGraphic(content);

        btn.setPrefSize(320, 60);

        final String normalStyle =
                "-fx-background-color: linear-gradient(to bottom, #4a2511, #2d1508);" +
                        "-fx-border-color: #8b4513;" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, black, 6, 0.5, 0, 3);";

        final String hoverStyle =
                "-fx-background-color: linear-gradient(to bottom, #6b3621, #4d250f);" +
                        "-fx-border-color: gold;" +
                        "-fx-border-width: 4;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, gold, 12, 0.8, 0, 0);";

        btn.setStyle(normalStyle);

        btn.setOnMouseEntered(e -> {
            btn.setStyle(hoverStyle);
            nameText.setFill(Color.GOLD);
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), btn);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle(normalStyle);
            nameText.setFill(Color.WHITE);
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), btn);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });

        btn.setOnAction(e -> useSkill(skill));

        return btn;
    }

    private Button createUtilityButton(String text, Color baseColor) {
        final Button btn = new Button(text);
        btn.setPrefSize(135, 60);

        String colorHex = String.format("#%02x%02x%02x",
                (int)(baseColor.getRed() * 255),
                (int)(baseColor.getGreen() * 255),
                (int)(baseColor.getBlue() * 255));

        final String normalStyle =
                "-fx-background-color: linear-gradient(to bottom, " + colorHex + ", derive(" + colorHex + ", -30%));" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-color: derive(" + colorHex + ", 20%);" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, black, 6, 0.5, 0, 3);";

        final String hoverStyle =
                "-fx-background-color: linear-gradient(to bottom, derive(" + colorHex + ", 20%), " + colorHex + ");" +
                        "-fx-text-fill: lightgreen;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-color: lightgreen;" +
                        "-fx-border-width: 4;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, lime, 12, 0.8, 0, 0);";

        btn.setStyle(normalStyle);

        btn.setOnMouseEntered(e -> {
            btn.setStyle(hoverStyle);
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), btn);
            scale.setToX(1.08);
            scale.setToY(1.08);
            scale.play();
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle(normalStyle);
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), btn);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });

        return btn;
    }

    private void setupCombatLog() {
        Pane logBox = new Pane();
        logBox.setPrefSize(860, 50);
        logBox.setLayoutX(400);
        logBox.setLayoutY(445);
        logBox.setStyle(
                "-fx-background-color: rgba(10, 10, 10, 0.85);" +
                        "-fx-border-color: #8b7355;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;"
        );

        combatLog = new Text(" À ton tour ! Choisis une action.");
        combatLog.setLayoutX(20);
        combatLog.setLayoutY(30);
        combatLog.setFill(Color.YELLOW);
        combatLog.setFont(Font.font("Serif", 20));
        combatLog.setStyle("-fx-font-weight: bold; -fx-effect: dropshadow(gaussian, black, 4, 0.9, 2, 2);");
        combatLog.setWrappingWidth(820);

        logBox.getChildren().add(combatLog);
        this.getChildren().add(logBox);
    }

    private void playHitAnimation(ImageView target, boolean isPlayer) {
        TranslateTransition shake1 = new TranslateTransition(Duration.millis(40), target);
        shake1.setByX(isPlayer ? -20 : 20);

        TranslateTransition shake2 = new TranslateTransition(Duration.millis(40), target);
        shake2.setByX(isPlayer ? 20 : -20);

        TranslateTransition shake3 = new TranslateTransition(Duration.millis(40), target);
        shake3.setByX(isPlayer ? -15 : 15);

        TranslateTransition shake4 = new TranslateTransition(Duration.millis(40), target);
        shake4.setByX(isPlayer ? 15 : -15);

        TranslateTransition reset = new TranslateTransition(Duration.millis(50), target);
        reset.setToX(0);

        FadeTransition flash = new FadeTransition(Duration.millis(80), target);
        flash.setFromValue(1.0);
        flash.setToValue(0.2);
        flash.setCycleCount(4);
        flash.setAutoReverse(true);

        shake1.setOnFinished(e -> shake2.play());
        shake2.setOnFinished(e -> shake3.play());
        shake3.setOnFinished(e -> shake4.play());
        shake4.setOnFinished(e -> reset.play());

        shake1.play();
        flash.play();
    }

    private void playAttackAnimation(ImageView attacker, boolean isPlayerAttacking) {
        TranslateTransition dash = new TranslateTransition(Duration.millis(150), attacker);
        dash.setByX(isPlayerAttacking ? 150 : -150);

        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(150), attacker);
        scaleUp.setToX(1.2);
        scaleUp.setToY(1.2);

        TranslateTransition retreat = new TranslateTransition(Duration.millis(150), attacker);
        retreat.setByX(isPlayerAttacking ? -150 : 150);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(150), attacker);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        ParallelTransition attack = new ParallelTransition(dash, scaleUp);
        ParallelTransition back = new ParallelTransition(retreat, scaleDown);

        attack.setOnFinished(e -> back.play());
        attack.play();
    }

    private void useSkill(Skill skill) {
        if (!playerTurn) return;

        if (skill.getManaCost() > player.getMana()) {
            updateLog(" Pas assez de mana pour " + skill.getName() + " !");
            shakePanel(rightPanel);
            return;
        }

        player.setMana(player.getMana() - skill.getManaCost());
        int damage = skill.getDamage() + player.getAttack() - enemy.getDefense();
        damage = Math.max(1, damage);

        playAttackAnimation(playerSprite, true);

        final int finalDamage = damage;
        new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {}

            javafx.application.Platform.runLater(() -> {
                enemy.takeDamage(finalDamage);
                playHitAnimation(enemySprite, false);
                updateLog(" " + player.getName() + " utilise " + skill.getName() + " !  -" + finalDamage + " HP !");
                updateBars();

                if (enemy.isDead()) {
                    endCombat(true);
                    return;
                }

                playerTurn = false;
                enemyTurn();
            });
        }).start();
    }

    private void enemyTurn() {
        new Thread(() -> {
            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {}

            javafx.application.Platform.runLater(() -> {
                Skill enemySkill = enemy.getSkills().get((int)(Math.random() * enemy.getSkills().size()));
                int damage = enemySkill.getDamage() + enemy.getAttack() - player.getDefense();
                damage = Math.max(1, damage);

                playAttackAnimation(enemySprite, false);

                final int finalDamage = damage;
                new Thread(() -> {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {}

                    javafx.application.Platform.runLater(() -> {
                        player.takeDamage(finalDamage);
                        playHitAnimation(playerSprite, true);
                        updateLog(" " + enemy.getName() + " utilise " + enemySkill.getName() + " " + finalDamage + " HP !");
                        updateBars();

                        if (player.isDead()) {
                            endCombat(false);
                            return;
                        }

                        playerTurn = true;
                        updateLog(" À ton tour ! Choisis une action.");
                    });
                }).start();
            });
        }).start();
    }

    private void usePotion() {
        if (!playerTurn) return;
        updateLog(" Pas de potion dans l'inventaire !");
        shakePanel(leftPanel);
    }

    private void flee() {
        updateLog(" Tu prends la fuite !");

        FadeTransition fadeOut = new FadeTransition(Duration.millis(800), this);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> endCombat(false));
        fadeOut.play();
    }

    private void shakePanel(Pane panel) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(50), panel);
        shake.setByX(-10);
        shake.setCycleCount(4);
        shake.setAutoReverse(true);
        shake.play();
    }

    private void updateBars() {
        if (hudSheet != null) {
            double playerHpPercent = Math.max(0, (double) player.getHp() / player.getMaxHp());
            double targetPlayerHpWidth = PLAYER_HP_REGION.getWidth() * playerHpPercent;

            Timeline hpAnim = new Timeline(
                    new KeyFrame(Duration.millis(400),
                            new KeyValue(playerHpClip.widthProperty(), targetPlayerHpWidth)
                    )
            );
            hpAnim.play();

            double playerManaPercent = Math.max(0, (double) player.getMana() / player.getMaxMana());
            double targetPlayerManaWidth = PLAYER_MANA_REGION.getWidth() * playerManaPercent;

            Timeline manaAnim = new Timeline(
                    new KeyFrame(Duration.millis(400),
                            new KeyValue(playerManaClip.widthProperty(), targetPlayerManaWidth)
                    )
            );
            manaAnim.play();

            double enemyHpPercent = Math.max(0, (double) enemy.getHp() / enemy.getMaxHp());
            double targetEnemyHpWidth = ENEMY_HP_REGION.getWidth() * enemyHpPercent;

            Timeline enemyHpAnim = new Timeline(
                    new KeyFrame(Duration.millis(400),
                            new KeyValue(enemyHpClip.widthProperty(), targetEnemyHpWidth)
                    )
            );
            enemyHpAnim.play();
        }

        playerHpText.setText(Math.max(0, player.getHp()) + " / " + player.getMaxHp());
        playerManaText.setText("Mana : " + Math.max(0, player.getMana()) + " / " + player.getMaxMana());
        enemyHpText.setText(Math.max(0, enemy.getHp()) + " / " + enemy.getMaxHp());
    }

    private void updateLog(String message) {
        combatLog.setText(message);
        FadeTransition flash = new FadeTransition(Duration.millis(200), combatLog);
        flash.setFromValue(0.3);
        flash.setToValue(1.0);
        flash.play();
    }

    private void endCombat(boolean victory) {
        if (victory) {
            int xp = 50;
            player.gainXP(xp);
            updateLog(" VICTOIRE ! +" + xp + " XP | Niveau " + player.getLevel());

            ScaleTransition grow = new ScaleTransition(Duration.millis(600), playerSprite);
            grow.setToX(1.3);
            grow.setToY(1.3);
            grow.setCycleCount(2);
            grow.setAutoReverse(true);
            grow.play();
        } else {
            updateLog(" DÉFAITE... Tu retournes à l'exploration.");
        }

        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {}
            javafx.application.Platform.runLater(() -> {
                if (onCombatEnd != null) onCombatEnd.run();
            });
        }).start();
    }
}