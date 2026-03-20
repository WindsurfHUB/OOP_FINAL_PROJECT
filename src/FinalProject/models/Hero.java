package FinalProject.models;

import FinalProject.core.Entity;
import FinalProject.interfaces.IConsumable;
import FinalProject.interfaces.IDamageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Base abstract class for player characters in the Roguelike.
 */
public abstract class Hero extends Entity {
    protected static final Random random = new Random();
    protected boolean isBerserk = false;
    protected int gold;
    protected int currentAP;
    protected int maxAP = 100;
    
    // Equipment Slots
    protected Weapon weapon;
    protected Armor headgear;
    protected Armor chestplate;
    
    protected List<Skill> skills;

    public Hero(String name, int health, int attackPower) {
        super(name, health, attackPower);
        this.gold = 0;
        this.currentAP = maxAP;
        this.skills = new ArrayList<>();
        initializeSkills();
    }

    public void updateInitialHealth() {
        this.health = getEffectiveMaxHealth();
    }

    public void fullyRestore() {
        this.health = getEffectiveMaxHealth();
        this.currentAP = maxAP;
        System.out.println("\n>>> Level transition: " + getName() + " has been fully restored! (HP/AP Maxed) <<<");
    }

    public void restoreAP(int amount) {
        this.currentAP = Math.min(maxAP, currentAP + amount);
    }

    public boolean spendAP(int amount) {
        if (currentAP >= amount) {
            currentAP -= amount;
            return true;
        }
        return false;
    }

    public int getCurrentAP() {
        return currentAP;
    }

    protected abstract void initializeSkills();

    public void addGold(int amount) {
        this.gold += amount;
        System.out.println("Gained " + amount + " gold! Total: " + this.gold);
    }

    public boolean spendGold(int amount) {
        if (this.gold >= amount) {
            this.gold -= amount;
            return true;
        }
        return false;
    }

    public int getGold() {
        return gold;
    }

    public void setBerserk(boolean berserk) {
        this.isBerserk = berserk;
    }

    public void equip(Equipment item) {
        int oldMax = getEffectiveMaxHealth();
        if (item instanceof Weapon) {
            this.weapon = (Weapon) item;
            System.out.println("Equipped " + item.getName() + " as weapon.");
        } else if (item instanceof Armor) {
            Armor armor = (Armor) item;
            if (armor.getSlot() == Armor.ArmorSlot.HEAD) {
                this.headgear = armor;
            } else {
                this.chestplate = armor;
            }
            System.out.println("Equipped " + item.getName() + ".");
        }
        
        int newMax = getEffectiveMaxHealth();
        if (newMax > oldMax) {
            // Increase current health by the difference to keep health percentage similar or just reward the player
            this.health += (newMax - oldMax);
        }
        
        // Ensure current health doesn't exceed effective max
        if (this.health > getEffectiveMaxHealth()) {
            this.health = getEffectiveMaxHealth();
        }
    }

    public int getEffectiveAttack() {
        int bonus = (weapon != null) ? weapon.getDamageBonus() : 0;
        return this.attackPower + bonus;
    }

    public int getEffectiveMaxHealth() {
        int bonus = 0;
        if (headgear != null) bonus += headgear.getHealthBonus();
        if (chestplate != null) bonus += chestplate.getHealthBonus();
        if (weapon != null) bonus += weapon.getHealthBonus();
        return this.maxHealth + bonus;
    }

    @Override
    public int getMaxHealth() {
        return getEffectiveMaxHealth();
    }

    @Override
    public int getHealth() {
        // Ensure current health doesn't exceed effective max
        if (this.health > getEffectiveMaxHealth()) {
            this.health = getEffectiveMaxHealth();
        }
        return this.health;
    }

    public void heal(int amount) {
        int max = getEffectiveMaxHealth();
        this.health += amount;
        if (this.health > max) {
            this.health = max;
        }
        System.out.println(getName() + " was healed by " + amount + " HP. Current health: " + getHealth() + "/" + max);
    }

    // Overriding the base attack to handle skills specifically in RPGGame
    @Override
    public void attack(IDamageable target) {
        // Standard attack if no skill is specified (fallback)
        performSkillAttack(target, new Skill("Basic Attack", 1.0, "A standard strike."));
    }

    public void performSkillAttack(IDamageable target, Skill skill) {
        int baseDamage = getEffectiveAttack();
        int finalDamage = (int)(baseDamage * skill.getDamageMultiplier());
        
        String message = getAttackDescription(target, skill);
        
        if (isBerserk) {
            finalDamage *= 2;
            message += " (BERSERK DAMAGE!)";
            isBerserk = false; 
        }

        System.out.println(message);
        target.takeDamage(finalDamage);
        System.out.println(target.getName() + " takes " + finalDamage + " damage. (" + target.getHealth() + "/" + target.getMaxHealth() + ")");
        
        // Handle Side Effects (e.g., Stun)
        if (target instanceof Monster) {
            Monster m = (Monster) target;
            if (skill.getName().contains("Shield Bash")) {
                if (random.nextInt(100) < 60) {
                    m.stun(2); // Stuns for 2 turns (this turn and next)
                    System.out.println(">>> " + target.getName() + " was CRUSHED by the shield and STUNNED for 2 turns! <<<");
                }
            } else if (skill.getName().contains("Sword Bash")) {
                if (random.nextInt(100) < 80) { // Higher stun chance for greatsword
                    m.stun(2);
                    System.out.println(">>> " + target.getName() + " was STAGGERED by the massive blade and STUNNED for 2 turns! <<<");
                }
            }
        }

        if (target.isDestroyed()) {
            System.out.println(target.getName() + " has been defeated/destroyed!");
        }
    }

    protected abstract String getAttackDescription(IDamageable target, Skill skill);

    public List<Skill> getSkills() {
        return skills;
    }

    public Weapon getWeapon() { return weapon; }
    public Armor getHeadgear() { return headgear; }
    public Armor getChestplate() { return chestplate; }

    /**
     * Polymorphic method to use an item.
     * @param item The consumable item to use.
     */
    public void useItem(IConsumable item) {
        System.out.println(getName() + " uses " + item.getName());
        item.consume(this);
    }
}
