# ProjectTeam5 - Haunted Hotel Game

## Team Members
- Singh, Karanveer - ksa283@sfu.ca
- Singh, Prabhjot - psa167@sfu.ca  
- Mondair, Amar - ama378@sfu.ca
- Khubbar, Kiran - kkk246@sfu.ca

## Project Description
Haunted Hotel is a 2D tile-based adventure game developed in Java. Players navigate through multiple levels including a lobby, Myers basement, Christmas/Santa level, and final boss room. The game features:
- Enemy AI with A* pathfinding
- Combat system with projectiles
- Collectible items (keys, money, gifts)
- Progressive boss battles
- Event-driven level transitions

## Prerequisites
- Java 17 or higher
- Maven 3.x
- JUnit 5 (Jupiter 5.10.0) - included in pom.xml
- JaCoCo 0.8.7 - for coverage reporting

## Project Structure
```
.
└── ProjectTeam5
    ├── Design
    │   ├── Haunted Hotel.pdf
    │   ├── README.md
    │   ├── UML CLass Diagram.pdf
    │   ├── Usecase.pdf
    │   ├── Wireframe - Game.pdf
    │   ├── Wireframe - MainMenu.pdf
    │   └── Wireframe - Options.pdf
    ├── Documents
    │   ├── Phase2Report.pdf
    │   ├── Phase3Report.pdf
    │   └── Phase4Report.pdf
    ├── HauntedHotel
    │   ├── artifacts
    │   │   ├── HauntedHotel-1.0-SNAPSHOT.jar
    │   │   └── HauntedHotel-1.0-SNAPSHOT-javadoc.jar
    │   ├── pom.xml
    │   ├── src
    │   │   ├── main
    │   │   │   ├── java
    │   │   │   │   └── com
    │   │   │   │       └── group5
    │   │   │   │           ├── ai
    │   │   │   │           │   ├── Node.java
    │   │   │   │           │   └── Pathfinder.java
    │   │   │   │           ├── entity
    │   │   │   │           │   ├── Entity.java
    │   │   │   │           │   ├── NPC.java
    │   │   │   │           │   ├── Player.java
    │   │   │   │           │   └── Projectile.java
    │   │   │   │           ├── main
    │   │   │   │           │   ├── AssetSetter.java
    │   │   │   │           │   ├── CollisionChecker.java
    │   │   │   │           │   ├── EventHandler.java
    │   │   │   │           │   ├── EventRect.java
    │   │   │   │           │   ├── GamePanel.java
    │   │   │   │           │   ├── KeyHandler.java
    │   │   │   │           │   ├── Main.java
    │   │   │   │           │   ├── Sound.java
    │   │   │   │           │   ├── UI.java
    │   │   │   │           │   └── UtilityTool.java
    │   │   │   │           ├── monster
    │   │   │   │           │   ├── Enemy_Santa.java
    │   │   │   │           │   ├── Mon_Boss.java
    │   │   │   │           │   └── MyersEnemy.java
    │   │   │   │           ├── object
    │   │   │   │           │   ├── Obj_Chest.java
    │   │   │   │           │   ├── Obj_Chest_Key.java
    │   │   │   │           │   ├── Obj_Door.java
    │   │   │   │           │   ├── Obj_ElectricBall.java
    │   │   │   │           │   ├── Obj_Fireball.java
    │   │   │   │           │   ├── Obj_Gift.java
    │   │   │   │           │   ├── Obj_Heart.java
    │   │   │   │           │   ├── Obj_Money.java
    │   │   │   │           │   ├── Obj_Myers_Key.java
    │   │   │   │           │   └── Obj_Snowball.java
    │   │   │   │           └── tile
    │   │   │   │               ├── Tile.java
    │   │   │   │               └── TileManager.java
    │   │   │   └── resources
    │   │   │       ├── maps
    │   │   │       │   ├── Basement.txt
    │   │   │       │   ├── ChristmasMap.txt
    │   │   │       │   ├── Lobby.txt
    │   │   │       │   └── worldmap.txt
    │   │   │       ├── monster
    │   │   │       │   ├── Boss-down1.png
    │   │   │       │   ├── Boss-down2.png
    │   │   │       │   ├── Boss-down3.png
    │   │   │       │   ├── Boss-left1.png
    │   │   │       │   ├── Boss-left2.png
    │   │   │       │   ├── Boss-left3.png
    │   │   │       │   ├── Boss-right1.png
    │   │   │       │   ├── Boss-right2.png
    │   │   │       │   ├── Boss-right3.png
    │   │   │       │   ├── Boss-up1.png
    │   │   │       │   ├── Boss-up2.png
    │   │   │       │   ├── Boss-up3.png
    │   │   │       │   ├── down1.png
    │   │   │       │   ├── down2.png
    │   │   │       │   ├── left1.png
    │   │   │       │   ├── left2.png
    │   │   │       │   ├── Myersrun_L1.png
    │   │   │       │   ├── Myersrun_L2.png
    │   │   │       │   ├── Myersrun_r1.png
    │   │   │       │   ├── Myersrun_r2.png
    │   │   │       │   ├── Myers_stand2.png
    │   │   │       │   ├── Myers_stand.png
    │   │   │       │   ├── right1.png
    │   │   │       │   ├── right2.png
    │   │   │       │   ├── up1.png
    │   │   │       │   └── up2.png
    │   │   │       ├── npc
    │   │   │       │   ├── npc1.png
    │   │   │       │   └── npc2.png
    │   │   │       ├── objects
    │   │   │       │   ├── Chest_Key.png
    │   │   │       │   ├── door2.png
    │   │   │       │   ├── Door.png
    │   │   │       │   ├── gift.png
    │   │   │       │   ├── heart_blank.png
    │   │   │       │   ├── heart_full.png
    │   │   │       │   ├── heart_half.png
    │   │   │       │   ├── key.png
    │   │   │       │   ├── Left_cuboard.png
    │   │   │       │   ├── Money.png
    │   │   │       │   ├── Right_cuboard.png
    │   │   │       │   └── snowball.png
    │   │   │       ├── player
    │   │   │       │   ├── Player276-down1.png
    │   │   │       │   ├── Player276-down2.png
    │   │   │       │   ├── Player276-down3.png
    │   │   │       │   ├── Player276-left1.png
    │   │   │       │   ├── Player276-left2.png
    │   │   │       │   ├── Player276-left3.png
    │   │   │       │   ├── Player276-right1.png
    │   │   │       │   ├── Player276-right2.png
    │   │   │       │   ├── Player276-right3.png
    │   │   │       │   ├── Player276-up1.png
    │   │   │       │   ├── Player276-up2.png
    │   │   │       │   ├── Player276-up3.png
    │   │   │       │   ├── PlayerAttack-down1.png
    │   │   │       │   ├── PlayerAttack-down2.png
    │   │   │       │   ├── PlayerAttack-down3.png
    │   │   │       │   ├── PlayerAttack-left1.png
    │   │   │       │   ├── PlayerAttack-left2.png
    │   │   │       │   ├── PlayerAttack-left3.png
    │   │   │       │   ├── PlayerAttack-right1.png
    │   │   │       │   ├── PlayerAttack-right2.png
    │   │   │       │   ├── PlayerAttack-right3.png
    │   │   │       │   ├── PlayerAttack-up1.png
    │   │   │       │   ├── PlayerAttack-up2.png
    │   │   │       │   └── PlayerAttack-up3.png
    │   │   │       ├── projectile
    │   │   │       │   ├── ElectricBall.png
    │   │   │       │   └── Fireball.png
    │   │   │       ├── sound
    │   │   │       │   ├── BackgroundB.wav
    │   │   │       │   ├── Chest.wav
    │   │   │       │   └── Key.wav
    │   │   │       └── tiles
    │   │   │           ├── Black.png
    │   │   │           ├── BOMB_TILE.png
    │   │   │           ├── BottomLeftRug.png
    │   │   │           ├── BottomRightRug.png
    │   │   │           ├── BrickG.png
    │   │   │           ├── CandyCane.png
    │   │   │           ├── chimney.png
    │   │   │           ├── Cobblestone.png
    │   │   │           ├── Corner_Brick.png
    │   │   │           ├── deadPlant.png
    │   │   │           ├── Desk1.png
    │   │   │           ├── Desk.png
    │   │   │           ├── door.png
    │   │   │           ├── DoorT.png
    │   │   │           ├── EvilP.png
    │   │   │           ├── Fire.png
    │   │   │           ├── floor.png
    │   │   │           ├── Ice.png
    │   │   │           ├── LobbyFloor.png
    │   │   │           ├── LobbyWall.png
    │   │   │           ├── NormalP.png
    │   │   │           ├── RedBlock.png
    │   │   │           ├── snowing.png
    │   │   │           ├── Snow.png
    │   │   │           ├── Soil.png
    │   │   │           ├── stool.png
    │   │   │           ├── TimeP.png
    │   │   │           ├── TopLeftRug.png
    │   │   │           ├── TopRightRug.png
    │   │   │           ├── Torch.png
    │   │   │           ├── wall.png
    │   │   │           ├── woodBlock.png
    │   │   │           ├── WoodDoor.png
    │   │   │           └── Wood.png
    │   │   └── test
    │   │       └── java
    │   │           └── com
    │   │               └── group5
    │   │                   ├── ai
    │   │                   │   ├── NodeTest.java
    │   │                   │   └── PathfinderTest.java
    │   │                   ├── AppTest.java
    │   │                   ├── entity
    │   │                   │   ├── EntityTest.java
    │   │                   │   ├── NPCTest.java
    │   │                   │   ├── PlayerTest.java
    │   │                   │   └── ProjectileTest.java
    │   │                   ├── integration
    │   │                   │   ├── GamePanelObjectInitializationTest.java
    │   │                   │   ├── PlayerKeyHandlerIntegrationTest.java
    │   │                   │   └── PlayerTileCollisionIntegrationTest.java
    │   │                   ├── main
    │   │                   │   ├── AssetSetterTest.java
    │   │                   │   ├── CollisionCheckerTest.java
    │   │                   │   ├── EventHandlerTest.java
    │   │                   │   ├── KeyHandlerTest.java
    │   │                   │   ├── SoundTest.java
    │   │                   │   ├── TestGamePanel.java
    │   │                   │   ├── TileManagerTest.java
    │   │                   │   ├── UITest.java
    │   │                   │   └── UtilityToolTest.java
    │   │                   ├── MonBossTest.java
    │   │                   ├── monster
    │   │                   │   ├── Enemy_SantaTest.java
    │   │                   │   └── MyersEnemyTest.java
    │   │                   └── TileManagerTest.java
    │   └── target
    │       ├── classes
    │       │   ├── com
    │       │   │   └── group5
    │       │   │       ├── ai
    │       │   │       │   ├── Node.class
    │       │   │       │   └── Pathfinder.class
    │       │   │       ├── entity
    │       │   │       │   ├── Entity.class
    │       │   │       │   ├── NPC.class
    │       │   │       │   ├── Player.class
    │       │   │       │   └── Projectile.class
    │       │   │       ├── main
    │       │   │       │   ├── AssetSetter.class
    │       │   │       │   ├── CollisionChecker.class
    │       │   │       │   ├── EventHandler.class
    │       │   │       │   ├── EventRect.class
    │       │   │       │   ├── GamePanel$1.class
    │       │   │       │   ├── GamePanel.class
    │       │   │       │   ├── KeyHandler.class
    │       │   │       │   ├── Main.class
    │       │   │       │   ├── Sound.class
    │       │   │       │   ├── UI.class
    │       │   │       │   └── UtilityTool.class
    │       │   │       ├── monster
    │       │   │       │   ├── Enemy_Santa.class
    │       │   │       │   ├── Mon_Boss.class
    │       │   │       │   └── MyersEnemy.class
    │       │   │       ├── object
    │       │   │       │   ├── Obj_Chest.class
    │       │   │       │   ├── Obj_Chest_Key.class
    │       │   │       │   ├── Obj_Door.class
    │       │   │       │   ├── Obj_ElectricBall.class
    │       │   │       │   ├── Obj_Fireball.class
    │       │   │       │   ├── Obj_Gift.class
    │       │   │       │   ├── Obj_Heart.class
    │       │   │       │   ├── Obj_Money.class
    │       │   │       │   ├── Obj_Myers_Key.class
    │       │   │       │   └── Obj_Snowball.class
    │       │   │       └── tile
    │       │   │           ├── Tile.class
    │       │   │           └── TileManager.class
    │       │   ├── maps
    │       │   │   ├── Basement.txt
    │       │   │   ├── ChristmasMap.txt
    │       │   │   ├── Lobby.txt
    │       │   │   └── worldmap.txt
    │       │   ├── monster
    │       │   │   ├── Boss-down1.png
    │       │   │   ├── Boss-down2.png
    │       │   │   ├── Boss-down3.png
    │       │   │   ├── Boss-left1.png
    │       │   │   ├── Boss-left2.png
    │       │   │   ├── Boss-left3.png
    │       │   │   ├── Boss-right1.png
    │       │   │   ├── Boss-right2.png
    │       │   │   ├── Boss-right3.png
    │       │   │   ├── Boss-up1.png
    │       │   │   ├── Boss-up2.png
    │       │   │   ├── Boss-up3.png
    │       │   │   ├── down1.png
    │       │   │   ├── down2.png
    │       │   │   ├── left1.png
    │       │   │   ├── left2.png
    │       │   │   ├── Myersrun_L1.png
    │       │   │   ├── Myersrun_L2.png
    │       │   │   ├── Myersrun_r1.png
    │       │   │   ├── Myersrun_r2.png
    │       │   │   ├── Myers_stand2.png
    │       │   │   ├── Myers_stand.png
    │       │   │   ├── right1.png
    │       │   │   ├── right2.png
    │       │   │   ├── up1.png
    │       │   │   └── up2.png
    │       │   ├── npc
    │       │   │   ├── npc1.png
    │       │   │   └── npc2.png
    │       │   ├── objects
    │       │   │   ├── Chest_Key.png
    │       │   │   ├── door2.png
    │       │   │   ├── Door.png
    │       │   │   ├── gift.png
    │       │   │   ├── heart_blank.png
    │       │   │   ├── heart_full.png
    │       │   │   ├── heart_half.png
    │       │   │   ├── key.png
    │       │   │   ├── Left_cuboard.png
    │       │   │   ├── Money.png
    │       │   │   ├── Right_cuboard.png
    │       │   │   └── snowball.png
    │       │   ├── player
    │       │   │   ├── Player276-down1.png
    │       │   │   ├── Player276-down2.png
    │       │   │   ├── Player276-down3.png
    │       │   │   ├── Player276-left1.png
    │       │   │   ├── Player276-left2.png
    │       │   │   ├── Player276-left3.png
    │       │   │   ├── Player276-right1.png
    │       │   │   ├── Player276-right2.png
    │       │   │   ├── Player276-right3.png
    │       │   │   ├── Player276-up1.png
    │       │   │   ├── Player276-up2.png
    │       │   │   ├── Player276-up3.png
    │       │   │   ├── PlayerAttack-down1.png
    │       │   │   ├── PlayerAttack-down2.png
    │       │   │   ├── PlayerAttack-down3.png
    │       │   │   ├── PlayerAttack-left1.png
    │       │   │   ├── PlayerAttack-left2.png
    │       │   │   ├── PlayerAttack-left3.png
    │       │   │   ├── PlayerAttack-right1.png
    │       │   │   ├── PlayerAttack-right2.png
    │       │   │   ├── PlayerAttack-right3.png
    │       │   │   ├── PlayerAttack-up1.png
    │       │   │   ├── PlayerAttack-up2.png
    │       │   │   └── PlayerAttack-up3.png
    │       │   ├── projectile
    │       │   │   ├── ElectricBall.png
    │       │   │   └── Fireball.png
    │       │   ├── sound
    │       │   │   ├── BackgroundB.wav
    │       │   │   ├── Chest.wav
    │       │   │   └── Key.wav
    │       │   └── tiles
    │       │       ├── Black.png
    │       │       ├── BOMB_TILE.png
    │       │       ├── BottomLeftRug.png
    │       │       ├── BottomRightRug.png
    │       │       ├── BrickG.png
    │       │       ├── CandyCane.png
    │       │       ├── chimney.png
    │       │       ├── Cobblestone.png
    │       │       ├── Corner_Brick.png
    │       │       ├── deadPlant.png
    │       │       ├── Desk1.png
    │       │       ├── Desk.png
    │       │       ├── door.png
    │       │       ├── DoorT.png
    │       │       ├── EvilP.png
    │       │       ├── Fire.png
    │       │       ├── floor.png
    │       │       ├── Ice.png
    │       │       ├── LobbyFloor.png
    │       │       ├── LobbyWall.png
    │       │       ├── NormalP.png
    │       │       ├── RedBlock.png
    │       │       ├── snowing.png
    │       │       ├── Snow.png
    │       │       ├── Soil.png
    │       │       ├── stool.png
    │       │       ├── TimeP.png
    │       │       ├── TopLeftRug.png
    │       │       ├── TopRightRug.png
    │       │       ├── Torch.png
    │       │       ├── wall.png
    │       │       ├── woodBlock.png
    │       │       ├── WoodDoor.png
    │       │       └── Wood.png
    │       ├── generated-sources
    │       │   └── annotations
    │       └── maven-status
    │           └── maven-compiler-plugin
    │               └── compile
    │                   └── default-compile
    │                       ├── createdFiles.lst
    │                       └── inputFiles.lst
    └── README.md
```

## Building the Project

### Compile
```bash
cd HauntedHotel
mvn clean compile
```

### Run the Game
```bash
mvn exec:java
```

Or compile and run together:
```bash
mvn clean compile exec:java
```

## Running Tests

### Run All Tests
```bash
mvn test
```
## Creating Jar file 
```bash
mvn clean package
```

The JAR file is generated at: `HauntedHotel/target/HauntedHotel-1.0-SNAPSHOT.jar`

### Run the JAR file
```bash
cd HauntedHotel
java -jar target/HauntedHotel-1.0-SNAPSHOT.jar
```

## Creating Javadocs
```bash
mvn javadoc:javadoc
```

The javadoc is generated at: `HauntedHotel/target/reports/apidocs/index.html`

Open this file in a web browser to view the complete API documentation.

### Run Specific Test Class
```bash
mvn test -Dtest=PathfinderTest
mvn test -Dtest=NodeTest
mvn test -Dtest=Enemy_SantaTest
mvn test -Dtest=PlayerTest
```

### Run With Coverage Report
```bash
mvn clean test
```

After running tests, open the coverage report:
```bash
# The report is generated at:
target/site/jacoco/index.html
```

Open `target/site/jacoco/index.html` in a web browser to view detailed coverage metrics.

## Game Controls
- **WASD**: Move player
- **Enter**: Interact with objects/NPCs
- **Space**: Attack/Fire projectiles
- **ESC**: Pause game
- **T**: Close Dialogue Box
- **E**: Attack
- **Space Bar**: Shoot Electric Ball
- **C**: See Inventory
## How to Play - Game Walkthrough

### Level 0: Lobby (Starting Point)
The player spawns in the **Lobby** (Level 0). This is your central hub for accessing all other levels.

**Objectives:**
1. Approach the **NPC** in the lobby and interact to collect **1 key**
2. Head to the **door on the left wall** to enter Level 1

### Level 1: Myers Level
After using the left door, you will teleport to the **Myers Level**.

**Objectives:**
1. Defeat the **Myers enemy** while avoiding his attacks
2. Collect the **key** that appears on the map after defeating Myers
3. Return to the **door at the spawn point** to teleport back to the Lobby

### Level 2: Evil Santa Level (Christmas Map)
From the Lobby, proceed to the **door on the right wall** to access the **Evil Santa Level**.

**Objectives:**
1. Collect **3 good gifts** scattered around the map
   - ⚠️ **Warning**: Avoid collecting **bad gifts** (indices 4-8 on map 2) as they will reduce your health
2. Defeat **Evil Santa** while dodging his **snowball projectiles**
3. After defeating Evil Santa, return to the **door at the spawn point** to go back to the Lobby

### Final Level: Basement (Boss Fight)
From the Lobby, head to the **door at the top** to enter the **Basement** - the final level.

**Objectives:**
1. Face the **Final Boss** who has **twice the health** of previous enemies
2. Avoid the boss's **fireball projectiles**
3. Stay away from **fire tiles** that deal damage
4. Defeat the Final Boss to **complete the game**

**Victory Condition:** The game ends as soon as the Final Boss is defeated!

### Tips for Success
- Collect keys strategically - you need them to unlock doors between levels
- Monitor your health carefully, especially in Level 2 with bad gifts
- Use the Lobby as a safe zone to plan your next move
- Master dodging projectiles (snowballs and fireballs) during boss fights
- The Final Boss is significantly harder - make sure you're prepared!

## Game Features

### Enemy AI System
- **A* Pathfinding**: Enemies use intelligent pathfinding to chase the player
- **Stuck Detection**: Myers enemy includes anti-stuck mechanics with position tracking
- **Projectile Attacks**: Boss enemies fire snowballs and fireballs

### Combat System
- Player can fire projectiles using spacebar
- Health system with damage from enemies and bad items
- Heart pickups to restore health

### Level System
- 4 distinct maps: Lobby, Myers Level, Christmas Level, and Basement
- Event-based teleportation between levels
- Progressive difficulty with stronger bosses

### Collectibles
- **Keys**: Required to unlock doors and progress
- **Gifts**: Good gifts on Christmas level (avoid bad ones!)
- **Money**: Scoring system
- **Hearts**: Health restoration

## Troubleshooting

### Tests Not Running
If `mvn test` shows "Tests run: 0":
- Verify maven-surefire-plugin version is 3.0.0 in pom.xml
- Check JUnit 5 dependencies are present
- Run `mvn clean test` to rebuild

### Coverage Report Not Generating
- Ensure JaCoCo plugin is configured in pom.xml
- Run `mvn clean test` (clean is important)
- Check `target/site/jacoco/` directory exists after test run

### Game Won't Launch
- Verify Java 17+ is installed: `java -version`
- Check all resources are in `src/main/resources/`
- Run `mvn clean compile` before `mvn exec:java`

## License
Final Project for CMPT 276 - Introduction to Software Engineering 

---
Last Updated: November 22, 2025
