
package com.eremon.views;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class SettingsView extends Pane {

    private static double volumeValue = 80;
    private static double brightnessValue = 60;
    private static String playerColor = "#00BFFF";
    private static String windowSize = "1280x720";
    private static String keyUp     = "Z";
    private static String keyDown   = "S";
    private static String keyLeft   = "Q";
    private static String keyRight  = "D";
    private static String keyAttack = "ESPACE";

    public SettingsView(Runnable onClose) {
        setPrefSize(1280, 720);

        Rectangle bg = new Rectangle(1280, 720);
        bg.setFill(Color.rgb(15, 12, 20, 0.97));
        getChildren().add(bg);

        Text title = new Text("PARAMÈTRES");
        title.setStyle("-fx-font-size: 44px; -fx-font-family: 'Serif'; -fx-fill: #FFD700; -fx-font-weight: bold;");
        title.setLayoutX(490);
        title.setLayoutY(75);
        getChildren().add(title);

        // --- Volume ---
        Label volumeLabel = new Label("Volume général :");
        volumeLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #FFD700;");
        Slider volumeSlider = new Slider(0, 100, volumeValue);
        volumeSlider.setPrefWidth(300);
        volumeSlider.valueProperty().addListener((obs, o, n) -> volumeValue = n.doubleValue());

        // --- Luminosité ---
        Label brightnessLabel = new Label("Luminosité :");
        brightnessLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #FFD700;");
        Slider brightnessSlider = new Slider(0, 100, brightnessValue);
        brightnessSlider.setPrefWidth(300);
        brightnessSlider.valueProperty().addListener((obs, o, n) -> brightnessValue = n.doubleValue());

        // --- Couleur du joueur ---
        Label colorLabel = new Label("Couleur du joueur :");
        colorLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #FFD700;");
        ColorPicker colorPicker = new ColorPicker(Color.web(playerColor));
        colorPicker.setOnAction(e -> {
            Color c = colorPicker.getValue();
            playerColor = String.format("#%02X%02X%02X",
                    (int)(c.getRed() * 255), (int)(c.getGreen() * 255), (int)(c.getBlue() * 255));
        });

        // --- Taille de fenêtre ---
        Label sizeLabel = new Label("Taille de fenêtre :");
        sizeLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #FFD700;");
        ComboBox<String> sizeBox = new ComboBox<>();
        sizeBox.getItems().addAll("800x600", "1024x768", "1280x720", "1920x1080");
        sizeBox.setValue(windowSize);
        sizeBox.setOnAction(e -> windowSize = sizeBox.getValue());

        // --- Contrôles ---
        Label controlsTitle = new Label("Contrôles :");
        controlsTitle.setStyle("-fx-font-size: 20px; -fx-text-fill: #FFD700; -fx-font-weight: bold;");

        GridPane controlsGrid = new GridPane();
        controlsGrid.setHgap(20);
        controlsGrid.setVgap(8);

        String[] actionNames  = {"Haut", "Bas", "Gauche", "Droite", "Attaque"};
        String[] actionValues = {keyUp, keyDown, keyLeft, keyRight, keyAttack};

        for (int i = 0; i < actionNames.length; i++) {
            Label lbl = new Label(actionNames[i] + " :");
            lbl.setStyle("-fx-font-size: 15px; -fx-text-fill: #CCCCCC;");
            Label val = new Label("[" + actionValues[i] + "]");
            val.setStyle("-fx-font-size: 15px; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");
            controlsGrid.add(lbl, 0, i);
            controlsGrid.add(val, 1, i);
        }

        // --- Bouton Retour ---
        Button backBtn = new Button("Retour");
        backBtn.setPrefWidth(200);
        backBtn.setStyle("-fx-font-size:16px; -fx-font-family:'Serif'; -fx-background-color:#464646; -fx-text-fill:#FFD700;");
        backBtn.setOnAction(e -> onClose.run());

        // --- Layout ---
        VBox leftColumn = new VBox(18,
                volumeLabel, volumeSlider,
                brightnessLabel, brightnessSlider,
                colorLabel, colorPicker,
                sizeLabel, sizeBox
        );

        VBox rightColumn = new VBox(18, controlsTitle, controlsGrid);

        HBox columns = new HBox(80, leftColumn, rightColumn);
        columns.setLayoutX(180);
        columns.setLayoutY(110);
        getChildren().add(columns);

        backBtn.setLayoutX(540);
        backBtn.setLayoutY(640);
        getChildren().add(backBtn);
    }

    // --- Getters statiques utilisables depuis partout dans le jeu ---
    public static double getVolumeValue()     { return volumeValue; }
    public static double getBrightnessValue() { return brightnessValue; }
    public static String getPlayerColor()     { return playerColor; }
    public static String getWindowSize()      { return windowSize; }
    public static String getKeyUp()           { return keyUp; }
    public static String getKeyDown()         { return keyDown; }
    public static String getKeyLeft()         { return keyLeft; }
    public static String getKeyRight()        { return keyRight; }
    public static String getKeyAttack()       { return keyAttack; }
}