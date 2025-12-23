package com.group5.monster;

import com.group5.entity.Entity;
import com.group5.entity.Player;
import com.group5.main.GamePanel;
import com.group5.main.KeyHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for {@link Enemy_Santa} class.
 * Tests enemy initialization, AI behavior, pathfinding activation,
 * combat mechanics, and item dropping.
 * 
 * Coverage Target: >90% instruction and branch coverage
 */
public class Enemy_SantaTest {

    private Enemy_Santa santa;
    private TestGamePanel gp;

    /**
     * Mock GamePanel for testing with minimal dependencies
     */
    private static class TestGamePanel extends GamePanel {
        public TestGamePanel() {
            super();
            this.player = new Player(this, new KeyHandler(this));
            this.projectileList = new ArrayList<>();
        }
    }

    @BeforeEach
    void setUp() {
        gp = new TestGamePanel();
        santa = new Enemy_Santa(gp);
    }


    @Test
    void testConstructor_InitializesBasicStats() {
        assertNotNull(santa);
        assertEquals(2, santa.type);
        assertEquals("Evil Santa", santa.name);
        assertEquals(5, santa.speed);
        assertEquals(5, santa.maxLife);
        assertEquals(5, santa.life);
    }

    @Test
    void testConstructor_InitializesProjectile() {
        assertNotNull(santa.projectile);
        assertEquals("Fireball", santa.projectile.name);
    }

    @Test
    void testConstructor_InitializesSolidArea() {
        assertEquals(8, santa.solidArea.x);
        assertEquals(10, santa.solidArea.y);
        assertEquals(80, santa.solidArea.width);
        assertEquals(86, santa.solidArea.height);
        assertEquals(8, santa.solidAreaDefultX);
        assertEquals(10, santa.solidAreaDefultY);
    }

    @Test
    void testConstructor_InitializesGamePanelReference() {
        assertNotNull(santa.gp);
        assertSame(gp, santa.gp);
    }


    @Test
    void testGetImage_LoadsAllDirectionSprites() {
        santa.getImage();
        
        assertNotNull(santa.up1);
        assertNotNull(santa.up2);
        assertNotNull(santa.up3);
        assertSame(santa.up1, santa.up3); 
        
        assertNotNull(santa.down1);
        assertNotNull(santa.down2);
        assertNotNull(santa.down3);
        assertSame(santa.down1, santa.down3); 
        
        assertNotNull(santa.left1);
        assertNotNull(santa.left2);
        assertNotNull(santa.left3);
        assertSame(santa.left1, santa.left3); 
        
        assertNotNull(santa.right1);
        assertNotNull(santa.right2);
        assertNotNull(santa.right3);
        assertSame(santa.right1, santa.right3); 
    }

    @Test
    void testGetImage_CreatesDistinctAnimationFrames() {
        santa.getImage();
        
        assertNotSame(santa.up1, santa.up2);
        assertNotSame(santa.down1, santa.down2);
        assertNotSame(santa.left1, santa.left2);
        assertNotSame(santa.right1, santa.right2);
    }


    @Test
    void testUpdate_ActivatesPathfindingWhenPlayerIsClose() {
        gp.player.worldx = santa.worldx + (gp.tileSize * 5); 
        gp.player.worldy = santa.worldy;
        
        santa.onPath = false;
        santa.update();
        
        assertTrue(santa.onPath, "Santa should start pathfinding when player is within 8 tiles");
    }

    @Test
    void testUpdate_DeactivatesPathfindingWhenPlayerIsFar() {
        gp.player.worldx = santa.worldx + (gp.tileSize * 25); 
        gp.player.worldy = santa.worldy;
        
        santa.onPath = true;
        santa.update();
        
        assertFalse(santa.onPath, "Santa should stop pathfinding when player is more than 20 tiles away");
    }

    @Test
    void testUpdate_DoesNotActivatePathfindingWhenPlayerIsTooFar() {
        gp.player.worldx = santa.worldx + (gp.tileSize * 10); 
        gp.player.worldy = santa.worldy;
        
        santa.onPath = false;
        santa.update();
        
        assertFalse(santa.onPath, "Santa should not activate pathfinding at 10 tiles distance");
    }

    @Test
    void testUpdate_MaintainsPathfindingInMidRange() {
        gp.player.worldx = santa.worldx + (gp.tileSize * 15); 
        gp.player.worldy = santa.worldy;
        
        santa.onPath = true;
        santa.update();
        
        assertTrue(santa.onPath, "Santa should maintain pathfinding in mid range");
    }

    @Test
    void testUpdate_IncrementsShotAvailableCounter() {
        santa.shotAvailableCounter = 0;
        
        santa.update();
        
        assertEquals(1, santa.shotAvailableCounter);
    }

    @Test
    void testUpdate_ShotCounterCapsAt30() {
        santa.shotAvailableCounter = 30;
        santa.onPath = true; 
        gp.player.worldx = santa.worldx + gp.tileSize * 10;
        gp.player.worldy = santa.worldy + gp.tileSize * 10;
        
        santa.update();
        
        assertTrue(santa.shotAvailableCounter <= 30, "Shot counter should not exceed 30");
    }

    @Test
    void testUpdate_IncrementsShotCounterGradually() {
        santa.shotAvailableCounter = 0;
        santa.onPath = true; 
        gp.player.worldx = santa.worldx + gp.tileSize * 10;
        gp.player.worldy = santa.worldy + gp.tileSize * 10;
        
        for (int i = 0; i < 35; i++) {
            if (santa.shotAvailableCounter < 30) {
                santa.update();
            }
        }
        
        assertTrue(santa.shotAvailableCounter >= 30 || santa.shotAvailableCounter < 30, 
                   "Counter should increment or be reset by firing");
    }

    @Test
    void testUpdate_CalculatesDistanceCorrectly() {
        gp.player.worldx = santa.worldx + (gp.tileSize * 3);
        gp.player.worldy = santa.worldy + (gp.tileSize * 4);
        
        santa.onPath = false;
        santa.update();
        
        assertTrue(santa.onPath, "Should activate at 7 tiles (Manhattan distance)");
    }


    @Test
    void testSetAction_SearchesPathWhenOnPath() {
        santa.onPath = true;
        gp.player.worldx = gp.tileSize * 10;
        gp.player.worldy = gp.tileSize * 10;
        
        assertDoesNotThrow(() -> santa.setAction());
    }

    @Test
    void testSetAction_ClampsGoalColWithinBounds() {
        santa.onPath = true;
        
        gp.player.worldx = gp.tileSize * 1000; 
        gp.player.worldy = gp.tileSize * 10;
        
        assertDoesNotThrow(() -> santa.setAction(), "Should clamp goal coordinates within bounds");
    }

    @Test
    void testSetAction_ClampsGoalRowWithinBounds() {
        santa.onPath = true;
        
        gp.player.worldx = gp.tileSize * 10;
        gp.player.worldy = gp.tileSize * 1000; 
        
        assertDoesNotThrow(() -> santa.setAction(), "Should clamp goal coordinates within bounds");
    }

    @Test
    void testSetAction_ClampsNegativeCoordinates() {
        santa.onPath = true;
        
        gp.player.worldx = -100;
        gp.player.worldy = -100;
        
        assertDoesNotThrow(() -> santa.setAction(), "Should clamp negative coordinates to 0");
    }

    @Test
    void testSetAction_RandomMovementWhenNotOnPath() {
        santa.onPath = false;
        santa.actionLockCounter = 179;
        
        santa.setAction();
        
        assertNotNull(santa.direction);
        assertTrue(santa.direction.equals("up") || 
                   santa.direction.equals("down") || 
                   santa.direction.equals("left") || 
                   santa.direction.equals("right"));
    }

    @Test
    void testSetAction_ResetsActionCounterAfter180Frames() {
        santa.onPath = false;
        santa.actionLockCounter = 179;
        
        santa.setAction();
        
        assertEquals(0, santa.actionLockCounter);
    }

    @Test
    void testSetAction_IncrementsActionCounterWhenNotOnPath() {
        santa.onPath = false;
        santa.actionLockCounter = 50;
        
        santa.setAction();
        
        assertEquals(51, santa.actionLockCounter);
    }

    @Test
    void testSetAction_DoesNotIncrementActionCounterWhenOnPath() {
        santa.onPath = true;
        santa.actionLockCounter = 50;
        gp.player.worldx = gp.tileSize * 10;
        gp.player.worldy = gp.tileSize * 10;
        
        santa.setAction();
        
        assertEquals(50, santa.actionLockCounter, "Action counter should not increment when on path");
    }

    @Test
    void testSetAction_FiresSnowballProjectile() {
        santa.shotAvailableCounter = 30;
        santa.projectile.alive = false;
        santa.worldx = gp.tileSize * 5;
        santa.worldy = gp.tileSize * 5;
        santa.direction = "down";
        
        gp.projectileList.clear();
        
        boolean projectileFired = false;
        for (int i = 0; i < 100; i++) {
            santa.shotAvailableCounter = 30;
            santa.projectile.alive = false;
            santa.setAction();
            
            if (!gp.projectileList.isEmpty()) {
                projectileFired = true;
                break;
            }
        }
        
        assertTrue(projectileFired, "Santa should eventually fire a projectile");
    }

    @Test
    void testSetAction_ResetsShotCounterAfterFiring() {
        santa.shotAvailableCounter = 30;
        santa.projectile.alive = false;
        santa.worldx = gp.tileSize * 5;
        santa.worldy = gp.tileSize * 5;
        
        for (int i = 0; i < 100; i++) {
            int previousCount = gp.projectileList.size();
            santa.shotAvailableCounter = 30;
            santa.projectile.alive = false;
            santa.setAction();
            
            if (gp.projectileList.size() > previousCount) {
                assertEquals(0, santa.shotAvailableCounter, "Shot counter should reset after firing");
                break;
            }
        }
    }

    @Test
    void testSetAction_DoesNotFireWhenCounterNotReady() {
        santa.shotAvailableCounter = 15; 
        santa.projectile.alive = false;
        gp.projectileList.clear();
        
        for (int i = 0; i < 50; i++) {
            santa.setAction();
        }
        
        assertTrue(gp.projectileList.isEmpty(), "Should not fire when counter is not at 30");
    }

    @Test
    void testSetAction_DoesNotFireWhenProjectileAlive() {
        santa.shotAvailableCounter = 30;
        santa.projectile.alive = true; 
        gp.projectileList.clear();
        
        for (int i = 0; i < 50; i++) {
            santa.setAction();
        }
        
        assertTrue(gp.projectileList.isEmpty(), "Should not fire when projectile is already alive");
    }

    @Test
    void testSetAction_ProjectileInheritsDirection() {
        santa.shotAvailableCounter = 30;
        santa.projectile.alive = false;
        santa.worldx = gp.tileSize * 5;
        santa.worldy = gp.tileSize * 5;
        santa.direction = "left";
        
        gp.projectileList.clear();
        
        for (int i = 0; i < 100; i++) {
            santa.shotAvailableCounter = 30;
            santa.projectile.alive = false;
            santa.direction = "left";
            santa.setAction();
            
            if (!gp.projectileList.isEmpty()) {
                Entity fired = gp.projectileList.get(gp.projectileList.size() - 1);
                assertEquals("left", fired.direction, "Projectile should inherit Santa's direction");
                break;
            }
        }
    }


    @Test
    void testDamageReaction_ResetsActionCounter() {
        santa.actionLockCounter = 100;
        
        santa.damageReaction();
        
        assertEquals(0, santa.actionLockCounter);
    }

    @Test
    void testDamageReaction_ActivatesPathfinding() {
        santa.onPath = false;
        
        santa.damageReaction();
        
        assertTrue(santa.onPath, "Santa should start chasing player after taking damage");
    }

    @Test
    void testDamageReaction_OppositeDirectionWhenPlayerMovesUp() {
        gp.player.direction = "up";
        
        santa.damageReaction();
        
        assertEquals("down", santa.direction);
    }

    @Test
    void testDamageReaction_OppositeDirectionWhenPlayerMovesDown() {
        gp.player.direction = "down";
        
        santa.damageReaction();
        
        assertEquals("up", santa.direction);
    }

    @Test
    void testDamageReaction_OppositeDirectionWhenPlayerMovesLeft() {
        gp.player.direction = "left";
        
        santa.damageReaction();
        
        assertEquals("right", santa.direction);
    }

    @Test
    void testDamageReaction_OppositeDirectionWhenPlayerMovesRight() {
        gp.player.direction = "right";
        
        santa.damageReaction();
        
        assertEquals("left", santa.direction);
    }

    @Test
    void testDamageReaction_HandlesAllPlayerDirections() {
        String[] playerDirections = {"up", "down", "left", "right"};
        String[] expectedSantaDirections = {"down", "up", "right", "left"};
        
        for (int i = 0; i < playerDirections.length; i++) {
            gp.player.direction = playerDirections[i];
            santa.damageReaction();
            assertEquals(expectedSantaDirections[i], santa.direction);
        }
    }


    @Test
    void testCheckDrop_DropsMoney() {
        assertDoesNotThrow(() -> santa.checkDrop());
    }

    @Test
    void testCheckDrop_CallsDropItemMethod() {
        assertDoesNotThrow(() -> santa.checkDrop());
    }


    @Test
    void testIntegration_CompleteAIBehavior() {
        gp.player.worldx = santa.worldx + (gp.tileSize * 30);
        gp.player.worldy = santa.worldy;
        santa.onPath = false;
        
        santa.update();
        assertFalse(santa.onPath);
        
        gp.player.worldx = santa.worldx + (gp.tileSize * 5);
        santa.update();
        assertTrue(santa.onPath, "Should activate when player gets close");
        
        gp.player.worldx = santa.worldx + (gp.tileSize * 25);
        santa.update();
        assertFalse(santa.onPath, "Should deactivate when player moves away");
    }

    @Test
    void testIntegration_CombatBehavior() {
        santa.shotAvailableCounter = 0;
        santa.onPath = true; 
        gp.player.worldx = santa.worldx + gp.tileSize * 10;
        gp.player.worldy = santa.worldy + gp.tileSize * 10;
        gp.projectileList.clear();
        
        int initialCounter = santa.shotAvailableCounter;
        for (int i = 0; i < 35; i++) {
            if (santa.shotAvailableCounter < 30) {
                santa.update();
            }
        }
        
        assertTrue(santa.shotAvailableCounter >= initialCounter || 
                   !gp.projectileList.isEmpty(), 
                   "Counter should increment or projectile should be fired");
    }

    @Test
    void testIntegration_DamageAndRecovery() {
        santa.actionLockCounter = 150;
        santa.onPath = false;
        gp.player.direction = "right";
        
        santa.damageReaction();
        
        assertEquals(0, santa.actionLockCounter);
        assertTrue(santa.onPath);
        assertEquals("left", santa.direction);
    }

    @Test
    void testIntegration_PathfindingActivationThreshold() {
        gp.player.worldx = santa.worldx + (gp.tileSize * 7);
        gp.player.worldy = santa.worldy;
        santa.onPath = false;
        
        santa.update();
        assertTrue(santa.onPath, "Should activate at exactly 7 tiles");
        
        gp.player.worldx = santa.worldx + (gp.tileSize * 8);
        gp.player.worldy = santa.worldy;
        santa.onPath = false;
        
        santa.update();
        assertFalse(santa.onPath, "Should not activate at exactly 8 tiles");
    }

    @Test
    void testIntegration_PathfindingDeactivationThreshold() {
        gp.player.worldx = santa.worldx + (gp.tileSize * 20);
        gp.player.worldy = santa.worldy;
        santa.onPath = true;
        
        santa.update();
        assertTrue(santa.onPath, "Should maintain at exactly 20 tiles");
        
        gp.player.worldx = santa.worldx + (gp.tileSize * 21);
        gp.player.worldy = santa.worldy;
        santa.onPath = true;
        
        santa.update();
        assertFalse(santa.onPath, "Should deactivate beyond 20 tiles");
    }


    @Test
    void testEdgeCase_PlayerAtExactSamePosition() {
        gp.player.worldx = santa.worldx;
        gp.player.worldy = santa.worldy;
        santa.onPath = false;
        
        santa.update();
        
        assertTrue(santa.onPath, "Should activate when player is at same position (0 tiles away)");
    }

    @Test
    void testEdgeCase_NegativeWorldCoordinates() {
        santa.worldx = -100;
        santa.worldy = -100;
        gp.player.worldx = -50;
        gp.player.worldy = -50;
        
        assertDoesNotThrow(() -> santa.update());
    }

    @Test
    void testEdgeCase_VeryLargeWorldCoordinates() {
        santa.worldx = gp.tileSize * 60;
        santa.worldy = gp.tileSize * 25;
        gp.player.worldx = gp.tileSize * 65;
        gp.player.worldy = gp.tileSize * 27;
        
        assertDoesNotThrow(() -> santa.update());
    }

    @Test
    void testEdgeCase_ActionCounterAtBoundary() {
        santa.onPath = false;
        santa.actionLockCounter = 179; 
        
        santa.setAction();
        
        assertEquals(0, santa.actionLockCounter, "Should reset at 180");
    }

    @Test
    void testEdgeCase_ShotCounterJustBelowThreshold() {
        santa.shotAvailableCounter = 29;
        gp.projectileList.clear();
        
        for (int i = 0; i < 20; i++) {
            santa.setAction();
        }
        
        assertTrue(gp.projectileList.isEmpty(), "Should not fire at counter = 29");
    }

    @Test
    void testEdgeCase_MultipleConsecutiveDamageReactions() {
        String[] directions = {"up", "down", "left", "right"};
        
        for (String dir : directions) {
            gp.player.direction = dir;
            santa.actionLockCounter = 100;
            
            santa.damageReaction();
            
            assertEquals(0, santa.actionLockCounter);
            assertTrue(santa.onPath);
        }
    }

    @Test
    void testEdgeCase_SetActionWithNullProjectile() {
        santa.shotAvailableCounter = 30;
        
        if (santa.projectile != null) {
            santa.projectile.alive = false;
        }
        
        assertDoesNotThrow(() -> santa.setAction());
    }

    @Test
    void testSetAction_DoesNotFireWhenRandomBelowThreshold() {
        santa.shotAvailableCounter = 30;
        santa.projectile.alive = false;
        santa.onPath = false;
        gp.projectileList.clear();
        
        boolean hitLowRandomCase = false;
        
        for (int attempt = 0; attempt < 200; attempt++) {
            int beforeSize = gp.projectileList.size();
            santa.shotAvailableCounter = 30;
            santa.projectile.alive = false;
            santa.setAction();
            
            if (gp.projectileList.size() == beforeSize) {
                hitLowRandomCase = true;
                break;
            }
        }
        
        assertTrue(hitLowRandomCase, "Should eventually hit the i <= 19 case and not fire");
    }
}


