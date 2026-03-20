package FinalProject.game;

import FinalProject.interfaces.IConsumable;
import FinalProject.interfaces.IDamageable;
import FinalProject.models.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to initialize the game world with sample data.
 */
public class GameInitializer {
    private List<IDamageable> targets;
    private List<IConsumable> inventory;

    public GameInitializer() {
        targets = new ArrayList<>();
        inventory = new ArrayList<>();
        setupWorld();
    }

    private void setupWorld() {
        // Add multiple target types (Monster and WoodenBox) to demonstrate polymorphism
        targets.add(new Monster("Slime", 30, 5));
        targets.add(new WoodenBox());
        targets.add(new Monster("Grizzly Bear", 100, 15));
        targets.add(new Monster("Elite Goblin", 60, 10));

        // Add some items
        inventory.add(new HealthPotion("Minor Health Potion", 30));
        inventory.add(new HealthPotion("Major Health Potion", 60));
    }

    public List<IDamageable> getTargets() {
        return targets;
    }

    public List<IConsumable> getInventory() {
        return inventory;
    }
}
