package artifacts.ability;

import artifacts.registry.ModAbilities;

public class MakePiglinsNeutralAbility implements ArtifactAbility {

    @Override
    public Type<?> getType() {
        return ModAbilities.MAKE_PIGLINS_NEUTRAL.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
