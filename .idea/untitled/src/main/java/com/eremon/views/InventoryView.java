package com.eremon.views;

import com.eremon.models.entity.Player; // Correction ici !
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.effect.*;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

/**
 * INVENTAIRE - DARK FANTASY STYLE
 */
public class InventoryView extends Pane {

    private Player player; // Correction ici !
    private Runnable onClose;

    private int currentTab = 0;
    private String[] tabs = {"INVENTAIRE", "EQUIPEMENT", "ARTISANAT"};
    private Text[] tabTexts;

    private int selectedSlot = 0;
    private static final int SLOTS_PER_ROW = 6;
    private static final int MAX_SLOTS = 30;
    private Rectangle[] slotBackgrounds;

    public InventoryView(Player player, Runnable onClose) { // Correction ici !
        this.player = player;
        this.onClose = onClose;

        this.setPrefSize(1280, 720);
        setupOverlay();
        setupMainPanel();
        playShowAnimation();
    }

    private void setupOverlay() {
        Rectangle overlay = new Rectangle(0, 0, 1280, 720);
        overlay.setFill(Color.rgb(0, 0, 0, 0.85));
        this.getChildren().add(overlay);
    }

    private void setupMainPanel() {
        double panelWidth = 950;
        double panelHeight = 650;
        double panelX = (1280 - panelWidth) / 2;
        double panelY = (720 - panelHeight) / 2;

        Pane mainPanel = new Pane();
        mainPanel.setLayoutX(panelX);
        mainPanel.setLayoutY(panelY);
        mainPanel.setPrefSize(panelWidth, panelHeight);
        mainPanel.setId("mainPanel");

        // Fond principal
        Rectangle panelBg = new Rectangle(panelWidth, panelHeight);
        panelBg.setFill(new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(20, 10, 25, 0.98)),
                new Stop(1, Color.rgb(35, 20, 40, 0.98))
        ));
        panelBg.setStroke(Color.rgb(139, 69, 19));
        panelBg.setStrokeWidth(4);
        panelBg.setArcWidth(20);
        panelBg.setArcHeight(20);

        DropShadow panelShadow = new DropShadow();
        panelShadow.setColor(Color.BLACK);
        panelShadow.setRadius(35);
        panelShadow.setSpread(0.6);
        panelBg.setEffect(panelShadow);

        mainPanel.getChildren().add(panelBg);

        // Bordure intérieure
        Rectangle innerBorder = new Rectangle(12, 12, panelWidth - 24, panelHeight - 24);
        innerBorder.setFill(Color.TRANSPARENT);
        innerBorder.setStroke(Color.rgb(100, 50, 30, 0.7));
        innerBorder.setStrokeWidth(2);
        innerBorder.setArcWidth(15);
        innerBorder.setArcHeight(15);
        mainPanel.getChildren().add(innerBorder);

        // Onglets
        setupTabs(mainPanel, panelWidth);

        // Contenu selon l'onglet
        updateTabContent(mainPanel, panelWidth, panelHeight);

        // Instructions en bas
        Text instructions = new Text("TAB: Changer d'onglet | Fleches: Naviguer | ESC/I: Fermer");
        instructions.setStyle(
                "-fx-font-family: 'Monospace';" +
                        "-fx-font-size: 12px;" +
                        "-fx-fill: #808080;"
        );
        instructions.setLayoutX(panelWidth / 2 - 260);
        instructions.setLayoutY(panelHeight - 20);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setRadius(3);
        instructions.setEffect(shadow);

        mainPanel.getChildren().add(instructions);

        this.getChildren().add(mainPanel);
    }

    private void setupTabs(Pane panel, double panelWidth) {
        tabTexts = new Text[3];
        double tabWidth = 280;
        double tabY = 50;

        for (int i = 0; i < 3; i++) {
            final int index = i;

            // Fond de l'onglet
            Rectangle tabBg = new Rectangle(60 + i * tabWidth, 25, tabWidth - 20, 40);
            tabBg.setFill(i == currentTab
                    ? Color.rgb(35, 20, 40, 0.9)
                    : Color.rgb(20, 10, 25, 0.5)
            );
            tabBg.setStroke(Color.rgb(139, 69, 19));
            tabBg.setStrokeWidth(2);
            tabBg.setArcWidth(10);
            tabBg.setArcHeight(10);
            panel.getChildren().add(tabBg);

            // Texte de l'onglet
            Text tabText = new Text(tabs[i]);
            tabText.setStyle(
                    "-fx-font-family: 'Serif';" +
                            "-fx-font-size: 20px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-fill: " + (i == currentTab ? "#FFD700" : "#A0A0A0") + ";"
            );
            tabText.setLayoutX(150 + i * tabWidth - tabText.getLayoutBounds().getWidth() / 2);
            tabText.setLayoutY(tabY);

            DropShadow textShadow = new DropShadow();
            textShadow.setColor(Color.BLACK);
            textShadow.setRadius(4);
            tabText.setEffect(textShadow);

            tabText.setOnMouseClicked(e -> switchTab(index));
            tabText.setOnMouseEntered(e -> {
                if (index != currentTab) {
                    tabText.setStyle(
                            "-fx-font-family: 'Serif';" +
                                    "-fx-font-size: 20px;" +
                                    "-fx-font-weight: bold;" +
                                    "-fx-fill: #C0C0C0;"
                    );
                }
            });
            tabText.setOnMouseExited(e -> {
                if (index != currentTab) {
                    tabText.setStyle(
                            "-fx-font-family: 'Serif';" +
                                    "-fx-font-size: 20px;" +
                                    "-fx-font-weight: bold;" +
                                    "-fx-fill: #A0A0A0;"
                    );
                }
            });

            tabTexts[i] = tabText;
            panel.getChildren().add(tabText);
        }
    }

    private void updateTabContent(Pane panel, double panelWidth, double panelHeight) {
        panel.getChildren().removeIf(node ->
                node.getId() != null && node.getId().equals("tabContent")
        );

        switch (currentTab) {
            case 0:
                setupInventoryTab(panel, panelWidth, panelHeight);
                break;
            case 1:
                setupEquipmentTab(panel, panelWidth, panelHeight);
                break;
            case 2:
                setupCraftTab(panel, panelWidth, panelHeight);
                break;
        }
    }

    private void setupInventoryTab(Pane panel, double panelWidth, double panelHeight) {
        Pane content = new Pane();
        content.setId("tabContent");

        // Grille d'inventaire
        GridPane grid = new GridPane();
        grid.setLayoutX(50);
        grid.setLayoutY(100);
        grid.setHgap(12);
        grid.setVgap(12);

        slotBackgrounds = new Rectangle[MAX_SLOTS];
        int slotSize = 75;

        for (int i = 0; i < MAX_SLOTS; i++) {
            final int index = i;
            int row = i / SLOTS_PER_ROW;
            int col = i % SLOTS_PER_ROW;

            Pane slotPane = new Pane();
            slotPane.setPrefSize(slotSize, slotSize);

            // Fond du slot
            Rectangle slot = new Rectangle(slotSize, slotSize);
            slot.setFill(new LinearGradient(
                    0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.rgb(30, 20, 35)),
                    new Stop(1, Color.rgb(40, 25, 45))
            ));
            slot.setStroke(i == selectedSlot
                    ? Color.rgb(255, 215, 0)
                    : Color.rgb(100, 60, 50)
            );
            slot.setStrokeWidth(i == selectedSlot ? 3 : 2);
            slot.setArcWidth(8);
            slot.setArcHeight(8);

            InnerShadow innerShadow = new InnerShadow();
            innerShadow.setColor(Color.rgb(0, 0, 0, 0.6));
            innerShadow.setRadius(6);
            slot.setEffect(innerShadow);

            slotBackgrounds[i] = slot;
            slotPane.getChildren().add(slot);

            // Exemple d'item (simulé)
            if (i < 8) {
                Rectangle item = new Rectangle(15, 15, 45, 45);
                item.setFill(Color.rgb(80 + i * 15, 60, 120 - i * 10));
                item.setStroke(Color.rgb(139, 69, 19));
                item.setStrokeWidth(2);
                item.setArcWidth(5);
                item.setArcHeight(5);
                slotPane.getChildren().add(item);

                // Quantité
                Text qty = new Text("x" + (i + 1));
                qty.setStyle(
                        "-fx-font-family: 'Monospace';" +
                                "-fx-font-size: 11px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-fill: #FFFF00;"
                );
                qty.setLayoutX(slotSize - 22);
                qty.setLayoutY(slotSize - 8);

                DropShadow qtyShadow = new DropShadow();
                qtyShadow.setColor(Color.BLACK);
                qtyShadow.setRadius(2);
                qty.setEffect(qtyShadow);

                slotPane.getChildren().add(qty);
            }

            slotPane.setOnMouseClicked(e -> selectSlot(index));

            grid.add(slotPane, col, row);
        }

        content.getChildren().add(grid);

        // Panneau d'informations
        Rectangle infoPanel = new Rectangle(640, 100, 260, 450);
        infoPanel.setFill(new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(25, 15, 30, 0.9)),
                new Stop(1, Color.rgb(35, 20, 40, 0.9))
        ));
        infoPanel.setStroke(Color.rgb(139, 69, 19));
        infoPanel.setStrokeWidth(2);
        infoPanel.setArcWidth(12);
        infoPanel.setArcHeight(12);
        content.getChildren().add(infoPanel);

        Text infoTitle = new Text("DETAILS");
        infoTitle.setStyle(
                "-fx-font-family: 'Serif';" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-fill: #FFD700;"
        );
        infoTitle.setLayoutX(735);
        infoTitle.setLayoutY(135);
        content.getChildren().add(infoTitle);

        if (selectedSlot < 8) {
            Text itemName = new Text("Objet n " + (selectedSlot + 1));
            itemName.setStyle(
                    "-fx-font-family: 'Serif';" +
                            "-fx-font-size: 18px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-fill: #D4AF37;"
            );
            itemName.setLayoutX(665);
            itemName.setLayoutY(180);
            content.getChildren().add(itemName);

            Text itemDesc = new Text("Un objet mysterieux trouve\ndans les profondeurs du donjon.\n\nProprietes:\n- Valeur: " + (selectedSlot * 10 + 50) + " pieces\n- Rarete: Commun");
            itemDesc.setStyle(
                    "-fx-font-family: 'Serif';" +
                            "-fx-font-size: 14px;" +
                            "-fx-fill: #C0C0C0;"
            );
            itemDesc.setLayoutX(665);
            itemDesc.setLayoutY(210);
            itemDesc.setWrappingWidth(220);
            content.getChildren().add(itemDesc);
        } else {
            Text emptyText = new Text("Selectionnez un objet\npour voir ses details");
            emptyText.setStyle(
                    "-fx-font-family: 'Serif';" +
                            "-fx-font-size: 15px;" +
                            "-fx-fill: #808080;"
            );
            emptyText.setLayoutX(685);
            emptyText.setLayoutY(200);
            emptyText.setWrappingWidth(200);
            content.getChildren().add(emptyText);
        }

        panel.getChildren().add(content);
    }

    private void setupEquipmentTab(Pane panel, double panelWidth, double panelHeight) {
        Pane content = new Pane();
        content.setId("tabContent");

        Text title = new Text("EQUIPEMENT");
        title.setStyle(
                "-fx-font-family: 'Serif';" +
                        "-fx-font-size: 32px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-fill: #FFD700;"
        );
        title.setLayoutX(panelWidth / 2 - 110);
        title.setLayoutY(120);

        DropShadow titleGlow = new DropShadow();
        titleGlow.setColor(Color.rgb(255, 215, 0, 0.6));
        titleGlow.setRadius(15);
        title.setEffect(titleGlow);

        content.getChildren().add(title);

        // Silhouette du personnage
        Rectangle charSilhouette = new Rectangle(250, 180, 200, 350);
        charSilhouette.setFill(Color.rgb(30, 20, 35, 0.6));
        charSilhouette.setStroke(Color.rgb(139, 69, 19));
        charSilhouette.setStrokeWidth(2);
        charSilhouette.setArcWidth(100);
        charSilhouette.setArcHeight(100);
        content.getChildren().add(charSilhouette);

        // Slots d'équipement
        String[] equipSlots = {"Tete", "Torse", "Mains", "Jambes", "Arme", "Bouclier"};
        double slotX = 520;
        double slotY = 150;

        for (int i = 0; i < equipSlots.length; i++) {
            Rectangle slot = new Rectangle(slotX, slotY + i * 75, 360, 60);
            slot.setFill(new LinearGradient(
                    0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.rgb(30, 20, 35)),
                    new Stop(1, Color.rgb(40, 25, 45))
            ));
            slot.setStroke(Color.rgb(139, 69, 19));
            slot.setStrokeWidth(2);
            slot.setArcWidth(10);
            slot.setArcHeight(10);
            content.getChildren().add(slot);

            Text slotName = new Text(equipSlots[i]);
            slotName.setStyle(
                    "-fx-font-family: 'Serif';" +
                            "-fx-font-size: 20px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-fill: #D4AF37;"
            );
            slotName.setLayoutX(slotX + 20);
            slotName.setLayoutY(slotY + i * 75 + 35);
            content.getChildren().add(slotName);

            Text emptyText = new Text("[Vide]");
            emptyText.setStyle(
                    "-fx-font-family: 'Serif';" +
                            "-fx-font-size: 16px;" +
                            "-fx-font-style: italic;" +
                            "-fx-fill: #606060;"
            );
            emptyText.setLayoutX(slotX + 260);
            emptyText.setLayoutY(slotY + i * 75 + 35);
            content.getChildren().add(emptyText);
        }

        panel.getChildren().add(content);
    }

    private void setupCraftTab(Pane panel, double panelWidth, double panelHeight) {
        Pane content = new Pane();
        content.setId("tabContent");

        Text title = new Text("ARTISANAT");
        title.setStyle(
                "-fx-font-family: 'Serif';" +
                        "-fx-font-size: 38px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-fill: #FFD700;"
        );
        title.setLayoutX(panelWidth / 2 - 125);
        title.setLayoutY(140);

        DropShadow titleGlow = new DropShadow();
        titleGlow.setColor(Color.rgb(255, 215, 0, 0.6));
        titleGlow.setRadius(15);
        title.setEffect(titleGlow);

        content.getChildren().add(title);

        Text infoText = new Text("Selectionnez des objets dans votre inventaire\npour decouvrir des recettes d'artisanat.");
        infoText.setStyle(
                "-fx-font-family: 'Serif';" +
                        "-fx-font-size: 16px;" +
                        "-fx-fill: #C0C0C0;"
        );
        infoText.setLayoutX(panelWidth / 2 - 235);
        infoText.setLayoutY(200);
        infoText.setWrappingWidth(470);
        content.getChildren().add(infoText);

        // Recettes
        String[] recipes = {"Potion de Soin", "Epee en Fer", "Bouclier en Bois"};
        double recipeY = 260;

        for (int i = 0; i < recipes.length; i++) {
            Rectangle recipeBox = new Rectangle(200, recipeY + i * 75, 550, 60);
            recipeBox.setFill(Color.rgb(30, 20, 35, 0.7));
            recipeBox.setStroke(Color.rgb(139, 69, 19));
            recipeBox.setStrokeWidth(2);
            recipeBox.setArcWidth(10);
            recipeBox.setArcHeight(10);
            content.getChildren().add(recipeBox);

            Text recipeName = new Text(recipes[i]);
            recipeName.setStyle(
                    "-fx-font-family: 'Serif';" +
                            "-fx-font-size: 20px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-fill: #D4AF37;"
            );
            recipeName.setLayoutX(230);
            recipeName.setLayoutY(recipeY + i * 75 + 35);
            content.getChildren().add(recipeName);

            Text locked = new Text("Verrouille");
            locked.setStyle(
                    "-fx-font-family: 'Serif';" +
                            "-fx-font-size: 15px;" +
                            "-fx-font-style: italic;" +
                            "-fx-fill: #808080;"
            );
            locked.setLayoutX(630);
            locked.setLayoutY(recipeY + i * 75 + 35);
            content.getChildren().add(locked);
        }

        panel.getChildren().add(content);
    }

    private void switchTab(int newTab) {
        if (newTab == currentTab) return;

        currentTab = newTab;
        selectedSlot = 0;

        // MAJ des onglets
        Pane mainPanel = (Pane) this.getChildren().get(1);
        mainPanel.getChildren().clear();
        setupMainPanel();
    }

    private void selectSlot(int slot) {
        selectedSlot = slot;
        if (slotBackgrounds != null && currentTab == 0) {
            for (int i = 0; i < slotBackgrounds.length; i++) {
                if (slotBackgrounds[i] != null) {
                    slotBackgrounds[i].setStroke(i == selectedSlot
                            ? Color.rgb(255, 215, 0)
                            : Color.rgb(100, 60, 50)
                    );
                    slotBackgrounds[i].setStrokeWidth(i == selectedSlot ? 3 : 2);
                }
            }

            Pane mainPanel = (Pane) this.getChildren().get(1);
            updateTabContent(mainPanel, 950, 650);
        }
    }

    public void handleKeyPress(javafx.scene.input.KeyCode keyCode) {
        if (keyCode == javafx.scene.input.KeyCode.ESCAPE || keyCode == javafx.scene.input.KeyCode.I) {
            close();
        } else if (keyCode == javafx.scene.input.KeyCode.TAB) {
            switchTab((currentTab + 1) % 3);
        } else if (currentTab == 0) {
            if (keyCode == javafx.scene.input.KeyCode.LEFT && selectedSlot % SLOTS_PER_ROW > 0) {
                selectSlot(selectedSlot - 1);
            } else if (keyCode == javafx.scene.input.KeyCode.RIGHT && selectedSlot % SLOTS_PER_ROW < SLOTS_PER_ROW - 1 && selectedSlot < MAX_SLOTS - 1) {
                selectSlot(selectedSlot + 1);
            } else if (keyCode == javafx.scene.input.KeyCode.UP && selectedSlot >= SLOTS_PER_ROW) {
                selectSlot(selectedSlot - SLOTS_PER_ROW);
            } else if (keyCode == javafx.scene.input.KeyCode.DOWN && selectedSlot < MAX_SLOTS - SLOTS_PER_ROW) {
                selectSlot(selectedSlot + SLOTS_PER_ROW);
            }
        }
    }

    private void playShowAnimation() {
        FadeTransition fade = new FadeTransition(Duration.millis(300), this);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }

    private void close() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(250), this);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            if (onClose != null) onClose.run();
        });
        fadeOut.play();
    }
}
