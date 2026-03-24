package FinalProject.game;

import FinalProject.interfaces.IDamageable;
import FinalProject.models.Monster;
import FinalProject.models.WoodenBox;
import java.util.Random;

/**
 * Handles dungeon depth, enemy scaling, and searching logic.
 */
public class DungeonManager {
    public static class SearchResponse {
        public IDamageable target;
        public String event; // e.g. "DANGER_ZONE", "FOGGY_AREA", "TRAP"
        public IDamageable secondTarget; // For foggy area
        
        public SearchResponse(IDamageable target) { this.target = target; }
        public SearchResponse(String event) { this.event = event; }
    }

    private int depth;
    private int levelSearches;
    private Random random;

    public DungeonManager() {
        this.depth = 1;
        this.levelSearches = 0;
        this.random = new Random();
    }

    public void nextDepth() {
        this.depth++;
        this.levelSearches = 0;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
        this.levelSearches = 0;
    }

    public int getLevelSearches() {
        return levelSearches;
    }

    /**
     * Scales a monster's stats based on the current depth.
     */
    public Monster createScaledMonster(String type, int baseHealth, int baseAttack) {
        double multiplier = 1.0 + (depth - 1) * 0.25;
        int scaledHealth = (int)(baseHealth * multiplier);
        int scaledAttack = (int)(baseAttack * (1.0 + (depth - 1) * 0.15));
        return new Monster(type + " (Lv. " + depth + ")", scaledHealth, scaledAttack);
    }

    /**
     * Logic for searching the area.
     * @return A new target if found, otherwise null.
     */
    public SearchResponse search(int currentMonsterCount, int currentBoxCount) {
        levelSearches++;
        int totalTargets = currentMonsterCount + currentBoxCount;
        
        // GUARANTEED SPAWN: If search 4+ and nothing found yet, force a monster
        if (levelSearches >= 4 && totalTargets == 0) {
            return new SearchResponse(generateRandomMonster());
        }

        int chance = random.nextInt(100);
        // Base chances: Monster 70%, Box 20%
        int monsterChance = Math.max(30, 70 - (currentMonsterCount * 10));
        int boxChance = Math.max(10, 20 - (currentBoxCount * 5));

        if (chance < monsterChance) {
            return new SearchResponse(generateRandomMonster());
        } else if (chance < monsterChance + boxChance) {
            return new SearchResponse(new WoodenBox());
        }

        // Random Events (40% of the remaining 10%? No, let's make it more common if nothing found)
        // If we reach here, we found "nothing". Let's roll for event.
        if (random.nextInt(100) < 40) {
            int eventRoll = random.nextInt(3);
            if (eventRoll == 0) {
                return new SearchResponse("DANGER_ZONE");
            } else if (eventRoll == 1) {
                SearchResponse resp = new SearchResponse("FOGGY_AREA");
                resp.target = generateRandomMonster();
                resp.secondTarget = generateRandomMonster();
                return resp;
            } else {
                return new SearchResponse("TRAP");
            }
        }

        return new SearchResponse((IDamageable)null);
    }

    /**
     * Calculates probability that the level must end.
     */
    public boolean isForcedToEnd() {
        if (levelSearches < 5) return false;
        if (levelSearches >= 10) return true;
        
        // Probability increases by 20% for each search after 5
        int probability = (levelSearches - 4) * 20;
        return random.nextInt(100) < probability;
    }

    public Monster createScaledBoss() {
        return createScaledMonster("Dungeon Guardian", 150, 25);
    }

    private Monster generateRandomMonster() {
        int type = random.nextInt(7); // Range 0-6
        switch (type) {
            case 0: case 1: return createScaledMonster("Slime", 30, 5);
            case 2: case 3: return createScaledMonster("Goblin", 50, 10);
            case 4: case 5: return createScaledMonster("Orc", 80, 15);
            case 6: return createScaledMonster("Elite Knight", 120, 25);
            default: return createScaledMonster("Slime", 30, 5);
        }
    }

    public int getGoldReward(IDamageable target) {
        int baseGold = (target instanceof Monster) ? 30 : 10;
        return baseGold + random.nextInt(20) + (depth * 5);
    }
}
