package com.eremon.views;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MapLoader {
    private int mapWidth, mapHeight, tileWidth, tileHeight;
    private List<MapLayer> layers = new ArrayList<>();
    private String mapName = "";
    public static class MapLayer {
        public String name;
        public int[][] tiles;
        public MapLayer(String name, int[][] tiles) {
            this.name = name;
            this.tiles = tiles;
        }
    }

    public boolean loadMap(String mapPath) {
        this.mapName = mapPath.substring(mapPath.lastIndexOf("/") + 1);
        try {
            InputStream is = getClass().getResourceAsStream(mapPath);
            if (is == null) {
                System.err.println(" Fichier introuvable : " + mapPath);
                return false;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            doc.getDocumentElement().normalize();

            Element mapElement = doc.getDocumentElement();
            mapWidth = Integer.parseInt(mapElement.getAttribute("width"));
            mapHeight = Integer.parseInt(mapElement.getAttribute("height"));
            tileWidth = Integer.parseInt(mapElement.getAttribute("tilewidth"));
            tileHeight = Integer.parseInt(mapElement.getAttribute("tileheight"));

            layers.clear();
            NodeList layerList = doc.getElementsByTagName("layer");
            for (int i = 0; i < layerList.getLength(); i++) {
                Element layerElement = (Element) layerList.item(i);
                String layerName = layerElement.getAttribute("name");
                NodeList dataList = layerElement.getElementsByTagName("data");
                if (dataList.getLength() > 0) {
                    Element data = (Element) dataList.item(0);
                    String encoding = data.getAttribute("encoding");
                    int[][] layerTiles = new int[mapHeight][mapWidth];
                    if ("csv".equals(encoding)) {
                        loadCSVData(data.getTextContent(), layerTiles);
                    } else {
                        loadTileData(data, layerTiles);
                    }
                    layers.add(new MapLayer(layerName, layerTiles));
                }
            }
            return true;
        } catch (Exception e) {
            System.err.println(" Erreur : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void loadCSVData(String csvData, int[][] tiles) {
        String[] rows = csvData.trim().split("\n");
        for (int y = 0; y < mapHeight && y < rows.length; y++) {
            String[] cols = rows[y].trim().split(",");
            for (int x = 0; x < mapWidth && x < cols.length; x++) {
                try {
                    tiles[y][x] = (int) Long.parseLong(cols[x].trim());
                } catch (Exception ex) {
                    tiles[y][x] = 0;
                }
            }
        }
    }
    private void loadTileData(Element dataElement, int[][] tiles) {
        NodeList tileList = dataElement.getElementsByTagName("tile");
        for (int i = 0; i < tileList.getLength(); i++) {
            Element tile = (Element) tileList.item(i);
            try {
                int gid = (int) Long.parseLong(tile.getAttribute("gid"));
                int x = i % mapWidth;
                int y = i / mapWidth;
                tiles[y][x] = gid;
            } catch (Exception ex) {/* skip */}
        }
    }
    public int getMapWidth() { return mapWidth; }
    public int getMapHeight() { return mapHeight; }
    public int getTileWidth() { return tileWidth; }
    public int getTileHeight() { return tileHeight; }
    public List<MapLayer> getLayers() { return layers; }
    public String getMapName() { return mapName; }
}
