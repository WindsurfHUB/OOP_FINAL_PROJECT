package FinalProject.core;

import FinalProject.interfaces.IConsumable;
import FinalProject.interfaces.IDamageable;

/**
 * Abstract class representing any living being in the RPG.
 * Implements IDamageable to handle health and damage logic.
 */
public abstract class Entity implements IDamageable {
    protected String name;
    protected int health;
    protected int maxHealth;
    protected int attackPower;

    public Entity(String name, int health, int attackPower) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.attackPower = attackPower;
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
        return null; // By default, entities don't drop items.
    }

    public int getAttackPower() {
        return this.attackPower;
    }

    public void increaseAttackPower(int amount) {
        this.attackPower += amount;
    }

    public void increaseMaxHealth(int amount) {
        this.maxHealth += amount;
        this.health += amount; // Also heal for the amount increased
    }

    /**
     * Polymorphic attack method.
     * @param target The damageable object to attack.
     */
    public abstract void attack(IDamageable target);
}
