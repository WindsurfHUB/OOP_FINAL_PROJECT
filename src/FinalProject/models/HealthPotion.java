package FinalProject.models;

import FinalProject.interfaces.IConsumable;

/**
 * Concrete class for a consumable item.
 */
public class HealthPotion implements IConsumable {
    private String name;
    private int healAmount;

    public HealthPotion(String name, int healAmount) {
        this.name = name;
        this.healAmount = healAmount;
    }

    @Override
    public void consume(Hero hero) {
        System.out.println(hero.getName() + " drinks " + name + "...");
        hero.heal(healAmount);
    }

    @Override
    public String getName() {
        return name;
    }
}
