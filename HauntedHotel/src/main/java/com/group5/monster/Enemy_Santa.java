package com.group5.monster;

import com.group5.entity.Entity;
import com.group5.entity.Projectile;
import com.group5.main.GamePanel;
import com.group5.object.Obj_Money;
import com.group5.object.Obj_Snowball;

import java.util.Random;

/**
 * Represents the Santa monster in the game.
 * Extends the Entity class and includes behavior for movement, attacks, 
 * and item drops specific to the enemy characters.
 */
public class Enemy_Santa extends Entity {

     /** Reference to the main game panel for accessing player, tiles, and other entities. */
    GamePanel gp;

    /**
     * Constructor for the santa enemy.
     * Initializes its stats, collision box, and projectile type.
     * 
     * @param gp Reference to the GamePanel
     */
    public Enemy_Santa(GamePanel gp) {
        super(gp);

        this.gp = gp;

        type = 2;
        name = "Evil Santa";
        speed = 5;
        maxLife = 5;
        life = maxLife;
        projectile = new Obj_Snowball(gp);

        solidArea.x = 8;
        solidArea.y = 10;
        solidArea.width = 80;
        solidArea.height = 86;
        solidAreaDefultX = solidArea.x;
        solidAreaDefultY = solidArea.y;

        getImage();

    }

    /**
     * Loads all sprite images for santa in different directions.
     */
    public void getImage() {

        up1 = setUp("/monster/up1", gp.tileSize, gp.tileSize);
        up2 = setUp("/monster/up2", gp.tileSize, gp.tileSize);
        up3 = up1;

        down1 = setUp("/monster/down1", gp.tileSize, gp.tileSize);
        down2 = setUp("/monster/down2", gp.tileSize, gp.tileSize);
        down3 = down1;

        left1 = setUp("/monster/left1", gp.tileSize, gp.tileSize);
        left2 = setUp("/monster/left2", gp.tileSize, gp.tileSize);
        left3 = left1;

        right1 = setUp("/monster/right1", gp.tileSize, gp.tileSize);
        right2 = setUp("/monster/right2", gp.tileSize, gp.tileSize);
        right3 = right1; 
    }

    /**
     * Updates santa's state each frame.
     * Handles activation based on player distance.
     */
	public void update() {
        super.update();

        int xDistance = Math.abs(worldx - gp.player.worldx);
        int yDistance = Math.abs(worldy - gp.player.worldy);
        int tileDistance = (xDistance + yDistance) / gp.tileSize;

        if(!onPath && tileDistance < 8) {

            onPath = true;

        }
        if(onPath && tileDistance > 20) {
            onPath = false;
        }

        if (shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }
    }

    /**
     * Determines santa's actions each frame.
     * Handles chasing the player, firing projectiles, and random movement.
     */
    public void setAction() {

        if(onPath) {
                    
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
            if(i > 19 && !projectile.alive && shotAvailableCounter == 30) {
                Projectile newProjectile = new Obj_Snowball(gp);
                newProjectile.set(worldx,worldy,direction,true,this);
                gp.projectileList.add(newProjectile);
                shotAvailableCounter = 0;
                System.out.println("Santa fired projectile!");

            }

    }

    /**
     * Gets the opposite direction of the given direction.
     * 
     * @param dir The direction to reverse
     * @return The opposite direction
     */
    private String getOppositeDirection(String dir) {
        if ("up".equals(dir)) {
            return "down";
        } else if ("down".equals(dir)) {
            return "up";
        } else if ("left".equals(dir)) {
            return "right";
        } else if ("right".equals(dir)) {
            return "left";
        }
        return dir;
    }

    /**
     * Defines santa's reaction to taking damage.
     * Resets the action counter and sets it to chase the player.
     */
    public void damageReaction() {

        actionLockCounter = 0;
        direction = getOppositeDirection(gp.player.direction);
        onPath = true;
    }

    /**
     * Determines what item the boss drops upon death.
     */
    public void checkDrop() {

        dropItem(new Obj_Money(gp));

    }
}