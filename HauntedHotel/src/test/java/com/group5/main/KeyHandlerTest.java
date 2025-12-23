package com.group5.main;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Canvas;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

/**
 * KeyHandlerTest for unit testing KeyHandler class.
 * Uses TestGamePanel to avoid real GUI rendering.
 */
class KeyHandlerTest {

    TestGamePanel gp;
    KeyHandler keyH;

    // Flag to simulate System.exit in tests
    static boolean exitCalled = false;

    @BeforeAll
    static void setupHeadless() {
        System.setProperty("java.awt.headless", "true");
    }

    @BeforeEach
    void setUp() {
       // exitCalled = false;
        gp = new TestGamePanel();
        keyH = new KeyHandler(gp) {
            @Override
            protected void exitGame() {
                // do nothing, safely bypass System.exit in tests
            }
        };
        gp.keyH = keyH;
    }

    // -------------------
    // Helpers
    // -------------------
    private KeyEvent press(int code, char c) {
        return new KeyEvent(new Canvas(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, code, c);
    }

    private KeyEvent release(int code, char c) {
        return new KeyEvent(new Canvas(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, code, c);
    }

    // -------------------
    // TITLE STATE
    // -------------------
    @Test
    void testTitleNavigationWraps() {
        gp.gameState = gp.titleState;
        gp.ui.commandNum = 0;

        keyH.keyPressed(press(KeyEvent.VK_W, 'W'));
        assertEquals(1, gp.ui.commandNum);

        keyH.keyPressed(press(KeyEvent.VK_S, 'S'));
        assertEquals(0, gp.ui.commandNum);
    }

    @Test
    void testTitleEnterStartsGame() {
        gp.gameState = gp.titleState;
        gp.ui.commandNum = 0;

        keyH.keyPressed(press(KeyEvent.VK_ENTER, '\n'));
        assertEquals(gp.playState, gp.gameState);
    }

    @Test
    void testTitleEnterExit() {
        gp.gameState = gp.titleState;
        gp.ui.commandNum = 1;

        // Create a KeyHandler subclass with a field we can check
        class TestKeyHandler extends KeyHandler {
            boolean exitCalled = false;

            TestKeyHandler(GamePanel gp) {
                super(gp);
            }

            @Override
            protected void exitGame() {
                exitCalled = true; // mark that exit was "called"
            }
        }

        TestKeyHandler keyHWithFlag = new TestKeyHandler(gp);
        gp.keyH = keyHWithFlag;

        keyHWithFlag.keyPressed(press(KeyEvent.VK_ENTER, '\n'));

        // Assert that exitGame() was called
        assertTrue(keyHWithFlag.exitCalled);
    }


    // -------------------
    // PLAY STATE
    // -------------------
    @Test
    void testPlayMovementAndRelease() {
        gp.gameState = gp.playState;

        keyH.keyPressed(press(KeyEvent.VK_W, 'W'));
        keyH.keyPressed(press(KeyEvent.VK_S, 'S'));
        keyH.keyPressed(press(KeyEvent.VK_A, 'A'));
        keyH.keyPressed(press(KeyEvent.VK_D, 'D'));

        assertTrue(keyH.upPressed);
        assertTrue(keyH.downPressed);
        assertTrue(keyH.leftPressed);
        assertTrue(keyH.rightPressed);

        keyH.keyReleased(release(KeyEvent.VK_W, 'W'));
        keyH.keyReleased(release(KeyEvent.VK_S, 'S'));
        keyH.keyReleased(release(KeyEvent.VK_A, 'A'));
        keyH.keyReleased(release(KeyEvent.VK_D, 'D'));

        assertFalse(keyH.upPressed);
        assertFalse(keyH.downPressed);
        assertFalse(keyH.leftPressed);
        assertFalse(keyH.rightPressed);
    }

    @Test
    void testPlayAttackShootFlags() {
        gp.gameState = gp.playState;

        keyH.keyPressed(press(KeyEvent.VK_E, 'E'));
        keyH.keyPressed(press(KeyEvent.VK_SPACE, ' '));
        assertTrue(keyH.attackPressed);
        assertTrue(keyH.shootKeyPressed);

        keyH.keyReleased(release(KeyEvent.VK_E, 'E'));
        keyH.keyReleased(release(KeyEvent.VK_SPACE, ' '));
        assertFalse(keyH.attackPressed);
        assertFalse(keyH.shootKeyPressed);
    }

    @Test
    void testPlayMenuAndCharacter() {
        gp.gameState = gp.playState;

        keyH.keyPressed(press(KeyEvent.VK_ESCAPE, (char)27));
        assertEquals(gp.pauseState, gp.gameState);

        // Return to playState for the C test
        gp.gameState = gp.playState;
        
        keyH.keyPressed(press(KeyEvent.VK_C, 'C'));
        assertEquals(gp.characterState, gp.gameState);
    }

    // -------------------
    // PAUSE STATE
    // -------------------
    @Test
    void testPauseEscResumes() {
        gp.gameState = gp.pauseState;
        keyH.keyPressed(press(KeyEvent.VK_ESCAPE, (char)27));
        assertEquals(gp.playState, gp.gameState);
    }

    @Test
    void testPauseMenuNavigationAndEnter() {
        gp.gameState = gp.pauseState;
        gp.ui.commandNum = 0;

        keyH.keyPressed(press(KeyEvent.VK_W, 'W'));
        assertEquals(1, gp.ui.commandNum);

        keyH.keyPressed(press(KeyEvent.VK_S, 'S'));
        assertEquals(0, gp.ui.commandNum);

        keyH.keyPressed(press(KeyEvent.VK_ENTER, '\n'));
        assertTrue(keyH.enterPressed);
    }

    // -------------------
    // DIALOGUE STATE
    // -------------------
    @Test
    void testDialogueStateTKey() {
        gp.gameState = gp.dialogueState;
        keyH.keyPressed(press(KeyEvent.VK_T, 'T'));
        assertEquals(gp.playState, gp.gameState);
    }

    // -------------------
    // CHARACTER STATE
    // -------------------
    @Test
    void testCharacterNavigationBounds() {
        gp.gameState = gp.characterState;

        gp.ui.slotRow = 1;
        gp.ui.slotCol = 1;

        keyH.keyPressed(press(KeyEvent.VK_W, 'W'));
        keyH.keyPressed(press(KeyEvent.VK_A, 'A'));
        keyH.keyPressed(press(KeyEvent.VK_S, 'S'));
        keyH.keyPressed(press(KeyEvent.VK_D, 'D'));

        assertEquals(1, gp.ui.slotRow);
        assertEquals(1, gp.ui.slotCol);

        // move to bounds
        gp.ui.slotRow = 0;
        gp.ui.slotCol = 0;
        keyH.keyPressed(press(KeyEvent.VK_W, 'W'));
        keyH.keyPressed(press(KeyEvent.VK_A, 'A'));
        assertEquals(0, gp.ui.slotRow);
        assertEquals(0, gp.ui.slotCol);
    }

    @Test
    void testCharacterCClosesMenu() {
        gp.gameState = gp.characterState;
        keyH.keyPressed(press(KeyEvent.VK_C, 'C'));
        assertEquals(gp.playState, gp.gameState);
    }

    // -------------------
    // GAME OVER STATE
    // -------------------
    @Test
    void testGameOverNavigationAndEnter() {
        gp.gameState = gp.gameOverState;
        gp.ui.commandNum = 0;

        keyH.keyPressed(press(KeyEvent.VK_S, 'S'));
        keyH.keyPressed(press(KeyEvent.VK_W, 'W'));
        assertEquals(0, gp.ui.commandNum);

        keyH.keyPressed(press(KeyEvent.VK_ENTER, '\n'));
        assertTrue(keyH.enterPressed);
    }

    // -------------------
    // GAME WIN STATE
    // -------------------
    @Test
    void testGameWinStateEnter() {
        gp.gameState = gp.gameWinState;

        keyH.keyReleased(release(KeyEvent.VK_ENTER, '\n'));
        assertTrue(keyH.enterPressed);
    }
}
