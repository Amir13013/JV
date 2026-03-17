package com.eremon.utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class MusicManager {
    private static MediaPlayer currentPlayer;

    public static void playMusic(String filePath, boolean loop) {
        stopMusic();

        try {

            if (!filePath.endsWith(".mp3")) {
                filePath = filePath.replace(".wav", ".mp3");
            }

            // Charger la ressource correctement
            URL url = MusicManager.class.getResource(filePath);
            if (url == null) {
                System.err.println(" Fichier introuvable: " + filePath);
                return;
            }

            String resource = url.toExternalForm();
            System.out.println(" Lecture: " + resource);

            Media media = new Media(resource);
            currentPlayer = new MediaPlayer(media);
            currentPlayer.setVolume(0.3);

            if (loop) {
                currentPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            }

            currentPlayer.setOnReady(() -> {
                System.out.println(" Musique prête !");
                currentPlayer.play();
            });

            currentPlayer.setOnError(() -> {
                System.err.println(" Erreur lecteur media !");
                if (currentPlayer.getError() != null) {
                    System.err.println(currentPlayer.getError().getMessage());
                    currentPlayer.getError().printStackTrace();
                }
            });

        } catch (Exception e) {
            System.err.println(" Erreur lors du chargement: " + filePath);
            e.printStackTrace();
        }
    }

    public static void stopMusic() {
        if (currentPlayer != null) {
            currentPlayer.stop();
            currentPlayer.dispose();
            currentPlayer = null;
            System.out.println(" Musique arrêtée");
        }
    }
}
