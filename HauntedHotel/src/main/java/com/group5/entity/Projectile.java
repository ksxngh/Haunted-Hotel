package com.group5.entity;

import com.group5.main.GamePanel;


/**
 * The {@code Projectile} class represents any projectile fired by either the player or an enemy.
 * It handles its own movement, lifespan, and collision detection with monsters or the player.
 * <p>
 * This class extends {@link Entity} and uses the same coordinate system and collision logic
 * as other entities in the game.
 * </p>
 * 
 * @author Group 5
 * @version 1.0
 */
public class Projectile extends Entity{

    /** The entity (player or monster) that fired this projectile. */
    Entity user;

    /**
     * Constructs a new {@code Projectile} instance associated with the given {@link GamePanel}.
     *
     * @param gp the main {@link GamePanel} instance managing the game
     */

    public Projectile(GamePanel gp) {
        super(gp);

    }

    /**
     * Initializes the projectile’s position, direction, state, and lifespan.
     *
     * @param worldx the world X-coordinate where the projectile starts
     * @param worldy the world Y-coordinate where the projectile starts
     * @param direction the direction the projectile will travel ("up", "down", "left", or "right")
     * @param alive whether the projectile is active
     * @param user the entity that fired the projectile (e.g., player or monster)
     */
    public void set(int worldx, int worldy, String direction, boolean alive, Entity user) {

        this.worldx = worldx;
        this.worldy = worldy;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.life = this.maxLife;


    }

     /**
     * Updates the projectile’s movement, collision detection, and lifetime.
     * <ul>
     *     <li>If fired by the player, checks for monster collisions and applies damage.</li>
     *     <li>If fired by an enemy, checks for player collisions and applies damage.</li>
     *     <li>Moves the projectile in its direction at its current speed.</li>
     *     <li>Reduces its lifespan each frame and destroys itself when life reaches zero.</li>
     * </ul>
     */
    public void update() {

        if(user == gp.player) {
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            if(monsterIndex != 999) {
                gp.player.damageMonster(monsterIndex);
                alive = false;
            }
        }
        if(user != gp.player) {
            boolean contactPlayer = gp.cChecker.checkPlayer(this);
            if(gp.player.invincible == false && contactPlayer == true) {
                damagePlayer();
                alive = false;
            }
        }


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
        spriteNum = 1;
        life--;
        if(life <= 0) {
            alive = false;
        }

    }

}
