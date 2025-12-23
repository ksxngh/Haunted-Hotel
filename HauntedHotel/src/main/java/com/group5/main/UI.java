package com.group5.main;

import com.group5.entity.Entity;
import com.group5.object.Obj_Chest_Key;
import com.group5.object.Obj_Heart;
import com.group5.object.Obj_Money;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The {@code UI} class is responsible for rendering and managing all on-screen
 * user interface (UI) elements such as health, inventory, dialogue boxes,
 * game states (pause, title, win, and game over), and player statistics.
 * <p>
 * It interacts closely with {@link GamePanel}, reading data from the player
 * and other game entities to display visuals that represent game state and
 * player progress.
 * </p>
 * 
 * <p>Responsibilities include:</p>
 * <ul>
 *   <li>Drawing player life using heart icons</li>
 *   <li>Displaying inventory and collected items</li>
 *   <li>Showing game state screens (Title, Pause, Win, Game Over)</li>
 *   <li>Rendering dialogue boxes and in-game messages</li>
 * </ul>
 *
 * @author Group 5
 * @version 1.0
 */
public class UI {

      /** Reference to the main {@link GamePanel}. */
    GamePanel gp;

    /** The graphics context used for rendering UI elements. */
    Graphics2D g2;

    /** Fonts used throughout the UI. */
    Font arial_40, arial_45, end, dialogueFont;

    /** Heart icons representing player life. */
    BufferedImage heart_full, heart_half, heart_blank;

    /** Icon representing a key item. */
    BufferedImage keyImage;

    /** Boolean flag indicating if a temporary message should be displayed. */
    public boolean messageOn = false;

    /** Message text to display temporarily. */
    public String message = "";

    /** Counter controlling message display duration. */
    int messageCount = 0;

    /** Flag for tracking if the game is over. */
    public boolean gameOver = false;

    /** Current dialogue text displayed to the player. */
    public String currentDialogue = "";

    /** Index for navigating menus and selections. */
    public int commandNum = 0;

    /** Current column selection for the inventory grid. */
    public int slotCol = 0;

    /** Current row selection for the inventory grid. */
    public int slotRow = 0;

    /** Substate for managing different option screens. */
    int subState = 0;

    /** Icon representing money or currency. */
    BufferedImage moneyImage;

    /**
     * Constructs the UI and initializes fonts, item icons, and life images.
     *
     * @param gp reference to the main {@link GamePanel}.
     */
    public UI(GamePanel gp) {
        this.gp = gp;

        // FONTS
        arial_40 = new Font("Ariel", Font.PLAIN, 40);
        arial_45 = new Font("Ariel", Font.PLAIN, 42);
        end = new Font("Ariel", Font.BOLD, 55);
        dialogueFont = new Font("Ariel", Font.PLAIN, 30);

        // KEY IMAGE
        Obj_Chest_Key key = new Obj_Chest_Key(gp);
        keyImage = key.down1;
        Obj_Money money = new Obj_Money(gp);
        moneyImage = money.down1;



        // CREATE HEART OBJECT
        Entity heart = new Obj_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;


    }

     /**
     * Displays a message temporarily on the screen.
     *
     * @param text the message to display.
     */
    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    /**
     * Main method responsible for drawing all UI components based on the current game state.
     *
     * @param g2 the graphics context used for rendering.
     */
    public void draw(Graphics2D g2) {

        this.g2 = g2;

        g2.setFont(end);
        g2.setColor(Color.white);
        drawPlayerLife();

        if(gp.gameState == gp.titleState) {
            drawTitleScreen();
        }
        if(gp.gameState == gp.playState){

            drawPlayerLife();

            // FOR THE KEY STUFF
            if(gp.currentMap == 2){
                g2.setFont(arial_45);
                g2.setColor(Color.black);
                g2.drawString("x " + gp.player.hasKey, 111, 190);
                g2.drawString("x " + gp.player.hasKey, 115, 191);

                g2.drawString("x " + gp.player.hasMoney, 123, 249);
                g2.drawString("x " + gp.player.hasMoney, 125, 251);

                if(messageOn == true) {
                    g2.setFont(g2.getFont().deriveFont(32F));
                    g2.drawString(message, gp.tileSize/2, gp.tileSize*5);
                    messageCount++;

                    if(messageCount > 120){
                        messageCount = 0;
                        messageOn = false;
                    }
                }
            }
            
            g2.setFont(arial_40);
            g2.setColor(Color.white);// font name, font style, font size
            g2.drawImage(keyImage, 35, 130, gp.tileSize, gp.tileSize, null);
            g2.drawString("x " + gp.player.hasKey, 115, 190);

            // MONEY stuff
            g2.setFont(arial_40);
            g2.setColor(Color.white);// font name, font style, font size
            g2.drawImage(moneyImage, 35, 190, gp.tileSize, gp.tileSize, null);
            g2.drawString("x " + gp.player.hasMoney, 125, 250);
            
            // END OF KEY STUFF
            
            //MESSAGE
            if(gp.currentMap != 2) {
                if(messageOn == true) {
                    g2.setFont(g2.getFont().deriveFont(30F));
                    g2.drawString(message, gp.tileSize/2, gp.tileSize*5);
                    messageCount++;

                    if(messageCount > 120){
                        messageCount = 0;
                        messageOn = false;
                    }
                }
            }
            
            
        }
        if(gp.gameState == gp.pauseState) {
            drawPauseScreen();
            drawOptionsScreen();
            drawPlayerLife();
        }
        if(gp.gameState == gp.dialogueState) {
            drawDialogueScreen();
            drawPlayerLife();
        }

        if(gp.gameState == gp.characterState) {
            drawInventory();
        }

        if(gp.gameState == gp.gameOverState) {
            drawGameOverScreen();
        }



        if(gp.gameState == gp.gameWinState) {
            drawGameWinScreen();
        }


    }


    /**
     * Draws the player's life using heart icons (full, half, and empty).
     */
    public void drawPlayerLife() {

        int x = 25;
        int y = 30;
        int i = 0;

        // DRAW MAX LIFE
        while (i < gp.player.maxLife/2) {
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x+= gp.tileSize;
        }

        x = 25;
        y = 30;
        i = 0;

        // DRAW CURRENT LIFE
        while (i < gp.player.life) {
            g2.drawImage(heart_half, x, y, null);
            i++;
            if(i < gp.player.life) {
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gp.tileSize;
        }

    }


    /**
     * Draws the title screen with game title and menu options.
     */
    public void drawTitleScreen() {

        g2.setColor(new Color(70, 80, 120));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 90F));
        String text = " Haunted Hotel";
        int x = 335;
        int y = 150;

        // shadow color
        g2.setColor(Color.BLACK);
        g2.drawString(text, x + 5, y + 5);
        // main text
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        // player image
        x = gp.screenWidth/2 - gp.tileSize;
        y = 625;
        g2.drawImage(gp.player.down1, x ,y, gp.tileSize*2, gp.tileSize*2, null);

        //Menu
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,45F));

        text = "NEW GAME";
        x = 545;
        y = 300;
        g2.drawString(text, x, y);
        if(commandNum == 0) {
            g2.drawString(">", x - gp.tileSize, y);
        }


        text = "QUIT GAME";
        x = 545;
        y = 300 + (gp.tileSize);
        g2.drawString(text, x, y);
        if(commandNum == 1) {
            g2.drawString(">", x - gp.tileSize, y);
        }
    }

    /**
     * Draws the pause screen UI.
     */
    public void drawPauseScreen() {


        String text = "PAUSED";

        g2.setColor(Color.BLACK);
        g2.drawString(text, 573, 153);
        g2.setColor(Color.white);
        int x = getTextCentered(text);
        int y = gp.tileSize;
        g2.drawString(text, 570, 150);

    }

    /**
     * Draws the dialogue window and its contents.
     */
    public void drawDialogueScreen() {

        // dialogue window
        int x = gp.tileSize * 2;
        int y = gp.tileSize;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 3;
        drawSubWindow(x, y, width, height);

        g2.setFont(dialogueFont);
        x += gp.tileSize/2 - 20;
        y += gp.tileSize/2 + 20;

        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += 40;
        }
    }

    /**
     * Draws the player's inventory screen.
     */
    public void drawInventory() {
        int frameX = gp.tileSize * 8;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 5;
        int frameHeight = gp.tileSize * 4;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // INVENTORY SLOTS
        final int slotXstart = frameX + 6;
        final int  slotYstart = frameY + 6; // 5 is number of inventory slots
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotSize = gp.tileSize + 8;

        // DRAW INVENTORY ITEMS
        for(int i = 0; i < gp.player.inventory.size(); i++) {
            g2.drawImage(gp.player.inventory.get(i).down1, slotX, slotY, null);
            slotX += slotSize;
            if(i == 3 || i == 7 || i == 12) {
                slotX = slotXstart;
                slotY += slotSize;
            }
        }

        // CURSOR
        int cursorX = slotXstart + (slotSize * slotCol);
        int cursorY = slotYstart + (slotSize * slotRow);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;
        // DRAW CURSOR
        g2.setColor(Color.pink);
        g2.setStroke(new BasicStroke(3)); // width of cursor
        g2.drawRoundRect(cursorX , cursorY,cursorWidth,cursorHeight, 10, 10);

    }

    /**
     * Draws a rectangular sub-window with rounded corners and border.
     *
     * @param x      x-coordinate of the window.
     * @param y      y-coordinate of the window.
     * @param width  width of the window.
     * @param height height of the window.
     */
    public void drawSubWindow( int x, int y, int width, int height) {

        Color c = new Color(0, 0, 0, 210); // black
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255); //white lgb num
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));//5 pixel

        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public void drawOptionsScreen() {
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(20F));

        //SUB WINDOW
        int frameX = gp.tileSize * 4 + (gp.tileSize/2);
        int frameY = gp.tileSize * 2;
        int frameWidth = gp.tileSize * 5;
        int frameHeight = gp.tileSize * 7;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        options_top(frameX,frameY);
     
    }

     /**
     * Draws the options screen and sub-options such as controls and quit.
     */
    public void options_top(int frameX, int frameY) {

        int textX;
        int textY;
        int leftTextX;

        g2.setFont(new Font("Ariel", Font.BOLD, 30));
        String text = "OPTIONS";
        textX = frameX + gp.tileSize/2;
        textY = frameY + gp.tileSize/2;
        g2.drawString(text, textX, textY + 20);
        g2.setFont(g2.getFont().deriveFont(20F));

        // Controls
        textY += gp.tileSize;
        g2.drawString("Controls", textX, textY);
        if(commandNum == 0) {
            g2.drawString(">", textX - 30, textY);
            if(gp.keyH.enterPressed == true) {
                commandNum = 0;
                // substate 1
            }
        }

        leftTextX = textX + gp.tileSize*2 + 30;
        textX = frameX + gp.tileSize;
        textY += gp.tileSize/2;
        g2.drawString("Move: ", textX, textY);
        leftTextX += gp.tileSize;
        g2.drawString("WASD", leftTextX, textY);

        textY += gp.tileSize/2;
        g2.drawString("Attack: ", textX, textY);
        g2.drawString("E", leftTextX, textY);

        textY += gp.tileSize/2;
        g2.drawString("Shoot: ", textX, textY);
        g2.drawString("Q", leftTextX, textY);

        textY += gp.tileSize/2;
        g2.drawString("Inventory: ", textX, textY);
        g2.drawString("C", leftTextX, textY);

        textY += gp.tileSize/2;
        g2.drawString("Pause/Options: ", textX, textY);
        g2.drawString("ESC", leftTextX, textY);

        textY += gp.tileSize/2;
        g2.drawString("Open dialogue box: ", textX, textY);
        g2.drawString("T", leftTextX, textY);
        textY += gp.tileSize/2;
        g2.drawString("(must touch NPC)", textX, textY);
        textY += gp.tileSize/2;

        g2.drawString("Close any dialogue box: ", textX, textY);
        g2.drawString("T", leftTextX, textY);
        //textY += gp.tileSize/2;



        textX = frameX + gp.tileSize/2;
        textY += gp.tileSize;

        // Quit Game
        g2.drawString("Quit Game", textX, textY);
        if(commandNum == 1) {
            g2.drawString(">", textX - 30, textY);
            if(gp.keyH.enterPressed == true) {
                System.exit(0);
            }
        }
    }

    /**
     * Draws the game over screen and allows the player to restart or quit.
     */
    public void drawGameOverScreen() {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int x;
        int y;

        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));

        text = "Game Over";
        g2.setColor(Color.BLACK);
        x = gp.screenWidth/2 - (gp.tileSize*3);
        y = gp.tileSize * 3;
        g2.drawString(text, x, y);

        g2.setColor(Color.white);
        g2.drawString(text, x - 4, y - 4);

        // Retry
        g2.setFont(g2.getFont().deriveFont( 50f));
        text = "Restart";
        x = gp.screenWidth/2 - 40;
        y += gp.tileSize * 2;
        g2.drawString(text, x, y);
        if(commandNum == 0) {
            g2.drawString(">", x - 48, y);
        }


        // Titlescreen
        text = "Quit";
        y += gp.tileSize * 2;
        g2.drawString(text, x, y);
        if(commandNum == 1) {
            g2.drawString(">", x - 48, y);
        }

        if(gp.keyH.enterPressed == true){
            if(gp.ui.commandNum == 0) {
                gp.gameState = gp.titleState;
                gp.restart();
            }
            else if (gp.ui.commandNum == 1) {
                System.exit(0);
            }
        }
        gp.keyH.enterPressed = false;

    }

     /**
     * Draws the game win screen with final message and quit option.
     */
    public void drawGameWinScreen() {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int x;
        int y;

        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));

        text = "Game Win!";
        g2.setColor(Color.BLACK);
        x = gp.screenWidth/2 - (gp.tileSize*3);
        y = gp.tileSize * 3;
        g2.drawString(text, x, y);

        g2.setColor(Color.white);
        g2.drawString(text, x - 4, y - 4);

        // Quit
        g2.setFont(g2.getFont().deriveFont( 50f));
        text = "Quit";
        x = gp.screenWidth/2 - 40;
        y += gp.tileSize * 2;
        g2.drawString(text, x, y);
        commandNum = 0;
        g2.drawString(">", x - 48, y);


         if(gp.keyH.enterPressed == true){

            if (gp.ui.commandNum == 0) {
                System.exit(0);
            }
        }
        gp.keyH.enterPressed = false;
	
    }

    public void options_control(int frameX, int frameY) {
        int textX;
        int textY;

        String text = "CONTROLS";
        textX = frameX + gp.tileSize;
        textY = frameY + gp.tileSize/2;
        g2.drawString(text, textX, textY);

        textX = frameX + gp.tileSize;
        textY += gp.tileSize*2;
        g2.drawString("Move", textX, textY);
        g2.drawString("Attack", textX, textY);
        g2.drawString("Shoot", textX, textY);
        g2.drawString("Inventory", textX, textY);
        g2.drawString("Pause/Options", textX, textY);
        g2.drawString("Open dialogue box (must touch NPC)", textX, textY);
        g2.drawString("Close dialogue box", textX, textY);

        textX = frameX + gp.tileSize*5;
        textY += frameY + gp.tileSize*2;
        g2.drawString("WASD", textX, textY);
        g2.drawString("E", textX, textY);
        g2.drawString("Q", textX, textY);
        g2.drawString("C", textX, textY);
        g2.drawString("ESC", textX, textY);
        g2.drawString("T", textX, textY);
        g2.drawString("T", textX, textY);

        // back
        textX = frameX + gp.tileSize;
        textY = frameY + gp.tileSize*5;
        g2.drawString("Back", textX, textY);
        if(commandNum == 0) {
            g2.drawString(">", textX - 30, textY);
            if(gp.keyH.enterPressed == true) {
                subState = 0;
            }
        }
    }

    public int getTextCentered(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;
    }

}
