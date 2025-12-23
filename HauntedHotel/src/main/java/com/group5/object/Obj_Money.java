package com.group5.object;

import com.group5.entity.Entity;
import com.group5.main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * Represents a money object in the game.
 * Extends the Entity class and can be collected by the player.
 * Collision is enabled so the object interacts properly with the player and environment.
 */
public class Obj_Money extends Entity {

    /**
     * Constructor for the money object.
     * Initializes its name, sprite image, and enables collision.
     * 
     * @param gp Reference to the GamePanel
     */
    public Obj_Money(GamePanel gp) {
        super(gp);
        name = "Money";
        down1 = setUp("/objects/Money", gp.tileSize, gp.tileSize);


        collision = true;
    }
}