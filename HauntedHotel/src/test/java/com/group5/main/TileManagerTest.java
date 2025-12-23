package com.group5.main;

import com.group5.main.GamePanel;
import com.group5.tile.TileManager;
import com.group5.tile.Tile;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TileManagerTest {

    GamePanel gp;
    TileManager tileM;

    @BeforeEach
    void setup(){
        gp = new GamePanel();
        tileM = new TileManager(gp);
    }



    //test for loading files 
    @Test
    void testTIlesloaded(){
        assertNotNull(tileM.tile[0].image);
        assertNotNull(tileM.tile[1].image);
        assertNotNull(tileM.tile[2].image);
        assertNotNull(tileM.tile[3].image);
        assertNotNull(tileM.tile[4].image);
        assertNotNull(tileM.tile[5].image);
        assertNotNull(tileM.tile[6].image);
        assertNotNull(tileM.tile[7].image);
        assertNotNull(tileM.tile[8].image);
        assertNotNull(tileM.tile[9].image);
        assertNotNull(tileM.tile[10].image);
        assertNotNull(tileM.tile[11].image);
        assertNotNull(tileM.tile[12].image);
        assertNotNull(tileM.tile[13].image);
        assertNotNull(tileM.tile[14].image);
        assertNotNull(tileM.tile[15].image);
        assertNotNull(tileM.tile[16].image);
        assertNotNull(tileM.tile[17].image);
        assertNotNull(tileM.tile[18].image);
        assertNotNull(tileM.tile[19].image);
        assertNotNull(tileM.tile[20].image);
        assertNotNull(tileM.tile[21].image);
        assertNotNull(tileM.tile[22].image);
        assertNotNull(tileM.tile[23].image);
        assertNotNull(tileM.tile[24].image);
        assertNotNull(tileM.tile[25].image);
        assertNotNull(tileM.tile[26].image);
        assertNotNull(tileM.tile[27].image);
        assertNotNull(tileM.tile[28].image);
        assertNotNull(tileM.tile[29].image);
        assertNotNull(tileM.tile[30].image);
        assertNotNull(tileM.tile[31].image);



    }

    //test for collisions
    @Test
    void testTileCollision(){
        assertTrue(tileM.tile[0].collision);
        assertTrue(tileM.tile[1].collision);
        assertFalse(tileM.tile[2].collision);
        assertTrue(tileM.tile[3].collision);
        assertTrue(tileM.tile[4].collision);
        assertFalse(tileM.tile[5].collision);
        assertFalse(tileM.tile[6].collision);
        assertFalse(tileM.tile[7].collision);
        assertFalse(tileM.tile[8].collision);
        assertFalse(tileM.tile[9].collision);
        assertTrue(tileM.tile[10].collision);
        assertFalse(tileM.tile[11].collision);
        assertFalse(tileM.tile[12].collision);
        assertFalse(tileM.tile[13].collision);
        assertFalse(tileM.tile[14].collision);
        assertTrue(tileM.tile[15].collision);
        assertTrue(tileM.tile[16].collision);
        assertFalse(tileM.tile[17].collision);
        assertTrue(tileM.tile[18].collision);
        assertTrue(tileM.tile[19].collision);
        assertFalse(tileM.tile[20].collision);
        assertTrue(tileM.tile[21].collision);
        assertFalse(tileM.tile[22].collision);
        assertFalse(tileM.tile[23].collision);
        assertFalse(tileM.tile[24].collision);
        assertFalse(tileM.tile[25].collision);
        assertFalse(tileM.tile[26].collision);
        assertTrue(tileM.tile[27].collision);
        assertTrue(tileM.tile[28].collision);
        assertFalse(tileM.tile[29].collision);
        assertTrue(tileM.tile[30].collision);
        assertFalse(tileM.tile[31].collision);
    }
    //test for loading map with specific tile
    @Test
    void testMapload(){
        int tileNum = tileM.mapTilenum[1][0][0];
        assertEquals(18, tileNum);
    }

    @Test
    void testDraw_NoCrash() {
    BufferedImage img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = img.createGraphics();

    try {
        tileM.draw(g2);
    } catch (Exception ex) {
        fail("TileManager.draw() threw an exception: " + ex.getMessage());
    }


    }


}
