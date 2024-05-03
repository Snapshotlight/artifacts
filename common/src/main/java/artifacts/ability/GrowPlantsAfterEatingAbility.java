package artifacts.ability;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public class GrowPlantsAfterEatingAbility implements ArtifactAbility {

    @Override
    public Type<?> getType() {
        return ModAbilities.GROW_PLANTS_AFTER_EATING;
    }

    @Override
    public boolean isNonCosmetic() {
        return ModGameRules.ROOTED_BOOTS_DO_GROW_PLANTS_AFTER_EATING.get() && ModGameRules.ROOTED_BOOTS_ENABLED.get();
    }

    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        // TODO
    }
}
