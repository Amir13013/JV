package com.eremon.views;

import javafx.beans.value.ChangeListener;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.effect.*;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import javafx.geometry.Pos;

/**
 * MENU PRINCIPAL - DARK FANTASY STYLE
 */
public class MenuView extends Pane {

    private Runnable onStartGame;
    private Runnable onSettings;
    private Runnable onQuit;

    private int selectedOption = 0;
    private String[] menuOptions = {"COMMENCER L'AVENTURE", "PARAMETRES", "QUITTER"};
    private Pane[] buttonContainers;

    public MenuView(Runnable onStartGame, Runnable onSettings, Runnable onQuit) {
        this.onStartGame = onStartGame;
        this.onSettings = onSettings;
        this.onQuit = onQuit;

        // Écouter les changements de taille
        ChangeListener<Number> resizeListener = (obs, oldVal, newVal) -> recreateLayout();
        widthProperty().addListener(resizeListener);
        heightProperty().addListener(resizeListener);

        recreateLayout();
        playIntroAnimation();
    }

    private void recreateLayout() {
        getChildren().clear();

        double sceneW = getWidth() > 0 ? getWidth() : 1280;
        double sceneH = getHeight() > 0 ? getHeight() : 720;

        setupBackground(sceneW, sceneH);
        setupTitle(sceneW, sceneH);
        setupButtons(sceneW, sceneH);
        setupFooter(sceneW, sceneH);
    }

    private void setupBackground(double width, double height) {
        // Dégradé sombre avec particules
        LinearGradient gradient = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(10, 5, 15)),
                new Stop(0.5, Color.rgb(20, 10, 25)),
                new Stop(1, Color.rgb(15, 8, 20))
        );

        Rectangle bg = new Rectangle(width, height);
        bg.setFill(gradient);
        this.getChildren().add(bg);

        // Vignette sombre sur les bords
        Rectangle vignette = new Rectangle(width, height);
        vignette.setFill(new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(0, 0, 0, 0.6)),
                new Stop(0.5, Color.TRANSPARENT),
                new Stop(1, Color.rgb(0, 0, 0, 0.6))
        ));
        this.getChildren().add(vignette);

        // Particules décoratives
        addDecorativeElements(width, height);
    }

    private void addDecorativeElements(double width, double height) {
        // Coins décoratifs
        addCornerDecoration(50, 50);
        addCornerDecoration(width - 100, 50);
        addCornerDecoration(50, height - 100);
        addCornerDecoration(width - 100, height - 100);
    }

    private void addCornerDecoration(double x, double y) {
        Rectangle corner = new Rectangle(x, y, 50, 50);
        corner.setFill(Color.TRANSPARENT);
        corner.setStroke(Color.rgb(139, 69, 19, 0.5));
        corner.setStrokeWidth(2);
        corner.setArcWidth(10);
        corner.setArcHeight(10);

        DropShadow glow = new DropShadow();
        glow.setColor(Color.rgb(180, 100, 50, 0.4));
        glow.setRadius(15);
        corner.setEffect(glow);

        this.getChildren().add(corner);
    }

    private void setupTitle(double width, double height) {
        VBox titleBox = new VBox(10);
        titleBox.setAlignment(Pos.CENTER);

        // Titre principal
        Text title = new Text("LES OUBLIES D'EREMON");
        title.setStyle(
                "-fx-font-family: 'Serif';" +
                        "-fx-font-size: 64px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-fill: linear-gradient(to bottom, #FFD700 0%, #B8860B 100%);"
        );

        // Effet de lueur dorée
        DropShadow titleGlow = new DropShadow();
        titleGlow.setColor(Color.rgb(255, 215, 0, 0.8));
        titleGlow.setRadius(25);
        titleGlow.setSpread(0.6);

        InnerShadow titleDepth = new InnerShadow();
        titleDepth.setColor(Color.rgb(0, 0, 0, 0.7));
        titleDepth.setRadius(3);
        titleDepth.setOffsetY(2);

        titleGlow.setInput(titleDepth);
        title.setEffect(titleGlow);

        // Sous-titre
        Text subtitle = new Text("Fragment d'une legende oubliee");
        subtitle.setStyle(
                "-fx-font-family: 'Serif';" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-style: italic;" +
                        "-fx-fill: #C0C0C0;"
        );

        DropShadow subtitleShadow = new DropShadow();
        subtitleShadow.setColor(Color.BLACK);
        subtitleShadow.setRadius(5);
        subtitle.setEffect(subtitleShadow);

        // Ligne décorative
        Rectangle titleLine = new Rectangle(400, 3);
        titleLine.setFill(new LinearGradient(
                0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.TRANSPARENT),
                new Stop(0.5, Color.rgb(139, 69, 19)),
                new Stop(1, Color.TRANSPARENT)
        ));

        titleBox.getChildren().addAll(title, subtitle, titleLine);

        // Positionner le titre en haut centré
        double titleWidth = title.getLayoutBounds().getWidth();
        titleBox.setLayoutX((width - titleWidth) / 2.0);
        titleBox.setLayoutY(height * 0.12); // 12% du haut

        this.getChildren().add(titleBox);
    }

    private void setupButtons(double width, double height) {
        buttonContainers = new Pane[3];

        int buttonWidth = 500;
        int buttonHeight = 75;
        int spacing = 30;

        double startY = height * 0.45; // Commence à 45% de la hauteur
        double centerX = width / 2.0;

        for (int i = 0; i < 3; i++) {
            final int index = i;

            Pane buttonContainer = new Pane();
            buttonContainer.setPrefSize(buttonWidth, buttonHeight);
            buttonContainer.setLayoutX(centerX - buttonWidth / 2.0);
            buttonContainer.setLayoutY(startY + i * (buttonHeight + spacing));

            // Fond du bouton avec bordure médiévale
            Rectangle btnBg = new Rectangle(buttonWidth, buttonHeight);
            btnBg.setFill(new LinearGradient(
                    0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.rgb(20, 10, 15, 0.8)),
                    new Stop(1, Color.rgb(30, 15, 20, 0.8))
            ));
            btnBg.setStroke(Color.rgb(139, 69, 19));
            btnBg.setStrokeWidth(3);
            btnBg.setArcWidth(15);
            btnBg.setArcHeight(15);

            // Bordure intérieure
            Rectangle innerBorder = new Rectangle(10, 10, buttonWidth - 20, buttonHeight - 20);
            innerBorder.setFill(Color.TRANSPARENT);
            innerBorder.setStroke(Color.rgb(100, 50, 30, 0.5));
            innerBorder.setStrokeWidth(1);
            innerBorder.setArcWidth(10);
            innerBorder.setArcHeight(10);

            // Texte du bouton
            Text btnText = new Text(menuOptions[i]);
            btnText.setStyle(
                    "-fx-font-family: 'Serif';" +
                            "-fx-font-size: 24px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-fill: #D4AF37;"
            );

            double textWidth = btnText.getLayoutBounds().getWidth();
            btnText.setLayoutX((buttonWidth - textWidth) / 2.0);
            btnText.setLayoutY(buttonHeight / 2.0 + 8);

            DropShadow textShadow = new DropShadow();
            textShadow.setColor(Color.BLACK);
            textShadow.setRadius(4);
            textShadow.setOffsetY(2);
            btnText.setEffect(textShadow);

            // Indicateur de sélection (runes gauche/droite)
            Text leftRune = new Text("<");
            leftRune.setStyle(
                    "-fx-font-family: 'Serif';" +
                            "-fx-font-size: 36px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-fill: #FFD700;"
            );
            leftRune.setLayoutX(25);
            leftRune.setLayoutY(buttonHeight / 2.0 + 12);
            leftRune.setVisible(i == selectedOption);
            leftRune.setEffect(createRuneGlow());

            Text rightRune = new Text(">");
            rightRune.setStyle(
                    "-fx-font-family: 'Serif';" +
                            "-fx-font-size: 36px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-fill: #FFD700;"
            );
            rightRune.setLayoutX(buttonWidth - 45);
            rightRune.setLayoutY(buttonHeight / 2.0 + 12);
            rightRune.setVisible(i == selectedOption);
            rightRune.setEffect(createRuneGlow());

            buttonContainer.getChildren().addAll(btnBg, innerBorder, btnText, leftRune, rightRune);

            // Événements
            buttonContainer.setOnMouseEntered(e -> {
                selectedOption = index;
                updateSelection();
                playHoverEffect(buttonContainer);
            });

            buttonContainer.setOnMouseClicked(e -> executeAction(index));

            // Effet de survol permanent
            if (i == selectedOption) {
                applySelectionEffect(btnBg, true);
            }

            buttonContainers[i] = buttonContainer;
            this.getChildren().add(buttonContainer);
        }
    }

    private Effect createRuneGlow() {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.rgb(255, 215, 0, 0.9));
        glow.setRadius(20);
        glow.setSpread(0.7);
        return glow;
    }

    private void setupFooter(double width, double height) {
        Text instructions = new Text("Utilisez les fleches pour naviguer - Entree pour confirmer");
        instructions.setStyle(
                "-fx-font-family: 'Serif';" +
                        "-fx-font-size: 14px;" +
                        "-fx-fill: #808080;"
        );

        double textWidth = instructions.getLayoutBounds().getWidth();
        instructions.setLayoutX((width - textWidth) / 2.0);
        instructions.setLayoutY(height - 40);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setRadius(3);
        instructions.setEffect(shadow);

        this.getChildren().add(instructions);
    }

    public void handleKeyPress(javafx.scene.input.KeyCode keyCode) {
        if (keyCode == javafx.scene.input.KeyCode.UP) {
            selectedOption = (selectedOption - 1 + menuOptions.length) % menuOptions.length;
            updateSelection();
        } else if (keyCode == javafx.scene.input.KeyCode.DOWN) {
            selectedOption = (selectedOption + 1) % menuOptions.length;
            updateSelection();
        } else if (keyCode == javafx.scene.input.KeyCode.ENTER) {
            executeAction(selectedOption);
        }
    }

    private void updateSelection() {
        for (int i = 0; i < buttonContainers.length; i++) {
            Pane container = buttonContainers[i];
            Rectangle bg = (Rectangle) container.getChildren().get(0);
            Text leftRune = (Text) container.getChildren().get(3);
            Text rightRune = (Text) container.getChildren().get(4);

            boolean selected = (i == selectedOption);
            leftRune.setVisible(selected);
            rightRune.setVisible(selected);
            applySelectionEffect(bg, selected);
        }
    }

    private void applySelectionEffect(Rectangle bg, boolean selected) {
        if (selected) {
            DropShadow glow = new DropShadow();
            glow.setColor(Color.rgb(139, 69, 19, 0.8));
            glow.setRadius(20);
            glow.setSpread(0.4);
            bg.setEffect(glow);
            bg.setStroke(Color.rgb(180, 100, 50));
        } else {
            bg.setEffect(null);
            bg.setStroke(Color.rgb(139, 69, 19));
        }
    }

    private void playHoverEffect(Pane button) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(150), button);
        scale.setToX(1.05);
        scale.setToY(1.05);
        scale.play();

        scale.setOnFinished(e -> {
            ScaleTransition back = new ScaleTransition(Duration.millis(150), button);
            back.setToX(1.0);
            back.setToY(1.0);
            back.play();
        });
    }

    private void playIntroAnimation() {
        FadeTransition fade = new FadeTransition(Duration.millis(1500), this);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }

    private void executeAction(int index) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(800), this);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(e -> {
            switch (index) {
                case 0:
                    if (onStartGame != null) onStartGame.run();
                    break;
                case 1:
                    if (onSettings != null) onSettings.run();
                    break;
                case 2:
                    if (onQuit != null) onQuit.run();
                    break;
            }
        });

        fadeOut.play();
    }
}