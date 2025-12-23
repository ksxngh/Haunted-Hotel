package com.group5.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * The {@code KeyHandler} class handles all keyboard inputs for the game.
 * It implements {@link KeyListener} to process player interactions
 * and state transitions based on key events.
 *
 * <p>The class supports multiple game states such as title, play, pause,
 * dialogue, character, and game over, each with distinct key bindings.</p>
 *
 * <p>Controls include:</p>
 * <ul>
 *     <li>WASD — movement</li>
 *     <li>T — interact/dialogue</li>
 *     <li>E — attack</li>
 *     <li>SPACE — shoot projectile</li>
 *     <li>ESC — pause/resume</li>
 *     <li>C — inventory/character menu</li>
 * </ul>
 *
 * @author Group 5
 * @version 1.0
 */

public class KeyHandler implements KeyListener {

    /** Reference to the main {@link GamePanel}. */
    GamePanel gp;

    /** Boolean flags representing the current pressed state of each key. */
    public  boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, attackPressed, shootKeyPressed;

    /**
     * Constructs a new {@code KeyHandler} for the given game panel.
     *
     * @param gp the {@link GamePanel} instance this handler controls.
     */
    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }
     /** Not used but required by {@link KeyListener}. */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Called when a key is pressed.
     * Handles input differently based on the current game state.
     *
     * @param e the {@link KeyEvent} triggered when a key is pressed.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // TITLE STATE
        if(gp.gameState == gp.titleState){
            titleState(code);
        }
        // playState
        if(gp.gameState == gp.playState) {
            playState(code);
        }
        // pausestate
        else if (gp.gameState == gp.pauseState) {
            pauseState(code);
        }
        // dialogestate
        else if(gp.gameState == gp.dialogueState) {
            dialogueState(code);
        }
        else if(gp.gameState == gp.characterState) {
            characterState(code);
        }
        else if(gp.gameState == gp.gameOverState) {
            gameOverState(code);
        }

    }

    /**
     * Handles input for the title screen, allowing navigation between
     * menu options and starting or exiting the game.
     *
     * @param code the key code pressed by the user.
     */
    public void titleState(int code) {
        if(code == KeyEvent.VK_W){
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0) {
                gp.ui.commandNum = 1;
            }
        }
        if(code == KeyEvent.VK_S){
            gp.ui.commandNum++;
            if(gp.ui.commandNum > 1) {
                gp.ui.commandNum = 0;
            }
        }
        if(code == KeyEvent.VK_ENTER) {
            if(gp.ui.commandNum == 0) {
                gp.gameState = gp.playState;
                // gp.playeMusic();
            }
            if(gp.ui.commandNum == 1) {
                exitGame(); 
            }
        }
    }

    /**
     * Handles gameplay key presses such as movement, attacks,
     * shooting, and opening menus.
     *
     * @param code the key code pressed.
     */
    public void playState(int code) {
        if(code == KeyEvent.VK_W){
            upPressed = true;
        }
        if(code == KeyEvent.VK_S){
            downPressed = true;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = true;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = true;
        }
        if(code == KeyEvent.VK_ESCAPE){
            gp.gameState = gp.pauseState;
        }
        if(code == KeyEvent.VK_T){
            enterPressed = true;
        }
        if(code == KeyEvent.VK_E){
            attackPressed = true;
        }
        if(code == KeyEvent.VK_C){
            gp.gameState = gp.characterState;
        }
        if(code == KeyEvent.VK_SPACE){
            shootKeyPressed = true;
        }
    }

    /**
     * Handles input during pause state to navigate menu or resume play.
     *
     * @param code the key code pressed.
     */
    public void pauseState(int code) {
        if(code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
        }
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if(code == KeyEvent.VK_W){
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0) {
                gp.ui.commandNum = 1;
            }
        }
        if(code == KeyEvent.VK_S){
            gp.ui.commandNum++;
            if(gp.ui.commandNum > 1) {
                gp.ui.commandNum = 0;
            }
        }

    }

    /**
     * Handles player input while in dialogue mode.
     *
     * @param code the key code pressed.
     */
    public void dialogueState(int code) {
        if(code == KeyEvent.VK_T) {
            gp.gameState = gp.playState;
        }
    }

    /**
     * Handles navigation in the character/inventory menu.
     *
     * @param code the key code pressed.
     */
    public void characterState(int code) {
        if(code == KeyEvent.VK_C){
            gp.gameState = gp.playState;
        }
        if(code == KeyEvent.VK_W){
            if(gp.ui.slotRow != 0) {
                gp.ui.slotRow--;
            }

        }
        if(code == KeyEvent.VK_A){
            if(gp.ui.slotCol != 0) {
                gp.ui.slotCol--;
            }
        }
        if(code == KeyEvent.VK_S){
            if(gp.ui.slotRow != 2) {
                gp.ui.slotRow++;
            }
        }
        if(code == KeyEvent.VK_D){
            if(gp.ui.slotCol != 3) {
                gp.ui.slotCol++;
            }
        }
    }

    /**
     * Handles key input during the game over screen.
     *
     * @param code the key code pressed.
     */
    public void gameOverState(int code) {

        if(code == KeyEvent.VK_W){
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0) {
                gp.ui.commandNum = 1;
            }
        }
        if(code == KeyEvent.VK_S){
            gp.ui.commandNum++;
            if(gp.ui.commandNum > 1) {
                gp.ui.commandNum = 0;
            }
        }
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
    }

    /**
     * Handles input for the game win state, allowing
     * the player to confirm victory with Enter.
     *
     * @param code the key code pressed.
     */
    public void gameWinState(int code) {

        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
    }

    /**
     * Called when a key is released. Resets movement and action flags.
     *
     * @param e the {@link KeyEvent} triggered when a key is released.
     */
    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W){
            upPressed = false;
        }
        if(code == KeyEvent.VK_S){
            downPressed = false;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = false;
        }
        if(code == KeyEvent.VK_E){
            attackPressed = false;
        }
        if(code == KeyEvent.VK_SPACE){
            shootKeyPressed = false;
        }
        else if(gp.gameState == gp.gameWinState) {
            gameWinState(code);
        }
    }

    protected void exitGame() {
        System.exit(0);
    }
}