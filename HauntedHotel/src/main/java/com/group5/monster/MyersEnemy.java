package com.group5.monster;

import com.group5.entity.Entity;
import com.group5.main.GamePanel;
import com.group5.object.Obj_Money;
import java.awt.image.BufferedImage;

/**
 * This is the class for Michael Myers enemy
 * Extends Entity class and has movements and attacking
 */
public class MyersEnemy extends Entity {
    private static final int MONSTER_TYPE = 2;
    private static final int DEFAULT_SPEED = 5;
    private static final int DEFAULT_MAX_LIFE = 5;

    private static final int SOLID_X = 16;
    private static final int SOLID_Y = 32;
    private static final int SOLID_WIDTH = 24;
    private static final int SOLID_HEIGHT = 24;


    GamePanel gp;

    public MyersEnemy(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = MONSTER_TYPE; // monster
        name = "Myers";

        speed = DEFAULT_SPEED;
        maxLife = DEFAULT_MAX_LIFE;
        life = maxLife;

        //refactor: unjustified use of primitives
        solidArea.x = SOLID_X;
        solidArea.y = SOLID_Y;
        solidArea.width = SOLID_WIDTH;
        solidArea.height = SOLID_HEIGHT;
        solidAreaDefultX = solidArea.x;
        solidAreaDefultY = solidArea.y;

        // load sprites
        loadSprites();
    }

    //refactor:removed duplicates by using helper and more cleaner code
    private BufferedImage loadSprite(String path) {
    return setUp(path, gp.tileSize, gp.tileSize);
    }


    //refactor:improve escapsulation by making sprite loading private
   private void loadSprites() {

    up1 = loadSprite("/monster/Myersrun_r1");
    up2 = loadSprite("/monster/Myersrun_r2");
    up3 = up1;

    down1 = loadSprite("/monster/Myersrun_L1");
    down2 = loadSprite("/monster/Myersrun_L2");
    down3 = down1;

    left1 = loadSprite("/monster/Myersrun_L1");
    left2 = loadSprite("/monster/Myersrun_L2");
    left3 = left1;

    right1 = loadSprite("/monster/Myersrun_r1");
    right2 = loadSprite("/monster/Myersrun_r2");
    right3 = right1;

    attackUp1 = loadSprite("/monster/Myers_stand");
    }

    //Refactor
    //made these functions so theres less coupling 
    //was high coupling with game panel before
    private int getPlayerCol() {
    return (gp.player.worldx + gp.player.solidArea.x) / gp.tileSize;
    }

    private int getPlayerRow() {
        return (gp.player.worldy + gp.player.solidArea.y) / gp.tileSize;
    }

    @Override
    public void setAction() {

        //  chase player using pathfinding:
        onPath = true;

        int goalCol = getPlayerCol();
        int goalRow = getPlayerRow();

        goalCol = Math.max(0, Math.min(goalCol, gp.maxWorldCol - 1));
        goalRow = Math.max(0, Math.min(goalRow, gp.maxWorldRow - 1));



        int startCol = (worldx + solidArea.x)/ gp.tileSize;
        int startRow = (worldy + solidArea.y)/ gp.tileSize;

        gp.pFinder.setNode(startCol, startRow, goalCol, goalRow);

        

        if (gp.pFinder.search()) {
            int nextCol = gp.pFinder.pathList.get(0).col;
            int nextRow = gp.pFinder.pathList.get(0).row;

            if (nextRow < startRow) direction = "up";
            else if (nextRow > startRow) direction = "down";
            else if (nextCol < startCol) direction = "left";
            else if (nextCol > startCol) direction = "right";
        }
    }

    @Override
    public void damageReaction() {
        actionLockCounter = 0;

        //refactor
        //removed unsafe string comparisions using equals() insted of ==
        if("up".equals(gp.player.direction)){
            direction = "down";
        }
        if("down".equals(gp.player.direction)){
            direction = "up";
        }
        if("left".equals(gp.player.direction)){
            direction = "right";
        }
        if("right".equals(gp.player.direction)){
            direction = "left";
        }
        
    }

    /**
     * Determines what item the boss drops upon death.
     */
    public void checkDrop() {

        dropItem(new Obj_Money(gp));

    }
}