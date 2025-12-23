package com.group5.object;

import com.group5.entity.Projectile;
import com.group5.main.GamePanel;

/**
 * Represents an electric ball projectile in the game.
 * Extends the Projectile class and can be fired by entities such as monsters or players.
 */
public class Obj_ElectricBall extends Projectile {

    /** Reference to the main GamePanel for accessing tile size and game objects. */
    GamePanel gp;

    /**
     * Constructor for the electric ball projectile.
     * Initializes its name, speed, life, and sets up its images.
     * 
     * @param gp Reference to the GamePanel
     */
    public Obj_ElectricBall(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "ElectricBall";
        speed = 10;
        maxLife = 80;
        life = maxLife;
        alive = false;
        getImage();
    }

    /**
     * Loads the sprite images for the electric ball in all four directions.
     */
    public void getImage() {
        down1 = setUp("/projectile/ElectricBall", gp.tileSize, gp.tileSize);
        left1 = setUp("/projectile/ElectricBall", gp.tileSize, gp.tileSize);
        right1 = setUp("/projectile/ElectricBall", gp.tileSize, gp.tileSize);
        up1 = setUp("/projectile/ElectricBall", gp.tileSize, gp.tileSize);
    }


}
