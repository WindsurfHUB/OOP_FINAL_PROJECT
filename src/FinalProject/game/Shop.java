package FinalProject.game;

import FinalProject.interfaces.IConsumable;
import FinalProject.models.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Manages the in-game shop for upgrading gear and buying consumables.
 */
public class Shop {
    private Scanner scanner;
    
    // Upgrade levels (0-5)
    private int weaponTier = 1;
    private int headTier = 1;
    private int chestTier = 1;
    private final int MAX_TIER = 5;
    
    // Potion stock
    private int healthPotionStock = 0;
    private int berserkPotionStock = 0;

    public Shop() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Refreshes potion stock and prepares gear for the current depth.
     */
    public void restock(int depth) {
        // Replenish potions each level
        healthPotionStock = 2 + (depth / 3);
        berserkPotionStock = 1 + (depth / 5);
    }

    public void openShop(Hero hero, List<IConsumable> inventory) {
        boolean shopping = true;
        String heroClass = hero.getClass().getSimpleName();

        while (shopping) {
            System.out.println("\n--- WELCOME TO THE DUNGEON SHOP ---");
            System.out.println("Your Gold: " + hero.getGold() + " | Class: " + heroClass);
            
            List<ShopEntry> gearItems = new ArrayList<>();
            List<ShopEntry> consumables = new ArrayList<>();
            
            // 1. Gear Upgrades
            if (weaponTier <= MAX_TIER) {
                if (heroClass.equals("Warrior")) {
                    gearItems.add(new ShopEntry(generateWeapon(heroClass, weaponTier), "Weapon Tier " + weaponTier));
                    gearItems.add(new ShopEntry(generateGreatsword(weaponTier), "Weapon Tier " + weaponTier));
                } else {
                    gearItems.add(new ShopEntry(generateWeapon(heroClass, weaponTier), "Weapon Tier " + weaponTier));
                }
            }
            if (headTier <= MAX_TIER) gearItems.add(new ShopEntry(generateHeadgear(heroClass, headTier), "Headgear Tier " + headTier));
            if (chestTier <= MAX_TIER) gearItems.add(new ShopEntry(generateChestplate(heroClass, chestTier), "Chestplate Tier " + chestTier));
            
            // 2. Consumables
            if (healthPotionStock > 0) consumables.add(new ShopEntry(new HealthPotion("Major Health Potion", 60), "Consumable", 80, healthPotionStock));
            if (berserkPotionStock > 0) consumables.add(new ShopEntry(new BerserkPotion(), "Consumable", 150, berserkPotionStock));

            List<ShopEntry> allEntries = new ArrayList<>();
            allEntries.addAll(gearItems);
            allEntries.addAll(consumables);

            for (int i = 0; i < allEntries.size(); i++) {
                ShopEntry entry = allEntries.get(i);
                String typeLabel = entry.isConsumable ? " [POTION]" : " [GEAR]";
                String stockInfo = entry.isConsumable ? " (Stock: " + entry.stock + ")" : "";
                System.out.println((i + 1) + "." + typeLabel + " " + entry.name + stockInfo + " [" + entry.description + "] - " + entry.price + "g");
            }
            
            System.out.println((allEntries.size() + 1) + ". Leave Shop");
            System.out.print("Action: ");

            int choice = getIntInput(1, allEntries.size() + 1);

            if (choice > 0 && choice <= allEntries.size()) {
                ShopEntry selected = allEntries.get(choice - 1);
                if (hero.getGold() >= selected.price) {
                    hero.spendGold(selected.price);
                    if (selected.isConsumable) {
                        handleConsumablePurchase(hero, selected, inventory);
                    } else {
                        handleGearPurchase(hero, selected);
                    }
                } else {
                    System.out.println("Not enough gold!");
                }
            } else {
                shopping = false;
            }
        }
    }

    private void handleConsumablePurchase(Hero hero, ShopEntry entry, List<IConsumable> inventory) {
        if (entry.name.contains("Health")) {
            healthPotionStock--;
            inventory.add(new HealthPotion("Major Health Potion", 60));
            System.out.println("Purchased Major Health Potion!");
        } else if (entry.name.contains("Berserk")) {
            berserkPotionStock--;
            inventory.add(new BerserkPotion());
            System.out.println("Purchased Berserk Potion!");
        }
    }

    private void handleGearPurchase(Hero hero, ShopEntry entry) {
        hero.equip(entry.equipment);
        if (entry.description.contains("Weapon")) weaponTier++;
        else if (entry.description.contains("Headgear")) headTier++;
        else if (entry.description.contains("Chestplate")) chestTier++;
        System.out.println("Successfully upgraded your gear!");
    }

    private Weapon generateWeapon(String heroClass, int tier) {
        int bonus = tier * 10;
        int price = tier * 150;
        if (heroClass.equals("Warrior")) {
            return new Weapon("Steel Blade T" + tier + " & Shield", price, "Rare", "Warrior", bonus + 5, tier * 15, false);
        } else {
            return new Weapon("Magic Staff T" + tier, price, "Rare", "Mage", bonus + 8);
        }
    }

    private Weapon generateGreatsword(int tier) {
        int bonus = tier * 25; // Much higher damage
        int price = tier * 180;
        return new Weapon("Greatsword T" + tier, price, "Rare", "Warrior", bonus + 15, 0, true);
    }

    private Armor generateHeadgear(String heroClass, int tier) {
        int bonus = tier * 15;
        int price = tier * 100;
        if (heroClass.equals("Warrior")) {
            return new Armor("Knight Helmet T" + tier, price, "Rare", "Warrior", bonus, Armor.ArmorSlot.HEAD);
        } else {
            return new Armor("Mystic Hat T" + tier, price, "Rare", "Mage", bonus - 5, Armor.ArmorSlot.HEAD);
        }
    }

    private Armor generateChestplate(String heroClass, int tier) {
        int bonus = tier * 25;
        int price = tier * 200;
        if (heroClass.equals("Warrior")) {
            return new Armor("Plate Armor T" + tier, price, "Rare", "Warrior", bonus, Armor.ArmorSlot.CHEST);
        } else {
            return new Armor("Sorcerer Robe T" + tier, price, "Rare", "Mage", bonus - 5, Armor.ArmorSlot.CHEST);
        }
    }

    private int getIntInput(int min, int max) {
        int input = -1;
        while (input < min || input > max) {
            try {
                input = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                scanner.nextLine();
            }
        }
        return input;
    }

    // Helper class for shop display
    private class ShopEntry {
        String name;
        String description;
        int price;
        int stock;
        boolean isConsumable;
        Equipment equipment;
        IConsumable consumable;

        ShopEntry(Equipment eq, String desc) {
            this.name = eq.getName();
            this.description = desc + " (" + eq.getStatsDescription() + ")";
            this.price = eq.getPrice();
            this.isConsumable = false;
            this.equipment = eq;
        }

        ShopEntry(IConsumable con, String desc, int price, int stock) {
            this.name = con.getName();
            this.description = desc;
            this.price = price;
            this.stock = stock;
            this.isConsumable = true;
            this.consumable = con;
        }
    }
}
