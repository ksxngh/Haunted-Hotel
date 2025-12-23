package com.group5.object;

import com.group5.entity.Entity;
import com.group5.main.GamePanel;


/**
 * Represents a key object used to unlock chests in the game.
 * Extends the Entity class and is a collectible item that the player can pick up.
 */
public class Obj_Chest_Key extends Entity {

    
    /**
     * Constructor for the chest key object.
     * Sets its name, sprite image, and collision properties.
     * 
     * @param gp Reference to the GamePanel
     */
    public Obj_Chest_Key(GamePanel gp) {
        super(gp);
        name = "key";
        down1 = setUp("/objects/Chest_Key", gp.tileSize, gp.tileSize);


        collision = true;
    }
}