package FinalProject.models;

import FinalProject.interfaces.IDamageable;

/**
 * Concrete class representing a Warrior hero.
 * High physical damage and HP.
 */
public class Warrior extends Hero {
    public Warrior(String name) {
        super(name, 120, 20);
    }

    @Override
    public void attack(IDamageable target) {
        System.out.println(this.getName() + " swings a mighty sword at " + target.getName() + "!");
        target.takeDamage(this.attackPower);
        System.out.println(target.getName() + " takes " + this.attackPower + " damage. (" + target.getHealth() + "/" + target.getMaxHealth() + ")");
        if (target.isDestroyed()) {
            System.out.println(target.getName() + " has been defeated/destroyed!");
        }
    }
}
