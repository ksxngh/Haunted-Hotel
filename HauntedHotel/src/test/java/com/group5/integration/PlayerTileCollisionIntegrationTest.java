package com.group5.integration;

import com.group5.main.GamePanel;
import com.group5.monster.Enemy_Santa;
import com.group5.monster.Mon_Boss;
import com.group5.monster.MyersEnemy;
import com.group5.object.Obj_Fireball;
import com.group5.object.Obj_Snowball;
import com.group5.entity.Player;
import com.group5.tile.TileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTileCollisionIntegrationTest {

    GamePanel gp;

    @BeforeEach
    void setup() {
        gp = new GamePanel();
        gp.setupGame();

    }

//collission check
    @Test
    void testPlayerBlockedByCollidingTile() {

        // put player near a colliding tile
        gp.player.worldx = gp.tileSize * 5; 
        gp.player.worldy = gp.tileSize * 5;

        // force the tile above player to be a collision tile
        gp.tileM.tile[18].collision = true;  
        gp.tileM.mapTilenum[0][5][4] = 18;   

        gp.player.direction = "up";

        int beforeX = gp.player.worldx;
        int beforeY = gp.player.worldy;

        gp.player.update();

        assertEquals(beforeY, gp.player.worldy);
        assertEquals(beforeX, gp.player.worldx);
    }


//test for moving into free tiles no collide
    @Test
    void testPlayerMovesIntoFreeTile() {

    gp.player.worldx = gp.tileSize * 5;
    gp.player.worldy = gp.tileSize * 5;

    // make tile above non colliding
    gp.tileM.tile[17].collision = false; // floor tile
    gp.tileM.mapTilenum[0][5][4] = 17;

    gp.player.direction = "up";
    gp.keyH.upPressed = true;

    int beforeY = gp.player.worldy;

    gp.player.update();

    // player should move up by speed
    assertTrue(gp.player.worldy < beforeY);
    }

//test for leaving the world
    @Test
    void testPlayerCannotLeaveWorld() {

    gp.player.worldx = gp.tileSize * 5;
    gp.player.worldy = 0;

    gp.player.direction = "up";

    int beforeY = gp.player.worldy;

    gp.player.update();

    assertEquals(beforeY, gp.player.worldy);
    }


    @Test
    public void testMyersUpdate() {
    GamePanel gp = new GamePanel();
    gp.setupGame();

    // Add a Myers enemy in map 0
    MyersEnemy m = new MyersEnemy(gp);
    gp.monster[0][0] = m;

    m.worldx = gp.tileSize * 10;
    m.worldy = gp.tileSize * 10;

    int oldX = m.worldx;
    int oldY = m.worldy;

    gp.gameState = gp.playState;

    gp.update();

    boolean moved = m.worldx != oldX || m.worldy != oldY;

    assertTrue(moved, "Myers should move when GamePanel.update() calls myers.update().");
    }

    @Test
    public void testSantaFiresSnowball() {
    Enemy_Santa santa = new Enemy_Santa(gp);

    santa.worldx = 100;
    santa.worldy = 100;
    gp.player.worldx = 105;
    gp.player.worldy = 100;

    santa.projectile.alive = false;
    santa.shotAvailableCounter = 30;
    gp.projectileList.clear();

    santa.setAction();

    assertFalse(gp.projectileList.isEmpty(), "Santa should fire a projectile.");
    assertTrue(gp.projectileList.get(0) instanceof Obj_Snowball,
            "Santa should fire a snowball, not fireball.");
    }

    @Test
    public void testBossFiresFireball() {
    Mon_Boss boss = new Mon_Boss(gp);

    boss.worldx = 200;
    boss.worldy = 200;
    gp.player.worldx = 205;
    gp.player.worldy = 200;

    boss.projectile.alive = false;
    boss.shotAvailableCounter = 30;
    gp.projectileList.clear();

    boss.setAction();

    assertFalse(gp.projectileList.isEmpty(), "Boss should fire a projectile.");
    assertTrue(gp.projectileList.get(0) instanceof Obj_Fireball,
            "Boss should fire a fireball.");
}


}
