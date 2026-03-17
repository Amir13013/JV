package com.eremon.views;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import com.eremon.models.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class MapRenderer {

    private MapLoader mapLoader;
    private int tileWidth;
    private int tileHeight;
    private List<TilesetInfo> tilesets = new ArrayList<>();

    private static class TilesetInfo {
        int firstGid;
        int lastGid;
        Image image;
        int columns;
        int tileWidth;
        int tileHeight;
        String name;

        TilesetInfo(int firstGid, int tileCount, Image image, int columns, int tw, int th, String name) {
            this.firstGid = firstGid;
            this.lastGid = firstGid + tileCount - 1;
            this.image = image;
            this.columns = columns;
            this.tileWidth = tw;
            this.tileHeight = th;
            this.name = name;
        }

        boolean contains(int gid) {
            return gid >= firstGid && gid <= lastGid;
        }
    }

    public MapRenderer(MapLoader mapLoader, String basePath) {
        this.mapLoader = mapLoader;
        this.tileWidth = mapLoader.getTileWidth();
        this.tileHeight = mapLoader.getTileHeight();

        // Tileset principal - correspond exactement à ton .tmx : gid, colonnes, tilecount...
        loadTileset(1, 440, basePath + "/Tileset.png", 8, 16, 16, "Principal");

        // Tileset décoratif - ADAPTE si différent dans ton tmx (chemin, firstgid, colonnes, tilecount)
        loadTileset(441, 24, basePath + "/decorative.png", 6, 16, 16, "Decors"); // <- adapte le GID, le nombre de tiles et columns si besoin !
    }

    private void loadTileset(int firstGid, int tileCount, String path, int columns, int tw, int th, String name) {
        try {
            Image img = new Image(getClass().getResourceAsStream(path));
            tilesets.add(new TilesetInfo(firstGid, tileCount, img, columns, tw, th, name));
        } catch (Exception e) {
            System.err.println("  ⚠️  Erreur : " + name + " (" + path + ")");
        }
    }

    private TilesetInfo getTilesetForGid(int gid) {
        for (TilesetInfo ts : tilesets) {
            if (ts.contains(gid)) {
                return ts;
            }
        }
        return null;
    }

    public void renderCamera(GraphicsContext gc, double cameraX, double cameraY, int viewWidth, int viewHeight) {
        if (tilesets.isEmpty()) {
            System.err.println("❌ Aucun tileset chargé !");
            return;
        }

        int startTileX = Math.max(0, (int)(cameraX / tileWidth));
        int startTileY = Math.max(0, (int)(cameraY / tileHeight));
        int endTileX = Math.min(mapLoader.getMapWidth(), (int)((cameraX + viewWidth) / tileWidth) + 1);
        int endTileY = Math.min(mapLoader.getMapHeight(), (int)((cameraY + viewHeight) / tileHeight) + 1);

        List<MapLoader.MapLayer> layers = mapLoader.getLayers();
        for (MapLoader.MapLayer layer : layers) {
            for (int y = startTileY; y < endTileY; y++) {
                for (int x = startTileX; x < endTileX; x++) {
                    int tileId = layer.tiles[y][x];
                    if (tileId > 0) {
                        drawTile(gc, tileId, x, y);
                    }
                }
            }
        }
    }

    private void drawTile(GraphicsContext gc, int gid, int mapX, int mapY) {
        TilesetInfo tileset = getTilesetForGid(gid);
        if (tileset == null) return;

        int localId = gid - tileset.firstGid;
        int srcX = (localId % tileset.columns) * tileset.tileWidth;
        int srcY = (localId / tileset.columns) * tileset.tileHeight;
        int destX = mapX * tileWidth;
        int destY = mapY * tileHeight;

        try {
            gc.drawImage(
                    tileset.image,
                    srcX, srcY, tileset.tileWidth, tileset.tileHeight,
                    destX, destY, tileWidth, tileHeight
            );
        } catch (Exception e) { }
    }

    public void render(GraphicsContext gc, Player player) {
        if (tilesets.isEmpty()) {
            System.err.println("❌ Aucun tileset chargé !");
            return;
        }

        int windowWidth = (int) gc.getCanvas().getWidth();
        int windowHeight = (int) gc.getCanvas().getHeight();
        int mapPxWidth = mapLoader.getMapWidth() * tileWidth;
        int mapPxHeight = mapLoader.getMapHeight() * tileHeight;
        int offsetX = (windowWidth - mapPxWidth) / 2;
        int offsetY = (windowHeight - mapPxHeight) / 2;

        List<MapLoader.MapLayer> layers = mapLoader.getLayers();
        for (MapLoader.MapLayer layer : layers) {
            renderLayerCentered(gc, layer.tiles, offsetX, offsetY);
        }

        drawPlayer(gc, player, offsetX, offsetY);
        drawPlayerUI(gc, player);
    }

    private void renderLayerCentered(GraphicsContext gc, int[][] tiles, int offsetX, int offsetY) {
        for (int y = 0; y < mapLoader.getMapHeight(); y++) {
            for (int x = 0; x < mapLoader.getMapWidth(); x++) {
                int tileId = tiles[y][x];
                if (tileId > 0) {
                    drawTileCentered(gc, tileId, x, y, offsetX, offsetY);
                }
            }
        }
    }

    private void drawTileCentered(GraphicsContext gc, int gid, int mapX, int mapY, int offsetX, int offsetY) {
        TilesetInfo tileset = getTilesetForGid(gid);
        if (tileset == null) return;

        int localId = gid - tileset.firstGid;
        int srcX = (localId % tileset.columns) * tileset.tileWidth;
        int srcY = (localId / tileset.columns) * tileset.tileHeight;
        int destX = offsetX + mapX * tileWidth;
        int destY = offsetY + mapY * tileHeight;

        try {
            gc.drawImage(
                    tileset.image,
                    srcX, srcY, tileset.tileWidth, tileset.tileHeight,
                    destX, destY, tileWidth, tileHeight
            );
        } catch (Exception e) { }
    }

    private void drawPlayer(GraphicsContext gc, Player player, int offsetX, int offsetY) {
        double playerX = offsetX + player.getX();
        double playerY = offsetY + player.getY();

        gc.setFill(Color.CYAN);
        gc.fillOval(playerX - 8, playerY - 8, 16, 16);

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeOval(playerX - 8, playerY - 8, 16, 16);

        gc.setFill(Color.WHITE);
        gc.fillText(player.getName(), playerX - 20, playerY - 12);
    }

    private void drawPlayerUI(GraphicsContext gc, Player player) {
        int windowWidth = (int) gc.getCanvas().getWidth();

        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRect(10, 10, 300, 100);

        gc.setFill(Color.WHITE);
        gc.fillText("🗡️ " + player.getName() + " - Niveau " + player.getLevel(), 20, 30);

        double hpPercent = (double) player.getHealth() / player.getMaxHealth();
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(20, 40, 200, 15);
        gc.setFill(Color.rgb(200, 50, 50));
        gc.fillRect(20, 40, 200 * hpPercent, 15);
        gc.setStroke(Color.WHITE);
        gc.strokeRect(20, 40, 200, 15);
        gc.setFill(Color.WHITE);
        gc.fillText("HP: " + player.getHealth() + "/" + player.getMaxHealth(), 230, 52);

        gc.setFill(Color.DARKGRAY);
        gc.fillRect(20, 60, 200, 15);
        gc.setFill(Color.rgb(50, 100, 200));
        gc.fillRect(20, 60, 200 * 0.8, 15);
        gc.setStroke(Color.WHITE);
        gc.strokeRect(20, 60, 200, 15);
        gc.setFill(Color.WHITE);
        gc.fillText("MP: " + player.getMana() + "/100", 230, 72);

        double xpPercent = (double) player.getExperience() / player.getExperienceToNextLevel();
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(20, 80, 200, 10);
        gc.setFill(Color.rgb(255, 215, 0));
        gc.fillRect(20, 80, 200 * xpPercent, 10);
        gc.setStroke(Color.WHITE);
        gc.strokeRect(20, 80, 200, 10);
        gc.setFill(Color.WHITE);
        gc.fillText("XP: " + player.getExperience() + "/" + player.getExperienceToNextLevel(), 230, 89);

        gc.setFill(Color.rgb(255, 255, 255, 0.6));
        gc.fillText("ZQSD: Déplacer | I: Inventaire | E: Combat test", 20, windowWidth - 20);
    }

    public void render(GraphicsContext gc) {
        if (tilesets.isEmpty()) {
            System.err.println(" Aucun tileset chargé !");
            return;
        }
        int windowWidth = (int) gc.getCanvas().getWidth();
        int windowHeight = (int) gc.getCanvas().getHeight();
        int mapPxWidth = mapLoader.getMapWidth() * tileWidth;
        int mapPxHeight = mapLoader.getMapHeight() * tileHeight;
        int offsetX = (windowWidth - mapPxWidth) / 2;
        int offsetY = (windowHeight - mapPxHeight) / 2;

        List<MapLoader.MapLayer> layers = mapLoader.getLayers();
        for (MapLoader.MapLayer layer : layers) {
            renderLayerCentered(gc, layer.tiles, offsetX, offsetY);
        }
    }
}
