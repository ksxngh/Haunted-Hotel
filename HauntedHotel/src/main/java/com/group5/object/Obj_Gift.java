package com.group5.object;

import com.group5.entity.Entity;
import com.group5.main.GamePanel;

/**
 * Represents a gift object in the game.
 * Extends the Entity class and can serve as a container for items such as keys or bombs.
 */

    
 public class Obj_Gift extends Entity {

    private static final String NAME = "Gift";
    private static final String SPRITE_PATH = "/objects/gift";
    private GamePanel gp;

    /**
     * Constructor for the gifts hiding a basement key or bombs for christmas level.
     * Initializes its name, collision, and sets up its images.
     * 
     * @param gp Reference to the GamePanel
     */

    public Obj_Gift(GamePanel gp) {
        super(validateGamePanel(gp));
        this.gp = gp;
        initializeStats();
        loadImages();
        configureCollision();
    }

    private static GamePanel validateGamePanel(GamePanel gp) {
        if (gp == null) {
            throw new IllegalArgumentException("GamePanel cannot be null");
        }
        return gp;
    }

    private void initializeStats() {
        name = NAME;
    }

    private void loadImages() {
        down1 = setUp(SPRITE_PATH, gp.tileSize, gp.tileSize);
    }

    private void configureCollision() {
        collision = true;
    }
}