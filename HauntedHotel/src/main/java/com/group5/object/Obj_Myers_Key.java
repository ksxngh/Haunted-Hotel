package com.group5.object;

import com.group5.entity.Entity;
import com.group5.main.GamePanel;

/**
 * Represents a key object in the game.
 * Extends the Entity class and is a key collected to unlock basement door.
 */
public class Obj_Myers_Key extends Entity {
   //Refactor: lack of documentation and poorly structured
    /** Reference to the main game panel used for rendering and configuration. */
    private final GamePanel gp;

    /**
     * Constructor for the keys for basement door.
     * Initializes its name, type, collision, and sets up its images.
     * 
     * @param gp Reference to the GamePanel
     */


    //Refactor: improve cohesion by separating visual setup from object configuration
    //everything below

    public Obj_Myers_Key(GamePanel gp) {
        super(gp);
        this.gp = gp;
        initKey();
        loadKeySprite();
    }

    // game Logic 
    private void initKey() {
        name = "key";
        type = 3;
        collision = false;
    }

    //seperated for Cohesion
    private void loadKeySprite() {
        down1 = setUp("/objects/key", gp.tileSize, gp.tileSize);
    }
}