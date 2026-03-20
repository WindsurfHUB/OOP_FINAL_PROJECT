package FinalProject.core;

import FinalProject.interfaces.IConsumable;
import FinalProject.interfaces.IDamageable;

/**
 * Abstract class for non-living objects that can be destroyed.
 * Useful for environmental interactions.
 */
public abstract class DestructibleObject implements IDamageable {
    protected String name;
    protected int health;
    protected int maxHealth;

    public DestructibleObject(String name, int health) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
    }

    @Override
    public void takeDamage(int amount) {
        this.health -= amount;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    @Override
    public boolean isDestroyed() {
        return this.health <= 0;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getHealth() {
        return this.health;
    }

    @Override
    public int getMaxHealth() {
        return this.maxHealth;
    }

    @Override
    public IConsumable getDrop() {
        return null; // By default, destructible objects don't drop items.
    }
}
