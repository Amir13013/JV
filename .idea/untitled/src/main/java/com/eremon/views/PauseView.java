package com.eremon.views;

import javafx.scene.layout.Pane;
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

public class PauseView extends Pane {

    private Runnable onResume;
    private Runnable onSettings;
    private Runnable onMainMenu;
    private Runnable onQuit;

    private int selectedOption = 0;
    private String[] menuOptions = {"REPRENDRE", "PARAMETRES", "MENU PRINCIPAL", "QUITTER"};
    private Pane[] buttonContainers;
    private SettingsView settingsView;

    public PauseView(Runnable onResume, Runnable onSettings, Runnable onMainMenu, Runnable onQuit) {
        this.onResume = onResume;
        this.onSettings = () -> openSettings();
        this.onMainMenu = onMainMenu;
        this.onQuit = onQuit;

        this.setPrefSize(1280, 720);
        setupOverlay();
        setupPanel();
        playShowAnimation();
    }

    private void setupOverlay() {
        Rectangle overlay = new Rectangle(0, 0, 1280, 720);
        overlay.setFill(Color.rgb(0, 0, 0, 0.8));
        this.getChildren().add(overlay);
    }

    private void setupPanel() {
        double panelWidth = 550;
        double panelHeight = 600;
        double panelX = (1280 - panelWidth) / 2;
        double panelY = (720 - panelHeight) / 2;

        Pane centerPanel = new Pane();
        centerPanel.setLayoutX(panelX);
        centerPanel.setLayoutY(panelY);
        centerPanel.setPrefSize(panelWidth, panelHeight);

        Rectangle panelBg = new Rectangle(panelWidth, panelHeight);
        panelBg.setFill(new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(20, 10, 25, 0.95)),
                new Stop(1, Color.rgb(35, 20, 40, 0.95))
        ));
        panelBg.setStroke(Color.rgb(139, 69, 19));
        panelBg.setStrokeWidth(4);
        panelBg.setArcWidth(20);
        panelBg.setArcHeight(20);

        DropShadow panelShadow = new DropShadow();
        panelShadow.setColor(Color.BLACK);
        panelShadow.setRadius(30);
        panelShadow.setSpread(0.5);
        panelBg.setEffect(panelShadow);

        centerPanel.getChildren().add(panelBg);

        Rectangle innerBorder = new Rectangle(10, 10, panelWidth - 20, panelHeight - 20);
        innerBorder.setFill(Color.TRANSPARENT);
        innerBorder.setStroke(Color.rgb(100, 50, 30, 0.7));
        innerBorder.setStrokeWidth(2);
        innerBorder.setArcWidth(15);
        innerBorder.setArcHeight(15);
        centerPanel.getChildren().add(innerBorder);

        Text title = new Text("PAUSE");
        title.setStyle(
                "-fx-font-family: 'Serif';" +
                        "-fx-font-size: 56px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-fill: linear-gradient(to bottom, #FFD700 0%, #B8860B 100%);"
        );
        title.setLayoutX(panelWidth / 2 - 85);
        title.setLayoutY(90);

        DropShadow titleGlow = new DropShadow();
        titleGlow.setColor(Color.rgb(255, 215, 0, 0.7));
        titleGlow.setRadius(20);
        titleGlow.setSpread(0.5);
        title.setEffect(titleGlow);

        centerPanel.getChildren().add(title);

        Rectangle separator = new Rectangle(80, 110, panelWidth - 160, 3);
        separator.setFill(new LinearGradient(
                0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.TRANSPARENT),
                new Stop(0.5, Color.rgb(139, 69, 19)),
                new Stop(1, Color.TRANSPARENT)
        ));
        centerPanel.getChildren().add(separator);

        buttonContainers = new Pane[4];
        double startY = 160;

        for (int i = 0; i < 4; i++) {
            final int index = i;

            Pane btnPane = new Pane();
            btnPane.setPrefSize(450, 70);
            btnPane.setLayoutX((panelWidth - 450) / 2);
            btnPane.setLayoutY(startY + i * 85);

            Rectangle btnBg = new Rectangle(450, 70);
            btnBg.setFill(new LinearGradient(
                    0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.rgb(25, 15, 30, 0.7)),
                    new Stop(1, Color.rgb(35, 20, 40, 0.7))
            ));
            btnBg.setStroke(Color.rgb(139, 69, 19));
            btnBg.setStrokeWidth(2);
            btnBg.setArcWidth(12);
            btnBg.setArcHeight(12);

            btnPane.getChildren().add(btnBg);

            Rectangle btnInner = new Rectangle(6, 6, 438, 58);
            btnInner.setFill(Color.TRANSPARENT);
            btnInner.setStroke(Color.rgb(100, 50, 30, 0.4));
            btnInner.setStrokeWidth(1);
            btnInner.setArcWidth(10);
            btnInner.setArcHeight(10);
            btnPane.getChildren().add(btnInner);

            Text btnText = new Text(menuOptions[i]);
            btnText.setStyle(
                    "-fx-font-family: 'Serif';" +
                            "-fx-font-size: 24px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-fill: #D4AF37;"
            );
            btnText.setLayoutX(225 - btnText.getLayoutBounds().getWidth() / 2);
            btnText.setLayoutY(45);

            DropShadow textShadow = new DropShadow();
            textShadow.setColor(Color.BLACK);
            textShadow.setRadius(4);
            textShadow.setOffsetY(2);
            btnText.setEffect(textShadow);

            btnPane.getChildren().add(btnText);

            Text leftArrow = new Text("<");
            leftArrow.setStyle(
                    "-fx-font-family: 'Serif';" +
                            "-fx-font-size: 36px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-fill: #FFD700;"
            );
            leftArrow.setLayoutX(25);
            leftArrow.setLayoutY(48);
            leftArrow.setVisible(i == selectedOption);
            leftArrow.setEffect(createArrowGlow());
            btnPane.getChildren().add(leftArrow);

            Text rightArrow = new Text(">");
            rightArrow.setStyle(
                    "-fx-font-family: 'Serif';" +
                            "-fx-font-size: 36px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-fill: #FFD700;"
            );
            rightArrow.setLayoutX(415);
            rightArrow.setLayoutY(48);
            rightArrow.setVisible(i == selectedOption);
            rightArrow.setEffect(createArrowGlow());
            btnPane.getChildren().add(rightArrow);

            buttonContainers[i] = btnPane;

            btnPane.setOnMouseEntered(e -> {
                selectedOption = index;
                updateSelection();
                playHoverAnimation(btnPane);
            });

            btnPane.setOnMouseClicked(e -> executeAction(index));

            if (i == selectedOption) {
                applySelectionEffect((Rectangle) btnPane.getChildren().get(0), true);
            }

            centerPanel.getChildren().add(btnPane);
        }

        this.getChildren().add(centerPanel);
    }

    private Effect createArrowGlow() {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.rgb(255, 215, 0, 0.9));
        glow.setRadius(15);
        glow.setSpread(0.6);
        return glow;
    }

    public void handleKeyPress(javafx.scene.input.KeyCode keyCode) {
        if (settingsView != null) return;

        if (keyCode == javafx.scene.input.KeyCode.ESCAPE) {
            executeAction(0);
        } else if (keyCode == javafx.scene.input.KeyCode.UP) {
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
            Text leftArrow = (Text) container.getChildren().get(3);
            Text rightArrow = (Text) container.getChildren().get(4);

            boolean selected = (i == selectedOption);
            leftArrow.setVisible(selected);
            rightArrow.setVisible(selected);
            applySelectionEffect(bg, selected);
        }
    }

    private void applySelectionEffect(Rectangle bg, boolean selected) {
        if (selected) {
            DropShadow glow = new DropShadow();
            glow.setColor(Color.rgb(139, 69, 19, 0.9));
            glow.setRadius(20);
            glow.setSpread(0.5);
            bg.setEffect(glow);
            bg.setStroke(Color.rgb(180, 100, 50));
        } else {
            bg.setEffect(null);
            bg.setStroke(Color.rgb(139, 69, 19));
        }
    }

    private void playHoverAnimation(Pane button) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(120), button);
        scale.setToX(1.03);
        scale.setToY(1.03);
        scale.play();

        scale.setOnFinished(e -> {
            ScaleTransition back = new ScaleTransition(Duration.millis(120), button);
            back.setToX(1.0);
            back.setToY(1.0);
            back.play();
        });
    }

    private void playShowAnimation() {
        FadeTransition fade = new FadeTransition(Duration.millis(300), this);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }

    private void openSettings() {
        if (settingsView != null) return;
        settingsView = new SettingsView(() -> {
            getChildren().remove(settingsView);
            settingsView = null;
        });
        getChildren().add(settingsView);
    }

    private void executeAction(int index) {
        if (settingsView != null) return;

        FadeTransition fadeOut = new FadeTransition(Duration.millis(250), this);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(e -> {
            switch (index) {
                case 0:
                    if (onResume != null) onResume.run();
                    break;
                case 1:
                    if (onSettings != null) onSettings.run();
                    break;
                case 2:
                    if (onMainMenu != null) onMainMenu.run();
                    break;
                case 3:
                    if (onQuit != null) onQuit.run();
                    break;
            }
        });

        fadeOut.play();
    }
}
