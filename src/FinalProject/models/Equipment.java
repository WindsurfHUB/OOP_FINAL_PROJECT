package FinalProject.models;

/**
 * Abstract base class for all equipment in the game.
 */
public abstract class Equipment {
    protected String name;
    protected int price;
    protected String rarity;
    protected String requiredClass;

    public Equipment(String name, int price, String rarity, String requiredClass) {
        this.name = name;
        this.price = price;
        this.rarity = rarity;
        this.requiredClass = requiredClass;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getRarity() {
        return rarity;
    }

    public String getRequiredClass() {
        return requiredClass;
    }

    public abstract String getStatsDescription();
}
