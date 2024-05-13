package artifacts.ability;

import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public interface TooltiplessAbility extends ArtifactAbility {

    @Override
    default void addTooltipIfNonCosmetic(List<MutableComponent> tooltip) {

    }
}
