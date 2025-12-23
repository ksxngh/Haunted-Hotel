package com.group5.entity;

import com.group5.main.GamePanel;
import com.group5.main.KeyHandler;
import com.group5.object.Obj_ElectricBall;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * The {@code Player} class represents the player-controlled character in the game.
 * It handles user input, movement, attacks, interactions with objects, NPCs, and monsters,
 * as well as updating the player's status such as health, keys, and money.
 * <p>
 * This class extends {@link Entity} and integrates keyboard input through {@link KeyHandler}
 * to manage real-time gameplay behavior.
 * </p>
 *
 * @author Group 5
 * @version 1.0
 */
public class Player extends Entity {

    // =========================== CONSTANTS ===========================
    // Movement & gameplay
    private static final int PLAYER_SPEED = 8;
    private static final int PLAYER_MAX_LIFE = 10;
    private static final int INVINCIBILITY_TIME = 60;          // frames
    private static final int SHOT_COOLDOWN_FRAMES = 30;        // frames until next shot

    // Animation
    private static final int MOVEMENT_SPRITE_RATE = 10;        // frames per sprite step
    private static final int ATTACK_FRAME_1 = 5;
    private static final int ATTACK_FRAME_2 = 15;
    private static final int ATTACK_FRAME_3 = 25;

    // Hitboxes
    private static final int SOLID_AREA_X = 16;
    private static final int SOLID_AREA_Y = 34;
    private static final int SOLID_AREA_WIDTH = 64;
    private static final int SOLID_AREA_HEIGHT = 62;

    private static final int ATTACK_AREA_SIZE = 80;

    // Default spawn position (in tiles)
    private static final int DEFAULT_SPAWN_TILE_X = 10;
    private static final int DEFAULT_SPAWN_TILE_Y = 14;

    // Inventory
    private static final int INVENTORY_MAX = 12;

    // ================================================================

    /** Handles keyboard input for player movement and actions. */
    KeyHandler keyH;

    /** X-coordinate of the player's position on screen. */
    public final int screenX;

    /** Y-coordinate of the player's position on screen. */
    public final int screenY;

    /** Number of keys currently held by the player. */
    public int hasKey = 0;

    /** Amount of money currently collected by the player. */
    public int hasMoney = 0;

    /** Player's inventory storing collectible entities. */
    public ArrayList<Entity> inventory = new ArrayList<>(INVENTORY_MAX);

    // --- constructor ----------------------------------------------------------------
    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - gp.tileSize / 2;
        screenY = gp.screenHeight / 2 - gp.tileSize / 2;

        solidArea = new Rectangle(SOLID_AREA_X, SOLID_AREA_Y, SOLID_AREA_WIDTH, SOLID_AREA_HEIGHT);
        solidAreaDefultX = solidArea.x;
        solidAreaDefultY = solidArea.y;

        attackArea.width = ATTACK_AREA_SIZE;
        attackArea.height = ATTACK_AREA_SIZE;

        setDefaultValues();
        loadMovementSprites();
        loadAttackSprites();
    }

    // =========================== SETUP ===========================
    /** Sets default values for the player's position, speed, stats, and projectile type. */
    public void setDefaultValues() {
        worldx = gp.tileSize * DEFAULT_SPAWN_TILE_X;
        worldy = gp.tileSize * DEFAULT_SPAWN_TILE_Y;

        speed = PLAYER_SPEED;
        direction = "down";

        maxLife = PLAYER_MAX_LIFE;
        life = maxLife;

        invincible = false;
        inventory.clear();

        projectile = new Obj_ElectricBall(gp);
    }

    /** Loads all player movement sprite images. */
    private void loadMovementSprites() {
        up1 = setUp("/player/Player276-up1", gp.tileSize, gp.tileSize);
        up2 = setUp("/player/Player276-up2", gp.tileSize, gp.tileSize);
        up3 = setUp("/player/Player276-up3", gp.tileSize, gp.tileSize);

        down1 = setUp("/player/Player276-down1", gp.tileSize, gp.tileSize);
        down2 = setUp("/player/Player276-down2", gp.tileSize, gp.tileSize);
        down3 = setUp("/player/Player276-down3", gp.tileSize, gp.tileSize);

        right1 = setUp("/player/Player276-right1", gp.tileSize, gp.tileSize);
        right2 = setUp("/player/Player276-right2", gp.tileSize, gp.tileSize);
        right3 = setUp("/player/Player276-right3", gp.tileSize, gp.tileSize);

        left1 = setUp("/player/Player276-left1", gp.tileSize, gp.tileSize);
        left2 = setUp("/player/Player276-left2", gp.tileSize, gp.tileSize);
        left3 = setUp("/player/Player276-left3", gp.tileSize, gp.tileSize);
    }

    /** Loads all player attack sprite images. */
    private void loadAttackSprites() {
        attackUp1 = setUp("/player/PlayerAttack-up1", gp.tileSize, gp.tileSize * 2);
        attackUp2 = setUp("/player/PlayerAttack-up2", gp.tileSize, gp.tileSize * 2);
        attackUp3 = setUp("/player/PlayerAttack-up3", gp.tileSize, gp.tileSize * 2);

        attackDown1 = setUp("/player/PlayerAttack-down1", gp.tileSize, gp.tileSize * 2);
        attackDown2 = setUp("/player/PlayerAttack-down2", gp.tileSize, gp.tileSize * 2);
        attackDown3 = setUp("/player/PlayerAttack-down3", gp.tileSize, gp.tileSize * 2);

        attackRight1 = setUp("/player/PlayerAttack-right1", gp.tileSize * 2, gp.tileSize);
        attackRight2 = setUp("/player/PlayerAttack-right2", gp.tileSize * 2, gp.tileSize);
        attackRight3 = setUp("/player/PlayerAttack-right3", gp.tileSize * 2, gp.tileSize);

        attackLeft1 = setUp("/player/PlayerAttack-left1", gp.tileSize * 2, gp.tileSize);
        attackLeft2 = setUp("/player/PlayerAttack-left2", gp.tileSize * 2, gp.tileSize);
        attackLeft3 = setUp("/player/PlayerAttack-left3", gp.tileSize * 2, gp.tileSize);
    }

    // =========================== UPDATE ===========================
    /**
     * Updates player state including movement, attacks, collisions, and status effects.
     */
    public void update() {
        if (attacking) {
            updateAttack();
            return;
        }

        handleMovementInput();
        handleProjectileShooting();
        updateInvincibility();
        updateShotCooldown();
        checkDeathState();
    }

    // ================= MOVEMENT =================
    private void handleMovementInput() {
        if (!(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.enterPressed)) {
            return;
        }

        updateDirection();
        processCollisions();

        if (!collisionOn && !keyH.enterPressed) movePlayer();
        keyH.enterPressed = false;

        updateMovementAnimation();
    }

    private void updateDirection() {
        if (keyH.upPressed) direction = "up";
        else if (keyH.downPressed) direction = "down";
        else if (keyH.leftPressed) direction = "left";
        else if (keyH.rightPressed) direction = "right";
    }

    private void processCollisions() {
        collisionOn = false;

        // tile collision
        gp.cChecker.checkTile(this);

        // object/NPC/monster interactions
        int objIndex = gp.cChecker.checkObject(this, true);
        pickUpObject(objIndex);

        int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
        interactNPC(npcIndex);

        int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
        contactMonster(monsterIndex);

        gp.eHandler.checkEvent();
    }

    private void movePlayer() {
        switch (direction) {
            case "up": worldy -= speed; break;
            case "down": worldy += speed; break;
            case "left": worldx -= speed; break;
            case "right": worldx += speed; break;
        }
    }

    private void updateMovementAnimation() {
        spriteCounter++;
        if (spriteCounter > MOVEMENT_SPRITE_RATE) {
            spriteNum = (spriteNum % 3) + 1;
            spriteCounter = 0;
        }
    }

    // ================== ATTACKING ==================
    /**
     * Handles player attack animations and damage dealing logic.
     */
    private void updateAttack() {
        spriteCounter++;

        if (spriteCounter <= ATTACK_FRAME_1) spriteNum = 1;
        else if (spriteCounter <= ATTACK_FRAME_2) spriteNum = 2;
        else if (spriteCounter <= ATTACK_FRAME_3) spriteNum = 3;
        else {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }

        // save current values
        int currentWorldX = worldx;
        int currentWorldY = worldy;
        int solidAreaWidth = solidArea.width;
        int solidAreaHeight = solidArea.height;

        // shift hitbox to attack area and expand hitbox
        shiftAttackHitbox();

        // check collision with monsters using updated hitbox
        int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
        damageMonster(monsterIndex);

        // restore saved values
        worldx = currentWorldX;
        worldy = currentWorldY;
        solidArea.width = solidAreaWidth;
        solidArea.height = solidAreaHeight;
    }

    /** Moves player's position and expands the solidArea to represent an attack hitbox. */
    private void shiftAttackHitbox() {
        switch (direction) {
            case "up": worldy -= attackArea.height; break;
            case "down": worldy += attackArea.height; break;
            case "left": worldx -= attackArea.width; break;
            case "right": worldx += attackArea.width; break;
        }
        // expand solid area to match attack area for collision checking
        solidArea.width = attackArea.width;
        solidArea.height = attackArea.height;
    }

    // ================= PICKUP / INTERACT =================
    /**
     * Handles object collection logic.
     * Adds items to inventory or displays a message if full.
     *
     * @param i the index of the object being picked up
     */
    public void pickUpObject(int i) {
        if (i == 999) return;

        String text;
        if (inventory.size() < INVENTORY_MAX) {
            inventory.add(gp.obj[gp.currentMap][i]);
            text = "You got a " + gp.obj[gp.currentMap][i].name;

            String name = gp.obj[gp.currentMap][i].name;
            if ("key".equals(name)) hasKey++;
            if ("Money".equals(name)) hasMoney++;

            gp.playSE(1);
            gp.obj[gp.currentMap][i] = null;
        } else {
            text = "Inventory full";
        }

        gp.ui.showMessage(text);
    }

    /**
     * Handles interaction with NPCs when the player presses the interaction key.
     *
     * @param i the index of the NPC being interacted with
     */
    public void interactNPC(int i) {
        if (gp.keyH.enterPressed) {
            if (i != 999) {
                gp.gameState = gp.dialogueState;
                gp.npc[gp.currentMap][i].speak();
            }
        }
        if (gp.keyH.attackPressed) {
            attacking = true;
        }
        gp.keyH.attackPressed = false;
    }

    // ================= MONSTERS =================
    /**
     * Handles contact between the player and monsters.
     * Reduces health and triggers invincibility frames.
     *
     * @param i the index of the monster contacted
     */
    public void contactMonster(int i) {
        if (i != 999) {
            if (!invincible) {
                life -= 2;
                invincible = true;
                // simple knockback: move player slightly opposite to current direction
                switch (direction) {
                    case "up": worldy += gp.tileSize; break;
                    case "down": worldy -= gp.tileSize; break;
                    case "left": worldx += gp.tileSize; break;
                    case "right": worldx -= gp.tileSize; break;
                }
            }
        }
    }

    /**
     * Inflicts damage on monsters when attacking.
     * Updates game state upon monster death and win/loss conditions.
     *
     * @param i the index of the monster being damaged
     */
    public void damageMonster(int i) {
        if (i == 999) return;

        if (!gp.monster[gp.currentMap][i].invincible && !gp.monster[gp.currentMap][i].dying) {
            gp.monster[gp.currentMap][i].life -= 1;
            gp.monster[gp.currentMap][i].invincible = true;
            gp.monster[gp.currentMap][i].damageReaction();

            if (gp.monster[gp.currentMap][i].life <= 0) {
                gp.monster[gp.currentMap][i].dying = true;
                hasMoney++;

                // keep same behavior: win when money reaches 3 (as in original)
                if (hasMoney == 3) {
                    gp.gameState = gp.gameWinState;
                } else {
                    gp.gameState = gp.gameOverState;
                }
            }
        }
    }

    // ================= SHOOTING / COOLDOWNS =================
    private void handleProjectileShooting() {
        if (keyH.shootKeyPressed && !projectile.alive && shotAvailableCounter == SHOT_COOLDOWN_FRAMES) {
            projectile.set(worldx, worldy, direction, true, this);
            gp.projectileList.add(projectile);
            shotAvailableCounter = 0;
        }
    }

    private void updateShotCooldown() {
        if (shotAvailableCounter < SHOT_COOLDOWN_FRAMES) shotAvailableCounter++;
    }

    // ================= INVINCIBILITY =================
    private void updateInvincibility() {
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > INVINCIBILITY_TIME) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    // ================= DEATH =================
    private void checkDeathState() {
        if (life <= 0) {
            gp.gameState = gp.gameOverState;
            life = maxLife;
        }
    }

    // ================= DRAW =================
    /**
     * Renders the player on the screen with the correct sprite based on direction and state.
     *
     * @param g2 the {@link Graphics2D} context used for drawing
     */
    public void draw(Graphics2D g2) {
        BufferedImage image = getCurrentSprite();
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        if (attacking && "up".equals(direction)) tempScreenY = screenY - gp.tileSize;
        if (attacking && "left".equals(direction)) tempScreenX = screenX - gp.tileSize;

        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        g2.drawImage(image, tempScreenX, tempScreenY, null);

        // RESET ALPHA
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    private BufferedImage getCurrentSprite() {
        boolean attack = attacking;
        int s = spriteNum;

        switch (direction) {
            case "up":
                return attack ? spriteByNumber(s, attackUp1, attackUp2, attackUp3)
                              : spriteByNumber(s, up1, up2, up3);
            case "down":
                return attack ? spriteByNumber(s, attackDown1, attackDown2, attackDown3)
                              : spriteByNumber(s, down1, down2, down3);
            case "left":
                return attack ? spriteByNumber(s, attackLeft1, attackLeft2, attackLeft3)
                              : spriteByNumber(s, left1, left2, left3);
            case "right":
            default:
                return attack ? spriteByNumber(s, attackRight1, attackRight2, attackRight3)
                              : spriteByNumber(s, right1, right2, right3);
        }
    }

    private BufferedImage spriteByNumber(int s, BufferedImage a, BufferedImage b, BufferedImage c) {
        if (s == 1) return a;
        if (s == 2) return b;
        return c;
    }
}