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
└── game/
    ├── RPGGame.java         # Game loop & Input handling
    ├── GameInitializer.java # Sample data setup
    ├── DungeonManager.java  # Level and monster progression
    └── Shop.java            # Item purchasing system
```

## Key Features

- **Polymorphism**: The `attack()` method works on any object implementing `IDamageable`, whether it's a Monster or a Wooden Box.
- **Inheritance & Abstraction**: Heroes and Monsters share common logic via `Entity`, while specific class behaviors are defined in `Warrior` and `Mage`.
- **Robust Input Handling**: The game uses `try-catch` and scanner validation to ensure it doesn't crash on invalid menu selections (e.g., entering letters instead of numbers).
- **Extensible Architecture**: Adding new character classes or items is as simple as creating a new class and implementing the relevant interface.
- **Game State Management**: Clear Win/Loss conditions based on target status and player health.
