package com.group5.main;
import com.group5.entity.Player;
import com.group5.main.EventHandler;
import com.group5.main.GamePanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



public class EventHandlerTest {


    GamePanel gp;
    EventHandler e;

    @BeforeEach
    void setup(){
        gp = new GamePanel();
        e = gp.eHandler;
        gp.setupGame();
    }

    //testing for wrong map
    @Test
    public void hitWrongMap(){
        gp.currentMap = 1;
        boolean result = e.isEventTriggered(0,1,1);
        assertFalse(result);
    }
    
    @Test
    public void checkhit(){
        gp.currentMap = 0;
        gp.player.worldx = gp.tileSize *1;
        gp.player.worldy = gp.tileSize *1;

        boolean result = e.isEventTriggered(0,1,1);
        assertTrue(result, "EventHandler.isEventTriggered() should detect collision but didn't");


    }

    // simple bomb punishment test

    @Test
    public void testBombPunishment(){
        int oldLife = gp.player.life;
        e.bombPunishment(gp.dialogueState);
        assertEquals(oldLife - 1, gp.player.life);
    }




    // simple testing fire punihsment
    @Test


    public void testFirePunishment() {
    int oldLife = gp.player.life;

    e.firePunishment(gp.dialogueState);

    assertEquals(oldLife - 1, gp.player.life, "Player should lose 1 life.");
    assertEquals(gp.dialogueState, gp.gameState);
    assertEquals("You burned yourself", gp.ui.currentDialogue);
}

//event checker simulating to trigger an event 

    @Test
    public void testCanTouchEventReset() {
    e.previousEventX = gp.player.worldx;
    e.previousEventY = gp.player.worldy;
    e.canTouchEvent = false;

    gp.player.worldx += gp.tileSize * 2;

    e.checkEvent();

    assertTrue(e.canTouchEvent, "EventHandler should re-enable events after player moves away.");
}


    // CHECKING EVENTS for bomb,fire punishment and teleporting for all maps and no key

    @Test
    public void testCheckEventFire(){
        gp.currentMap = 3;

        int[][] fireTiles = {
            {25, 19},
            {44,  3},
            {65,  2},
            {55,  7},
            {39, 10},
            {65, 13},
            {65, 26},
            {44, 15},
            {43, 27},
            {33, 21},
            {29, 27},
            {55, 20},
            {25,  5}
        };
        for(int[] tile : fireTiles){
            int col = tile[0];
            int row = tile[1];

        gp.player.worldx = gp.tileSize * col;
        gp.player.worldy = gp.tileSize * row;

        int oldLife = gp.player.life;

        e.checkEvent();
        assertEquals(oldLife - 1, gp.player.life,
                "Fire tile (" + col + "," + row + ") should reduce life");
        assertEquals("You burned yourself", gp.ui.currentDialogue,
                "Fire tile (" + col + "," + row + ") should set fire dialogue");;

        }

    }

    @Test
    public void testAllBombEventsTrigger() {
    gp.currentMap = 2;

    int[][] bombTiles = {
            {17, 13},
            {22, 17},
            {31, 15},
            {34, 18},
            {37,  9}
    };

    for (int[] tile : bombTiles) {
        int col = tile[0];
        int row = tile[1];

        gp.player.worldx = gp.tileSize * col;
        gp.player.worldy = gp.tileSize * row;

        int lifeBefore = gp.player.life;

        e.checkEvent();

        assertEquals(lifeBefore - 1, gp.player.life,
                "Bomb tile (" + col + "," + row + ") should reduce life");
        assertEquals("You got a bomb for christmas", gp.ui.currentDialogue,
                "Bomb tile (" + col + "," + row + ") should set bomb dialogue");
    }
}


    @Test
    public void testCheckEventNokey(){
        gp.currentMap = 0;
        gp.player.hasKey = 0;
        gp.player.worldx = gp.tileSize * 1;
        gp.player.worldy = gp.tileSize * 7;

        e.checkEvent();

        assertEquals(0, gp.currentMap);
        assertEquals(gp.tileSize * 1, gp.player.worldx);
        assertEquals(gp.tileSize * 7, gp.player.worldy);


    }

    //Teleport from 0 to 1
    @Test
    public void testTeleport0to_1() {
    gp.currentMap = 0;
    gp.player.hasKey = 1;

    gp.player.worldx = gp.tileSize * 1;
    gp.player.worldy = gp.tileSize * 7;

    e.checkEvent();

    assertEquals(1, gp.currentMap);
    assertEquals(40 * gp.tileSize, gp.player.worldx);
    assertEquals(7 * gp.tileSize, gp.player.worldy);
}

//teleport from 1 to 0
    @Test
    public void testTeleport1to_0() {
    gp.currentMap = 1;
    gp.player.hasKey = 2;

    gp.player.worldx = gp.tileSize * 40;
    gp.player.worldy = gp.tileSize * 7;

    e.checkEvent();

    assertEquals(0, gp.currentMap);
    assertEquals(1 * gp.tileSize, gp.player.worldx);
    assertEquals(7 * gp.tileSize, gp.player.worldy);
}
//teleport from 0 to 2
    @Test
    public void testTeleport_0_to_2() {
    gp.currentMap = 0;
    gp.player.hasKey = 1;

    gp.player.worldx = gp.tileSize * 18;
    gp.player.worldy = gp.tileSize * 7;

    e.checkEvent();

    assertEquals(2, gp.currentMap);
    assertEquals(16 * gp.tileSize, gp.player.worldx);
    assertEquals(10 * gp.tileSize, gp.player.worldy);
}

//teleport from 2 to 0

    @Test
    public void testTeleport_2_to_0() {
    gp.currentMap = 2;
    gp.player.hasKey = 2;

    gp.player.worldx = gp.tileSize * 16;
    gp.player.worldy = gp.tileSize * 10;

    e.checkEvent();

    assertEquals(0, gp.currentMap);
    assertEquals(18 * gp.tileSize, gp.player.worldx);
    assertEquals(7 * gp.tileSize, gp.player.worldy);
}

//teleport from 0 to 3
    @Test
    public void testTeleport_0_to_3_tileA() {
    gp.currentMap = 0;
    gp.player.hasKey = 3;

    gp.player.worldx = gp.tileSize * 10;
    gp.player.worldy = gp.tileSize * 1;

    e.checkEvent();

    assertEquals(3, gp.currentMap);
    assertEquals(1 * gp.tileSize, gp.player.worldx);
    assertEquals(15 * gp.tileSize, gp.player.worldy);
}






}
