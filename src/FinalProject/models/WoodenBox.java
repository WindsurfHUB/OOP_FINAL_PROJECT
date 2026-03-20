package FinalProject.models;

import FinalProject.core.DestructibleObject;
import FinalProject.interfaces.IConsumable;
import java.util.Random;

/**
 * Concrete class for a non-living destructible item.
 */
public class WoodenBox extends DestructibleObject {
    private static final Random random = new Random();

    public WoodenBox() {
        super("Wooden Box", 40);
    }

    @Override
    public void takeDamage(int amount) {
        super.takeDamage(amount);
        System.out.println("The Wooden Box splinters slightly...");
        if (isDestroyed()) {
            System.out.println("The Wooden Box shatters into pieces!");
        }
    }

    @Override
    public IConsumable getDrop() {
        int chance = random.nextInt(100);
        if (chance < 40) { // 40% chance for a potion
            if (random.nextBoolean()) {
                return new HealthPotion("Minor Health Potion", 30);
            } else {
                return new BerserkPotion();
            }
        }
        return null;
    }
}
