package com.group5.integration;

import com.group5.main.TestGamePanel;
import com.group5.main.KeyHandler;
import com.group5.entity.Player;
import com.group5.entity.Entity;
import com.group5.main.CollisionChecker;
import com.group5.main.EventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests verifying that keyboard input (KeyHandler)
 * correctly influences player movement through the full game loop
 * (GamePanel.update → Player.update).
 */
public class PlayerKeyHandlerIntegrationTest {

    TestGamePanel gp;
    Player player;
    KeyHandler keyH;

    @BeforeEach
    void setup() {
        gp = new TestGamePanel();
        keyH = gp.keyH;
        player = gp.player;

        // Replace collision + event handlers so they don't interfere.
        gp.cChecker = new CollisionChecker(gp) {
            @Override
            public void checkTile(Entity e) { }
            @Override
            public int checkObject(Entity e, boolean player) { return 999; }
            @Override
            public int checkEntity(Entity e, Entity[][] t) { return 999; }
        };
        gp.eHandler = new EventHandler(gp) {
            @Override public void checkEvent() { }
        };

        gp.gameState = gp.playState; // simulate actual game running
    }

    // --------------------------------------------------------------------
    // MOVEMENT INTEGRATION TEST
    // --------------------------------------------------------------------
    @Test
    void testMoveRightThroughGameLoop() {
        int startX = player.worldx;

        // simulate user pressing "D" key
        keyH.rightPressed = true;

        // GamePanel update SHOULD call player.update()
        gp.update();

        assertEquals("right", player.direction, "Direction should be set by KeyHandler");
        assertTrue(player.worldx > startX, "Player should move right when D is pressed");
    }

    @Test
    void testMoveLeftThroughGameLoop() {
        int startX = player.worldx;

        keyH.leftPressed = true;
        gp.update();

        assertEquals("left", player.direction);
        assertTrue(player.worldx < startX);
    }

    @Test
    void testMoveUpThroughGameLoop() {
        int startY = player.worldy;

        keyH.upPressed = true;
        gp.update();

        assertEquals("up", player.direction);
        assertTrue(player.worldy < startY);
    }

    @Test
    void testMoveDownThroughGameLoop() {
        int startY = player.worldy;

        keyH.downPressed = true;
        gp.update();

        assertEquals("down", player.direction);
        assertTrue(player.worldy > startY);
    }

    // --------------------------------------------------------------------
    // MOVEMENT ANIMATION INTEGRATION
    // --------------------------------------------------------------------
    @Test
    void testSpriteAnimationIncreasesThroughFullUpdate() {
        keyH.rightPressed = true;

        int startSprite = player.spriteNum;

        // simulate multiple updates like the game loop
        for (int i = 0; i < 15; i++) {
            gp.update();
        }

        assertNotEquals(startSprite, player.spriteNum,
                "Sprite should animate when player moves");
    }

    // --------------------------------------------------------------------
    // SHOOTING INTEGRATION
    // --------------------------------------------------------------------
    @Test
    void testShootProjectileThroughGameLoop() {
        player.shotAvailableCounter = 30;
        keyH.shootKeyPressed = true;

        gp.update(); // should trigger projectile logic

        assertEquals(1, gp.projectileList.size(),
                "Projectile should be added through Player.update when Q is pressed");
    }
}
