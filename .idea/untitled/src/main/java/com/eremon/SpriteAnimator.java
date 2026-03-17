package com.eremon;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SpriteAnimator {

    private Image[] frames;
    private int currentFrame = 0;
    private double frameDuration;
    private double elapsed = 0;
    private int frameCount;

    public SpriteAnimator(String basePath, int frameCount, double frameDuration) {
        this.frameCount    = frameCount;
        this.frameDuration = frameDuration;
        this.frames        = new Image[frameCount];
        loadFrames(basePath, frameCount);
    }

    private void loadFrames(String basePath, int count) {
        for (int i = 0; i < count; i++) {
            String path = basePath + "_0" + (i + 1) + ".png";
            try {
                Image img = new Image(getClass().getResourceAsStream(path));
                frames[i] = img;
            } catch (Exception e) {
                System.err.println("Frame non trouvée : " + path);
                frames[i] = null;
            }
        }
    }

    public void update(double deltaTime) {
        elapsed += deltaTime;
        if (elapsed >= frameDuration) {
            elapsed -= frameDuration;
            currentFrame = (currentFrame + 1) % frameCount;
        }
    }

    public void render(GraphicsContext gc, double x, double y, double width, double height) {
        if (frames != null && currentFrame < frames.length && frames[currentFrame] != null) {
            gc.drawImage(frames[currentFrame], x, y, width, height);
        }
    }

    public Image getCurrentFrame() {
        if (frames != null && currentFrame < frames.length) return frames[currentFrame];
        return null;
    }
}