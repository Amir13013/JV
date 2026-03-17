package com.eremon.controllers;

import com.eremon.models.entity.Player;
import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

public class GameController {

    public static final double PLAYER_SPEED = 180.0; // pixels par seconde

    private Player player;
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private boolean inCombat = false;

    public GameController() {}

    public void initPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void keyPressed(KeyCode code) {
        pressedKeys.add(code);
    }

    public void keyReleased(KeyCode code) {
        pressedKeys.remove(code);
    }

    public void update(double deltaTime) {
        if (player == null || inCombat) return;

        double dx = 0, dy = 0;

        if (pressedKeys.contains(KeyCode.Z)    || pressedKeys.contains(KeyCode.UP))    dy -= PLAYER_SPEED * deltaTime;
        if (pressedKeys.contains(KeyCode.S)    || pressedKeys.contains(KeyCode.DOWN))  dy += PLAYER_SPEED * deltaTime;
        if (pressedKeys.contains(KeyCode.Q)    || pressedKeys.contains(KeyCode.LEFT))  dx -= PLAYER_SPEED * deltaTime;
        if (pressedKeys.contains(KeyCode.D)    || pressedKeys.contains(KeyCode.RIGHT)) dx += PLAYER_SPEED * deltaTime;

        if (dx != 0 || dy != 0) {
            player.moveBy(dx, dy);
        }
    }

    public boolean isInCombat()              { return inCombat; }
    public void setInCombat(boolean combat)  { this.inCombat = combat; }
}