
package com.eremon.views;

import javafx.beans.value.ChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ClassSelectionView extends Pane {

    private final String[] classIllustrationPaths = {
            "/image/class_illustrations/MageIllustration.jpg",
            "/image/class_illustrations/GuerrierIllustration.jpg",
            "/image/class_illustrations/NinjaaaIllustration.jpg",
            "/image/class_illustrations/ArcherIllustration.jpg"
    };

    private final String[] classNames = {
            "PORTE-CENDRE", "GARDIEN DES ÉCHOS", "LAME-SOUVENIR", "ÉCLAT ESPRIT"
    };

    private final String[] classDescriptions = {
            "Guerrier des cendres\naux frappes dévastatrices",
            "Combattant équilibré maîtrisant\nl'épée et la magie",
            "Ombre furtive\naux coups rapides et mortels",
            "Maître spirituel invoquant\nles échos des morts"
    };

    private final String[] classStats = {
            "HP: 120 | ATK: 25 | DEF: 12 | MANA: 50",
            "HP: 110 | ATK: 12 | DEF: 10 | MANA: 60",
            "HP: 85  | ATK: 18 | DEF: 8  | MANA: 40",
            "HP: 100 | ATK: 15 | DEF: 10 | MANA: 60"
    };

    private int selectedIndex = 0;
    private final List<Pane> classCards = new ArrayList<>();

    // onSelect reçoit la HeroClass choisie, onBack retourne au menu
    private final Consumer<HeroClass> onSelect;
    private final Runnable onBack;

    /** Constructeur avec sélection + retour (utilisé par Main.java) */
    public ClassSelectionView(Consumer<HeroClass> onSelect, Runnable onBack) {
        this.onSelect = onSelect;
        this.onBack   = onBack;
        init();
    }

    /** Constructeur simple (rétro-compatibilité) */
    public ClassSelectionView(Runnable onValidate) {
        this.onSelect = heroClass -> { if (onValidate != null) onValidate.run(); };
        this.onBack   = null;
        init();
    }

    private void init() {
        setStyle("-fx-background-color: #18131d;");
        ChangeListener<Number> resizeListener = (obs, o, n) -> createLayout();
        widthProperty().addListener(resizeListener);
        heightProperty().addListener(resizeListener);
        createLayout();
        setFocusTraversable(true);
        requestFocus();
    }

    private void createLayout() {
        getChildren().clear();
        classCards.clear();

        double sceneW = getWidth()  > 0 ? getWidth()  : 1280;
        double sceneH = getHeight() > 0 ? getHeight() : 720;

        Text title = new Text("CHOISISSEZ VOTRE HÉROS");
        title.setFont(Font.font("Serif", FontWeight.EXTRA_BOLD, 54));
        title.setFill(Color.web("#ffe33a"));
        title.setStroke(Color.web("#ffd700"));
        title.setStrokeWidth(1.5);
        double titleWidth = title.getLayoutBounds().getWidth();
        title.setLayoutX((sceneW - titleWidth) / 2.0);
        title.setLayoutY(70);
        getChildren().add(title);

        int cardWidth  = 280;
        int cardHeight = 400;
        int spacing    = 25;
        int totalWidth = 4 * cardWidth + 3 * spacing;
        if (totalWidth > sceneW - 40) {
            cardWidth  = (int)((sceneW - 40 - 3 * spacing) / 4);
            totalWidth = 4 * cardWidth + 3 * spacing;
        }
        int startX = (int)((sceneW - totalWidth) / 2.0);
        int startY = 110;

        for (int i = 0; i < 4; i++) {
            Pane card = createClassCard(i, cardWidth, cardHeight);
            card.setLayoutX(startX + i * (cardWidth + spacing));
            card.setLayoutY(startY);
            classCards.add(card);
            getChildren().add(card);
        }
        updateSelectionHighlight();

        Text instructions = new Text("← → pour naviguer  |  Entrée ou Clic pour confirmer  |  Échap pour retour");
        instructions.setFont(Font.font("Serif", FontWeight.NORMAL, 16));
        instructions.setFill(Color.web("#FFFFFFCC"));
        double instWidth = instructions.getLayoutBounds().getWidth();
        instructions.setLayoutX((sceneW - instWidth) / 2.0);
        instructions.setLayoutY(sceneH - 30);
        getChildren().add(instructions);

        setOnKeyPressed(e -> handleKeyPress(e.getCode()));
        setOnMouseClicked(e -> validateSelection());
    }

    private Pane createClassCard(int index, int width, int height) {
        Pane card = new Pane();
        Rectangle bg = new Rectangle(width, height);
        bg.setArcWidth(22); bg.setArcHeight(22);
        bg.setStroke(Color.web("#ffdf8a")); bg.setStrokeWidth(3);
        bg.setFill(Color.web("#231e28", 0.42));
        card.getChildren().add(bg);

        ImageView illustrationView = loadClassIllustration(index, width);
        if (illustrationView != null) card.getChildren().add(illustrationView);

        Text name = new Text(classNames[index]);
        name.setFont(Font.font("Serif", FontWeight.EXTRA_BOLD, 24));
        name.setFill(Color.web("#ffe33a"));
        name.setWrappingWidth(width - 20);
        name.setTextAlignment(TextAlignment.CENTER);
        name.setLayoutX(10); name.setLayoutY(220);
        card.getChildren().add(name);

        Text desc = new Text(classDescriptions[index]);
        desc.setFont(Font.font("Serif", FontWeight.NORMAL, 15));
        desc.setFill(Color.web("#fffbb3"));
        desc.setWrappingWidth(width - 20);
        desc.setTextAlignment(TextAlignment.CENTER);
        desc.setLayoutX(10); desc.setLayoutY(260);
        card.getChildren().add(desc);

        Text stats = new Text(classStats[index]);
        stats.setFont(Font.font("Monospaced", FontWeight.NORMAL, 13));
        stats.setFill(Color.web("#e4d377cc"));
        stats.setWrappingWidth(width - 20);
        stats.setTextAlignment(TextAlignment.CENTER);
        stats.setLayoutX(10); stats.setLayoutY(320);
        card.getChildren().add(stats);

        return card;
    }

    private ImageView loadClassIllustration(int classIndex, int cardWidth) {
        try {
            Image illustration = new Image(getClass().getResourceAsStream(classIllustrationPaths[classIndex]));
            ImageView view = new ImageView(illustration);
            view.setPreserveRatio(true); view.setSmooth(true);
            double maxW = Math.min(160, cardWidth - 40);
            view.setFitWidth(maxW); view.setFitHeight(160);
            view.setLayoutX((cardWidth - maxW) / 2.0); view.setLayoutY(25);
            return view;
        } catch (Exception e) {
            System.err.println("Illustration non trouvée: " + classIllustrationPaths[classIndex]);
            return null;
        }
    }

    private void updateSelectionHighlight() {
        for (int i = 0; i < classCards.size(); i++) {
            classCards.get(i).setStyle(i == selectedIndex
                    ? "-fx-effect: dropshadow(gaussian, #ffe33a, 25, 0.8, 0, 0);"
                    : "");
        }
        updateValidateLabel();
    }

    public void handleKeyPress(KeyCode code) {
        switch (code) {
            case LEFT  -> { selectedIndex = (selectedIndex + 3) % 4; updateSelectionHighlight(); }
            case RIGHT -> { selectedIndex = (selectedIndex + 1) % 4; updateSelectionHighlight(); }
            case ENTER, SPACE -> validateSelection();
            case ESCAPE -> { if (onBack != null) onBack.run(); }
            default -> {}
        }
    }

    private void validateSelection() {
        if (onSelect != null) {
            onSelect.accept(new HeroClass(selectedIndex));
        }
    }

    public int getSelectedClassIndex() { return selectedIndex; }

    private void updateValidateLabel() {
        getChildren().removeIf(node -> "selectedLabel".equals(node.getId()));
        if (selectedIndex >= 0 && selectedIndex < classCards.size()) {
            Pane card = classCards.get(selectedIndex);
            Text selectedLabel = new Text("► SÉLECTIONNÉ ◄");
            selectedLabel.setFont(Font.font("Serif", FontWeight.BOLD, 18));
            selectedLabel.setFill(Color.web("#ffe33a"));
            selectedLabel.setId("selectedLabel");
            double labelWidth = selectedLabel.getLayoutBounds().getWidth();
            double cardWidth  = ((Rectangle) card.getChildren().get(0)).getWidth();
            selectedLabel.setLayoutX(card.getLayoutX() + (cardWidth - labelWidth) / 2.0);
            selectedLabel.setLayoutY(card.getLayoutY() + 365);
            getChildren().add(selectedLabel);
        }
    }
}