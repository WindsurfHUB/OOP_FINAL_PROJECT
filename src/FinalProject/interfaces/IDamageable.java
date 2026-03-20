package FinalProject.interfaces;

public interface IDamageable {
    void takeDamage(int amount);
    boolean isDestroyed();
    String getName();
    int getHealth();
    int getMaxHealth();
    IConsumable getDrop();
}
