package com.group5.integration;

import com.group5.main.GamePanel;
import com.group5.main.TestGamePanel;
import com.group5.object.Obj_Chest_Key;
import com.group5.object.Obj_Myers_Key;
import com.group5.object.Obj_Gift;
import com.group5.entity.Entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GamePanelObjectInitializationTest {

    TestGamePanel gp;

    @BeforeEach
    void setup() {
        gp = new TestGamePanel();
    }

    @Test
    void testSetupGame_initializesObjectsNPCsAndMonsters() {
        gp.setupGame(); // <- Integration entry point

        // MAP 0 : Chest Key
        Entity chestKey = gp.obj[0][0];
        assertNotNull(chestKey, "Map 0 should have a chest key at index 0");
        assertEquals(Obj_Chest_Key.class, chestKey.getClass());
        assertEquals(16 * gp.tileSize, chestKey.worldx);
        assertEquals(12 * gp.tileSize, chestKey.worldy);

        // MAP 1 : Myers Key
        Entity myersKey = gp.obj[1][0];
        assertNotNull(myersKey, "Map 1 should have a Myers key at index 0");
        assertEquals(Obj_Myers_Key.class, myersKey.getClass());

        // MAP 2 : Christmas Key + Gifts
        Entity christmasKey = gp.obj[2][0];
        assertNotNull(christmasKey, "Map 2 should have a random Christmas Myers key");
        assertEquals(Obj_Myers_Key.class, christmasKey.getClass());

        // Gift objects should exist on map 2 at indexes 1–7
        for (int i = 1; i <= 7; i++) {
            Entity gift = gp.obj[2][i];
            assertNotNull(gift, "Gift object at index " + i + " should exist on map 2");
            assertEquals(Obj_Gift.class, gift.getClass(), "Object at index " + i + " must be a gift");
        }

        // NPC on Map 0
        assertNotNull(gp.npc[0][0], "NPC should be spawned on map 0");

        // Monsters on maps 1, 2, 3
        assertNotNull(gp.monster[1][0], "MyersEnemy should spawn on map 1");
        assertNotNull(gp.monster[2][0], "Enemy_Santa should spawn on map 2");
        assertNotNull(gp.monster[3][0], "Mon_Boss should spawn on map 3");
    }
}
