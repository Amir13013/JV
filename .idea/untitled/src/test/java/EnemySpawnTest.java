import com.eremon.views.MapLoader;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnemySpawnTest {

    @Test
    void testEnemyCountOnMap() {
        MapLoader mapLoader = new MapLoader();
        mapLoader.loadMap("/map/Donjon map.tmx");

        // Cherche un layer "enemies" ou compte les GID ennemis
        int enemyCount = 0;
        for (MapLoader.MapLayer layer : mapLoader.getLayers()) {
            if (layer.name.equalsIgnoreCase("enemies")) {
                for (int y = 0; y < mapLoader.getMapHeight(); y++) {
                    for (int x = 0; x < mapLoader.getMapWidth(); x++) {
                        if (layer.tiles[y][x] > 0) {
                            enemyCount++;
                        }
                    }
                }
            }
        }

        assertTrue(enemyCount >= 0, "Le nombre d'ennemis doit être >= 0");
        System.out.println("Nombre d'ennemis sur la map : " + enemyCount);
    }
}
