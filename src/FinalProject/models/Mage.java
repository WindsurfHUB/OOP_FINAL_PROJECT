package FinalProject.models;

import FinalProject.interfaces.IDamageable;

/**
 * Concrete class representing a Mage hero.
 * Higher damage but lower HP.
 */
public class Mage extends Hero {
    public Mage(String name) {
        super(name, 80, 25); // Lowered base attack because gear adds more
        
        System.out.println("\n--- RECEIVING STARTER GEAR ---");
        Weapon staff = new Weapon("Wooden Staff", 0, "Common", "Mage", 8);
        Armor hat = new Armor("Wizards Hat", 0, "Common", "Mage", 3, Armor.ArmorSlot.HEAD);
        Armor robe = new Armor("Old Robes", 0, "Common", "Mage", 5, Armor.ArmorSlot.CHEST);
        
        System.out.println(staff.getName() + ": " + staff.getStatsDescription());
        System.out.println(hat.getName() + ": " + hat.getStatsDescription());
        System.out.println(robe.getName() + ": " + robe.getStatsDescription());
        
        equip(staff);
        equip(hat);
        equip(robe);
        updateInitialHealth();
    }

    @Override
    protected void initializeSkills() {
        skills.add(new Skill("Arcane Bolt", 1.0, "A blast of pure energy."));
        skills.add(new Skill("Fireball", 1.8, "A massive explosion of fire."));
        skills.add(new Skill("Magic Missile", 1.2, "Three quick magical darts."));
    }

    @Override
    protected String getAttackDescription(IDamageable target, Skill skill) {
        switch (skill.getName()) {
            case "Fireball":
                return this.getName() + " incants a massive Fireball at " + target.getName() + "!";
            case "Magic Missile":
                return this.getName() + " fires several Magic Missiles at " + target.getName() + "!";
            default:
                return this.getName() + " casts an Arcane Bolt at " + target.getName() + "!";
        }
    }
}
