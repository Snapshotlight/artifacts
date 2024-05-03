package artifacts.ability;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;

public class BonusInvincibilityTicksAbility implements ArtifactAbility {

    public int getBonusInvincibilityTicks() {
        return ModGameRules.CROSS_NECKLACE_BONUS_INVINCIBILITY_TICKS.get();
    }

    public int getCooldown() {
        return ModGameRules.CROSS_NECKLACE_COOLDOWN.get();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.BONUS_INVINCIBILITY_TICKS;
    }

    @Override
    public boolean isNonCosmetic() {
        return getBonusInvincibilityTicks() > 0;
    }
}
