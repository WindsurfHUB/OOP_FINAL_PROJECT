package FinalProject.models;

import FinalProject.core.Entity;
import FinalProject.interfaces.IDamageable;

/**
 * Concrete class representing an enemy monster.
 * Implements its own attack behavior.
 */
public class Monster extends Entity {
    public Monster(String name, int health, int attackPower) {
        super(name, health, attackPower);
    }

    @Override
    public void attack(IDamageable target) {
        System.out.println(this.getName() + " bites " + target.getName() + " for " + this.attackPower + " damage!");
        target.takeDamage(this.attackPower);
        System.out.println(target.getName() + " now has " + target.getHealth() + "/" + target.getMaxHealth() + " HP.");
        if (target.isDestroyed()) {
            System.out.println(target.getName() + " has fallen in battle!");
        }
    }
}
