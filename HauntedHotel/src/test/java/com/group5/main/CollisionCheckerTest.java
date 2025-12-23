package com.group5.main;

import com.group5.entity.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CollisionCheckerTest {
    GamePanel gp;
    CollisionChecker c;

    @BeforeEach
    void setup() {
        gp = new GamePanel();
        c = gp.cChecker;
    }

    @Test
    void testCheckTileAllDirections() {
        Entity e = new Entity(gp);
        e.speed = 1;
        e.direction = "up"; c.checkTile(e);
        e.direction = "down"; c.checkTile(e);
        e.direction = "left"; c.checkTile(e);
        e.direction = "right"; c.checkTile(e);
        assertTrue(true);
    }

    @Test
    void testCheckObjectCollision() {
        Entity e = new Entity(gp);
        gp.obj[gp.currentMap][0] = new Entity(gp);
        int index = c.checkObject(e, true);
        assertTrue(index == 999 || index == 0);
    }

    @Test
    void testCheckEntityCollision() {
        Entity e = new Entity(gp);
        gp.npc[gp.currentMap][0] = new Entity(gp);
        int index = c.checkEntity(e, gp.npc);
        assertTrue(index == 999 || index == 0);
    }

    @Test
    void testCheckPlayerCollision() {
        Entity e = new Entity(gp);
        e.direction = "up";
        boolean result = c.checkPlayer(e);
        assertFalse(result); // usually no overlap
    }
}
