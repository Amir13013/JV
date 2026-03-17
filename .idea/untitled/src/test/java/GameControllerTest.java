import com.eremon.controllers.GameController;
import com.eremon.models.entity.Player;
import com.eremon.models.player.HeroClass;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameControllerTest {
    private GameController controller;
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Test", 100, 10, 5, 3, 50, 1, 0);
        Pane pane = new Pane();
        HeroClass heroClass = new HeroClass(0);
        controller = new GameController(player, pane, heroClass);
    }

    @Test
    void testIsWalkableWithoutCollisionLayer() {
        assertTrue(controller.isWalkable(5, 5));
    }

    @Test
    void testIsWalkableWithCollisionLayer() {
        int[][] collisionLayer = new int[10][10];
        collisionLayer[5][5] = 1; // bloc
        controller.setSolLayer(collisionLayer);

        assertFalse(controller.isWalkable(5, 5));
        assertTrue(controller.isWalkable(3, 3));
    }

    @Test
    void testInitialGameState() {
        assertEquals("EXPLORATION", controller.getCurrentState());
        assertFalse(controller.isInCombat());
    }
}
