package artifacts.ability;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;

public class SwimSpeedAbility implements ArtifactAbility {

    public double getSwimSpeedBonus() {
        return ModGameRules.FLIPPERS_SWIM_SPEED_BONUS.get();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.SWIM_SPEED;
    }

    @Override
    public boolean isNonCosmetic() {
        return getSwimSpeedBonus() > 0;
    }
}
