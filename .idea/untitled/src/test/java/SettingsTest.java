import com.eremon.views.SettingsView;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests automatiques des valeurs par défaut et des getters statiques de SettingsView
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SettingsTest {

    @Test
    @Order(1)
    public void testDefaultVolume() {
        assertEquals(80.0, SettingsView.getVolumeValue(), 0.01,
                "Le volume par défaut doit être 80");
    }

    @Test
    @Order(2)
    public void testDefaultBrightness() {
        assertEquals(60.0, SettingsView.getBrightnessValue(), 0.01,
                "La luminosité par défaut doit être 60");
    }

    @Test
    @Order(3)
    public void testDefaultPlayerColorFormat() {
        String color = SettingsView.getPlayerColor();
        assertNotNull(color, "La couleur du joueur ne doit pas être null");
        assertTrue(color.startsWith("#"), "La couleur doit être au format #RRGGBB");
        assertEquals(7, color.length(), "La couleur doit avoir 7 caractères (#RRGGBB)");
    }

    @Test
    @Order(4)
    public void testDefaultWindowSize() {
        assertEquals("1280x720", SettingsView.getWindowSize(),
                "La taille de fenêtre par défaut doit être 1280x720");
    }

    @Test
    @Order(5)
    public void testDefaultControls() {
        assertEquals("Z",      SettingsView.getKeyUp(),     "Touche Haut = Z");
        assertEquals("S",      SettingsView.getKeyDown(),   "Touche Bas = S");
        assertEquals("Q",      SettingsView.getKeyLeft(),   "Touche Gauche = Q");
        assertEquals("D",      SettingsView.getKeyRight(),  "Touche Droite = D");
        assertEquals("ESPACE", SettingsView.getKeyAttack(), "Touche Attaque = ESPACE");
    }

    @Test
    @Order(6)
    public void testWindowSizeIsKnownFormat() {
        String size = SettingsView.getWindowSize();
        assertTrue(size.matches("\\d+x\\d+"),
                "La taille de fenêtre doit être au format NxN (ex: 1280x720)");
    }
}