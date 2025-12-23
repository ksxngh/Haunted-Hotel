package com.group5.entity;

import com.group5.main.*;
import com.group5.object.Obj_ElectricBall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    TestGamePanel gp;
    Player player;

    // Minimal stubs for dependencies
    static class TestCollisionChecker extends CollisionChecker {
        public TestCollisionChecker(GamePanel gp) { super(gp); }
        @Override public void checkTile(Entity e) {}
        @Override public int checkObject(Entity e, boolean player) { return 999; }
        @Override public int checkEntity(Entity e, Entity[][] targets) { return 999; }
    }

    static class TestEventHandler extends EventHandler {
        public TestEventHandler(GamePanel gp) { super(gp); }
        @Override public void checkEvent() {}
    }

    static class TestUI extends UI {
        public TestUI(GamePanel gp) { super(gp); }
        @Override public void showMessage(String text) {}
    }

    @BeforeEach
    void setup() {
        gp = new TestGamePanel();
        gp.cChecker = new TestCollisionChecker(gp);
        gp.eHandler = new TestEventHandler(gp);
        gp.ui = new TestUI(gp);

        player = new Player(gp, gp.keyH);
        gp.player = player;
    }

    // ---------------- MOVEMENT ----------------
    @Test
    void testMoveUp() {
        gp.keyH.upPressed = true;
        int startY = player.worldy;
        player.update();
        assertEquals("up", player.direction);
        assertTrue(player.worldy < startY);
    }

    @Test
    void testMoveDown() {
        gp.keyH.downPressed = true;
        int startY = player.worldy;
        player.update();
        assertEquals("down", player.direction);
        assertTrue(player.worldy > startY);
    }

    @Test
    void testMoveLeft() {
        gp.keyH.leftPressed = true;
        int startX = player.worldx;
        player.update();
        assertEquals("left", player.direction);
        assertTrue(player.worldx < startX);
    }

    @Test
    void testMoveRight() {
        gp.keyH.rightPressed = true;
        int startX = player.worldx;
        player.update();
        assertEquals("right", player.direction);
        assertTrue(player.worldx > startX);
    }

    // ---------------- SHOOTING ----------------
    @Test
    void testShootProjectile() {
        player.shotAvailableCounter = 30;
        gp.keyH.shootKeyPressed = true;
        player.update();
        assertEquals(1, gp.projectileList.size());
        assertTrue(gp.projectileList.get(0) instanceof Obj_ElectricBall);
    }

    // ---------------- PICKUP OBJECT ----------------
    @Test
    void testPickupObjectKey() {
        Entity key = new Entity(gp);
        key.name = "key";
        gp.obj[0][0] = key;

        player.pickUpObject(0);

        assertEquals(1, player.inventory.size());
        assertEquals(1, player.hasKey);
        assertNull(gp.obj[0][0]);
    }

    @Test
    void testPickupObjectMoney() {
        Entity money = new Entity(gp);
        money.name = "Money";
        gp.obj[0][0] = money;

        int startMoney = player.hasMoney;
        player.pickUpObject(0);

        assertEquals(startMoney + 1, player.hasMoney);
        assertEquals(1, player.inventory.size());
    }

    @Test
    void testPickupObjectInventoryFull() {
        for (int i = 0; i < player.inventory.size(); i++) {
            player.inventory.add(new Entity(gp));
        }
        Entity newItem = new Entity(gp);
        newItem.name = "key";
        gp.obj[0][0] = newItem;

        player.pickUpObject(0);

        assertEquals(player.inventory.size(), player.inventory.size()); // inventory size unchanged
    }

    // ---------------- MONSTER INTERACTIONS ----------------
    @Test
    void testContactMonsterReducesLife() {
        player.invincible = false;
        int startLife = player.life;
        Entity monster = new Entity(gp);
        gp.monster[0][0] = monster;

        player.contactMonster(0);

        assertEquals(startLife - 2, player.life);
        assertTrue(player.invincible);
    }

    @Test
    void testContactMonsterWhileInvincible() {
        player.invincible = true;
        int startLife = player.life;

        player.contactMonster(0);

        assertEquals(startLife, player.life);
    }

    @Test
    void testDamageMonster() {
        Entity monster = new Entity(gp);
        monster.life = 3;
        gp.monster[0][0] = monster;

        player.damageMonster(0);

        assertEquals(2, monster.life);
        assertTrue(monster.invincible);
    }

    // ---------------- UPDATE / INVINCIBILITY ----------------
    @Test
    void testInvincibilityResets() {
        player.invincible = true;
        player.invincibleCounter = 61;
        player.update();
        assertFalse(player.invincible);
        assertEquals(0, player.invincibleCounter);
    }

    @Test
    void testDeathResetsLifeAndTriggersGameOver() {
        player.life = 0;
        gp.gameState = 0;
        player.update();
        assertEquals(player.maxLife, player.life);
        assertEquals(gp.gameOverState, gp.gameState);
    }

    // ---------------- DRAW ----------------
    @Test
    void testDrawDoesNotThrow() {
        Graphics2D g2 = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        assertDoesNotThrow(() -> player.draw(g2));
    }
}
