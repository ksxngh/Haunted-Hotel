package com.group5.object;

import com.group5.entity.Projectile;
import com.group5.main.GamePanel;

/**
 * Represents a fireball projectile in the game.
 * Extends the Projectile class and can be fired by entities such as monsters or players.
 */
public class Obj_Fireball extends Projectile {

    /** Reference to the main GamePanel for accessing tile size and game objects. */
    GamePanel gp;

    /**
     * Constructor for the fireball projectile.
     * Initializes its name, speed, life, and sets up its images.
     * 
     * @param gp Reference to the GamePanel
     */
    public Obj_Fireball(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "Fireball";
        speed = 10;
        maxLife = 80;
        life = maxLife;
        alive = false;
        getImage();
    }

    /**
     * Loads the sprite images for the fireball in all four directions.
     */
    public void getImage() {
        down1 = setUp("/projectile/Fireball", gp.tileSize, gp.tileSize);
        left1 = setUp("/projectile/Fireball", gp.tileSize, gp.tileSize);
        right1 = setUp("/projectile/Fireball", gp.tileSize, gp.tileSize);
        up1 = setUp("/projectile/Fireball", gp.tileSize, gp.tileSize);
    }

}

