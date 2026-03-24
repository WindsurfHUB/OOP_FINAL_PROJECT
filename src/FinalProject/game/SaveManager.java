package FinalProject.game;

import FinalProject.interfaces.IConsumable;
import FinalProject.models.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading game state to/from a TXT file.
 * Uses key=value format for simple persistence.
 */
public class SaveManager {
    private static final String SAVE_FILE = "savegame.txt";

    /**
     * Saves the current game state to a text file.
     * @param player The hero to save.
     * @param dungeon The dungeon manager (for depth).
     * @param shop The shop (for tier levels).
     * @param inventory The player's consumable inventory.
     * @return true if save was successful, false otherwise.
     */
    public static boolean saveGame(Hero player, DungeonManager dungeon, Shop shop, List<IConsumable> inventory) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE))) {
            // Hero basic info
            writer.write("heroName=" + player.getName());
            writer.newLine();
            writer.write("heroClass=" + (player instanceof Warrior ? "Warrior" : "Mage"));
            writer.newLine();
            writer.write("gold=" + player.getGold());
            writer.newLine();
            writer.write("baseAttack=" + player.getAttackPower());
            writer.newLine();
            writer.write("baseMaxHealth=" + player.getBaseMaxHealth());
            writer.newLine();

            // Weapon
            Weapon w = player.getWeapon();
            if (w != null) {
                writer.write("weaponName=" + w.getName());
                writer.newLine();
                writer.write("weaponDamage=" + w.getDamageBonus());
                writer.newLine();
                writer.write("weaponHealth=" + w.getHealthBonus());
                writer.newLine();
                writer.write("weaponTwoHanded=" + w.isTwoHanded());
                writer.newLine();
                writer.write("weaponPrice=" + w.getPrice());
                writer.newLine();
                writer.write("weaponRarity=" + w.getRarity());
                writer.newLine();
                writer.write("weaponClass=" + w.getRequiredClass());
                writer.newLine();
            } else {
                writer.write("weaponName=NONE");
                writer.newLine();
            }

            // Headgear
            Armor head = player.getHeadgear();
            if (head != null) {
                writer.write("headName=" + head.getName());
                writer.newLine();
                writer.write("headHealth=" + head.getHealthBonus());
                writer.newLine();
                writer.write("headPrice=" + head.getPrice());
                writer.newLine();
                writer.write("headRarity=" + head.getRarity());
                writer.newLine();
                writer.write("headClass=" + head.getRequiredClass());
                writer.newLine();
            } else {
                writer.write("headName=NONE");
                writer.newLine();
            }

            // Chestplate
            Armor chest = player.getChestplate();
            if (chest != null) {
                writer.write("chestName=" + chest.getName());
                writer.newLine();
                writer.write("chestHealth=" + chest.getHealthBonus());
                writer.newLine();
                writer.write("chestPrice=" + chest.getPrice());
                writer.newLine();
                writer.write("chestRarity=" + chest.getRarity());
                writer.newLine();
                writer.write("chestClass=" + chest.getRequiredClass());
                writer.newLine();
            } else {
                writer.write("chestName=NONE");
                writer.newLine();
            }

            // Dungeon & Shop state
            writer.write("dungeonDepth=" + dungeon.getDepth());
            writer.newLine();
            writer.write("weaponTier=" + shop.getWeaponTier());
            writer.newLine();
            writer.write("headTier=" + shop.getHeadTier());
            writer.newLine();
            writer.write("chestTier=" + shop.getChestTier());
            writer.newLine();

            // Inventory
            writer.write("inventoryCount=" + inventory.size());
            writer.newLine();
            for (int i = 0; i < inventory.size(); i++) {
                IConsumable item = inventory.get(i);
                if (item instanceof HealthPotion) {
                    HealthPotion hp = (HealthPotion) item;
                    writer.write("inventory_" + i + "=HealthPotion," + hp.getName() + "," + hp.getHealAmount());
                } else if (item instanceof BerserkPotion) {
                    writer.write("inventory_" + i + "=BerserkPotion," + item.getName());
                }
                writer.newLine();
            }

            System.out.println("Game saved successfully to " + SAVE_FILE + "!");
            return true;
        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
            return false;
        }
    }

    /**
     * Loads a saved game state from the text file.
     * @return A SaveData object containing the loaded state, or null if loading fails.
     */
    public static SaveData loadGame() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            System.out.println("No save file found.");
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE))) {
            java.util.Map<String, String> data = new java.util.HashMap<>();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                int eqIndex = line.indexOf('=');
                if (eqIndex > 0) {
                    String key = line.substring(0, eqIndex);
                    String value = line.substring(eqIndex + 1);
                    data.put(key, value);
                }
            }

            // Validate required fields
            if (!data.containsKey("heroName") || !data.containsKey("heroClass")) {
                System.out.println("Save file is corrupted: missing hero data.");
                return null;
            }

            SaveData save = new SaveData();

            // Hero basics
            save.heroName = data.get("heroName");
            save.heroClass = data.get("heroClass");
            save.gold = parseIntSafe(data.get("gold"), 0);
            save.baseAttack = parseIntSafe(data.get("baseAttack"), 15);
            save.baseMaxHealth = parseIntSafe(data.get("baseMaxHealth"), 100);

            // Weapon
            String weaponName = data.getOrDefault("weaponName", "NONE");
            if (!weaponName.equals("NONE")) {
                int dmg = parseIntSafe(data.get("weaponDamage"), 0);
                int hp = parseIntSafe(data.get("weaponHealth"), 0);
                boolean twoHand = Boolean.parseBoolean(data.getOrDefault("weaponTwoHanded", "false"));
                int price = parseIntSafe(data.get("weaponPrice"), 0);
                String rarity = data.getOrDefault("weaponRarity", "Common");
                String reqClass = data.getOrDefault("weaponClass", save.heroClass);
                save.weapon = new Weapon(weaponName, price, rarity, reqClass, dmg, hp, twoHand);
            }

            // Headgear
            String headName = data.getOrDefault("headName", "NONE");
            if (!headName.equals("NONE")) {
                int hp = parseIntSafe(data.get("headHealth"), 0);
                int price = parseIntSafe(data.get("headPrice"), 0);
                String rarity = data.getOrDefault("headRarity", "Common");
                String reqClass = data.getOrDefault("headClass", save.heroClass);
                save.headgear = new Armor(headName, price, rarity, reqClass, hp, Armor.ArmorSlot.HEAD);
            }

            // Chestplate
            String chestName = data.getOrDefault("chestName", "NONE");
            if (!chestName.equals("NONE")) {
                int hp = parseIntSafe(data.get("chestHealth"), 0);
                int price = parseIntSafe(data.get("chestPrice"), 0);
                String rarity = data.getOrDefault("chestRarity", "Common");
                String reqClass = data.getOrDefault("chestClass", save.heroClass);
                save.chestplate = new Armor(chestName, price, rarity, reqClass, hp, Armor.ArmorSlot.CHEST);
            }

            // Dungeon & Shop
            save.dungeonDepth = parseIntSafe(data.get("dungeonDepth"), 1);
            save.weaponTier = parseIntSafe(data.get("weaponTier"), 1);
            save.headTier = parseIntSafe(data.get("headTier"), 1);
            save.chestTier = parseIntSafe(data.get("chestTier"), 1);

            // Inventory
            int invCount = parseIntSafe(data.get("inventoryCount"), 0);
            save.inventory = new ArrayList<>();
            for (int i = 0; i < invCount; i++) {
                String itemData = data.get("inventory_" + i);
                if (itemData != null) {
                    String[] parts = itemData.split(",");
                    if (parts[0].equals("HealthPotion") && parts.length >= 3) {
                        save.inventory.add(new HealthPotion(parts[1], parseIntSafe(parts[2], 30)));
                    } else if (parts[0].equals("BerserkPotion")) {
                        save.inventory.add(new BerserkPotion());
                    }
                }
            }

            System.out.println("Game loaded successfully!");
            return save;
        } catch (IOException e) {
            System.out.println("Error loading game: " + e.getMessage());
            return null;
        }
    }

    /**
     * Checks if a save file exists.
     */
    public static boolean saveExists() {
        return new File(SAVE_FILE).exists();
    }

    private static int parseIntSafe(String value, int defaultValue) {
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Data class to hold loaded game state.
     */
    public static class SaveData {
        public String heroName;
        public String heroClass;
        public int gold;
        public int baseAttack;
        public int baseMaxHealth;
        public Weapon weapon;
        public Armor headgear;
        public Armor chestplate;
        public int dungeonDepth;
        public int weaponTier;
        public int headTier;
        public int chestTier;
        public List<IConsumable> inventory;
    }
}
