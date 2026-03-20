package FinalProject.models;

import FinalProject.interfaces.IDamageable;

/**
 * Concrete class representing a Mage hero.
 * Higher damage but lower HP.
 */
public class Mage extends Hero {
    public Mage(String name) {
        super(name, 80, 35);
    }

    @Override
    public void attack(IDamageable target) {
        System.out.println(this.getName() + " casts a Fireball at " + target.getName() + "!");
        target.takeDamage(this.attackPower);
        System.out.println(target.getName() + " takes " + this.attackPower + " damage. (" + target.getHealth() + "/" + target.getMaxHealth() + ")");
        if (target.isDestroyed()) {
            System.out.println(target.getName() + " has been defeated/destroyed!");
        }
    }
}
