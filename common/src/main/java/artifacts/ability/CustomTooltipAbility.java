package artifacts.ability;

import artifacts.Artifacts;
import artifacts.registry.ModAbilities;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public class CustomTooltipAbility implements ArtifactAbility {

    private final String name;

    public CustomTooltipAbility(String name) {
        this.name = name;
    }

    public static CustomTooltipAbility attributeTooltip(String name) {
        return new CustomTooltipAbility("%s.tooltip.ability.attribute.%s".formatted(Artifacts.MOD_ID, name));
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.CUSTOM_TOOLTIP.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return false;
    }

    @Override
    public void addTooltipIfNonCosmetic(List<MutableComponent> tooltip) {
        tooltip.add(Component.translatable(name));
    }
}
