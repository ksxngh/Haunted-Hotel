package com.group5.object;

import com.group5.entity.Entity;
import com.group5.main.GamePanel;

/**
 * Represents a heart object in the game used to restore the player's health.
 * Extends the Entity class and includes multiple images for full, half, and empty heart states.
 */
public class Obj_Heart extends Entity {

    /**
     * Constructor for the heart object.
     * Initializes its name and loads its sprite images for different health states.
     * 
     * @param gp Reference to the GamePanel
     */
    public Obj_Heart(GamePanel gp) {
        super(gp);

        name = "Heart";
        image = setUp("/objects/heart_full", gp.tileSize, gp.tileSize);
        image2 = setUp("/objects/heart_half", gp.tileSize, gp.tileSize);
        image3 = setUp("/objects/heart_blank", gp.tileSize, gp.tileSize);

    }
}