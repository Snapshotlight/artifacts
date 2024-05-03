package artifacts.ability;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;

public class FartAbility implements ArtifactAbility {

    public double getFartChance() {
        return ModGameRules.WHOOPEE_CUSHION_FART_CHANCE.get();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.FART.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return true;
    }
}
