package FinalProject.models;

import FinalProject.core.DestructibleObject;

/**
 * Concrete class for a non-living destructible item.
 */
public class WoodenBox extends DestructibleObject {
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
}
