package FinalProject.models;

import FinalProject.core.Entity;
import FinalProject.interfaces.IDamageable;

/**
 * Concrete class representing an enemy monster.
 * Implements its own attack behavior.
 */
public class Monster extends Entity {
    private int stunTurns = 0;

    public Monster(String name, int health, int attackPower) {
        super(name, health, attackPower);
    }

    public void stun(int turns) {
        this.stunTurns = turns;
    }

    public boolean isStunned() {
        return stunTurns > 0;
    }

    @Override
    public void attack(IDamageable target) {
        if (stunTurns > 0) {
            System.out.println(this.getName() + " is stunned and cannot move! (" + stunTurns + " turn(s) remaining)");
            stunTurns--;
            return;
        }

        String attackMsg;
        String nameLower = name.toLowerCase();
        
        if (nameLower.contains("elite knight")) {
            attackMsg = name + " swings their sword and hits " + target.getName();
        } else if (nameLower.contains("slime")) {
            attackMsg = name + " shoots slimeballs at " + target.getName();
        } else if (nameLower.contains("goblin")) {
            attackMsg = name + " stabs " + target.getName() + " with a rusty dagger";
        } else if (nameLower.contains("orc")) {
            attackMsg = name + " smashes " + target.getName() + " with a heavy club";
        } else if (nameLower.contains("guardian")) {
            attackMsg = name + " unleashes a powerful shockwave at " + target.getName();
        } else {
            attackMsg = name + " bites " + target.getName();
        }

        System.out.println(attackMsg + " for " + this.attackPower + " damage!");
        target.takeDamage(this.attackPower);
        System.out.println(target.getName() + " now has " + target.getHealth() + "/" + target.getMaxHealth() + " HP.");
        if (target.isDestroyed()) {
            System.out.println(target.getName() + " has fallen in battle!");
        }
    }
}
