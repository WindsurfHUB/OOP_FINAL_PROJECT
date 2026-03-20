package FinalProject.game;

import FinalProject.interfaces.IConsumable;
import FinalProject.interfaces.IDamageable;
import java.util.ArrayList;
import java.util.List;

/**
 * Legacy initializer. World generation is now handled by DungeonManager.
 */
public class GameInitializer {
    public List<IDamageable> getTargets() {
        return new ArrayList<>();
    }

    public List<IConsumable> getInventory() {
        return new ArrayList<>();
    }
}
