package FinalProject.models;

import FinalProject.interfaces.IConsumable;

/**
 * Potion that doubles the hero's attack power for one turn.
 */
public class BerserkPotion implements IConsumable {
    private String name;

    public BerserkPotion() {
        this.name = "Berserk Potion";
    }

    @Override
    public void consume(Hero hero) {
        System.out.println(hero.getName() + " drinks the " + name + " and feels a surge of power!");
        hero.setBerserk(true);
    }

    @Override
    public String getName() {
        return name;
    }
}
