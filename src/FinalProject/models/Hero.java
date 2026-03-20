package FinalProject.models;

import FinalProject.core.Entity;
import FinalProject.interfaces.IConsumable;

/**
 * Base abstract class for player characters.
 */
public abstract class Hero extends Entity {
    public Hero(String name, int health, int attackPower) {
        super(name, health, attackPower);
    }

    public void heal(int amount) {
        this.health += amount;
        if (this.health > this.maxHealth) {
            this.health = this.maxHealth;
        }
        System.out.println(getName() + " was healed by " + amount + " HP. Current health: " + getHealth() + "/" + getMaxHealth());
    }

    /**
     * Polymorphic method to use an item.
     * @param item The consumable item to use.
     */
    public void useItem(IConsumable item) {
        System.out.println(getName() + " uses " + item.getName());
        item.consume(this);
    }
}
