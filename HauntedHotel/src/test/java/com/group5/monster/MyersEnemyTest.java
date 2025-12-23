package com.group5.monster;

import com.group5.main.GamePanel;
import com.group5.monster.MyersEnemy;
import com.group5.object.Obj_Money;
import com.group5.entity.Entity;
import com.group5.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
public class MyersEnemyTest {

    GamePanel gp;
    MyersEnemy myers;

    @BeforeEach
    void setup(){
        gp = new GamePanel();
        gp.player = new Player(gp, gp.keyH);
        myers = new MyersEnemy(gp);
    }

    //testing default stats
    @Test
    void testMyersDefaultsStat(){
        assertEquals(2, myers.type);
        assertEquals("Myers", myers.name);
        assertEquals(5,myers.speed);
        assertEquals(5, myers.life);

    }
    //testing images
    @Test
    void testImages(){
        assertNotNull(myers.up1);
        assertNotNull(myers.up2);
        assertNotNull(myers.up3);
        assertNotNull(myers.down1);
        assertNotNull(myers.down2);
        assertNotNull(myers.down3);
        assertNotNull(myers.left1);
        assertNotNull(myers.left2);
        assertNotNull(myers.left3);
        assertNotNull(myers.right1);
        assertNotNull(myers.right2);
        assertNotNull(myers.right3);
        assertNotNull(myers.attackUp1);

        
    }

    //Testing directions when damaged 
    @Test
    void testDamageReactiondDirectionup(){

        gp.player.direction = "up";
        myers.direction = "left";
        
        myers.damageReaction();
        
        assertEquals("down", myers.direction);
        assertEquals(0, myers.actionLockCounter);
    }

    @Test
    void testDamageReactiondDirectiondown(){

        gp.player.direction = "down";
        myers.direction = "right";
        
        myers.damageReaction();
        
        assertEquals("up", myers.direction);
        assertEquals(0, myers.actionLockCounter);
    }

    @Test
    void testDamageReactiondDirectionleft(){

        gp.player.direction = "left";
        
        myers.damageReaction();
        
        assertEquals("right", myers.direction);
    }

    @Test
    void testDamageReactiondDirectionright(){

        gp.player.direction = "right";
        
        myers.damageReaction();
        
        assertEquals("left", myers.direction);
    }



    //pathfinding testing based on player movements

    @Test
    void testSetAction_StartChasingPlayer() {
        gp.player.worldx = 200;
        gp.player.worldy = 200;

        myers.worldx = 100;
        myers.worldy = 100;

        myers.setAction();

        assertTrue(myers.onPath, "myers should be onpath during chase");
    }


    @Test
    void testSetActionPathf(){
        gp.player.worldx = 100;
        gp.player.worldy = 120;
        myers.worldx = 100;
        myers.worldy = 100;

        myers.setAction();
        assertTrue(myers.onPath);

    }

    @Test
    void testSetAction_DirectionChange() {
        gp.player.worldx = 300;
        gp.player.worldy = 100;

        myers.worldx = 100;
        myers.worldy = 100;

        myers.setAction();

        assertEquals("right", myers.direction, "myers should move right toward the player");
    }
    @Test
    void testCheckDrop() {

    int before = 0;
    for (Entity e : gp.obj[gp.currentMap]) {
        if (e != null) before++;
    }

    myers.checkDrop();

    //after drop
    int after = 0;
    for (Entity e : gp.obj[gp.currentMap]) {
        if (e != null) after++;
    }

    // should increase by 1
    assertEquals(before + 1, after, "myers should drop exactly one item");

    Entity dropped = null;
    for (Entity e : gp.obj[gp.currentMap]) {
        if (e != null) {
            if (e instanceof Obj_Money) {
                dropped = e;
                break;
            }
        }
    }

    assertNotNull(dropped, "dropped item must be Obj_Money");
    }
}