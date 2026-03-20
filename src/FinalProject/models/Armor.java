package FinalProject.models;

/**
 * Concrete class for armor equipment.
 */
public class Armor extends Equipment {
    public enum ArmorSlot { HEAD, CHEST }
    
    private int healthBonus;
    private ArmorSlot slot;

    public Armor(String name, int price, String rarity, String requiredClass, int healthBonus, ArmorSlot slot) {
        super(name, price, rarity, requiredClass);
        this.healthBonus = healthBonus;
        this.slot = slot;
    }

    public int getHealthBonus() {
        return healthBonus;
    }

    public ArmorSlot getSlot() {
        return slot;
    }

    @Override
    public String getStatsDescription() {
        return "+" + healthBonus + " Max HP";
    }
}
