package FinalProject.models;

/**
 * Concrete class for weapon equipment.
 */
public class Weapon extends Equipment {
    private int damageBonus;
    private int healthBonus;
    private boolean isTwoHanded;

    public Weapon(String name, int price, String rarity, String requiredClass, int damageBonus) {
        super(name, price, rarity, requiredClass);
        this.damageBonus = damageBonus;
        this.healthBonus = 0;
        this.isTwoHanded = false;
    }

    public Weapon(String name, int price, String rarity, String requiredClass, int damageBonus, int healthBonus, boolean isTwoHanded) {
        super(name, price, rarity, requiredClass);
        this.damageBonus = damageBonus;
        this.healthBonus = healthBonus;
        this.isTwoHanded = isTwoHanded;
    }

    public int getDamageBonus() {
        return damageBonus;
    }

    public int getHealthBonus() {
        return healthBonus;
    }

    public boolean isTwoHanded() {
        return isTwoHanded;
    }

    @Override
    public String getStatsDescription() {
        String desc = "+" + damageBonus + " ATK";
        if (healthBonus > 0) desc += ", +" + healthBonus + " HP";
        if (isTwoHanded) desc += " (2-Handed)";
        return desc;
    }
}
