package com.group5.monster;

import com.group5.entity.Entity;
import com.group5.entity.Projectile;
import com.group5.main.GamePanel;
import com.group5.object.Obj_Fireball;
import com.group5.object.Obj_Money;

import java.util.Random;

/**
 * Represents the Boss monster in the game.
 * Extends the Entity class and includes behavior for movement, attacks, 
 * and item drops specific to the enemy characters.
 */
public class Mon_Boss extends Entity {

    /** Reference to the main game panel for accessing player, tiles, and other entities. */
    GamePanel gp;

    /**
     * Constructor for the Boss monster.
     * Initializes its stats, collision box, and projectile type.
     * 
     * @param gp Reference to the GamePanel
     */
    public Mon_Boss(GamePanel gp) {
        super(gp);

        this.gp = gp;

        type = 2;
        name = "Boss";
        speed = 5;
        maxLife = 10;
        life = maxLife;
        projectile = new Obj_Fireball(gp);

        solidArea.x = 8;
        solidArea.y = 10;
        solidArea.width = 80;
        solidArea.height = 86;
        solidAreaDefultX = solidArea.x;
        solidAreaDefultY = solidArea.y;

        getImage();

    }

    /**
     * Loads all sprite images for the boss in different directions.
     */
    public void getImage() {

        // UP
        up1 = setUp("/monster/Boss-up1", gp.tileSize, gp.tileSize);
        up2 = setUp("/monster/Boss-up2", gp.tileSize, gp.tileSize);
        up3 = setUp("/monster/Boss-up3", gp.tileSize, gp.tileSize);
        // DOWN
        down1 = setUp("/monster/Boss-down1", gp.tileSize, gp.tileSize);
        down2 = setUp("/monster/Boss-down2", gp.tileSize, gp.tileSize);
        down3 = setUp("/monster/Boss-down3", gp.tileSize, gp.tileSize);
        // RIGHT
        right1 = setUp("/monster/Boss-right1", gp.tileSize, gp.tileSize);
        right2 = setUp("/monster/Boss-right2", gp.tileSize, gp.tileSize);
        right3 = setUp("/monster/Boss-right3", gp.tileSize, gp.tileSize);
        // LEFT
        left1 = setUp("/monster/Boss-left1", gp.tileSize, gp.tileSize);
        left2 = setUp("/monster/Boss-left2", gp.tileSize, gp.tileSize);
        left3 = setUp("/monster/Boss-left3", gp.tileSize, gp.tileSize);
    }

    /**
     * Updates the boss's state each frame.
     * Handles activation based on player distance.
     */
	public void update() {
        super.update();

        int xDistance = Math.abs(worldx - gp.player.worldx);
        int yDistance = Math.abs(worldy - gp.player.worldy);
        int tileDistance = (xDistance + yDistance) / gp.tileSize;

        if(onPath == false && tileDistance < 8) {

            onPath = true;

        }
        if(onPath == true && tileDistance > 20) {
            onPath = false;
        }

        if (shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }
    }

    /**
     * Determines the boss's actions each frame.
     * Handles chasing the player, firing projectiles, and random movement.
     */
    public void setAction() {

        if(onPath == true) {
                    
                // fixed

                int goalCol = (gp.player.worldx + gp.player.solidArea.x) / gp.tileSize;
                int goalRow = (gp.player.worldy + gp.player.solidArea.y) / gp.tileSize;

                // clamp so it never goes out of bounds
                goalCol = Math.max(0, Math.min(goalCol, gp.maxWorldCol - 1));
                goalRow = Math.max(0, Math.min(goalRow, gp.maxWorldRow - 1));

                searchPath(goalCol, goalRow);

            }

            else {
                actionLockCounter++;

                if(actionLockCounter == 180) {
                    Random random = new Random();
                    int i = random.nextInt(100)+1;

                    if (i <= 25) {
                        direction = "up";
                    }
                    if (i > 25 && i <= 50) {
                        direction = "down";
                    }
                    if (i > 50 && i <= 75) {
                        direction = "left";
                    }
                    if (i > 75 && i <= 100) {
                        direction = "right";
                    }
                    actionLockCounter = 0;


                }

            }

            int i = new Random().nextInt(100) + 1;
            if(i > 19 && projectile.alive == false && shotAvailableCounter == 30) {
                Projectile newProjectile = new Obj_Fireball(gp);
                newProjectile.set(worldx,worldy,direction,true,this);
                gp.projectileList.add(newProjectile);
                shotAvailableCounter = 0;
                System.out.println("Boss fired projectile!");

            }

    }

    /**
     * Defines the boss's reaction to taking damage.
     * Resets the action counter and sets it to chase the player.
     */
    public void damageReaction() {

        actionLockCounter = 0;
        direction = gp.player.direction;
        onPath = true;
        if(gp.player.direction == "up"){
            direction = "down";
        }
        if(gp.player.direction == "down"){
            direction = "up";
        }
        if(gp.player.direction == "left"){
            direction = "right";
        }
        if(gp.player.direction == "right"){
            direction = "left";
        }
    }

    /**
     * Determines what item the boss drops upon death.
     */
    public void checkDrop() {

        dropItem(new Obj_Money(gp));

    }
}
