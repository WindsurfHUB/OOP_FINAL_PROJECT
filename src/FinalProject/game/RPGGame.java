package FinalProject.game;

import FinalProject.interfaces.IConsumable;
import FinalProject.interfaces.IDamageable;
import FinalProject.models.*;
import FinalProject.game.SaveManager;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

/**
 * Main game engine for the Roguelike RPG Dungeon.
 */
public class RPGGame {
    private Scanner scanner;
    private Random random;
    private Hero player;
    private List<IDamageable> targets;
    private List<IConsumable> inventory;
    private DungeonManager dungeon;
    private Shop shop;
    private boolean isRunning;

    public RPGGame() {
        this.scanner = new Scanner(System.in);
        this.random = new Random();
        this.dungeon = new DungeonManager();
        this.shop = new Shop();
        this.targets = new ArrayList<>();
        this.inventory = new ArrayList<>();
        this.isRunning = true;
    }

    public void start() {
        boolean showExitMessage = true;
        while (showExitMessage) {
            showMainMenu();
            if (isRunning) {
                while (isRunning && !player.isDestroyed()) {
                    gameLoop();
                    
                    if (player.isDestroyed()) {
                        System.out.println("\nWould you like to try again with your saved Gold? (1. Yes, 2. No)");
                        int choice = getValidIntInput(1, 2);
                        if (choice == 1) {
                            respawnHero();
                            isRunning = true; // Stay in the while loop
                        } else {
                            showExitMessage = false;
                            isRunning = false;
                        }
                    } else {
                        showExitMessage = false;
                    }
                }
            } else {
                showExitMessage = false;
            }
        }
        System.out.println("Farewell, adventurer...");
    }

    private void showMainMenu() {
        if (player != null && !player.isDestroyed()) return; // Don't show menu if game is ongoing
        System.out.println("  _____           _           _   :");
        System.out.println(" |  __ \\         (_)         | |  :");
        System.out.println(" | |__) | __ ___  _  ___  ___| |_ :");
        System.out.println(" |  ___/ '__/ _ \\| |/ _ \\/ __| __|:");
        System.out.println(" | |   | | | (_) | |  __/ (__| |_ :");
        System.out.println(" |_|   |_|  \\___/| |\\___|\\___|\\__|:");
        System.out.println("                _/ |              ");
        System.out.println("               |__/               ");
        System.out.println("  _____  _____ _____      _____  _    _ _   _  _____ ______ ____  _   _ ");
        System.out.println(" |  __ \\|_   _|_   _|    |  __ \\| |  | | \\ | |/ ____|  ____/ __ \\| \\ | |");
        System.out.println(" | |  | | | |   | | ____ | |  | | |  | |  \\| | |  __| |__ | |  | |  \\| |");
        System.out.println(" | |  | | | |   | |/ ___|| |  | | |  | | . ` | | |_ |  __|| |  | | . ` |");
        System.out.println(" | |__| |_| |_ _| |____  | |__| | |__| | |\\  | |__| | |___| |__| | |\\  |");
        System.out.println(" |_____/|_____|____|____||_____/ \\____/|_| \\_|\\_____|______\\____/|_| \\_|");
        System.out.println();
        System.out.println("1. [ Start Exploring ]");
        if (SaveManager.saveExists()) System.out.println("2. [ Load Game ]");
        System.out.println("3. How to Play");
        System.out.println("4. Exit");
        System.out.print("\nChoose an option: ");

        int choice = getValidIntInput(1, 4);
        switch (choice) {
            case 1:
                if (player == null) initializeGame();
                isRunning = true;
                break;
            case 2:
                if (SaveManager.saveExists()) {
                    SaveManager.SaveData data = SaveManager.loadGame();
                    if (data != null) {
                        loadFromSave(data);
                        isRunning = true;
                    }
                } else {
                    System.out.println("No save file found.");
                    showMainMenu();
                }
                break;
            case 3:
                showInstructions();
                showMainMenu();
                break;
            case 4:
                isRunning = false;
                break;
        }
    }

    private void gameLoop() {
        while (isRunning && !player.isDestroyed()) {
            System.out.println("\n=== [ DUNGEON DEPTH: " + dungeon.getDepth() + " ] ===");
            System.out.println("Searches: " + dungeon.getLevelSearches() + "/4");
            if (dungeon.getLevelSearches() < 4) {
                System.out.println("→ Complete the minimum searches to proceed");
            } else if (!targets.isEmpty()) {
                System.out.println("→ Defeat all " + targets.size() + " remaining target(s)");
            }
            handleAction();
            
            // Level clears only if:
            // 1. All found targets are defeated
            // 2. Minimum 4 searches have been performed
            if (targets.isEmpty() && dungeon.getLevelSearches() >= 4 && isRunning) {
                // FORCE a spawn if level search requirement met but nothing was found
                // This ensures every level has at least one challenge
                if (dungeon.getLevelSearches() == 4 && targets.isEmpty()) {
                    System.out.println("\nAn ambush! A monster appears before you can leave!");
                    DungeonManager.SearchResponse resp = dungeon.search(0, 0); 
                    if (resp.target != null) targets.add(resp.target);
                    continue; // Go back to top of loop to handle the new target
                }
                
                System.out.println("\nLevel Cleared! You've found a safe spot to rest.");
                player.fullyRestore(); // Recover all HP and AP
                dungeon.nextDepth();
                intermissionMenu();
            }
        }
    }

    private void intermissionMenu() {
        boolean resting = true;
        shop.restock(dungeon.getDepth());
        
        while (resting) {
            System.out.println("\n--- INTERMISSION (Depth " + dungeon.getDepth() + ") ---");
            System.out.println("1. Open Shop");
            System.out.println("2. View Hero Stats & Gear");
            System.out.println("3. Use Item");
            System.out.println("4. Save Game");
            System.out.println("5. Continue Exploring");
            System.out.print("Action: ");

            int choice = getValidIntInput(1, 5);
            switch (choice) {
                case 1: shop.openShop(player, inventory); break;
                case 2: showHeroStats(); break;
                case 3: useItemMenu(); break;
                case 4: SaveManager.saveGame(player, dungeon, shop, inventory); break;
                case 5: resting = false; break;
            }
        }
    }

    private void showHeroStats() {
        System.out.println("\n--- " + player.getName().toUpperCase() + " STATS ---");
        System.out.println("HP: " + player.getHealth() + "/" + player.getEffectiveMaxHealth());
        System.out.println("Attack: " + player.getEffectiveAttack());
        System.out.println("Gold: " + player.getGold());
        System.out.println("Weapon: " + (player.getWeapon() != null ? player.getWeapon().getName() : "None"));
        System.out.println("Head: " + (player.getHeadgear() != null ? player.getHeadgear().getName() : "None"));
        System.out.println("Chest: " + (player.getChestplate() != null ? player.getChestplate().getName() : "None"));
    }

    private void initializeGame() {
        System.out.println("\nSelect your hero name: ");
        String name = scanner.nextLine();

        System.out.println("Choose your class:");
        System.out.println("1. Warrior (Sword & Shield, High HP)");
        System.out.println("2. Mage (Staff & Spells, High Damage)");
        int classChoice = getValidIntInput(1, 2);

        if (classChoice == 1) player = new Warrior(name);
        else player = new Mage(name);

        inventory.add(new HealthPotion("Minor Health Potion", 30));
        System.out.println("\nWelcome, " + player.getName() + "! The dungeon awaits.");
    }

    private void respawnHero() {
        String name = player.getName();
        int savedGold = player.getGold();
        Weapon savedWeapon = player.getWeapon();
        Armor savedHead = player.getHeadgear();
        Armor savedChest = player.getChestplate();
        
        // Re-initialize based on class
        if (player instanceof Warrior) player = new Warrior(name);
        else player = new Mage(name);
        
        player.addGold(savedGold);
        if (savedWeapon != null) player.equip(savedWeapon);
        if (savedHead != null) player.equip(savedHead);
        if (savedChest != null) player.equip(savedChest);
        
        dungeon = new DungeonManager(); // Reset depth to 1
        targets.clear();
        inventory.clear();
        inventory.add(new HealthPotion("Minor Health Potion", 30));
        
        System.out.println("\nBack to Level 1, but your gear remains...");
        intermissionMenu(); // Allow shopping before restart
    }

    private void handleAction() {
        System.out.println("\n--- " + player.getName().toUpperCase() + " [" + player.getHealth() + "/" + player.getEffectiveMaxHealth() + " HP] ---");
        System.out.println("1. Search for Target");
        if (!targets.isEmpty()) System.out.println("2. Encounter Found Targets (" + targets.size() + ")");
        System.out.println("3. Use Item");
        System.out.println("4. Save & Quit");
        System.out.println("5. Retreat (Quit Game)");
        System.out.print("Action: ");

        int choice = getValidIntInput(1, 5);
        switch (choice) {
            case 1: searchArea(); break;
            case 2: 
                if (!targets.isEmpty()) attackTargetMenu(); 
                else System.out.println("Nothing to encounter. Search first!");
                break;
            case 3: useItemMenu(); break;
            case 4:
                SaveManager.saveGame(player, dungeon, shop, inventory);
                isRunning = false;
                break;
            case 5: isRunning = false; break;
        }
    }

    private void searchArea() {
        if (targets.size() >= 5) {
            System.out.println("\nYour path is blocked by existing targets! Defeat them first.");
            return;
        }
        System.out.println("\nYou search the dark corridors...");
        
        // Count existing monsters and boxes
        int monsters = 0;
        int boxes = 0;
        for (IDamageable t : targets) {
            if (t instanceof Monster) monsters++;
            else if (t instanceof WoodenBox) boxes++;
        }

        DungeonManager.SearchResponse resp = dungeon.search(monsters, boxes);
        
        // Regenerate AP on search (Passive regen)
        player.restoreAP(40);
        System.out.println("Rested briefly while searching (+40 AP). Current AP: " + player.getCurrentAP());

        if (resp.event != null) {
            handleSearchEvent(resp);
        } else if (resp.target != null) {
            System.out.println("You discovered a " + resp.target.getName() + "!");
            targets.add(resp.target);
        } else {
            int goldFound = random.nextInt(10) + 5;
            System.out.println("You found nothing but " + goldFound + " gold on the floor.");
            player.addGold(goldFound);
        }

        // Check if level is forced to end
        if (dungeon.isForcedToEnd()) {
            System.out.println("\n!!! The floor begins to crumble! You must face your foes now !!!");
            if (targets.isEmpty()) {
                System.out.println("A powerful guardian blocks the exit!");
                targets.add(dungeon.createScaledBoss());
            }
            attackTargetMenu();
        }
    }

    private void handleSearchEvent(DungeonManager.SearchResponse resp) {
        switch (resp.event) {
            case "DANGER_ZONE":
                System.out.println("\n!!! DANGER ZONE !!!");
                System.out.println("The air grows heavy... all monsters in this area have boosted attack!");
                for (IDamageable t : targets) {
                    if (t instanceof Monster) {
                        ((Monster) t).increaseAttackPower(5);
                    }
                }
                // Spawn an aggressive monster
                Monster m = dungeon.createScaledMonster("Aggressive Monster", 40, 10);
                m.increaseAttackPower(5);
                System.out.println("A " + m.getName() + " emerges from the shadows!");
                targets.add(m);
                break;
            case "FOGGY_AREA":
                System.out.println("\n!!! FOGGY AREA !!!");
                System.out.println("Thick fog rolls in. You can't see far, and multiple figures approach...");
                if (resp.target != null) {
                    System.out.println("Discovered: " + resp.target.getName());
                    targets.add(resp.target);
                }
                if (resp.secondTarget != null) {
                    System.out.println("Discovered: " + resp.secondTarget.getName());
                    targets.add(resp.secondTarget);
                }
                break;
            case "TRAP":
                System.out.println("\n!!! TRAP !!!");
                System.out.print("You stepped on a pressure plate! ");
                if (random.nextBoolean()) {
                    System.out.println("You quickly roll away and dodge the arrows!");
                } else {
                    int damage = (int) (player.getMaxHealth() * 0.15);
                    System.out.println("Searing arrows hit you for " + damage + " damage!");
                    player.takeDamage(damage);
                    System.out.println("Your HP: " + player.getHealth() + "/" + player.getMaxHealth());
                }
                break;
        }
    }

    private void attackTargetMenu() {
        while (!targets.isEmpty() && !player.isDestroyed()) {
            System.out.println("\nSelect a target to engage (Combat until one side falls):");
            for (int i = 0; i < targets.size(); i++) {
                IDamageable t = targets.get(i);
                System.out.println((i + 1) + ". " + t.getName() + " (" + t.getHealth() + "/" + t.getMaxHealth() + " HP)");
            }
            if (!dungeon.isForcedToEnd()) {
                System.out.println((targets.size() + 1) + ". Back");
            }

            int choice = getValidIntInput(1, targets.size() + (dungeon.isForcedToEnd() ? 0 : 1));
            if (choice <= targets.size()) {
                IDamageable selectedTarget = targets.get(choice - 1);
                boolean fled = combatUntilDeath(selectedTarget);

                if (fled) {
                    targets.remove(selectedTarget);
                    if (dungeon.isForcedToEnd() && !targets.isEmpty()) {
                        System.out.println("\nAnother foe approaches!");
                    }
                } else if (selectedTarget.isDestroyed()) {
                    handleVictory(selectedTarget);
                    targets.remove(selectedTarget);
                    if (dungeon.isForcedToEnd() && !targets.isEmpty()) {
                        System.out.println("\nAnother foe approaches!");
                    }
                }
            } else {
                break; // Back selected
            }
        }
    }

    private boolean combatUntilDeath(IDamageable target) {
        System.out.println("\n--- COMMENCING COMBAT: " + player.getName() + " vs " + target.getName() + " ---");
        while (!player.isDestroyed() && !target.isDestroyed()) {
            // Player Turn
            System.out.println("\n[" + player.getName() + ": " + player.getHealth() + "/" + player.getEffectiveMaxHealth() + " HP | " + player.getCurrentAP() + " AP]");
            
            System.out.println("1. Choose Skill (Action)");
            System.out.println("2. Use Item (Quick Action)");
            System.out.println("3. Flee (40% Success)");
            
            int actionChoice = getValidIntInput(1, 3);
            if (actionChoice == 2) {
                useItemMenu();
                // Items don't end turn, let them choose again
                continue;
            }

            if (actionChoice == 3) {
                if (random.nextInt(100) < 40) {
                    System.out.println("\nYou successfully fled from combat!");
                    return true;
                } else {
                    System.out.println("\nFlee failed! The enemy catches you off guard!");
                }
            } else {
                // Choose Skill
                System.out.println("Choose your attack:");
                List<Skill> skills = player.getSkills();
                for (int i = 0; i < skills.size(); i++) {
                    Skill s = skills.get(i);
                    String apInfo = (s.getApCost() == 0) ? "Restores 20 AP" : "Costs " + s.getApCost() + " AP";
                    System.out.println((i + 1) + ". " + s.getName() + " (Damage: " + (int)(s.getDamageMultiplier() * 100) + "%, " + apInfo + ")");
                }
                
                int skillChoice = -1;
                while (skillChoice == -1) {
                    int input = getValidIntInput(1, skills.size());
                    if (player.getCurrentAP() >= skills.get(input - 1).getApCost()) {
                        skillChoice = input;
                    } else {
                        System.out.print("Not enough AP! Choose another: ");
                    }
                }
                
                Skill selectedSkill = skills.get(skillChoice - 1);
                
                // AP Logic: 0 AP attacks restore 20 AP, others spend AP
                if (selectedSkill.getApCost() == 0) {
                    player.restoreAP(20);
                    System.out.println("Used a basic attack and caught your breath (+20 AP).");
                } else {
                    player.spendAP(selectedSkill.getApCost());
                }
                
                player.performSkillAttack(target, selectedSkill);
            }

            // Enemy Turn
            if (!target.isDestroyed() && target instanceof Monster) {
                ((Monster) target).attack(player);
            }
        }
        return false;
    }

    private void handleVictory(IDamageable target) {
        int gold = dungeon.getGoldReward(target);
        player.addGold(gold);
        
        IConsumable drop = target.getDrop();
        if (drop != null) {
            System.out.println("The " + target.getName() + " dropped a " + drop.getName() + "!");
            inventory.add(drop);
        }

        // Powerup Choice (10% chance)
        if (random.nextInt(100) < 10) {
            choosePowerup(target);
        } else {
            System.out.println("\nVictory achieved! You press on...");
        }
    }

    private void choosePowerup(IDamageable defeated) {
        System.out.println("\nYou feel stronger after the victory! Choose a Powerup:");
        int scale = defeated.getMaxHealth() / 10;
        
        System.out.println("1. Titan's Strength (+ " + (scale/2 + 1) + " Base Attack)");
        System.out.println("2. Vitality Surge (+ " + (scale + 5) + " Max HP)");
        System.out.println("3. Quick Mending (Heal " + (scale * 2 + 10) + " HP)");

        int choice = getValidIntInput(1, 3);
        switch (choice) {
            case 1: 
                int atkBonus = scale/2 + 1;
                player.increaseAttackPower(atkBonus); 
                System.out.println("Base attack increased by " + atkBonus);
                break;
            case 2: 
                int hpBonus = scale + 5;
                player.increaseMaxHealth(hpBonus); 
                System.out.println("Max HP increased by " + hpBonus);
                break;
            case 3: 
                player.heal(scale * 2 + 10); 
                break;
        }
    }

    private void useItemMenu() {
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty!");
            return;
        }
        System.out.println("\nSelect an item:");
        for (int i = 0; i < inventory.size(); i++) {
            System.out.println((i + 1) + ". " + inventory.get(i).getName());
        }
        System.out.println((inventory.size() + 1) + ". Back");

        int choice = getValidIntInput(1, inventory.size() + 1);
        if (choice <= inventory.size()) {
            IConsumable item = inventory.get(choice - 1);
            player.useItem(item);
            inventory.remove(item);
        }
    }

    private void showInstructions() {
        System.out.println("\n--- HOW TO PLAY ---");
        System.out.println("- Choose a class and start exploring the dungeon.");
        System.out.println("- 'Search' to find enemies and loot.");
        System.out.println("- Defeating enemies grants Gold, Gear, and Powerups.");
        System.out.println("- Clear all found targets to descend deeper into the dungeon.");
        System.out.println("- If you die, you can restart at Depth 1 with your saved gear!");
    }

    private void loadFromSave(SaveManager.SaveData data) {
        if (data.heroClass.equals("Warrior")) player = new Warrior(data.heroName);
        else player = new Mage(data.heroName);

        // Restore base stats (powerups)
        player.setAttackPower(data.baseAttack);
        player.setBaseMaxHealth(data.baseMaxHealth);

        if (data.gold > 0) player.addGold(data.gold);
        if (data.weapon != null) player.equip(data.weapon);
        if (data.headgear != null) player.equip(data.headgear);
        if (data.chestplate != null) player.equip(data.chestplate);

        // Restore current health and AP
        player.setHealth(data.currentHP);
        player.setCurrentAP(data.currentAP);

        dungeon.setDepth(data.dungeonDepth);
        shop.setWeaponTier(data.weaponTier);
        shop.setHeadTier(data.headTier);
        shop.setChestTier(data.chestTier);

        inventory.clear();
        if (data.inventory != null) inventory.addAll(data.inventory);

        System.out.println("Welcome back, " + player.getName() + "! Resuming at Depth " + dungeon.getDepth() + ".");
    }

    private int getValidIntInput(int min, int max) {
        int input = -1;
        while (input < min || input > max) {
            try {
                input = scanner.nextInt();
                if (input < min || input > max) {
                    System.out.print("Invalid choice (" + min + "-" + max + "): ");
                }
            } catch (InputMismatchException e) {
                System.out.print("Please enter a number: ");
            } finally {
                scanner.nextLine();
            }
        }
        return input;
    }
}
