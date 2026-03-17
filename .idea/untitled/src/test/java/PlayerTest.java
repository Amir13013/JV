import com.eremon.models.entity.Player;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests séquentiels du joueur : création → déplacement → XP → levelup → inventaire → mort
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlayerTest {

    static Player player;

    @BeforeAll
    static void setup() {
        player = new Player("Marvin", 100, 15, 5, 10, 50, 1, 0);
    }

    @Test
    @Order(1)
    public void testPlayerCreation() {
        assertNotNull(player, "Le joueur ne doit pas être null");
        assertEquals("Marvin", player.getName());
        assertEquals(100, player.getHp());
        assertEquals(1, player.getLevel());
        assertEquals(0, player.getExperience());
    }

    @Test
    @Order(2)
    public void testPlayerInitialPosition() {
        assertEquals(0.0, player.getX(), 0.01);
        assertEquals(0.0, player.getY(), 0.01);
    }

    @Test
    @Order(3)
    public void testPlayerMovementPlayableSpeed() {
        // Simule 1 seconde de déplacement à 180px/s (vitesse jouable)
        double speed = 180.0;
        player.setX(0);
        player.setY(0);
        player.moveBy(speed * 1.0, 0);
        assertEquals(180.0, player.getX(), 0.01,
                "Le joueur doit se déplacer à au moins 100px/s pour être jouable");
        assertTrue(player.getX() >= 100,
                "Vitesse trop lente ! (< 100px/s) — jeu non jouable en démo");
    }

    @Test
    @Order(4)
    public void testPlayerGainXPNoLevelUp() {
        player.gainExperience(50);
        assertEquals(50, player.getExperience());
        assertEquals(1, player.getLevel(), "Pas encore de level up à 50 XP");
    }

    @Test
    @Order(5)
    public void testPlayerLevelUp() {
        player.gainExperience(60); // total = 110, dépasse 100 → level up
        assertEquals(2, player.getLevel(), "Le joueur doit passer niveau 2");
        assertTrue(player.getExperience() < player.getExperienceToNextLevel(),
                "L'XP restant doit être inférieur au seuil du prochain niveau");
    }

    @Test
    @Order(6)
    public void testPlayerStatsAfterLevelUp() {
        // Après un level up : +10 HP max, +5 mana, +2 atk, +1 def
        assertTrue(player.getMaxHp() >= 110, "MaxHP doit avoir augmenté après level up");
    }

    @Test
    @Order(7)
    public void testPlayerTakeDamage() {
        int hpBefore = player.getHp();
        player.takeDamage(30);
        assertEquals(hpBefore - 30, player.getHp(), "Les HP doivent diminuer après dégâts");
        assertTrue(player.isAlive(), "Le joueur doit encore être en vie");
    }

    @Test
    @Order(8)
    public void testPlayerHeal() {
        int hpBefore = player.getHp();
        player.heal(10);
        assertTrue(player.getHp() >= hpBefore, "Les HP doivent augmenter après soin");
        assertTrue(player.getHp() <= player.getMaxHp(), "Les HP ne dépassent pas le max");
    }

    @Test
    @Order(9)
    public void testPlayerDeath() {
        player.takeDamage(9999);
        assertEquals(0, player.getHp(), "Les HP doivent être à 0 après dégâts fatals");
        assertFalse(player.isAlive(), "Le joueur doit être mort");
        assertTrue(player.isDead(), "isDead() doit retourner true");
    }
}