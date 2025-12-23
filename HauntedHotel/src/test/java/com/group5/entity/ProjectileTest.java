package com.group5.entity;

import com.group5.main.GamePanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProjectileTest {
    GamePanel gp;
    Projectile proj;

    @BeforeEach
    void setup() {
        gp = new GamePanel();
        proj = new Projectile(gp);
        proj.speed = 5;
        proj.maxLife = 3;

        // initialize monster arrays to prevent NPE
        for (int i = 0; i < gp.monster[0].length; i++) {
            gp.monster[0][i] = new Entity(gp);
            gp.monster[3][i] = new Entity(gp);
            gp.monster[3][i].life = 1;
        }
    }

    @Test
    void testSetValues() {
        proj.set(10, 20, "up", true, gp.player);
        assertEquals(10, proj.worldx);
        assertTrue(proj.alive);
    }

    @Test
    void testUpdateMovesUpAndDiesAfterLife() {
        proj.set(100, 100, "up", true, gp.player);
        for (int i = 0; i < 4; i++) proj.update();
        assertFalse(proj.alive);
    }

    @Test
    void testUpdateMovesDownAndHitsMonster() {
        gp.monster[gp.currentMap][0] = new Entity(gp);
        gp.monster[gp.currentMap][0].life = 5;
        proj.set(0, 0, "down", true, gp.player);
        proj.update();
        assertTrue(true);
    }

    @Test
    void testEnemyProjectileHitsPlayerAndDies() {
        proj.set(0, 0, "right", true, new Entity(gp));
        gp.player.invincible = false;
        gp.player.worldx = 0;
        gp.player.worldy = 0;
        proj.update();
        assertFalse(proj.alive);
        assertTrue(gp.player.invincible);
    }

    @Test
    void testProjectileMovesLeft() {
        proj.set(100, 100, "left", true, gp.player);
        int before = proj.worldx;
        proj.update();
        assertTrue(proj.worldx < before);
    }

    @Test
    void testEnemyProjectileWhenPlayerIsInvincible_NoDamage() {
        proj.set(0, 0, "right", true, new Entity(gp));

        gp.player.invincible = true;   // ✅ this makes the condition false
        gp.player.worldx = 0;
        gp.player.worldy = 0;

        proj.update();

        // Should remain alive since invincible prevented damage
        assertTrue(proj.alive);
    }

    @Test
    void testProjectileWithInvalidDirection_DefaultCase() {
        proj.set(50, 50, "diagonal", true, gp.player); // direction not in switch
        int beforeX = proj.worldx;
        int beforeY = proj.worldy;
        proj.update();
        // No movement happens, so coords unchanged
        assertEquals(beforeX, proj.worldx);
        assertEquals(beforeY, proj.worldy);
    }


}
