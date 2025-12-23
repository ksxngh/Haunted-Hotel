package com.group5;

import com.group5.main.GamePanel;
import com.group5.entity.Projectile;
import com.group5.monster.Mon_Boss;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MonBossTest {
    GamePanel gp;
    Mon_Boss boss;

    @BeforeEach
    void setup() {
        gp = new GamePanel();
        boss = new Mon_Boss(gp);
        gp.player.worldx = 100;
        gp.player.worldy = 100;
        boss.worldx = 150;
        boss.worldy = 150;
    }

    @Test
    void testConstructorAndGetImage() {
        assertEquals("Boss", boss.name);
        assertEquals(10, boss.maxLife);
        assertNotNull(boss.projectile);
    }

    @Test
    void testUpdateTurnsOnPathWhenClose() {
        boss.worldx = gp.player.worldx + 10;
        boss.worldy = gp.player.worldy + 10;
        boss.onPath = false;
        boss.update();
        assertTrue(boss.onPath);  // should turn on path
    }

    @Test
    void testUpdateTurnsOffPathWhenFar() {
        boss.worldx = gp.player.worldx + gp.tileSize * 20;
        boss.worldy = gp.player.worldy + gp.tileSize * 20;
        boss.onPath = true;
        boss.update();
        assertFalse(boss.onPath);  // should turn off path
    }

    @Test
    void testUpdateIncrementsShotCounter() {
        boss.shotAvailableCounter = 20;
        boss.update();
        assertEquals(21, boss.shotAvailableCounter);  // should increment counter
    }

    @Test
    void testSetActionPathFollowing() {
        boss.onPath = true;
        boss.shotAvailableCounter = 30;
        int before = gp.projectileList.size();
        
        // Call setAction multiple times since projectile firing is probabilistic (81% chance)
        // This ensures at least one projectile is fired
        boolean projectileFired = false;
        for (int attempt = 0; attempt < 10; attempt++) {
            boss.setAction();
            if (gp.projectileList.size() > before) {
                projectileFired = true;
                break;
            }
            boss.shotAvailableCounter = 30; // Reset counter for next attempt
        }
        
        assertTrue(boss.onPath);  // should be following the player
        assertTrue(projectileFired);  // should fire a projectile (eventually)
    }

    @Test
    void testSetActionRandomMovement() {
        boss.onPath = false;
        boss.actionLockCounter = 180;
        boss.setAction();
        assertTrue(boss.direction.equals("up") ||
                   boss.direction.equals("down") ||
                   boss.direction.equals("left") ||
                   boss.direction.equals("right"));  // should randomly pick a direction
    }

    @Test
    void testDamageReactionChangesDirection() {
        // Test for all possible player directions and verify the boss reacts accordingly
        gp.player.direction = "up";
        boss.damageReaction();
        assertEquals("down", boss.direction);  // should change direction to down

        gp.player.direction = "down";
        boss.damageReaction();
        assertEquals("up", boss.direction);  // should change direction to up

        gp.player.direction = "left";
        boss.damageReaction();
        assertEquals("right", boss.direction);  // should change direction to right

        gp.player.direction = "right";
        boss.damageReaction();
        assertEquals("left", boss.direction);  // should change direction to left
    }

    @Test
    void testCheckDropAddsMoneyObject() {
        int before = countObjects();
        boss.checkDrop();
        assertTrue(countObjects() > before);  // should drop money
    }

    private int countObjects() {
        int count = 0;
        for (int i = 0; i < gp.obj[gp.currentMap].length; i++) {
            if (gp.obj[gp.currentMap][i] != null) count++;
        }
        return count;
    }
}

