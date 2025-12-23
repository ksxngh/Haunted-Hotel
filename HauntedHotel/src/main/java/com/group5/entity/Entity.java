package com.group5.entity;

import com.group5.main.GamePanel;
import com.group5.main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * The {@code Entity} class serves as a base class for all movable and drawable objects in the game,
 * including the player, NPCs, monsters, and projectiles. It defines shared properties such as position,
 * movement, collision detection, image handling, and drawing logic.
 * 
 * <p>Entities are managed by the {@link GamePanel} and can interact with tiles, objects, and other entities.</p>
 * 
 * @author Group 5
 * @version 1.0
 */
public class Entity {

    /** Reference to the main {@link GamePanel}. */
    GamePanel gp;

    /** Entity movement and animation sprites. */
    public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;

    /** Attack animation sprites for all directions. */
    public BufferedImage attackUp1, attackUp2, attackUp3, attackDown1, attackDown2, attackDown3, attackLeft1, attackLeft2, attackLeft3, attackRight1, attackRight2, attackRight3;

    /** General-purpose image references for objects or UI. */
    public BufferedImage image, image2, image3;

    /** Rectangle used for collision detection. */
    public Rectangle solidArea = new Rectangle(0, 0, 96, 96);

    /** Default X and Y position of the solid area. */
    public int solidAreaDefultX, solidAreaDefultY;

    /** Optional dialogue text array for NPCs. */
    String dialogues[] = new String[20];

    /** Rectangle representing the attack area for melee or projectile attacks. */
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);


     /** World coordinates for entity position. */
    public int worldx, worldy;

    /** Current facing direction ("up", "down", "left", or "right"). */
    public String direction = "down";

    /** Movement speed of the entity. */
    public int speed;

    /** Current sprite frame number for animation. */
    public int spriteNum = 1;

    /** Index used for cycling through dialogue lines. */
    public int dialogeIndex = 0;

    /** Whether the entity is currently colliding with another object or tile. */
    public boolean collisionOn = false;

    /** Whether the entity is temporarily invincible (e.g., after being hit). */
    public boolean invincible = false;

    /** Whether the entity is currently performing an attack. */
    boolean attacking = false;

    /** Whether the entity is alive. */
    public boolean alive = true;

    /** Whether the entity is dying and playing a death animation. */
    public boolean dying = false;

    /** Whether to display a health bar above the entity. */
    public boolean hpBarOn = false;

    /** Whether the entity is currently following a path. */
    public boolean onPath = false;

    /** Timers and counters for controlling animation and behavior. */
    public int actionLockCounter = 0;
    public int spriteCounter = 0;
    public int invincibleCounter = 0;
    public int dyingCounter = 0;
    public int hpBarCounter = 0;
    public int shotAvailableCounter = 0;

    /** The entity's display name. */
    public String name;

    /** Whether the entity's collision area blocks movement. */
    public boolean collision = false;

    /** Type identifier (0 = player, 1 = NPC, 2 = monster, 3 = object). */
    public int type;

    /** Constant representing objects that can only be picked up. */
    public int type_pickUpOnly = 3;

    /** Maximum health of the entity. */
    public int maxLife;

    /** Current health of the entity. */
    public int life;

    /** Reference to a projectile object associated with the entity. */
    public Projectile projectile;


    /**
     * Constructs an {@code Entity} linked to the given {@link GamePanel}.
     *
     * @param gp the game panel the entity belongs to
     */
    public Entity(GamePanel gp){
        this.gp = gp;
    }
/** Defines specific entity actions, overridden by subclasses. */
    public void setAction() {}

    /** Handles reaction logic when the entity takes damage. */
    public void damageReaction() {}

    /** Checks what item the entity should drop upon death. */
    public void checkDrop() {}

    /**
     * Spawns a dropped item at the entity's current location.
     *
     * @param droppedItem the item entity to drop
     */
    public void dropItem(Entity droppedItem) {
        for (int i = 0; i < gp.obj[1].length; i++) {
            if (gp.obj[gp.currentMap][i] == null) {
                gp.obj[gp.currentMap][i] = droppedItem;
                gp.obj[gp.currentMap][i].worldx = worldx;
                gp.obj[gp.currentMap][i].worldy = worldy;
                break;
            }
        }
    }

    /** Defines dialogue behavior for speaking entities (e.g., NPCs). */
    public void speak() {}

    /**
     * Checks for collisions with tiles, objects, other entities, and the player.
     * If the entity is a monster and contacts the player, it causes damage.
     */
    public void checkCollision() {
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        if(this.type == 2 && contactPlayer == true) {
            damagePlayer();
        }
    }

    /**
     * Updates the entity each frame by setting its action, checking for collisions,
     * moving if possible, updating animation frames, and handling invincibility timing.
     */
    public void update() {

        // USED FOR MOVEMENT
        setAction();
        checkCollision();

        // IF COLLIS IS FALSE PLAYERS MOVES
        if(collisionOn == false){
            switch (direction){
                case "up":
                    worldy -= speed;
                    break;
                case "down":
                    worldy += speed;
                    break;
                case "left":
                    worldx -= speed;
                    break;
                case "right":
                    worldx += speed;
                    break;
            }
        }

        spriteCounter++;
        if (spriteCounter > 10) {
            if (spriteNum == 1){
                spriteNum = 2;
            }
            else if (spriteNum == 2) {
                spriteNum = 3;
            }
            else if (spriteNum == 3 ){
                spriteNum = 1;
            }
            spriteCounter = 0;
        }


        if(invincible == true) {
            invincibleCounter++;
            if(invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

    }

    /**
     * Uses the pathfinding system to move the entity toward a specific goal tile.
     *
     * @param goalCol the goal column position
     * @param goalRow the goal row position
     */
    public void searchPath(int goalCol, int goalRow) {

        int startCol = (worldx + solidArea.x) / gp.tileSize;
        int startRow = (worldy + solidArea.y) / gp.tileSize;

        gp.pFinder.setNode(startCol, startRow, goalCol, goalRow);

        if(gp.pFinder.search() == true) {
            int nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
            int nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;

            // ENTITY SOLID AREA POS
            int enLeftX = worldx + solidArea.x;
            int enRightX = worldx + solidArea.x + solidArea.width;
            int enTopY = worldy + solidArea.y;
            int enBottomY = worldy + solidArea.y + solidArea.height;

            if(enTopY > nextY && enLeftX >= nextX && enRightX < nextX +gp.tileSize) {
                direction = "up";
            }
            else if(enTopY < nextY && enLeftX >= nextX && enRightX < nextX +gp.tileSize) {
                direction = "down";
            }
            else if(enTopY >= nextY && enBottomY < nextY + gp.tileSize) {
                // left or right
                if(enLeftX > nextX) {
                    direction = "left";
                }
                if(enLeftX < nextX) {
                    direction = "right";
                }
            }
            else if(enTopY > nextY && enLeftX > nextX) {
                // up or left
                direction = "up";
                checkCollision();
                if(collisionOn == true) {
                    direction = "left";
                }
            }
            else if(enTopY > nextY && enLeftX < nextX) {
                // up or right
                direction = "up";
                checkCollision();
                if(collisionOn == true) {
                    direction = "right";
                }
            }
            else if(enTopY < nextY && enLeftX > nextX) {
                // down or left
                direction = "down";
                checkCollision();
                if(collisionOn == true) {
                    direction = "left";
                }
            }
            else if(enTopY < nextY && enLeftX < nextX) {
                // down or right
                direction = "down";
                checkCollision();
                if(collisionOn == true) {
                    direction = "right";
                }
            }

         
        }
    }


    /**
     * Damages the player if the entity is a monster and the player is not invincible.
     */
    public void damagePlayer() {

        if(gp.player.invincible == false) {
            gp.player.life -= 2;
            gp.player.invincible = true;
        }

    }

    /**
     * Loads and scales an image from a given path.
     *
     * @param imagePath the relative path to the image (without file extension)
     * @param width the target width of the image
     * @param height the target height of the image
     * @return the scaled image as a {@link BufferedImage}
     */
    public BufferedImage setUp(String imagePath, int width, int height) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage scaledImage = null;

        try {
            scaledImage = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            scaledImage = uTool.scaleImage(scaledImage, width, height);

        }catch(IOException e) {
            e.printStackTrace();
        }
        return scaledImage;
    }


    /**
     * Draws the entity on the screen based on its world position and sprite state.
     *
     * @param g2 the {@link Graphics2D} object used for rendering
     */
    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        int screenX = worldx - gp.player.worldx + gp.player.screenX;
        int screenY = worldy - gp.player.worldy + gp.player.screenY;

        if(worldx + gp.tileSize > gp.player.worldx - gp.player.screenX &&
                worldx - gp.tileSize < gp.player.worldx + gp.player.screenX &&
                worldy + gp.tileSize > gp.player.worldy - gp.player.screenY &&
                worldy - gp.tileSize < gp.player.worldy + gp.player.screenY) {

            switch(direction) {
                case "up":
                    if(spriteNum == 1){
                        image = up1;
                    }
                    if(spriteNum == 2){
                        image = up2;
                    }
                    if(spriteNum == 3){
                        image = up3;
                    }
                    break;
                case "down":
                    if(spriteNum == 1){
                        image = down1;
                    }
                    if(spriteNum == 2){
                        image = down2;
                    }
                    if(spriteNum == 3){
                        image = down3;
                    }
                    break;
                case "right":
                    if(spriteNum == 1){
                        image = right1;
                    }
                    if(spriteNum == 2){
                        image = right2;
                    }
                    if(spriteNum == 3){
                        image = right3;
                    }
                    break;
                case "left":
                    if(spriteNum == 1){
                        image = left1;
                    }
                    if(spriteNum == 2){
                        image = left2;
                    }
                    if(spriteNum == 3){
                        image = left3;
                    }
                    break;
            }
            
            if(type == 2 && hpBarOn == true) {

                double oneScale = (double)gp.tileSize/maxLife;
                double hpBarValue = oneScale*life;


                g2.setColor(new Color(20,22,40));
                g2.fillRect(screenX-2, screenY - 32, gp.tileSize + 2, 12);

                g2.setColor(new Color(225,30,60));
                g2.fillRect(screenX, screenY - 30, (int)hpBarValue, 15);
                hpBarCounter++;

                if(hpBarCounter > 600) {
                    hpBarOn = false;
                    hpBarCounter = 0;
                }
            }

            if(type == 1) {

                String text = "Press T Here";

                g2.setColor(new Color(20,22,40));
                g2.setFont(g2.getFont().deriveFont(Font.BOLD,20F));
                g2.drawString(text, screenX, screenY -30);

                g2.setColor(Color.white);
                g2.setFont(g2.getFont().deriveFont(Font.BOLD,21F));
                g2.drawString(text, screenX + 1, screenY - 30);
                
            }

            if(invincible == true) {
                hpBarOn = true;
                hpBarCounter = 0;
                changeAlpha(g2,0.4f);
            }
            if(dying == true) {
                dyingAnimation(g2);
            }

            g2.drawImage(image, screenX, screenY, null);

            
            changeAlpha(g2,1f);


        }

    }


    /**
     * Plays the dying animation by toggling the entity’s visibility over time.
     *
     * @param g2 the {@link Graphics2D} object used for rendering
     */
    public void dyingAnimation(Graphics2D g2) {
        dyingCounter++;

        int i =5;

        if(dyingCounter <= i) {
            changeAlpha(g2, 0f);
        }
        if(dyingCounter > i && dyingCounter <= i*2) {
            changeAlpha(g2, 1f);
        }
        if(dyingCounter > i*2 && dyingCounter <= i*3) {
            changeAlpha(g2, 0f);
        }
        if(dyingCounter > i*3 && dyingCounter <= i*4) {
            changeAlpha(g2, 1f);
        }
        if(dyingCounter > i*4 && dyingCounter <= i*5) {
            changeAlpha(g2, 0f);
        }
        if(dyingCounter > i*5 && dyingCounter <= i*6) {
            changeAlpha(g2, 1f);
        }
        if(dyingCounter > i*6) {
            alive = false;

        }
    }

    /**
     * Changes the transparency level of the {@link Graphics2D} context.
     *
     * @param g2 the graphics context
     * @param alphaValue the transparency level (0.0f = invisible, 1.0f = fully opaque)
     */
    public void changeAlpha(Graphics2D g2, float alphaValue) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

}


