package FinalProject.models;

import FinalProject.interfaces.IDamageable;
import java.util.ArrayList;
import java.util.List;

/**
 * Concrete class representing a Warrior hero.
 * High physical damage and HP.
 */
public class Warrior extends Hero {
    public Warrior(String name) {
        super(name, 120, 15); // Slightly lowered base attack because gear adds more
        
        System.out.println("\n--- RECEIVING STARTER GEAR ---");
        Weapon sword = new Weapon("Stone Sword & Wooden Shield", 0, "Common", "Warrior", 5, 10, false);
        Armor head = new Armor("Iron Pot", 0, "Common", "Warrior", 5, Armor.ArmorSlot.HEAD);
        Armor chest = new Armor("Leather Tunic", 0, "Common", "Warrior", 10, Armor.ArmorSlot.CHEST);
        
        System.out.println(sword.getName() + ": " + sword.getStatsDescription());
        System.out.println(head.getName() + ": " + head.getStatsDescription());
        System.out.println(chest.getName() + ": " + chest.getStatsDescription());
        
        equip(sword);
        equip(head);
        equip(chest);
        updateInitialHealth();
    }

    @Override
    protected void initializeSkills() {
        // Initializing common skills, dynamic skills handled in getSkills()
    }

    @Override
    public List<Skill> getSkills() {
        List<Skill> availableSkills = new ArrayList<>();
        availableSkills.add(new Skill("Normal Strike", 1.0, "A reliable strike."));
        availableSkills.add(new Skill("Mighty Swing", 1.5, "A powerful but heavy swing."));
        
        if (weapon != null && weapon.isTwoHanded()) {
            availableSkills.add(new Skill("Sword Bash", 1.2, "A heavy strike with the flat of the blade. (80% Stun)", 25));
        } else {
            availableSkills.add(new Skill("Shield Bash", 0.8, "A quick strike with the shield. (60% Stun)", 10));
        }
        return availableSkills;
    }

    @Override
    protected String getAttackDescription(IDamageable target, Skill skill) {
        switch (skill.getName()) {
            case "Mighty Swing":
                return this.getName() + " puts all their weight into a Mighty Swing at " + target.getName() + "!";
            case "Shield Bash":
                return this.getName() + " slams their shield into " + target.getName() + "!";
            case "Sword Bash":
                return this.getName() + " crashes the flat of their Greatsword into " + target.getName() + "!";
            default:
                return this.getName() + " strikes " + target.getName() + "!";
        }
    }
}
