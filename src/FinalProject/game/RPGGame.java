package FinalProject.game;

import FinalProject.interfaces.IConsumable;
import FinalProject.interfaces.IDamageable;
import FinalProject.models.*;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Main game engine for the Text-Based RPG.
 */
public class RPGGame {
    private Scanner scanner;
    private Hero player;
    private List<IDamageable> targets;
    private List<IConsumable> inventory;
    private boolean isRunning;

    public RPGGame() {
        scanner = new Scanner(System.in);
        isRunning = true;
    }

    /**
     * Start the game's main loop.
     */
    public void start() {
        showMainMenu();
        while (isRunning) {
            handleAction();
            checkGameConditions();
        }
        System.out.println("Thank you for playing!");
    }

    private void showMainMenu() {
        System.out.println("================================");
        System.out.println("     WELCOME TO DII DUNGEON    ");
        System.out.println("================================");
        System.out.println("1. Start New Game");
        System.out.println("2. How to Play");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");

        int choice = getValidIntInput(1, 3);
        switch (choice) {
            case 1:
                initializeGame();
                break;
            case 2:
                showInstructions();
                showMainMenu();
                break;
            case 3:
                isRunning = false;
                break;
        }
    }

    private void showInstructions() {
        System.out.println("\n--- HOW TO PLAY ---");
        System.out.println("- Choose a class: Warrior (High HP/Def) or Mage (High Attack).");
        System.out.println("- Battle monsters or break boxes to progress.");
        System.out.println("- Use potions to heal yourself if your HP is low.");
        System.out.println("- Defeat all targets in the world to win.");
        System.out.println("- If your HP reaches 0, you lose.\n");
    }

    private void initializeGame() {
        System.out.println("\nSelect your hero name: ");
        String name = scanner.nextLine();

        System.out.println("Choose your class:");
        System.out.println("1. Warrior (HP: 120, Attack: 20)");
        System.out.println("2. Mage (HP: 80, Attack: 35)");
        int classChoice = getValidIntInput(1, 2);

        if (classChoice == 1) {
            player = new Warrior(name);
        } else {
            player = new Mage(name);
        }

        GameInitializer initializer = new GameInitializer();
        targets = initializer.getTargets();
        inventory = initializer.getInventory();

        System.out.println("\nWelcome, " + player.getName() + "! Your journey begins.");
    }

    private void handleAction() {
        if (!isRunning || player == null) return;

        System.out.println("\n--- " + player.getName().toUpperCase() + " [" + player.getHealth() + "/" + player.getMaxHealth() + " HP] ---");
        System.out.println("1. Attack a Target");
        System.out.println("2. Use an Item");
        System.out.println("3. Retreat (Quit Game)");
        System.out.print("Action: ");

        int choice = getValidIntInput(1, 3);
        switch (choice) {
            case 1:
                attackTargetMenu();
                break;
            case 2:
                useItemMenu();
                break;
            case 3:
                isRunning = false;
                break;
        }
    }

    private void attackTargetMenu() {
        if (targets.isEmpty()) {
            System.out.println("There are no more targets!");
            return;
        }

        System.out.println("\nWho/What do you want to attack?");
        for (int i = 0; i < targets.size(); i++) {
            IDamageable t = targets.get(i);
            System.out.println((i + 1) + ". " + t.getName() + " (" + t.getHealth() + "/" + t.getMaxHealth() + " HP)");
        }
        System.out.println((targets.size() + 1) + ". Back");

        int choice = getValidIntInput(1, targets.size() + 1);
        if (choice <= targets.size()) {
            IDamageable selectedTarget = targets.get(choice - 1);
            player.attack(selectedTarget);

            // Counter-attack logic if it's a living Entity
            if (!selectedTarget.isDestroyed() && selectedTarget instanceof Monster) {
                Monster monster = (Monster) selectedTarget;
                monster.attack(player);
            }

            // Cleanup destroyed targets
            if (selectedTarget.isDestroyed()) {
                targets.remove(selectedTarget);
            }
        }
    }

    private void useItemMenu() {
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty!");
            return;
        }

        System.out.println("\nSelect an item to use:");
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

    private void checkGameConditions() {
        if (player.isDestroyed()) {
            System.out.println("\nGAME OVER: Your hero has fallen.");
            isRunning = false;
        } else if (targets.isEmpty()) {
            System.out.println("\nVICTORY: You have cleared all targets!");
            isRunning = false;
        }
    }

    /**
     * Robust input handling for integers with validation.
     * Prevents crashes on string inputs and ensures choices are within range.
     */
    private int getValidIntInput(int min, int max) {
        int input = -1;
        while (input < min || input > max) {
            try {
                input = scanner.nextInt();
                if (input < min || input > max) {
                    System.out.print("Invalid choice. Please enter a number between " + min + " and " + max + ": ");
                }
            } catch (InputMismatchException e) {
                System.out.print("Input Error: Please enter a valid number: ");
            } finally {
                scanner.nextLine(); // Clear scanner buffer
            }
        }
        return input;
    }
}
