package com.group5.main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class UITest {

    GamePanel gp;
    UI ui;
    Graphics2D g2;

    @BeforeEach
    void setup() {
        gp = new GamePanel();
        gp.setupGame();
        ui = gp.ui;

        // Fake graphics for rendering test
        BufferedImage img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        g2 = img.createGraphics();
    }

   
    // basic logic test

    @Test
    public void testShowMessage() {
        ui.showMessage("Hello");

        assertTrue(ui.messageOn, "message flag should turn on.");
        assertEquals("Hello", ui.message, "message should be stored correctly.");
    }

    @Test
    public void testGetTextCentered() {
        ui.g2 = g2;

        int x = ui.getTextCentered("centered Text");

        assertTrue(x >= 0, "centered text must be positioned on screen.");
        assertTrue(x <= gp.screenWidth, "centered text must be within screen width.");
    }

    // draw subwindow

    @Test
    public void testDrawSubWindow() {
        ui.g2 = g2;
        assertDoesNotThrow(() -> ui.drawSubWindow(50, 50, 200, 100));
    }

    
    // player life drawing
    @Test
    public void testDrawPlayerLife() {
        ui.g2 = g2;
        assertDoesNotThrow(() -> ui.drawPlayerLife());
    }

    // main draw method tests

    @Test
    public void testDraw_TitleScreen() {
        gp.gameState = gp.titleState;
        assertDoesNotThrow(() -> ui.draw(g2));
    }

    @Test
    public void testDraw_PlayState() {
        gp.gameState = gp.playState;
        assertDoesNotThrow(() -> ui.draw(g2));
    }

    @Test
    public void testDraw_PauseState() {
        gp.gameState = gp.pauseState;
        assertDoesNotThrow(() -> ui.draw(g2));
    }

    @Test
    public void testDraw_DialogueState() {
        gp.gameState = gp.dialogueState;
        ui.currentDialogue = "Hello\nWorld";
        assertDoesNotThrow(() -> ui.draw(g2));
    }

    @Test
    public void testDraw_InventoryState() {
        gp.gameState = gp.characterState;
        assertDoesNotThrow(() -> ui.draw(g2));
    }

    @Test
    public void testDraw_GameOverState() {
        gp.gameState = gp.gameOverState;
        assertDoesNotThrow(() -> ui.draw(g2));
    }

    @Test
    public void testDraw_GameWinState() {
        gp.gameState = gp.gameWinState;
        assertDoesNotThrow(() -> ui.draw(g2));
    }

    // dialouge window tests

    @Test
    public void testDrawDialogueScreen() {
        ui.g2 = g2;

        ui.currentDialogue = "Line1\nLine2\nLine3";
        assertDoesNotThrow(() -> ui.drawDialogueScreen());
    }

   
    //option control tests

    @Test
    public void testOptionsControl() {
        ui.g2 = g2;

        // Just call it normally to hit most lines
        assertDoesNotThrow(() -> ui.options_control(50, 50));
    }

    @Test
    public void testOptionsControl_CommandNumZero() {
        ui.g2 = g2;

        ui.commandNum = 0;     
        gp.keyH.enterPressed = false;

        assertDoesNotThrow(() -> ui.options_control(50, 50));
    }

    @Test
    public void testOptionsControl_EnterPressed() {
        ui.g2 = g2;

        ui.commandNum = 0;
        gp.keyH.enterPressed = true;

        ui.subState = 5;        

        ui.options_control(50, 50);

        assertEquals(0, ui.subState, 
                "substate should reset to 0 when enterPressed is true");
    }

}
