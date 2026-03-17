package com.eremon.models.map;

public class Map {
    private int width;
    private int height;
    private int[][] tiles;

    public static final int TILE_FLOOR = 0;
    public static final int TILE_WALL = 1;
    public static final int TILE_WATER = 2;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new int[height][width];
        initializeDefaultMap();
    }

    private void initializeDefaultMap() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (y == 0 || y == height - 1 || x == 0 || x == width - 1) {
                    tiles[y][x] = TILE_WALL;
                } else {
                    tiles[y][x] = TILE_FLOOR;
                }
            }
        }
    }

    public boolean isWalkable(double x, double y) {
        int tileX = (int) x;
        int tileY = (int) y;

        if (tileX < 0 || tileX >= width || tileY < 0 || tileY >= height) {
            return false;
        }

        return tiles[tileY][tileX] == TILE_FLOOR;
    }

    public int getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return TILE_WALL;
        }
        return tiles[y][x];
    }

    public void setTile(int x, int y, int tileType) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            tiles[y][x] = tileType;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void displayMap() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                switch (tiles[y][x]) {
                    case TILE_FLOOR:
                        System.out.print(". ");
                        break;
                    case TILE_WALL:
                        System.out.print("# ");
                        break;
                    case TILE_WATER:
                        System.out.print("~ ");
                        break;
                    default:
                        System.out.print("? ");
                }
            }
            System.out.println();
        }
    }
}
