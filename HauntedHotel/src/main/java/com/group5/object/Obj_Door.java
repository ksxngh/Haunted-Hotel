package com.group5.object;

import com.group5.entity.Entity;
import com.group5.main.GamePanel;

/**
 * Represents a door object in the game.
 * Extends the Entity class and serves as a blocking or interactive object.
 * The player cannot pass through it if collision is enabled.
 */
public class Obj_Door extends Entity {


    /**
     * Constructor for the door object.
     * Sets its name, sprite image, and enables collision.
     * 
     * @param gp Reference to the GamePanel
     */
    public Obj_Door(GamePanel gp) {

        super(gp);

        name = "Door";
        down1 = setUp("/objects/Door", gp.tileSize, gp.tileSize);


        collision = true;
    }
}