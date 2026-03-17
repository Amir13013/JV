
package com.eremon;

import com.eremon.controllers.GameController;
import com.eremon.models.entity.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    private static final int WINDOW_WIDTH  = 1280;
    private static final int WINDOW_HEIGHT = 720;
    private static final int TILE_SIZE     = 16;

    private enum GameState {
        MAIN_MENU, CLASS_SELECTION, PLAYING, PAUSED, INVENTORY, SETTINGS
    }

    private GameState currentState = GameState.MAIN_MENU;

    private MapLoader           mapLoader;
    private MapRenderer         mapRenderer;
    private Player              player;
    private GameController      gameController;
    private HeroClass           selectedHeroClass;

    private AnimationTimer gameLoop;
    private long lastUpdate = System.nanoTime();

    private MenuView           menuView;
    private ClassSelectionView classSelectionView;
    private HUDView            hudView;
    private PauseView          pauseView;
    private InventoryView      inventoryView;

    private SpriteAnimator playerAnimator;

    private Pane            root;
    private Canvas          canvas;
    private GraphicsContext gc;
    private Scene           scene;

    @Override
    public void start(Stage primaryStage) {
        root   = new Pane();
        canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        gc     = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setOnKeyPressed(this::handleKeyPressed);
        scene.setOnKeyReleased(this::handleKeyReleased);

        primaryStage.setTitle("Eremon");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        gameController = new GameController();
        showMainMenu();
    }

    // ─── NAVIGATION ────────────────────────────────────────────────────────────

    private void showMainMenu() {
        currentState = GameState.MAIN_MENU;
        menuView = new MenuView(
                this::showClassSelection,
                this::showSettings,
                this::quitGame
        );
        root.getChildren().clear();
        root.getChildren().addAll(canvas, menuView);
    }

    private void showClassSelection() {
        currentState = GameState.CLASS_SELECTION;
        classSelectionView = new ClassSelectionView(
                heroClass -> { selectedHeroClass = heroClass; startGame(); },
                this::showMainMenu
        );
        root.getChildren().clear();
        root.getChildren().addAll(canvas, classSelectionView);
        classSelectionView.requestFocus();
    }

    private void showSettings() {
        GameState previousState = currentState;
        currentState = GameState.SETTINGS;
        SettingsView settingsView = new SettingsView(() -> {
            root.getChildren().remove(settingsView);
            if (previousState == GameState.PLAYING) {
                currentState = GameState.PLAYING;
                if (gameLoop != null) gameLoop.start();
            } else if (previousState == GameState.PAUSED) {
                currentState = GameState.PAUSED;
            } else {
                showMainMenu();
            }
        });
        if (gameLoop != null) gameLoop.stop();
        root.getChildren().add(settingsView);
    }

    private void showPauseMenu() {
        currentState = GameState.PAUSED;
        if (gameLoop != null) gameLoop.stop();
        pauseView = new PauseView(
                this::resumeGame,
                this::showSettings,
                this::showInventory,
                this::backToMainMenu
        );
        root.getChildren().add(pauseView);
    }

    private void resumeGame() {
        currentState = GameState.PLAYING;
        root.getChildren().remove(pauseView);
        if (gameLoop != null) gameLoop.start();
    }

    private void showInventory() {
        currentState = GameState.INVENTORY;
        if (gameLoop != null) gameLoop.stop();
        inventoryView = new InventoryView(player, this::closeInventory);
        root.getChildren().add(inventoryView);
    }

    private void closeInventory() {
        currentState = GameState.PLAYING;
        root.getChildren().remove(inventoryView);
        if (gameLoop != null) gameLoop.start();
    }

    private void backToMainMenu() {
        currentState = GameState.MAIN_MENU;
        if (gameLoop != null) gameLoop.stop();
        showMainMenu();
    }

    private void quitGame() {
        System.exit(0);
    }

    // ─── GAME START ────────────────────────────────────────────────────────────

    private void startGame() {
        if (selectedHeroClass == null) return;

        player = selectedHeroClass.createPlayer();
        player.setPosition(WINDOW_WIDTH / 2.0, WINDOW_HEIGHT / 2.0);
        gameController.initPlayer(player);

        mapLoader = new MapLoader();
        boolean loaded = mapLoader.loadMap("/maps/map1.tmx");
        if (!loaded) System.err.println("Carte non chargée !");

        mapRenderer  = new MapRenderer(mapLoader, "/tilesets");
        playerAnimator = new SpriteAnimator(selectedHeroClass.getWalkSpritePath()
                .replace("_01.png", "").replace("_03.png", ""), 4, 0.15);

        root.getChildren().clear();
        root.getChildren().add(canvas);
        currentState = GameState.PLAYING;
        lastUpdate   = System.nanoTime();

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;
                if (deltaTime > 0.05) deltaTime = 0.05;
                update(deltaTime);
                render(gc);
            }
        };
        gameLoop.start();
    }

    // ─── GAME LOOP ─────────────────────────────────────────────────────────────

    private void update(double deltaTime) {
        if (currentState != GameState.PLAYING || player == null) return;
        gameController.update(deltaTime);
        if (playerAnimator != null) playerAnimator.update(deltaTime);
    }

    private void render(GraphicsContext gc) {
        gc.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        if (mapRenderer != null && player != null) {
            mapRenderer.render(gc, player);
        }
    }

    // ─── INPUT ─────────────────────────────────────────────────────────────────

    private void handleKeyPressed(javafx.scene.input.KeyEvent event) {
        KeyCode code = event.getCode();
        gameController.keyPressed(code);

        switch (currentState) {
            case PLAYING -> {
                if (code == KeyCode.ESCAPE) showPauseMenu();
                if (code == KeyCode.I)      showInventory();
            }
            case PAUSED -> {
                if (pauseView != null) pauseView.handleKeyPress(code);
            }
            case INVENTORY -> {
                if (inventoryView != null) inventoryView.handleKeyPress(code);
            }
            case CLASS_SELECTION -> {
                if (classSelectionView != null) classSelectionView.handleKeyPress(code);
            }
            default -> {}
        }
    }

    private void handleKeyReleased(javafx.scene.input.KeyEvent event) {
        gameController.keyReleased(event.getCode());
    }

    // ─── GETTER ────────────────────────────────────────────────────────────────

    public HeroClass getSelectedHeroClass() {
        return selectedHeroClass;
    }

    public static void main(String[] args) {
        launch(args);
    }
}