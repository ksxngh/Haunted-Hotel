# Haunted Hotel (Java 2D Game)

Built as part of a team project at Simon Fraser University.

## Overview
Haunted Hotel is a 2D tile-based adventure game developed in Java. Players explore multiple themed levels, fight AI-controlled enemies, collect items, and progress through increasingly difficult boss encounters.

The project emphasizes enemy AI, event-driven gameplay, and a modular tile and collision system.

## Key Features
- Enemy AI using A* pathfinding
- Projectile-based combat system
- Event-driven level transitions
- Multiple boss fights with unique mechanics
- Tile-based map and collision system
- Collectible items (keys, gifts, money, health)

## Tech Stack
- Java 17
- Maven
- JUnit 5
- JaCoCo (test coverage)

## Running the Game
```bash
cd HauntedHotel
mvn clean compile exec:java
```

## Controls
- WASD — Move player
- Space — Attack / Fire projectile
- Enter — Interact with NPCs or objects
- ESC — Pause game
  
## My Contributions
- Implemented enemy AI behavior using A* pathfinding
- Developed combat and projectile mechanics
- Built collision detection and event-handling systems
- Wrote unit and integration tests for core gameplay components
  
Testing
Run all tests:
```bash
mvn test
```

---
Last Updated: December 22, 2025
