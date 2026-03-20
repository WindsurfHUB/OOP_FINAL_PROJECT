package FinalProject.models;

/**
 * Class representing a combat skill for a hero.
 */
public class Skill {
    private String name;
    private double damageMultiplier;
    private String description;
    private int apCost;

    public Skill(String name, double damageMultiplier, String description) {
        this.name = name;
        this.damageMultiplier = damageMultiplier;
        this.description = description;
        // Default cost calculation: (multiplier - 1.0) * 50
        this.apCost = (int) Math.max(0, (damageMultiplier - 1.0) * 50);
    }

    public Skill(String name, double damageMultiplier, String description, int customApCost) {
        this.name = name;
        this.damageMultiplier = damageMultiplier;
        this.description = description;
        this.apCost = customApCost;
    }

    public String getName() {
        return name;
    }

    public double getDamageMultiplier() {
        return damageMultiplier;
    }

    public String getDescription() {
        return description;
    }

    public int getApCost() {
        return apCost;
    }
}
