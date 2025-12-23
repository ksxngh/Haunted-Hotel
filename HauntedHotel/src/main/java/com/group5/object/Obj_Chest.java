package com.group5.object;

import com.group5.entity.Entity;
import com.group5.main.GamePanel;

/**
 * Represents a chest object in the game.
 * Extends the Entity class and can serve as a container for items such as keys or money.
 */
public class Obj_Chest extends Entity {

    /**
     * Constructor for the chest object.
     * Sets its name and sprite image.
     * 
     * @param gp Reference to the GamePanel
     */
    public Obj_Chest(GamePanel gp) {
        super(gp);
        name = "Chest";
        down1 = setUp("/objects/Left_cuboard", gp.tileSize, gp.tileSize);

    }
}