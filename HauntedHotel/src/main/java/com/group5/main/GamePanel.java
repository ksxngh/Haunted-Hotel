package com.group5.main;

import com.group5.entity.Entity;
import com.group5.entity.Player;
import com.group5.tile.TileManager;
import com.group5.ai.Pathfinder;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * The {@code GamePanel} class serves as the core of the game engine. It manages all
 * rendering, updating, and game logic including entities, objects, NPCs, and monsters.
 * It extends {@link JPanel} and implements {@link Runnable} to run the main game loop
 * in a separate thread.
 * <p>
 * The class is responsible for controlling the game state, handling collisions,
 * managing assets, and orchestrating interactions between all major components
 * such as the {@link Player}, {@link TileManager}, and {@link UI}.
 * </p>
 *
 * @author Group 5
 * @version 1.0
 */
public class GamePanel extends JPanel implements Runnable {

    //SCREEN SETTINGS
    final int originalTileSize = 32; // 32 x 32 tile defult size of player char
    final int scale = 3;

/** The size of each tile in the game, after scaling. */
    public final int tileSize = originalTileSize * scale;

    /** Number of columns and rows in the visible screen. */
    public final int maxScreenCol = 14, maxScreenRow = 9;

    /** Screen dimensions in pixels. */
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // WORLD SETTINGS
    public final int maxWorldCol = 70;
    public final int maxWorldRow = 30;
    public final int maxMap = 5;
    public int currentMap = 0;
  

    /** Frames per second target for the main game loop. */
    int FPS = 60;

    // CORE COMPONENTS
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    public Pathfinder pFinder = new Pathfinder(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);

    /** Main game thread used to run the loop. */
    Thread gameThread; //used for time if you wanna do smt multiple times
    

    // ENTITY AND OBJECT MANAGEMENT
    public Player player = new Player(this, keyH);
    public Entity obj[][] = new Entity[maxMap][20];
    public Entity npc[][] = new Entity[maxMap][2];
    public Entity monster[][] = new Entity[maxMap][8];
    ArrayList<Entity> entityList = new ArrayList<>();
    public ArrayList<Entity> projectileList = new ArrayList<>();

    // GAME STATES
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int gameOverState = 5;
    public final int gameWinState = 6;


     /**
     * Sets up the initial game environment by loading objects, NPCs, and monsters,
     * then setting the starting state to the title screen.
     */
    public void setupGame() {

        aSetter.setObject();
        aSetter.setMonster();
        aSetter.setNPC();

        gameState = titleState;

        playMusic(0);

        // refactor: removing dead code from here
        
    }

     /** Resets player values to default for restarting the game. */
    public void restart() {
        currentMap = 0;
        player.setDefaultValues();
        player.hasKey = 0;
        aSetter.setObject();
        aSetter.setMonster();
        aSetter.setNPC();
        gameState = playState;


    }

    /**
     * Constructor that initializes panel settings, input listeners, and rendering options.
     */
    public GamePanel () {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

    }

    /** Starts the main game loop in a separate thread. */
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();

    }

    /**
     * The main game loop. Controls update and rendering frequency to maintain target FPS.
     * This method continuously updates game state and repaints the screen.
     */
    @Override
    public void run() {
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta > 1){
                update();
                repaint();
                delta--;
                drawCount++;
            }
            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

     /**
     * Updates all active game entities depending on the current game state.
     * Handles player, monsters, and projectile logic.
     */
    public void update() {

        if(gameState == playState) {
            player.update();
            //UPDATE NPC IF NEEDED

            // UPDATE MONSTER
            for(int i = 0; i < monster[1].length; i++) {
                if(monster[currentMap][i] != null) {
                    if(monster[currentMap][i].alive == true && monster[currentMap][i].dying == false) {
                        monster[currentMap][i].update();
                    }
                    if(monster[currentMap][i].alive == false){
                        monster[currentMap][i].checkDrop();
                        monster[currentMap][i] = null;
                    }
                }
            }

            for(int i = 0; i < projectileList.size(); i++) {
                if(projectileList.get(i) != null) {
                    if(projectileList.get(i).alive == true) {
                        projectileList.get(i).update();
                    }
                    if(projectileList.get(i).alive == false){
                        projectileList.remove(i);
                    }
                }
            }


        }
        if(gameState == pauseState) {

        }

    }

    /**
     * Handles all rendering for the game, including tiles, entities, projectiles, and UI.
     * Entities are sorted by Y-coordinate for proper depth rendering.
     *
     * @param g The {@link Graphics} context used for drawing.
     */
    public void paintComponent(Graphics g) { //written compon in java
        super.paintComponent(g); // super means parent graph of the graph (jpanel)
        Graphics2D g2 = (Graphics2D) g;

        // TITLE SCREEN
        if(gameState == titleState) {
            ui.draw(g2);
        }
        // others
        else {

            // TILE
            tileM.draw(g2);

            // FANCY

            // ENTITIES ADDED TO LIST
            entityList.add(player);

            if (npc[currentMap][0] != null)
                entityList.add(npc[currentMap][0]); // if you add more npc then you create a for loop

            for(int i = 0; i < monster[1].length; i++) {
                if(monster[currentMap][i] != null) {
                    entityList.add(monster[currentMap][i]);
                }
            }

            for(int i = 0; i < projectileList.size(); i++) {
                if(projectileList.get(i) != null) {
                    entityList.add(projectileList.get(i));
                }
            }

            //refactor: removing dead code from here 
            


            for(int i = 0; i < obj[1].length; i++) {
                if(obj[currentMap][i] != null) {
                    entityList.add(obj[currentMap][i]);
                }
            }



            Collections.sort(entityList, new Comparator<Entity>() {

                @Override
                public int compare(Entity e1, Entity e2) {
                    int result = Integer.compare(e1.worldy, e2.worldy);
                    return result;
                }
            });


            // DRAW ENITY SORTED BY WHO IS DRAWN FIRST BASED ON Y VAL
            for(int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }

            //npc[0].draw(g2);


            // RESET ENTITY LIST

            entityList.clear();



            //UI
            ui.draw(g2);
        }

        g2.dispose();
    }

    /** Plays background music for the given track index. */
    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }
    
     /** Stops the background music. */
    public void stopMusic() {
        music.stop();
    }

    /** Stops all sound effects. */
    public void stopSound() {
        se.stop();
    }

    /** Plays a specific sound effect once. */
    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }
}
