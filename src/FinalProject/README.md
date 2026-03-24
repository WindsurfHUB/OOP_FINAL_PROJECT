# Text-Based Java RPG (OOP Final Project)

A professional, university-level object-oriented programming project implemented in Java. This RPG demonstrates core OOP principles through a clean, extensible architecture.

## How to Run

1. **Compilation**:
   Open a terminal in the `src` directory and run:
   ```bash
   javac FinalProject/Main.java
   ```
   *Note: Standard Java compilers will recursively compile dependencies.*

2. **Execution**:
   From the `src` directory, run:
   ```bash
   java FinalProject.Main
   ```

## Project Structure

```text
FinalProject/
├── Main.java                # Application Entry Point
├── DESIGN_DOCUMENT.md       # Technical design documentation
├── README.md                # Project overview and instructions
├── interfaces/
│   ├── IDamageable.java     # Health management interface
│   └── IConsumable.java     # Item usage interface
├── core/
│   ├── Entity.java          # Abstract base for living beings
│   └── DestructibleObject.java # Abstract base for non-living objects
├── models/
│   ├── Hero.java            # Abstract player base class
│   ├── Warrior.java         # Physical damage specialist
│   ├── Mage.java            # Magic damage specialist
│   ├── Monster.java         # Enemy NPC
│   ├── WoodenBox.java       # Destructible environment object
│   ├── Equipment.java       # Base class for wearable items
│   ├── Weapon.java          # Combat-enhancing equipment
│   ├── Armor.java           # Defense-enhancing equipment
│   ├── HealthPotion.java    # Consumable recovery item
│   ├── BerserkPotion.java   # Temporary power-up item
│   └── Skill.java           # Special combat abilities
├── game/
│   ├── RPGGame.java         # Game loop & Input handling
│   ├── DungeonManager.java  # Level and monster progression
│   ├── SaveManager.java     # Game state persistence (Save/Load)
│   └── Shop.java            # Item purchasing system
└── savegame.txt             # Saved game data (Auto-generated)
```

## Key Features

- **Polymorphism**: The `attack()` method works on any object implementing `IDamageable`, whether it's a Monster or a Wooden Box.
- **Combat Depth (AP System)**: A strategic Action Point (AP) system where powerful skills (like `Fireball` or `Shield Bash`) require energy, while basic attacks replenish it.
- **Persistent Save System**: A robust save/load mechanism that preserves hero name, class, gold, equipment, tiered shop levels, dungeon depth, and even permanent powerups gained through exploration.
- **Roguelike Progression**: A depth-based scaling system where enemies grow stronger as you descend, featuring persistent gear and gold that carry over after death.

- **Dungeon Exploration**: A dynamic "Search" mechanic that can reveal monsters, loot-filled wooden boxes, or trigger random events (Traps, Foggy Areas, Danger Zones).
- **Integrated Shop System**: A tiered equipment upgrade path (Tier 1-5) and potion restock system available during level intermissions.
- **Inheritance & Abstraction**: A clean class hierarchy from `Entity` to specialized heroes (`Warrior`, `Mage`), following robust OOP standards.
- **Robust Input Handling**: Extensive use of `try-catch` and scanner validation to ensure a crash-free experience on invalid menu selections.
- **Extensible Architecture**: The project is designed for easy expansion, allowing for new character classes, items, or dungeon events with minimal code changes.
- **Game State Management**: Clear Win/Loss conditions and a "Death & Respawn" loop that preserves meta-progression.
