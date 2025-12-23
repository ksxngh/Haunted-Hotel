package com.group5.entity;

import com.group5.main.GamePanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NPCTest {

    GamePanel gp;
    NPC npc;

    @BeforeEach
    void setup() {
        gp = new GamePanel();
        npc = new NPC(gp);
    }

    @Test
    void testConstructorSetsDirectionAndDialogue() {
        assertEquals("down", npc.direction);
        assertNotNull(npc.down1);
        assertTrue(npc.dialogues[0].contains("money"));
    }

    @Test
    void testGetNPCImageLoadsBoth() {
        npc.getNPCImage();
        assertNotNull(npc.down2);
    }

    @Test
    void testSpeakCyclesDialogue() {
        npc.dialogeIndex = 0;
        npc.speak();
        assertNotNull(gp.ui.currentDialogue);
        assertEquals(npc.dialogues[0], gp.ui.currentDialogue);
    }

    @Test
    void testSpeakResetsWhenDialogueNull() {
        npc.dialogues[0] = null;
        npc.dialogeIndex = 0;

        npc.speak();

        assertNull(gp.ui.currentDialogue);  // sets null safely
        assertEquals(1, npc.dialogeIndex);  // increments after speak
    }

    @Test
    void testSetActionCoversAllRandomBranches() {

        boolean up = false, down = false, left = false, right = false;

        // Attempt many cycles to guarantee all random cases occur
        for (int i = 0; i < 8000; i++) {

            // MUST be 179 → inside setAction() it increments to 180
            npc.actionLockCounter = 179;

            npc.setAction();

            switch (npc.direction) {
                case "up":    up = true; break;
                case "down":  down = true; break;
                case "left":  left = true; break;
                case "right": right = true; break;
            }

            // Break early if all covered
            if (up && down && left && right) break;
        }

        assertTrue(up, "Random direction 'up' was never reached");
        assertTrue(down, "Random direction 'down' was never reached");
        assertTrue(left, "Random direction 'left' was never reached");
        assertTrue(right, "Random direction 'right' was never reached");
    }

    @Test
    void testActionLockCounterResets() {
        // Simulate the actionLockCounter before 180 (it must be at 179)
        npc.actionLockCounter = 179;  // Just before it will increment in setAction()

        // Call setAction and ensure counter resets
        npc.setAction();

        // We expect actionLockCounter to reset to 0 after calling setAction() once
        assertEquals(0, npc.actionLockCounter);
    }
}
