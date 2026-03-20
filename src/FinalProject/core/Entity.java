package FinalProject.core;

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

    public int getAttackPower() {
        return this.attackPower;
    }

    /**
     * Polymorphic attack method.
     * @param target The damageable object to attack.
     */
    public abstract void attack(IDamageable target);
}
