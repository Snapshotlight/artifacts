package artifacts.ability;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;

public class VillagerReputation implements ArtifactAbility {

    public int getReputationBonus() {
        return ModGameRules.VILLAGER_HAT_REPUTATION_BONUS.get();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.VILLAGER_REPUTATION.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return getReputationBonus() > 0;
    }
}
