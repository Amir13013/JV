import com.eremon.views.MapLoader;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MapLoaderTest {

    @Test
    void testMapLoading() {
        MapLoader mapLoader = new MapLoader();
        boolean loaded = mapLoader.loadMap("/map/Donjon map.tmx");

        assertTrue(loaded, "La map devrait se charger correctement");
        assertTrue(mapLoader.getMapWidth() > 0);
        assertTrue(mapLoader.getMapHeight() > 0);
    }

    @Test
    void testCollisionLayerExists() {
        MapLoader mapLoader = new MapLoader();
        mapLoader.loadMap("/map/Donjon map.tmx");

        boolean hasCollisionLayer = mapLoader.getLayers().stream()
                .anyMatch(layer -> layer.name.equalsIgnoreCase("collision"));

        assertTrue(hasCollisionLayer, "Le layer 'collision' doit exister");
    }
}
